package org.localstorm.mcc.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Alexey Kuznetsov
 */
public class ForceCacheFilter implements Filter {

    public ForceCacheFilter() {
    }

    @Override
    public void doFilter(ServletRequest _req, ServletResponse _res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) _res;
        resp.setHeader("Cache-Control", "max-age=172800");
        resp.setDateHeader("Expires", System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000);
        chain.doFilter(_req, _res);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
}
