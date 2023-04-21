package org.apache.felix.cm.integration.helper;

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ManagedServiceFactory;

public class MultiManagedServiceFactoryTestActivator extends ManagedServiceFactoryTestActivator {

    public static MultiManagedServiceFactoryTestActivator INSTANCE;

    public void start(BundleContext context) throws Exception {
        super.start(context);
        context.registerService(ManagedServiceFactory.class.getName(), this, getServiceProperties(context));
        INSTANCE = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        INSTANCE = null;
        super.stop(context);
    }
}
