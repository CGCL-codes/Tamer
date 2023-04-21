package com.fatsatsuma.email;

import java.util.HashMap;
import java.util.Map;
import org.apache.velocity.context.Context;

public class VelocityContext implements IContext, Context {

    private Map<String, Object> internal = new HashMap<String, Object>();

    public void setParameter(String key, Object value) {
        put(key, value);
    }

    public boolean containsKey(Object key) {
        return internal.containsKey(key);
    }

    public Object get(String key) {
        return internal.get(key);
    }

    public Object[] getKeys() {
        return internal.keySet().toArray();
    }

    public Object put(String key, Object value) {
        return put(key, value);
    }

    public Object remove(Object key) {
        return remove(key);
    }

    public Context getContext() {
        return this;
    }
}
