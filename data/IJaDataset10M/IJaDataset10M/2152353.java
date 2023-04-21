package org.ice.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRegistry implements IRegistry {

    private Map<String, Object> map = new ConcurrentHashMap<String, Object>();

    public void set(String key, Object value) {
        if (key != null) map.put(key, value);
    }

    public Object get(String key) {
        try {
            return map.get(key);
        } catch (Exception ex) {
            return null;
        }
    }
}
