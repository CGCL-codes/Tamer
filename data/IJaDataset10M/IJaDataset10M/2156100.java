package org.avis.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import static org.avis.util.Format.appendEscaped;
import static org.avis.util.Format.appendBytes;

public class Notification implements Map<String, Object>, Cloneable {

    private Map<String, Object> attributes;

    public Notification() {
        this.attributes = new HashMap<String, Object>();
    }

    public Notification(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public void clear() {
        attributes.clear();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Notification copy = (Notification) super.clone();
        copy.attributes = new HashMap<String, Object>(attributes);
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        boolean first = true;
        for (Entry<String, Object> entry : attributes.entrySet()) {
            if (!first) str.append('\n');
            first = false;
            appendEscaped(str, entry.getKey(), ' ');
            str.append(": ");
            formatValue(str, entry.getValue());
        }
        return str.toString();
    }

    private static void formatValue(StringBuilder str, Object value) {
        if (value instanceof String) {
            str.append('"');
            appendEscaped(str, value.toString(), '"');
            str.append('"');
        } else if (value instanceof Number) {
            str.append(value);
            if (value instanceof Long) str.append('L');
        } else {
            str.append('[');
            appendBytes(str, (byte[]) value);
            str.append(']');
        }
    }

    public boolean containsKey(Object key) {
        return attributes.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return attributes.containsValue(value);
    }

    public Set<Entry<String, Object>> entrySet() {
        return attributes.entrySet();
    }

    public boolean equals(Object arg0) {
        return attributes.equals(arg0);
    }

    public Object get(Object key) {
        return attributes.get(key);
    }

    public int hashCode() {
        return attributes.hashCode();
    }

    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    public Set<String> keySet() {
        return attributes.keySet();
    }

    public Object put(String key, Object value) {
        return attributes.put(key, value);
    }

    public void putAll(Map<? extends String, ? extends Object> m) {
        attributes.putAll(m);
    }

    public Object remove(Object key) {
        return attributes.remove(key);
    }

    public int size() {
        return attributes.size();
    }

    public Collection<Object> values() {
        return attributes.values();
    }
}
