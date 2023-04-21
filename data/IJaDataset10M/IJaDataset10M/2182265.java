package net.sf.molae.pipe.basic;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Delegates all method calls to another map.
 * Useful base class for map wrapper implementations.
 * @version 2.0
 * @author Ralph Wagner
 */
public class MapProxy<B extends Map<K, V>, K, V> extends ObjectProxy<B> implements Map<K, V> {

    /**
     * Constructs a new proxy of the specified object.
     * @param base the underlying object.
     * @throws NullPointerException if the specified object is
     * <code>null</code>.
     */
    public MapProxy(B base) {
        super(base);
    }

    public boolean equals(Object o) {
        return getBase().equals(o);
    }

    public int size() {
        return getBase().size();
    }

    public boolean isEmpty() {
        return getBase().isEmpty();
    }

    public boolean containsKey(Object o) {
        return getBase().containsKey(o);
    }

    public boolean containsValue(Object value) {
        return getBase().containsValue(value);
    }

    public V get(Object key) {
        return getBase().get(key);
    }

    public V put(K key, V value) {
        return getBase().put(key, value);
    }

    public V remove(Object key) {
        return getBase().remove(key);
    }

    public void putAll(Map<? extends K, ? extends V> t) {
        getBase().putAll(t);
    }

    public void clear() {
        getBase().clear();
    }

    public Set<K> keySet() {
        return getBase().keySet();
    }

    public Collection<V> values() {
        return getBase().values();
    }

    public Set<Entry<K, V>> entrySet() {
        return getBase().entrySet();
    }
}
