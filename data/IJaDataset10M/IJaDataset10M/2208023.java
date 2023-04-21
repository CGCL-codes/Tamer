package org.jomper.mvc.filter.impl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jomper.mvc.filter.ActionFilter;
import org.jomper.mvc.filter.chain.JomperFilterChain;

/**
 * @author Jomper.Chow
 * @project 
 * @version 2007-7-9
 * @description 
 */
public class RelayFilter implements ActionFilter {

    public void doFilter(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, JomperFilterChain chain) {
        chain.doChain(request, response, servletContext);
    }
}
