package com.vitria.test.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SecurityCheckFilter implements Filter {

    public static final String USER_NAME = "user";

    public static final String PASSWORD = "password";

    public static final String LOGIN_URL = "login_url";

    private ServletContext context_;

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        String userName = (String) session.getAttribute(USER_NAME);
        if (userName != null) {
            chain.doFilter(request, response);
        } else {
        }
    }

    public void init(FilterConfig config) throws ServletException {
        context_ = config.getServletContext();
    }
}
