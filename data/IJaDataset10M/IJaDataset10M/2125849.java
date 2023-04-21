package com.bradmcevoy.http;

import java.io.*;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** This is an implementation of a standard Servlet API filter, not
 *  to be confused with the milton filter which achieves the same objective
 *
 *  USers may use a MiltonFilter or a MiltonServlet to achieve slightly different
 *  functionality
 *  
 *  Using this class instead of a servlet means that if no resource is found
 *  for a given request that request falls through to the web container for     
 *  further processing
 *
 *  This means that you can have a combination of static file system resources
 *  and resource factory isntances
 */
public class MiltonFilter extends AbstractMiltonEndPoint implements Filter {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MiltonServlet.class);

    private FilterConfig config = null;

    public void init(FilterConfig config) throws ServletException {
        this.config = config;
        String resourceFactoryClassName = config.getInitParameter("resource.factory.class");
        init(resourceFactoryClassName);
        httpManager.init(new ApplicationConfig(config), httpManager);
    }

    public void doFilter(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        Request request = new ServletRequest(req);
        Response response = new ServletResponse(resp);
        httpManager.process(request, response);
    }
}
