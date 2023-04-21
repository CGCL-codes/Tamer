package com.osgisamples.congress.dispatcher.activator;

import javax.servlet.Servlet;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;
import com.osgisamples.congress.dispatcher.SOAPDispatcher;
import com.osgisamples.congress.servicelocator.ServiceLocator;
import com.osgisamples.congress.servicelocator.XmlWebServiceProviderLocator;
import com.osgisamples.congress.servlet.SchemaServlet;

public class Activator implements BundleActivator {

    private ServletRegistrar servletRegistrar;

    public void start(BundleContext context) throws Exception {
        ServiceLocator locator = new XmlWebServiceProviderLocator(context);
        SOAPDispatcher.setServiceLocator(locator);
        SchemaServlet schemaServlet = new SchemaServlet();
        schemaServlet.setServiceLocator(locator);
        servletRegistrar = new ServletRegistrar(context, "/wsdl", schemaServlet);
        servletRegistrar.open();
    }

    public void stop(BundleContext context) throws Exception {
        servletRegistrar.close();
        servletRegistrar = null;
    }

    private class ServletRegistrar extends ServiceTracker {

        private Servlet registeredServlet;

        private String contextPath;

        public ServletRegistrar(BundleContext context, String contextPath, Servlet servlet) {
            super(context, HttpService.class.getName(), null);
            this.registeredServlet = servlet;
            this.contextPath = contextPath;
        }

        public Object addingService(ServiceReference reference) {
            HttpService httpService = (HttpService) context.getService(reference);
            try {
                httpService.registerServlet(contextPath, registeredServlet, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return httpService;
        }

        public void removedService(ServiceReference reference, Object service) {
            HttpService httpService = (HttpService) service;
            httpService.unregister(contextPath);
            super.removedService(reference, service);
        }
    }
}
