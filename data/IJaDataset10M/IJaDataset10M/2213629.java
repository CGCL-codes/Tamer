package com.leemba.monitor.server.servlet;

import com.leemba.monitor.server.dao.Dao;
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
 * @author mrjohnson
 */
public class ConfigFilter implements Filter {

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean configured = Dao.getHead().isConfigured();
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getServletPath();
        if (path == null) path = "";
        if (!configured) {
            if (path.startsWith("/admin/") || path.startsWith("/api/") || path.startsWith("/login/")) chain.doFilter(request, response); else res.sendRedirect("/admin/wizard/");
        } else chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
