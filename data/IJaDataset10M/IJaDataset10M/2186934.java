package org.judo.service;

import java.util.HashMap;

public class ServiceManager {

    private static HashMap services = new HashMap();

    public static void defineService(String name, JudoService service) {
        services.put(name, service);
    }

    public static JudoService locateService(String serviceName, Class type) throws ServiceNotFoundException, IncorectServiceTypeException {
        if (services.containsKey(serviceName)) {
            JudoService service = (JudoService) services.get(serviceName);
            if (type.isAssignableFrom(service.getClass())) return service; else throw new IncorectServiceTypeException("The service '" + serviceName + "' in the component is defined as type '" + type.getCanonicalName() + "' but the service manager has the type as '" + service.getClass().getCanonicalName() + "'.  There types must be assignable.");
        } else {
            throw new ServiceNotFoundException("Service not found: " + serviceName);
        }
    }
}
