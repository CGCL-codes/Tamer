package com.jiutian.util;

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

public class CommentFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(true);
        String username = (String) session.getAttribute("user");
        String usertype = (String) session.getAttribute("type");
        if (!StringUtils.isBlank(username) && !StringUtils.isBlank(usertype) && "p".equals(usertype)) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect("http://www.ijiutian.com/login/login.jsp");
        }
    }

    public void destroy() {
    }
}
