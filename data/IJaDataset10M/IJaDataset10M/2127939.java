package com.integrationpath.mengine.resources;

import javax.servlet.Servlet;
import org.eclipse.equinox.jsp.jasper.JspServlet;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;
import com.integrationpath.mengine.console.helper.BundleEntryHttpContext;
import com.integrationpath.mengine.console.helper.ContextPathServletAdaptor;
import com.integrationpath.mengine.console.service.PluginService;
import com.integrationpath.mengine.resources.service.impl.ResourcesServiceImpl;

/**
 * @author bciurdea@gmail.com
 */
public class Activator implements BundleActivator {

    private ServiceTracker httpServiceTracker;

    public void start(BundleContext context) throws Exception {
        this.httpServiceTracker = new HttpServiceTracker(context);
        this.httpServiceTracker.open();
        PluginService service = new ResourcesServiceImpl();
        context.registerService(PluginService.class.getName(), service, null);
        System.out.println("RESOURCES widget registered.");
    }

    public void stop(BundleContext context) throws Exception {
        httpServiceTracker.open();
        System.out.println("RESOURCES unregistered");
    }

    private class HttpServiceTracker extends ServiceTracker {

        public HttpServiceTracker(BundleContext context) {
            super(context, HttpService.class.getName(), null);
        }

        public Object addingService(ServiceReference reference) {
            final HttpService httpService = (HttpService) context.getService(reference);
            try {
                HttpContext commonContext = new BundleEntryHttpContext(this.context.getBundle(), "/web");
                httpService.registerResources("/resources", "/", commonContext);
                Servlet adaptedJspServlet = new ContextPathServletAdaptor(new JspServlet(this.context.getBundle(), "/web"), "/resources");
                httpService.registerServlet("/resources/*.jsp", adaptedJspServlet, null, commonContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return httpService;
        }

        public void removedService(ServiceReference reference, Object service) {
            final HttpService httpService = (HttpService) service;
            httpService.unregister("/resources");
            httpService.unregister("/resources/*.jsp");
            super.removedService(reference, service);
        }
    }
}
