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
import com.sts.webmeet.web.util.SSLUtil;

public class SslEnforcingFilter implements Filter {

    public void init(FilterConfig config) {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws java.io.IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (SSLUtil.isTransportAcceptable(request)) {
            if (SSLUtil.isKeyLengthAcceptable(request)) {
                chain.doFilter(request, response);
            } else {
                response.getWriter().write("Your browser does not appear " + "to support " + SSLUtil.getMinKeyLength() + "-bit encryption. " + "Please update your browser and try again.");
            }
        } else {
            StringBuffer file = new StringBuffer();
            String protocol = "https";
            String host = request.getServerName();
            file.append(protocol).append("://").append(host);
            if (SSLUtil.getSslPort() != 443) {
                file.append(":").append(SSLUtil.getSslPort());
            }
            file.append(request.getRequestURI());
            String requestedSessionId = request.getRequestedSessionId();
            if ((requestedSessionId != null) && request.isRequestedSessionIdFromURL()) {
                file.append(";jsessionid=");
                file.append(requestedSessionId);
            }
            String queryString = request.getQueryString();
            if (queryString != null) {
                file.append('?');
                file.append(queryString);
            }
            response.sendRedirect(file.toString());
        }
    }

    public void destroy() {
    }
}
