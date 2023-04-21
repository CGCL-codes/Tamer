package net.sf.jimo.equinox;

import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

    private JimoService service;

    private ServiceTracker helloServiceTracker;

    public void start(BundleContext context) throws Exception {
        service = new HelloServiceImpl();
        context.registerService(JimoService.class.getName(), service, new Hashtable());
        helloServiceTracker = new ServiceTracker(context, JimoService.class.getName(), null);
        helloServiceTracker.open();
        service = (JimoService) helloServiceTracker.getService();
        service.speak();
        service.yell();
    }

    public void stop(BundleContext context) throws Exception {
        helloServiceTracker.close();
        helloServiceTracker = null;
        service = null;
    }
}
