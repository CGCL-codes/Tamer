package com.sitescape.team.web.servlet.filter;

import java.io.IOException;
import java.security.Principal;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.sitescape.team.web.WebKeys;
import com.sitescape.team.web.servlet.PrincipalServletRequest;

public class PrincipalInjectionFilter implements Filter {

    private FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        Principal principal = req.getUserPrincipal();
        if (principal == null) {
            HttpSession ses = req.getSession(false);
            if (ses != null) {
                principal = (Principal) ses.getAttribute(WebKeys.USER_PRINCIPAL);
                if (principal != null) {
                    PrincipalServletRequest reqWithPrincipal = new PrincipalServletRequest(req, principal);
                    chain.doFilter(reqWithPrincipal, response);
                } else {
                    throw new ServletException("No user information available - Illegal request sequence.");
                }
            } else {
                throw new ServletException("No session in place - Illegal request sequence.");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
    }
}
