package com.sts.webmeet.server.filters;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sts.webmeet.web.Constants;

public class HostPrependingFilter implements Filter {

    public void init(FilterConfig config) {
        context = config.getServletContext();
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException {
        String strHost = System.getProperty(Constants.HOST_PREFIX_KEY);
        if (null != strHost && strHost.trim().length() > 0 && request instanceof HttpServletRequest) {
            URL urlRedirect = checkHost((HttpServletRequest) request, strHost);
            if (null != urlRedirect) {
                HttpServletResponse hres = (HttpServletResponse) response;
                HttpServletRequest hreq = (HttpServletRequest) request;
                String encoded = hres.encodeRedirectURL(urlRedirect.toString());
                context.log(getClass().getName() + ": doFilter(): redirecting request for " + hreq.getRequestURL().toString() + " to " + encoded);
                hres.sendRedirect(encoded);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private URL checkHost(HttpServletRequest request, String strHost) {
        try {
            String strRequestedHost = request.getServerName();
            if (!strRequestedHost.startsWith(strHost) && !isDottedQuadIP(strRequestedHost)) {
                java.net.URL urlRequest = new java.net.URL(request.getRequestURL().toString());
                return new URL(urlRequest.getProtocol(), strHost + "." + urlRequest.getHost(), urlRequest.getPort(), urlRequest.getFile());
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        }
        return null;
    }

    private boolean isDottedQuadIP(String strHost) {
        StringTokenizer st = new StringTokenizer(strHost, ".");
        try {
            while (st.hasMoreTokens()) {
                Integer.parseInt(st.nextToken());
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void destroy() {
        context = null;
    }

    private ServletContext context;
}
