package net.sf.orcc.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class defines a simple MultiMap.
 * 
 * @author Jerome Gorin
 * 
 */
public class MultiMap<K, V> extends AbstractMap<K, Collection<V>> {

    private Map<K, Collection<V>> map;

    public MultiMap() {
        this(null);
    }

    public MultiMap(Map<K, Collection<V>> copy) {
        map = new HashMap<K, Collection<V>>();
        if (copy != null) {
            Iterator<Entry<K, Collection<V>>> iter = copy.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<K, Collection<V>> entry = iter.next();
                addAll(entry.getKey(), entry.getValue());
            }
        }
    }

    public boolean add(K key, V value) {
        return getValues(key).add(value);
    }

    public void addAll(MultiMap<K, V> m) {
        for (Entry<K, Collection<V>> entry : m.entrySet()) {
            addAll(entry.getKey(), entry.getValue());
        }
    }

    public boolean addAll(Object key, Collection<V> values) {
        return getValues(key).addAll(values);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        Collection<V> values = map.get(key);
        return ((values != null) && (values.size() != 0));
    }

    @Override
    public boolean containsValue(Object value) {
        Iterator<Entry<K, Collection<V>>> iter = map.entrySet().iterator();
        boolean found = false;
        while (iter.hasNext()) {
            Entry<K, Collection<V>> entry = iter.next();
            Collection<V> values = entry.getValue();
            if (values.contains(value)) {
                found = true;
                break;
            }
        }
        return found;
    }

    @Override
    public Set<Entry<K, Collection<V>>> entrySet() {
        return map.entrySet();
    }

    @Override
    public Collection<V> get(Object key) {
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            return new ArrayList<V>();
        }
    }

    @SuppressWarnings("unchecked")
    private Collection<V> getValues(Object key) {
        Collection<V> col = map.get(key);
        if (col == null) {
            col = new HashSet<V>();
            map.put((K) key, col);
        }
        return col;
    }

    @Override
    public Collection<V> put(K key, Collection<V> value) {
        if (!(value instanceof Collection)) {
            throw new IllegalArgumentException(value.getClass().toString());
        }
        Collection<V> original = get(key);
        map.put(key, value);
        return original;
    }

    @Override
    public Collection<V> remove(Object key) {
        Collection<V> original = get(key);
        map.remove(key);
        return original;
    }

    public boolean remove(Object key, Object value) {
        Collection<V> values = map.get(key);
        if (values == null) {
            return false;
        } else {
            return values.remove(value);
        }
    }

    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("{");
        Iterator<K> keys = map.keySet().iterator();
        boolean first = true;
        while (keys.hasNext()) {
            if (first) {
                first = false;
            } else {
                buff.append(", ");
            }
            Object key = keys.next();
            Collection<V> values = getValues(key);
            buff.append("[" + key + ": " + values + "]");
        }
        buff.append("}");
        return buff.toString();
    }
}
