package org.wwweeeportal.util.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * <p>
 * Set an HTTP response header for each init parameter.
 * </p>
 * 
 * <p>
 * Place something like the following in your web.xml, as a child of the web-app element, after the display-name and
 * description elements, but before any servlet elements:
 * 
 * <pre>
 * &lt;filter&gt;
 *   &lt;filter-name&gt;OneHourCache&lt;/filter-name&gt;
 *   &lt;filter-class&gt;org.wwweeeportal.util.ee.ServletUtil$ResponseHeaderFilter&lt;/filter-class&gt;
 *   &lt;init-param&gt;
 *     &lt;param-name&gt;Cache-Control&lt;/param-name&gt;
 *     &lt;param-value&gt;max-age=3600, public&lt;/param-value&gt;
 *   &lt;/init-param&gt;
 * &lt;/filter&gt;
 * &lt;filter-mapping&gt;
 *   &lt;filter-name&gt;OneHourCache&lt;/filter-name&gt;
 *   &lt;url-pattern&gt;*.png&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 * 
 * </p>
 */
public class ResponseHeaderFilter implements Filter {

    /**
   * The configuration supplied to this filter instance.
   */
    protected FilterConfig filterConfig;

    @Override
    public void init(final FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        return;
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws ServletException, IOException {
        final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        final Enumeration<?> initParameterNames = filterConfig.getInitParameterNames();
        while (initParameterNames.hasMoreElements()) {
            final String parameterName = (String) initParameterNames.nextElement();
            final String parameterValue = filterConfig.getInitParameter(parameterName);
            httpServletResponse.addHeader(parameterName, parameterValue);
        }
        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }

    @Override
    public void destroy() {
        filterConfig = null;
        return;
    }
}
