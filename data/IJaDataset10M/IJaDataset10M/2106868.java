package org.jadapter.servicelookup;

import java.util.Map;

/**
 * Simple map-backed implementation of the service lookup delegate.
 * Useful for testing, if nothing else. See also
 * org.jadapter.spring.SpringBeanServiceLookupDelegate.
 * @author optilude
 */
public class SimpleServiceLookupDelegate implements ServiceLookupDelegate {

    private Map<Class<?>, Map<String, Object>> services;

    /**
	 * Initialise with a map of services. The keys are the service types.
	 * The values are a map of names to service instances. The default
	 * instance should have the name "".
	 * @param services
	 */
    public SimpleServiceLookupDelegate(Map<Class<?>, Map<String, Object>> services) {
        this.services = services;
    }

    /**
	 * Get the services map.
	 */
    public Map<Class<?>, Map<String, Object>> getServicesMap() {
        return services;
    }

    public <T> T lookupService(Class<T> type) {
        Map<String, Object> services = this.services.get(type);
        if (services == null) {
            return null;
        }
        return (T) services.get("");
    }

    public <T> T lookupService(Class<T> type, String name) {
        Map<String, Object> services = this.services.get(type);
        if (services == null) {
            return null;
        }
        return (T) services.get(name);
    }
}
