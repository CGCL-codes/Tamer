package org.personalsmartspace.pro.decisionMaker.impl;

import org.osgi.util.tracker.ServiceTracker;
import org.osgi.framework.BundleContext;

/**
 * @author Elizabeth
 *
 */
public class ServiceRetriever {

    BundleContext myContext;

    public ServiceRetriever(BundleContext cont) {
        this.myContext = cont;
    }

    public Object getService(String serviceClassName) {
        ServiceTracker servTracker = new ServiceTracker(myContext, serviceClassName, null);
        servTracker.open();
        Object[] services = servTracker.getServices();
        if (null != services) {
            if (services.length > 0) {
                return services[0];
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
