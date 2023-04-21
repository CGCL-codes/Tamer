package ch.unibe.eindermu.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author emanuel
 *
 */
public class CacheMap<K, V> implements Map<K, V> {

    private HashMap<K, Integer> index;

    private int capacity = 0;

    private Object[] elements;

    private int last = -1;

    public CacheMap(int capacity) {
        index = new HashMap<K, Integer>(capacity);
        this.capacity = capacity;
        new HashMap<K, Integer>(capacity);
        elements = new Object[capacity];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        elements = new Object[capacity];
        index.clear();
        last = -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(Object key) {
        return index.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(Object value) {
        for (int i = 0; i <= capacity; i++) {
            if (((Entry) elements[i]).value == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        throw new NotImplementedException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V get(Object key) {
        return ((Entry) elements[index.get(key)]).value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return index.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<K> keySet() {
        return index.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V put(K key, V value) {
        if (index.containsKey(key)) {
            ((Entry) elements[index.get(key)]).value = value;
            return value;
        }
        int next = (last + 1) % capacity;
        if (elements[next] != null) {
            index.remove(((Entry) elements[next]).key);
        }
        Entry e = new Entry();
        e.value = value;
        e.key = key;
        elements[next] = e;
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V remove(Object key) {
        return get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return index.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<V> values() {
        Set<V> set = new HashSet<V>();
        for (int i : index.values()) {
            set.add(((Entry) elements[i]).value);
        }
        return set;
    }

    private class Entry {

        public K key;

        public V value;
    }
}
