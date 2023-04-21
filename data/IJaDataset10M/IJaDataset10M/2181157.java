package ServiceDDS.service.registry;

import ServiceDDS.service.RemoteServiceInstance;
import ServiceDDS.service.contract.ServiceContract;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * A data structure that contains RemoteServiceInstance objects. In the ServiceDDS context is used
 * to store all the services discovered by the peer.
 * @author Jose Angel Dianes
 * @version 0.1b, 09/24/2010
 */
public class RemoteServicesRegistry {

    Hashtable serviceTable;

    /**
     * Default constructor
     */
    public RemoteServicesRegistry() {
        serviceTable = new Hashtable();
    }

    /**
     * Add a new service reference to the registry
     * @param s The Service to be stored.
     */
    public void addService(RemoteServiceInstance s) {
        serviceTable.put(s.provider + "." + s.contract.serviceName, s);
    }

    /**
     * Get the service reference with the name given
     * @param serviceName The name of the Service
     * @return The Service if exist. Null in other case.
     */
    public ServiceContract getService(String serviceName) {
        return (ServiceContract) (serviceTable.get(serviceName));
    }

    public String toString() {
        String res = new String();
        Iterator it = serviceTable.values().iterator();
        while (it.hasNext()) {
            RemoteServiceInstance newEntry = (RemoteServiceInstance) it.next();
            res = res + newEntry + System.getProperty("line.separator");
        }
        return res;
    }
}
