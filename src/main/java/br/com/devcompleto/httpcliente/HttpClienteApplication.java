package br.com.devcompleto.httpcliente;

import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;

@SpringBootApplication
public class HttpClienteApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpClienteApplication.class, args);
	}

	WebClient webClient = WebClient.builder()
	.baseUrl("https://my-json-server.typicode.com/typicode/demo")
	.build();


	@Bean
	PostClient postClient(HttpServiceProxyFactory factory) {
		return factory.createClient(PostClient.class);
	}

	
	@Bean
	HttpServiceProxyFactory httpServiceProxyFactory(WebClient.Builder builder) {
		return HttpServiceProxyFactory
		.builder(WebClientAdapter.forClient(webClient))
		.build();
	}

	@HttpExchange("/posts")
	interface PostClient {

		@GetExchange
		List<Post> list();

		@GetExchange("/{id}")
		Post get(@PathVariable("id") Long id);
		
		@PostExchange
		Post create(@RequestBody Post post);
	}

	record Post(Long id, String title) {

	}

	@Bean
	ApplicationRunner applicationRunner(PostClient postClient) {
		return args -> {
			System.out.println("List posts: " + postClient.list());
			System.out.println("POst 1: " + postClient.get(1l));
			System.out.println("List posts: " + postClient.create(new Post(4l, "Post 4")));
		};
	}

}
