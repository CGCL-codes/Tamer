package br.com.devcase.servlet.autoriza;

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

public class FiltroSession implements Filter {

    private FilterConfig filterConfig = null;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("passoaosjfksadngklangjkf");
        HttpServletRequest rq = (HttpServletRequest) request;
        HttpSession session = rq.getSession(false);
        HttpServletResponse rp = (HttpServletResponse) response;
        if (session == null) {
            System.out.println("passou");
            rp.sendRedirect("ExemploWeb/faces/Principal.jsp");
        } else {
            System.out.println("tem session");
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
    }
}
