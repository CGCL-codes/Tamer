package powermock.examples.staticmocking;

import java.util.HashMap;
import java.util.Map;

public class ServiceRegistrator {

    /**
	 * Holds all services that has been registered to this service registry.
	 */
    private final Map<Long, Object> serviceRegistry = new HashMap<Long, Object>();

    public long registerService(Object service) {
        final long id = IdGenerator.generateNewId();
        serviceRegistry.put(id, service);
        return id;
    }
}
