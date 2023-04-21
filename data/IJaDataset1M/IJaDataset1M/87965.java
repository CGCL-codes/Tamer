package org.eclipse.equinox.http.servlet.internal;

import java.io.IOException;
import java.security.AccessController;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.NamespaceException;

/**
 * The ProxyServlet is the private side of a Servlet that when registered (and init() called) in a servlet container
 * will in-turn register and provide an OSGi Http Service implementation.
 * This class is not meant for extending or even using directly and is purely meant for registering
 * in a servlet container.
 */
public class ProxyServlet extends HttpServlet {

    private static final long serialVersionUID = 4117456123807468871L;

    private Map registrations = new HashMap();

    private Set servlets = new HashSet();

    private ProxyContext proxyContext;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        proxyContext = new ProxyContext(config.getServletContext());
        Activator.addProxyServlet(this);
    }

    public void destroy() {
        Activator.removeProxyServlet(this);
        proxyContext.destroy();
        proxyContext = null;
        super.destroy();
    }

    /**
	 * @see HttpServlet#service(ServletRequest, ServletResponse)
	 */
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        proxyContext.initializeServletPath(req);
        String alias = HttpServletRequestAdaptor.getDispatchPathInfo(req);
        if (alias == null) alias = "/";
        if (processAlias(req, resp, alias, null)) return;
        String extensionAlias = findExtensionAlias(alias);
        alias = alias.substring(0, alias.lastIndexOf('/'));
        while (alias.length() != 0) {
            if (processAlias(req, resp, alias, extensionAlias)) return;
            alias = alias.substring(0, alias.lastIndexOf('/'));
        }
        if (extensionAlias != null) extensionAlias = extensionAlias.substring(1);
        if (processAlias(req, resp, "/", extensionAlias)) return;
        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "ProxyServlet: " + req.getRequestURI());
    }

    private String findExtensionAlias(String alias) {
        String lastSegment = alias.substring(alias.lastIndexOf('/') + 1);
        int dot = lastSegment.indexOf('.');
        if (dot == -1) return null;
        String extension = lastSegment.substring(dot + 1);
        if (extension.length() == 0) return null;
        return "/*." + extension;
    }

    private boolean processAlias(HttpServletRequest req, HttpServletResponse resp, String alias, String extensionAlias) throws ServletException, IOException {
        Registration registration = null;
        synchronized (this) {
            if (extensionAlias == null) registration = (Registration) registrations.get(alias); else {
                registration = (Registration) registrations.get(alias + extensionAlias);
                if (registration != null) {
                    if (registration instanceof ServletRegistration) alias = HttpServletRequestAdaptor.getDispatchPathInfo(req);
                } else registration = (Registration) registrations.get(alias);
            }
            if (registration != null) registration.addReference();
        }
        if (registration != null) {
            try {
                if (registration.handleRequest(req, resp, alias)) return true;
            } finally {
                registration.removeReference();
            }
        }
        return false;
    }

    synchronized void unregister(String alias, boolean destroy) {
        Registration removedRegistration = (Registration) registrations.remove(alias);
        if (removedRegistration != null) {
            if (destroy) removedRegistration.destroy();
            removedRegistration.close();
        }
    }

    synchronized void registerServlet(String alias, Servlet servlet, Dictionary initparams, HttpContext context, Bundle bundle) throws ServletException, NamespaceException {
        checkAlias(alias);
        if (servlet == null) throw new IllegalArgumentException("Servlet cannot be null");
        ServletRegistration registration = new ServletRegistration(servlet, proxyContext, context, bundle, servlets);
        registration.checkServletRegistration();
        ServletContext wrappedServletContext = new ServletContextAdaptor(proxyContext, getServletContext(), context, AccessController.getContext());
        ServletConfig servletConfig = new ServletConfigImpl(servlet, initparams, wrappedServletContext);
        registration.init(servletConfig);
        registrations.put(alias, registration);
    }

    synchronized void registerResources(String alias, String name, HttpContext context) throws NamespaceException {
        checkAlias(alias);
        checkName(name);
        registrations.put(alias, new ResourceRegistration(name, context, getServletContext(), AccessController.getContext()));
    }

    private void checkName(String name) {
        if (name == null) throw new IllegalArgumentException("Name cannot be null");
        if (name.endsWith("/") && !name.equals("/")) throw new IllegalArgumentException("Invalid Name '" + name + "'");
    }

    private void checkAlias(String alias) throws NamespaceException {
        if (alias == null) throw new IllegalArgumentException("Alias cannot be null");
        if (!alias.startsWith("/") || (alias.endsWith("/") && !alias.equals("/"))) throw new IllegalArgumentException("Invalid alias '" + alias + "'");
        if (registrations.containsKey(alias)) throw new NamespaceException("The alias '" + alias + "' is already in use.");
    }
}
