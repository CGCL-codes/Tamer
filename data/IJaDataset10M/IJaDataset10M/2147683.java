package org.ironrhino.core.spring.security;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class DefaultUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired(required = false)
    protected AuthenticationFailureHandler authenticationFailureHandler = new DefaultAuthenticationFailureHandler();

    @Autowired(required = false)
    protected AuthenticationSuccessHandler authenticationSuccessHandler = new DefaultAuthenticationSuccessHandler();

    public static final String TARGET_URL = "targetUrl";

    @PostConstruct
    public void init() {
        setAuthenticationFailureHandler(authenticationFailureHandler);
        setAuthenticationSuccessHandler(authenticationSuccessHandler);
    }

    public void success(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, null, authResult);
    }

    public void unsuccess(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
