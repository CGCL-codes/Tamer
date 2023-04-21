package net.sf.opentranquera.web.utils;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Este filtro verifica que no existan mas de 2 posts (double post) durante el mismo request.
 * @author Guillermo Meyer
 */
public class DoublePostCheckerFilter implements Filter {

    public static final String ATTR_MAP = DoublePostCheckerFilter.class.getName() + ".CurrentURLs";

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (!this.isURLCurrentlyRunning(request)) {
            try {
                chain.doFilter(req, resp);
            } finally {
                this.endURLCurrentlyRunning(request);
            }
        } else {
            System.out.println("El URI esta duplicado: " + request.getRequestURI());
            response.sendError(204, "El request ya esta siendo procesado");
        }
    }

    public void destroy() {
    }

    private Map getCurrentURLs(HttpServletRequest request) {
        Map map = (Map) request.getSession().getAttribute(ATTR_MAP);
        if (map == null) {
            map = new Hashtable();
            request.getSession().setAttribute(ATTR_MAP, map);
        }
        return map;
    }

    private boolean isURLCurrentlyRunning(HttpServletRequest request) {
        synchronized (request.getSession()) {
            String url = request.getRequestURI();
            boolean ret = this.getCurrentURLs(request).containsKey(url);
            if (!ret) this.getCurrentURLs(request).put(url, "");
            return ret;
        }
    }

    private void endURLCurrentlyRunning(HttpServletRequest request) {
        synchronized (request.getSession()) {
            String url = request.getRequestURI();
            this.getCurrentURLs(request).remove(url);
        }
    }
}
