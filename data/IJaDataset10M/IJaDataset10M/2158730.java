package eu.ict.persist.RFID.proxy;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.personalsmartspace.sre.api.pss3p.callback.IProxyFactory;
import eu.ict.persist.RFID.api.RFIDAPI;

public class Activator implements BundleActivator {

    private ServiceRegistration sreIProxyFactory;

    public void start(BundleContext bundleContext) throws Exception {
        IProxyFactory factory = new RFIDProxyFactory(bundleContext);
        sreIProxyFactory = bundleContext.registerService(new String[] { IProxyFactory.class.getName() }, factory, null);
    }

    public void stop(BundleContext bundleContext) throws Exception {
        sreIProxyFactory.unregister();
    }
}
