package org.logtime.redirect.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * RedirectFilter filter all the request 301 to redirectDomain
 * 
 * @version: 1.0
 * @author: sumin
 * @createdate: 2011-11-06
 */
public class RedirectFilter implements Filter {

    /**
	 * RedirectDomain from web init param
	 */
    private String redirectDomain;

    /**
	 * Filter all
	 * 
	 * @param arg0
	 *            ServletRequest
	 * @param arg1
	 *            ServletResponse
	 * @param arg2
	 *            FilterChain
	 * @throws IOException
	 *             io exception
	 * @throws ServletException
	 *             servlet exception
	 */
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        request.setCharacterEncoding("UTF-8");
        String redirectUrl = redirectDomain + request.getRequestURI();
        for (Object key : request.getParameterMap().keySet()) {
            String value = request.getParameter(key.toString());
            if (null != value && !"".equals(value.trim())) {
                redirectUrl = redirectUrl + (redirectUrl.contains("?") ? "&" : "?") + key.toString() + "=" + value;
            }
        }
        response.setStatus(301);
        response.setHeader("Location", redirectUrl);
        response.setHeader("Connection", "close");
    }

    /**
	 * Get init param
	 * 
	 * @param arg0
	 *            FilterConfig
	 */
    public void init(FilterConfig arg0) throws ServletException {
        redirectDomain = arg0.getInitParameter("redirectDomain");
    }

    /**
	 * Run when destroy
	 */
    public void destroy() {
    }
}
