package eu.ict.persist.examples.HelloWorld.proxy;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.ServiceRegistration;
import org.personalsmartspace.sre.api.pss3p.callback.IProxyFactory;

/**
 * @author Kajetan Dolinar
 *
 */
public class Activator implements BundleActivator {

    private ServiceRegistration sreIProxyFactory;

    public void start(BundleContext bundleContext) throws Exception {
        IProxyFactory factory = new HelloWorldServiceProxyFactory(bundleContext);
        sreIProxyFactory = bundleContext.registerService(IProxyFactory.class.getName(), factory, null);
    }

    public void stop(BundleContext bundleContext) throws Exception {
        sreIProxyFactory.unregister();
    }
}
