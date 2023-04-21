package com.googlecode.httl.support.runtime;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import com.googlecode.httl.support.util.ArraySet;
import com.googlecode.httl.support.util.MapEntry;

/**
 * OrderedTypeMap
 * 
 * @author Liang Fei (liangfei0201 AT gmail DOT com)
 */
public class OrderedTypeMap implements Map<String, Class<?>> {

    private final String[] keys;

    private final Class<?>[] values;

    public OrderedTypeMap(String[] keys, Class<?>[] values) {
        if (keys == null) throw new IllegalArgumentException("keys == null");
        if (values == null) throw new IllegalArgumentException("values == null");
        if (keys.length != values.length) throw new IllegalArgumentException("keys.length != values.length");
        this.keys = keys;
        this.values = values;
    }

    public int size() {
        return keys.length;
    }

    public boolean isEmpty() {
        return keys.length > 0;
    }

    public boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        for (String k : keys) {
            if (key.equals(k)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsValue(Object value) {
        if (value == null) {
            return false;
        }
        for (Class<?> v : values) {
            if (value.equals(v)) {
                return true;
            }
        }
        return false;
    }

    public Class<?> get(Object key) {
        if (key == null) {
            return null;
        }
        for (int i = 0; i < keys.length; i++) {
            if (key.equals(keys[i])) {
                return values[i];
            }
        }
        return null;
    }

    public Set<String> keySet() {
        Set<String> set = new ArraySet<String>(keys.length);
        for (String key : keys) {
            set.add(key);
        }
        return set;
    }

    public Collection<Class<?>> values() {
        return Arrays.asList(values);
    }

    public Set<java.util.Map.Entry<String, Class<?>>> entrySet() {
        Set<Entry<String, Class<?>>> set = new ArraySet<Entry<String, Class<?>>>(keys.length);
        for (int i = 0; i < keys.length; i++) {
            set.add(new MapEntry<String, Class<?>>(keys[i], values[i]));
        }
        return set;
    }

    public Class<?> put(String key, Class<?> value) {
        throw new UnsupportedOperationException("Readonly.");
    }

    public Class<?> remove(Object key) {
        throw new UnsupportedOperationException("Readonly.");
    }

    public void putAll(Map<? extends String, ? extends Class<?>> m) {
        throw new UnsupportedOperationException("Readonly.");
    }

    public void clear() {
        throw new UnsupportedOperationException("Readonly.");
    }
}
