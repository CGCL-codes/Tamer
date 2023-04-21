package openrpg2.common.core.engine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import openrpg2.common.module.*;

/**
 *
 * @author Snowdog
 */
public class MessageRegistry {

    HashMap registry = new HashMap();

    /**
     * Creates a new instance of MessageRegistry 
     */
    public MessageRegistry() {
    }

    public synchronized void register(String messageType, NetworkedModule reference) {
        if (registry.containsKey(messageType)) {
        } else {
            registry.put(messageType, reference);
        }
    }

    public synchronized void deregister(String messageType) {
        registry.remove(messageType);
    }

    public synchronized NetworkedModule getReference(String type) throws NoSuchModuleException {
        if (registry.containsKey(type)) {
            return (NetworkedModule) registry.get(type);
        }
        throw new NoSuchModuleException();
    }

    public synchronized boolean hasRegistered(String type) {
        if (registry.containsKey(type)) {
            return true;
        }
        return false;
    }

    public synchronized void showKeys() {
        Set s = registry.keySet();
        Iterator i = s.iterator();
        while (i.hasNext()) {
            System.out.println("Key: " + (String) i.next());
        }
    }
}
