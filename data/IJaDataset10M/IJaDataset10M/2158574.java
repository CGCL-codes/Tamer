package ca.beq.util.win32.registry.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        System.loadLibrary("jRegistryKey");
    }

    public void stop(BundleContext context) throws Exception {
    }
}
