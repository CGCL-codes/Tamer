package org.flow.framework.annotations.impl;

import java.util.Hashtable;
import org.flow.framework.IServiceDescriptorRepository;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

    private ServiceRegistration serviceAnnotationSupportRegistration;

    public void start(BundleContext context) throws Exception {
        AnnotatedServiceDescriptorRepository serviceAnnotationSupport = new AnnotatedServiceDescriptorRepository(context);
        serviceAnnotationSupportRegistration = context.registerService(IServiceDescriptorRepository.class.getName(), serviceAnnotationSupport, new Hashtable());
    }

    public void stop(BundleContext context) throws Exception {
        serviceAnnotationSupportRegistration.unregister();
    }
}
