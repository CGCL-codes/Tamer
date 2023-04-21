package org.knopflerfish.bundle.http;

import java.util.Dictionary;
import java.util.Hashtable;
import javax.servlet.ServletContext;
import org.knopflerfish.service.log.LogRef;
import org.osgi.service.http.HttpContext;

public class ServletContextManager {

    private static final class ContextKey {

        int referenceCount = 1;

        final HttpContext httpContext;

        final String realPath;

        ContextKey(HttpContext httpContext, String realPath) {
            this.httpContext = httpContext;
            this.realPath = realPath;
        }

        public int hashCode() {
            return httpContext.hashCode() ^ realPath.hashCode();
        }

        public boolean equals(Object object) {
            if (object instanceof ContextKey) {
                ContextKey key = (ContextKey) object;
                return httpContext.equals(key.httpContext) && realPath.equals(key.realPath);
            }
            return false;
        }
    }

    private final Dictionary keyMap = new Hashtable();

    private final Dictionary contextMap = new Hashtable();

    private final HttpConfig httpConfig;

    private final LogRef log;

    private final Registrations registrations;

    public ServletContextManager(final HttpConfig httpConfig, final LogRef log, final Registrations registrations) {
        this.httpConfig = httpConfig;
        this.log = log;
        this.registrations = registrations;
    }

    public synchronized ServletContext getServletContext(final HttpContext httpContext, String realPath) {
        if (realPath == null) realPath = "";
        final ContextKey key = new ContextKey(httpContext, realPath);
        ServletContextImpl context = (ServletContextImpl) contextMap.get(key);
        if (context == null) {
            context = new ServletContextImpl(httpContext, realPath, httpConfig, log, registrations);
            keyMap.put(context, key);
            contextMap.put(key, context);
        } else {
            ((ContextKey) keyMap.get(context)).referenceCount++;
        }
        return context;
    }

    public synchronized void ungetServletContext(ServletContext context) {
        final ContextKey key = (ContextKey) keyMap.get(context);
        if (key != null && --key.referenceCount <= 0) {
            contextMap.remove(key);
            keyMap.remove(context);
        }
    }
}
