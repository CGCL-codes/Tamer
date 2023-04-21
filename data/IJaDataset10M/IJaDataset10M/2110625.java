package adt;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 * @param <K>
 *            keys type.
 * @param <V>
 *            values type.
 * 
 */
public class HashMultiValMap<K, V> implements MultiValMap<K, V>, Serializable {

    private static final long serialVersionUID = -6028202660344028763L;

    Map<K, Set<V>> data = new HashMap<K, Set<V>>();

    @Override
    public boolean containsKey(K key) {
        return data.containsKey(key);
    }

    @Override
    public Collection<V> get(K key) {
        return data.get(key);
    }

    @Override
    public void put(K key, V value) {
        Set<V> values;
        if (data.containsKey(key)) {
            values = data.get(key);
        } else {
            values = new HashSet<V>();
            data.put(key, values);
        }
        values.add(value);
    }

    @Override
    public void put(K key, Collection<V> vs) {
        Set<V> values;
        if (data.containsKey(key)) {
            values = data.get(key);
        } else {
            values = new HashSet<V>();
            data.put(key, values);
        }
        values.addAll(vs);
    }

    @Override
    public void remove(K key, V value) {
        if (!data.containsKey(key)) return;
        Set<V> values = data.get(key);
        values.remove(value);
        if (values.size() == 0) data.remove(key);
    }

    @Override
    public void remove(K key, Collection<V> vs) {
        if (!data.containsKey(key)) return;
        Set<V> values = data.get(key);
        values.removeAll(vs);
        if (values.size() == 0) data.remove(key);
    }

    @Override
    public void removeAll(K key) {
        if (!data.containsKey(key)) return;
        Set<V> values = data.get(key);
        values.clear();
        data.remove(key);
    }

    @Override
    public Collection<K> keys() {
        return data.keySet();
    }

    @Override
    public int size() {
        return data.size();
    }
}
