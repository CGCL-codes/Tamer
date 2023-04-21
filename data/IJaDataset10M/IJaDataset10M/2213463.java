package com.softaspects.jsf.support.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Eliminates "flicker" effect of backround images and rollovers in
 * tabbed panel and other WGF component, when displayed using
 * Internet Explorer for Windows.
 * <p/>
 * Solution is to add cache-control header, which instructs
 * browser to cache images for a long time.
 */
public class IEFlickerFixFilter implements Filter {

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.addHeader("Cache-control", "max-age=2592000");
        chain.doFilter(request, response);
    }

    /**
     * Method init.
     *
     * @param config
     * @throws javax.servlet.ServletException
     */
    public void init(FilterConfig config) throws ServletException {
    }
}
