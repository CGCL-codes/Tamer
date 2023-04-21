package ch.exm.storm.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MultiValueMap<K, V> extends HashMap<K, Collection<V>> {

    public void add(K key, V value) {
        getValuesForKey(key).add(value);
    }

    public void addAll(K key, Collection<? extends V> values) {
        getValuesForKey(key).addAll(values);
    }

    public void remove(K key, V value) {
        Collection<V> values = get(key);
        if (values != null) {
            values.remove(value);
            if (values.isEmpty()) remove(key);
        }
    }

    private Collection<V> getValuesForKey(K key) {
        Collection<V> values = get(key);
        if (values == null) {
            values = new ArrayList<V>();
            put(key, values);
        }
        return values;
    }
}
