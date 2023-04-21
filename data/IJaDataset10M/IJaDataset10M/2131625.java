package com.integrationpath.mengine.console.helper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import javax.servlet.*;

public class ContextInitParametersServletAdaptor implements Servlet {

    private Servlet delegate;

    Properties initParameters;

    public ContextInitParametersServletAdaptor(Servlet delegate, Properties initParameters) {
        this.delegate = delegate;
        this.initParameters = initParameters;
    }

    public void init(ServletConfig config) throws ServletException {
        delegate.init(new ServletConfigAdaptor(config));
    }

    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        delegate.service(request, response);
    }

    public void destroy() {
        delegate.destroy();
    }

    public ServletConfig getServletConfig() {
        return delegate.getServletConfig();
    }

    public String getServletInfo() {
        return delegate.getServletInfo();
    }

    private class ServletConfigAdaptor implements ServletConfig {

        private ServletConfig config;

        private ServletContext context;

        public ServletConfigAdaptor(ServletConfig config) {
            this.config = config;
            this.context = new ServletContextAdaptor(config.getServletContext());
        }

        public String getInitParameter(String arg0) {
            return config.getInitParameter(arg0);
        }

        public Enumeration getInitParameterNames() {
            return config.getInitParameterNames();
        }

        public ServletContext getServletContext() {
            return context;
        }

        public String getServletName() {
            return config.getServletName();
        }
    }

    private class ServletContextAdaptor implements ServletContext {

        private ServletContext delegate;

        public ServletContextAdaptor(ServletContext delegate) {
            this.delegate = delegate;
        }

        public RequestDispatcher getRequestDispatcher(String path) {
            return delegate.getRequestDispatcher(path);
        }

        public URL getResource(String name) throws MalformedURLException {
            return delegate.getResource(name);
        }

        public InputStream getResourceAsStream(String name) {
            return delegate.getResourceAsStream(name);
        }

        public Set getResourcePaths(String name) {
            return delegate.getResourcePaths(name);
        }

        public Object getAttribute(String arg0) {
            return delegate.getAttribute(arg0);
        }

        public Enumeration getAttributeNames() {
            return delegate.getAttributeNames();
        }

        public ServletContext getContext(String arg0) {
            return delegate.getContext(arg0);
        }

        public String getInitParameter(String arg0) {
            return initParameters.getProperty(arg0);
        }

        public Enumeration getInitParameterNames() {
            return initParameters.propertyNames();
        }

        public int getMajorVersion() {
            return delegate.getMajorVersion();
        }

        public String getMimeType(String arg0) {
            return delegate.getMimeType(arg0);
        }

        public int getMinorVersion() {
            return delegate.getMinorVersion();
        }

        public RequestDispatcher getNamedDispatcher(String arg0) {
            return delegate.getNamedDispatcher(arg0);
        }

        public String getRealPath(String arg0) {
            return delegate.getRealPath(arg0);
        }

        public String getServerInfo() {
            return delegate.getServerInfo();
        }

        /** @deprecated **/
        public Servlet getServlet(String arg0) throws ServletException {
            return delegate.getServlet(arg0);
        }

        public String getServletContextName() {
            return delegate.getServletContextName();
        }

        /** @deprecated **/
        public Enumeration getServletNames() {
            return delegate.getServletNames();
        }

        /** @deprecated **/
        public Enumeration getServlets() {
            return delegate.getServlets();
        }

        /** @deprecated **/
        public void log(Exception arg0, String arg1) {
            delegate.log(arg0, arg1);
        }

        public void log(String arg0, Throwable arg1) {
            delegate.log(arg0, arg1);
        }

        public void log(String arg0) {
            delegate.log(arg0);
        }

        public void removeAttribute(String arg0) {
            delegate.removeAttribute(arg0);
        }

        public void setAttribute(String arg0, Object arg1) {
            delegate.setAttribute(arg0, arg1);
        }

        public String getContextPath() {
            try {
                Method getContextPathMethod = delegate.getClass().getMethod("getContextPath", null);
                return (String) getContextPathMethod.invoke(delegate, null);
            } catch (Exception e) {
            }
            return null;
        }
    }
}
