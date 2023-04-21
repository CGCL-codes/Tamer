package eu.ict.persist.examples.PrivacyAwareHelloWorld.impl;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleActivator;
import eu.ict.persist.examples.PrivacyAwareHelloWorld.api.PrivacyAwareHelloWorldService;
import org.personalsmartspace.sre.slm.api.pss3p.callback.IService;

/**
 * Hello world!
 * This is a basic 3rd party service for PSS
 *
 */
public class Activator implements BundleActivator {

    Implementation impl;

    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("Hello World starting");
        impl = new Implementation(context);
        String[] interfaces = { IService.class.getName(), PrivacyAwareHelloWorldService.class.getName() };
        context.registerService(interfaces, impl, null);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("Goodbye World stopping");
    }
}
