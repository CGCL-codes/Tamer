package org.extwind.osgi.service.http;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

/**
 * @author donf.yang
 * 
 */
public class HttpServiceImpl implements HttpService {

    private Bundle bundle;

    private ServiceListener listener;

    private Set<String> aliases = new HashSet<String>();

    public HttpServiceImpl(Bundle bundle, ServiceListener listener) {
        this.bundle = bundle;
        this.listener = listener;
    }

    protected Bundle getBundle() {
        return this.bundle;
    }

    protected synchronized void destroy() {
        for (String alias : aliases) {
            listener.unregister(alias, bundle);
        }
    }

    public synchronized void registerResources(String alias, String path, HttpContext context) throws NamespaceException {
        HttpContext _context = context == null ? createDefaultHttpContext() : context;
        listener.registerResources(alias, path, _context, bundle);
        this.aliases.add(alias);
    }

    public synchronized void registerServlet(String alias, Servlet servlet, Dictionary initparams, HttpContext context) throws ServletException, NamespaceException {
        HttpContext _context = context == null ? createDefaultHttpContext() : context;
        listener.registerServlet(alias, servlet, initparams, _context, bundle);
        this.aliases.add(alias);
    }

    public synchronized void unregister(String alias) {
        listener.unregister(alias, bundle);
        aliases.remove(alias);
    }

    public HttpContext createDefaultHttpContext() {
        return new DefaultHttpContext(bundle);
    }
}
