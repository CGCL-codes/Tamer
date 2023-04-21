package powermock.examples.tutorial.staticmocking.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import powermock.examples.tutorial.staticmocking.IServiceRegistrator;
import powermock.examples.tutorial.staticmocking.osgi.BundleContext;
import powermock.examples.tutorial.staticmocking.osgi.ServiceRegistration;

/**
 * An "OSGi"-ish implementation of the {@link IServiceRegistrator} interface.
 * The test for this class demonstrates static mocking as well as getting and
 * setting internal state.
 */
public final class ServiceRegistrator implements IServiceRegistrator {

    private BundleContext bundleContext;

    /**
    * Holds all services registrations that has been registered by this service registrator.
    */
    private final Map<Long, ServiceRegistration> serviceRegistrations;

    /**
    * Default constructor, initializes internal state.
    */
    public ServiceRegistrator() {
        serviceRegistrations = new ConcurrentHashMap<Long, ServiceRegistration>();
    }

    public long registerService(String name, Object serviceImplementation) {
        ServiceRegistration registerService = bundleContext.registerService(name, serviceImplementation, null);
        long id = IdGenerator.generateNewId();
        serviceRegistrations.put(id, registerService);
        return id;
    }

    public void unregisterService(long id) {
        ServiceRegistration registration = serviceRegistrations.remove(id);
        if (registration == null) {
            throw new IllegalStateException("Registration with id " + id + " has already been removed or has never been registered");
        }
        registration.unregister();
    }
}
