package org.cishell.framework;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * A simple implementation of {@link CIShellContext} that pulls the
 * CIShell services from the provided {@link BundleContext} that all OSGi
 * bundles receive on activation. This was included in the standard API since 
 * it will be used frequently by CIShell application developers. 
 * 
 * This implementation only returns standard services or the service strings
 * given to it in its constructor.
 * 
 * @author Bruce Herr (bh2@bh2.net)
 */
public class LocalCIShellContext implements CIShellContext {

    private BundleContext bContext;

    private String[] standardServices;

    /**
     * Initializes the CIShell context based on the provided 
     * <code>BundleContext</code>
     * 
     * @param bContext The <code>BundleContext</code> to use to find 
     *                 the registered standard services
     */
    public LocalCIShellContext(BundleContext bContext) {
        this(bContext, DEFAULT_SERVICES);
    }

    /**
     * Initializes the CIShell context with a custom set of standard services.
     * Only the service in the array will be allowed to be retrieved from 
     * this <code>CIShellContext</code>.
     * 
     * @param bContext         The <code>BundleContext</code> to use to find 
     *                         registered standard services
     * @param standardServices An array of strings specifying the services that
     *                         are allowed to be retrieved from this class
     */
    public LocalCIShellContext(BundleContext bContext, String[] standardServices) {
        this.bContext = bContext;
        this.standardServices = standardServices;
    }

    /**
     * @see org.cishell.framework.CIShellContext#getService(java.lang.String)
     */
    public Object getService(String service) {
        for (int i = 0; i < standardServices.length; i++) {
            if (standardServices[i].equals(service)) {
                ServiceReference ref = bContext.getServiceReference(service);
                if (ref != null) {
                    return bContext.getService(ref);
                } else {
                    throw new RuntimeException("Standard CIShell Service: " + service + " not installed!");
                }
            }
        }
        ServiceReference ref = bContext.getServiceReference(service);
        return bContext.getService(ref);
    }
}
