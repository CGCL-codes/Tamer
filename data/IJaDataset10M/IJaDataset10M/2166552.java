package flex.management;

import flex.messaging.MessageBroker;
import java.util.Iterator;
import java.util.Set;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * Helper class for managing MBean lifecycles externally from the core server
 * components where necessary.
 * 
 * @author shodgson
 */
public class MBeanLifecycleManager {

    /**
     * Unregisters all runtime MBeans that are registered in the same domain as the 
     * MessageBrokerControl for the target MessageBroker. 
     *  
     * @param broker The MessageBroker component that has been stopped.
     */
    public static void unregisterRuntimeMBeans(MessageBroker broker) {
        MBeanServer server = MBeanServerLocatorFactory.getMBeanServerLocator().getMBeanServer();
        ObjectName brokerMBean = broker.getControl().getObjectName();
        String domain = brokerMBean.getDomain();
        try {
            ObjectName pattern = new ObjectName(domain + ":*");
            Set names = server.queryNames(pattern, null);
            Iterator iter = names.iterator();
            while (iter.hasNext()) {
                ObjectName on = (ObjectName) iter.next();
                server.unregisterMBean(on);
            }
        } catch (Exception e) {
        }
    }
}
