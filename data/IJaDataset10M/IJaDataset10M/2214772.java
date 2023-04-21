package org.t2framework.lucy.scope;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * {@.en Filter for using servlet scope, such as request scope, session scope,
 * application scope for Lucy.}
 * 
 * <br />
 * 
 * {@.ja }
 * 
 * @author shot
 * 
 */
public class ScopeFilter implements Filter {

    protected static ThreadLocal<Context> contexts = new ThreadLocal<Context>();

    protected FilterConfig config;

    protected ServletContext servletContext;

    @Override
    public void init(FilterConfig config) throws ServletException {
        this.config = config;
        this.servletContext = config.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;
        Context previous = contexts.get();
        try {
            contexts.set(new Context(req, res, this.servletContext, this.config));
            chain.doFilter(request, response);
        } finally {
            contexts.set(previous);
        }
    }

    public static HttpServletRequest getRequest() {
        return getContext().getRequest();
    }

    public static HttpServletResponse getResponse() {
        return getContext().getResponse();
    }

    public static ServletContext getServletContext() {
        return getContext().getServletContext();
    }

    public static FilterConfig getFilterConfig() {
        return getContext().getFilterConfig();
    }

    protected static Context getContext() {
        Context context = contexts.get();
        if (context == null) {
            throw new IllegalStateException("no context");
        }
        return context;
    }

    @Override
    public void destroy() {
        this.config = null;
        this.servletContext = null;
    }

    protected static class Context {

        final HttpServletRequest request;

        final HttpServletResponse response;

        final ServletContext servletContext;

        final FilterConfig config;

        Context(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, FilterConfig config) {
            this.request = request;
            this.response = response;
            this.servletContext = servletContext;
            this.config = config;
        }

        HttpServletRequest getRequest() {
            return request;
        }

        HttpServletResponse getResponse() {
            return response;
        }

        public ServletContext getServletContext() {
            return servletContext;
        }

        public FilterConfig getFilterConfig() {
            return config;
        }
    }
}
