package com.zeus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.zeus.common.CustomAccessDeniedHandler;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, AccessDeniedHandler accessDeniedHandler) throws Exception {

		log.info("security config ...");

		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/board/list", "/notice/list").permitAll()
						.requestMatchers("/board/register").hasRole("MEMBER").requestMatchers("/notice/register")
						.hasRole("ADMIN").anyRequest().authenticated())
				.formLogin(form -> form.permitAll()).exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler) // ✅
																															// 커스텀
																															// 403
																															// 처리
				);

		return http.build();
	}

	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("member").password("{noop}1234").roles("MEMBER");

		auth.inMemoryAuthentication().withUser("admin").password("{noop}1234").roles("ADMIN", "MEMBER");
	}

	@Bean
	AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}
}
