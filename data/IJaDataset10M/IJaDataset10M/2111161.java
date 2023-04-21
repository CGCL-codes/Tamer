package org.tolven.web.el;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Map that does nothing except to allow overriding of the get method.
 * This is typically used to implement xxx[yyyy] el semantics.
 * @author John Churin
 *
 */
public abstract class ELMap implements Map<String, String> {

    @Override
    public void clear() {
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Set<java.util.Map.Entry<String, String>> entrySet() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public String put(String key, String value) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
    }

    @Override
    public String remove(Object key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Collection<String> values() {
        return null;
    }
}
