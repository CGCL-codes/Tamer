package com.sibnet.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class Filter3 implements Filter {

    public Filter3() {
        super();
    }

    public void init(FilterConfig arg0) throws ServletException {
    }

    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {
        System.out.println("Filter 3 started...");
        System.out.println("Port : " + arg0.getLocalPort() + " \nTried to access FilterTest in : " + arg0.getRemoteHost() + System.currentTimeMillis());
        chain.doFilter(arg0, arg1);
    }

    public void destroy() {
    }
}
