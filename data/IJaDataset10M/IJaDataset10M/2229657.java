package com.tchepannou.rails.core.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Implementation of {@link Cache} based on {@link java.util.Map}
 * @author herve
 */
public class MapCache implements Cache {

    private Map _cache = new HashMap();

    public void clear() {
        _cache.clear();
    }

    public void init(Properties props) {
        _cache = new HashMap();
    }

    public Object get(Object key) {
        return _cache.get(key);
    }

    public void put(Object key, Object o) {
        _cache.put(key, o);
    }

    public Object remove(Object key) {
        return _cache.remove(key);
    }

    public Map mget(List keys) {
        Map data = new HashMap();
        for (Object key : keys) {
            Object value = get(key);
            if (value != null) {
                data.put(key, value);
            }
        }
        return data;
    }

    public void mput(Map data) {
        for (Object key : data.keySet()) {
            Object value = data.get(key);
            if (value != null) {
                put(key, value);
            }
        }
    }

    public Map mremove(List keys) {
        Map data = new HashMap();
        for (Object key : keys) {
            Object value = remove(key);
            if (value != null) {
                data.put(key, value);
            }
        }
        return data;
    }
}
