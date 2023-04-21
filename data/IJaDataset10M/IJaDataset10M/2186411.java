package com.googlecode.avgas.core.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Create Date: 2009/5/27
 *
 * @author Alan She
 */
public class CharacterEncodingFilter implements Filter {

    private String encoding;

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (encoding != null) {
            request.setCharacterEncoding(encoding);
        }
        filterChain.doFilter(request, servletResponse);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");
    }

    public void destroy() {
    }
}
