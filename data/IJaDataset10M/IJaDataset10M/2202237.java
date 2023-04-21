package org.mwanzia;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MwanziaFilter extends MwanziaServlet implements Filter {

    private static final long serialVersionUID = 2412157171073295409L;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String method = req.getMethod().toUpperCase();
        if ("GET".equals(method)) getJavaScript(req, resp, getServletContext()); else if ("POST".equals(method)) call(req, resp, getServletContext()); else throw new ServletException("Unsupported request method: " + method);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        Map<String, String> configMap = new HashMap<String, String>();
        Enumeration<String> keys = config.getInitParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            configMap.put(key, config.getInitParameter(key));
        }
        try {
            mwanzia = new Mwanzia(configMap);
        } catch (Exception e) {
            throw new ServletException("Unable to initialize Mwanzia: " + e.getMessage(), e);
        }
    }

    @Override
    public void destroy() {
    }
}
