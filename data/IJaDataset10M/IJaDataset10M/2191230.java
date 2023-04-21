package com.mycila.jdbc;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Mathieu Carbou
 */
public abstract class OncePerRequestFilter implements Filter {

    private static final String ALREADY_FILTERED_SUFFIX = ".FILTERED";

    private FilterConfig filterConfig;

    @Override
    public final void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        init();
    }

    @Override
    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("OncePerRequestFilter just supports HTTP requests");
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
        if (request.getAttribute(alreadyFilteredAttributeName) != null) {
            filterChain.doFilter(request, response);
        } else {
            request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
            doFilterInternal(httpRequest, httpResponse, filterChain);
        }
    }

    protected final FilterConfig getFilterConfig() {
        return filterConfig;
    }

    private String getAlreadyFilteredAttributeName() {
        String name = getFilterConfig().getFilterName();
        if (name == null) {
            name = getClass().getName();
        }
        return name + ALREADY_FILTERED_SUFFIX;
    }

    protected abstract void init() throws ServletException;

    protected abstract void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException;
}
