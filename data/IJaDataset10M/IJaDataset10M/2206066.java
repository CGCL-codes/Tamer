package ru.adv.mozart.framework.el;

import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * ReadOnly map
 */
public abstract class AbstractReadOnlyScopeMap extends AbstractMap<String, Object> {

    protected abstract Enumeration<String> getAttributeNames();

    protected abstract Object getAttribute(String name);

    protected void removeAttribute(String name) {
        throw new UnsupportedOperationException();
    }

    protected void setAttribute(String name, Object value) {
        throw new UnsupportedOperationException();
    }

    public final Set<Map.Entry<String, Object>> entrySet() {
        Enumeration<String> e = getAttributeNames();
        Set<Map.Entry<String, Object>> set = new HashSet<Map.Entry<String, Object>>();
        if (e != null) {
            while (e.hasMoreElements()) {
                set.add(new ScopeEntry((String) e.nextElement()));
            }
        }
        return set;
    }

    public final Object get(Object key) {
        if (key != null) {
            return getAttribute(key.toString());
        }
        return null;
    }

    public final Object put(String key, Object value) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (value == null) {
            this.removeAttribute(key);
        } else {
            this.setAttribute(key, value);
        }
        return null;
    }

    public final Object remove(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        this.removeAttribute(key.toString());
        return null;
    }

    /** ===================================================
	 *  ScopeEntry
	 =====================================================*/
    private class ScopeEntry implements Map.Entry<String, Object> {

        private final String key;

        public ScopeEntry(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }

        public Object getValue() {
            return getAttribute(this.key);
        }

        public Object setValue(Object value) {
            if (value == null) {
                removeAttribute(this.key);
            } else {
                setAttribute(this.key, value);
            }
            return null;
        }

        public boolean equals(Object obj) {
            return (obj != null && this.hashCode() == obj.hashCode());
        }

        public int hashCode() {
            return this.key.hashCode();
        }
    }
}
