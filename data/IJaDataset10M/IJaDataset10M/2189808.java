package net.sourceforge.ondex.workflow2.base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Implements a store for the shared Objects that can be passed between
 * workflow plugins
 * 
 * @author lysenkoa
 *
 */
public class ResourcePool {

    private final Map<UUID, Object> map = new HashMap<UUID, Object>();

    private final Set<UUID> temp = new HashSet<UUID>();

    public ResourcePool() {
    }

    public UUID putResource(UUID id, Object obj, boolean isTemporary) {
        map.put(id, obj);
        if (isTemporary) {
            temp.add(id);
        }
        return id;
    }

    public boolean setResource(UUID id, Object obj) {
        boolean result = false;
        if (map.containsKey(id)) {
            result = true;
        }
        map.put(id, obj);
        temp.add(id);
        return result;
    }

    public void clearTemporary() {
        map.keySet().removeAll(temp);
        temp.clear();
    }

    public void clear() {
        map.clear();
        temp.clear();
    }

    public Object getResource(UUID id) {
        return map.get(id);
    }
}
