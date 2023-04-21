package com.simpleblog.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * jsp过滤
 * @author  hexinglun@gmail.com
 * @version 2011-11-8下午08:07:20
 */
public class JspFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession();
        Object object = session.getAttribute("isLogin");
        if (object == null || !((Boolean) object).booleanValue()) {
            ((HttpServletResponse) response).sendRedirect("/admin/login.htm");
        } else {
            doFilter(request, response, chain);
        }
    }

    @Override
    public void destroy() {
    }
}
