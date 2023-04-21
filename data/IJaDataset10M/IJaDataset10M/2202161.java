package com.sitechasia.webx.components.datasource.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import com.sitechasia.webx.components.datasource.context.DomainContextHolder;

/**
 * 用于对Http请求进行过滤，获得URL域名部分的filter
 *
 * @author Administrator
 * @version 1.2 , 2008/5/9
 * @since JDK1.5
 */
public class DomainNameFilter implements Filter {

    public void destroy() {
    }

    private String hostname(HttpServletRequest req) {
        String url = req.getRequestURL().toString();
        String[] ss = url.split("/");
        return ss[2];
    }

    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
        DomainContextHolder.setMyDomain(hostname((HttpServletRequest) arg0));
        arg2.doFilter(arg0, arg1);
    }

    public void init(FilterConfig arg0) throws ServletException {
    }
}
