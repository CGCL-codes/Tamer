package net.sf.ediknight.codec.csv.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The OSGi activator.
 */
public class ServiceActivator implements BundleActivator {

    /**
     * {@inheritDoc}
     *
     * @see BundleActivator#start(BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        System.err.println("#########  OSGi (start) codec-csv ############");
    }

    /**
     * {@inheritDoc}
     *
     * @see BundleActivator#stop(BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        System.err.println("#########  OSGi (stop) codec-csv ############");
    }
}
