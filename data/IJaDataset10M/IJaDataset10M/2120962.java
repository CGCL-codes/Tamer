package net.sourceforge.birmi;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public abstract class AbstractServiceParty {

    public static final String VERSION = "0.0.1";

    protected Map<String, ServiceDescriptor> services = new HashMap<String, ServiceDescriptor>();

    protected boolean listening = true;

    protected static Logger log;

    /**
	 * Find service with given name
	 * 
	 * @param serviceName
	 * @return
	 */
    ServiceDescriptor getServiceByName(String serviceName) {
        return services.get(serviceName);
    }

    /**
	 * Register server service
	 * 
	 * @param serviceInterfaces
	 * @param implementation
	 * @param serviceName
	 */
    protected void registerService(String serviceName, Object implementation, Class<?>[] serviceInterfaces) {
        ServiceDescriptor descriptor = new ServiceDescriptor();
        descriptor.setServiceName(serviceName);
        descriptor.setServiceInterfaces(serviceInterfaces);
        descriptor.setImplementation(implementation);
        services.put(serviceName, descriptor);
    }

    /**
	 * Register server service
	 * 
	 * @param serviceInterface
	 * @param implementation
	 * @param serviceName
	 */
    protected void registerService(String serviceName, Object implementation, Class<?> serviceInterface) {
        registerService(serviceName, implementation, new Class<?>[] { serviceInterface });
    }

    protected boolean isListening() {
        return listening;
    }
}
