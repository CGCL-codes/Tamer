package org.tolven.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class ImageCacheFilter implements Filter {

    GregorianCalendar lastModified = new GregorianCalendar();

    public void init(FilterConfig arg0) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
        HttpServletResponse hResponse = (HttpServletResponse) response;
        GregorianCalendar expires = new GregorianCalendar();
        expires.add(GregorianCalendar.HOUR, 24);
        hResponse.setHeader("Pragma", "private");
        hResponse.setHeader("Expires", sdf.format(expires.getTime()));
        hResponse.setHeader("Cache-Control", "private");
        hResponse.setHeader("Last-Modified", sdf.format(lastModified.getTime()));
        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}
