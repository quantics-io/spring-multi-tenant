package io.quantics.multitenant;

import com.nimbusds.jwt.proc.JWTClaimsSetAwareJWSKeySelector;
import com.nimbusds.jwt.proc.JWTProcessor;
import io.quantics.multitenant.tenant.controller.HomeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest({
        "spring.security.oauth2.resourceserver.multitenant.enabled=false",
        "spring.security.oauth2.resourceserver.multitenant.use-header=false",
        "spring.security.oauth2.resourceserver.multitenant.use-token=false",
})
@AutoConfigureMockMvc
@WithMockUser(username = "test")
class DisabledMultiTenantApplicationTests {

    @Autowired
    private HomeController controller;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
        assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean(JWTClaimsSetAwareJWSKeySelector.class));
        assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean(OAuth2TokenValidator.class));
        assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean(JWTProcessor.class));
        assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean(JwtDecoder.class));
        assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean(AuthenticationManagerResolver.class));
        assertThrows(NoSuchBeanDefinitionException.class,
                () -> context.getBean("multiTenantWebSecurityConfigurerAdapter", WebSecurityConfigurerAdapter.class));
        assertThrows(NoSuchBeanDefinitionException.class,
                () -> context.getBean("multiTenantWebMvcConfigurer", WebMvcConfigurer.class));
    }

    @Test
    void shouldReturnHelloWorld() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello World!")));
    }

}