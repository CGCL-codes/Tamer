package com.tien.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.tien.model.Employee;

/**
 * @author xt40919
 * @version Create Time：Aug 17, 2011 10:35:08 AM
 */
public class LoginPermissionFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpResp = (HttpServletResponse) resp;
        String currentURL = httpReq.getRequestURI();
        Employee employee = (Employee) httpReq.getSession().getAttribute("employee");
        if ("/".equals(currentURL) || "/index.jsp".equals(currentURL) || "/register.jsp".equals(currentURL) || "/login".equals(currentURL) || "/register".equals(currentURL)) {
            if (null != employee) {
                httpResp.sendRedirect("/blog");
                return;
            }
            chain.doFilter(req, resp);
            return;
        }
        if (null == employee) {
            httpResp.sendRedirect("/index.jsp");
            return;
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
}
