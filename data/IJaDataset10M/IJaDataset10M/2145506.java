package org.columba.core.services;

import java.util.Hashtable;
import java.util.Map;
import org.columba.api.exception.ServiceNotFoundException;

/**
 * Service registry and locator. Upper layers can register a service which can
 * be used by others. A service is registered using its full interface name
 * including package and the name of the implementation.
 * <p>
 * <code>ServiceManager</code> uses reflection to instanciate the
 * implementation.
 * <p>
 * This registry should be only used to enable different components to interact
 * with each other. For example: Mail component makes use of the addressbook
 * component
 * 
 * @author fdietz
 */
public class ServiceRegistry {

    private static ServiceRegistry instance = new ServiceRegistry();

    private Map<Class, Object> map = new Hashtable<Class, Object>();

    private ServiceRegistry() {
    }

    public static ServiceRegistry getInstance() {
        return instance;
    }

    public void register(Class serviceInterface, Object serviceInstance) {
        Service service = new Service(serviceInterface, serviceInstance);
        map.put(serviceInterface, service);
    }

    public void unregister(Class serviceInterface) {
        map.remove(serviceInterface);
    }

    public Object getService(Class serviceInterface) throws ServiceNotFoundException {
        Object o = null;
        Service service = null;
        if (map.containsKey(serviceInterface)) {
            service = (Service) map.get(serviceInterface);
            if (service != null) o = service.getServiceInstance();
        }
        if (o == null) throw new ServiceNotFoundException(serviceInterface);
        return o;
    }
}
