package com.hs.framework.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharacterEncodingFilter implements Filter {

    private static final String DEFAULT_ENCODING = "UTF=8";

    private String encoding = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.encoding = filterConfig.getInitParameter("encoding");
        if (encoding == null || encoding.equalsIgnoreCase("")) {
            encoding = DEFAULT_ENCODING;
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(this.encoding);
        chain.doFilter(request, response);
    }

    public void destroy() {
        this.encoding = null;
    }
}
