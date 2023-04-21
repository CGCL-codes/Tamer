package net.sourceforge.ondex.core.memory.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * A shim to manage lazily instantiated maps.
 *
 * @author Matthew Pocock
 */
public abstract class MapShim<K, V> implements Map<K, V> {

    public abstract Map<K, V> fetchDelegate();

    public abstract void updateDelegate(Map<K, V> delegate);

    public abstract Map<K, V> createSmallMap(Map<? extends K, ? extends V> m);

    @Override
    public int size() {
        Map<K, V> d = fetchDelegate();
        if (d == null) return 0;
        return d.size();
    }

    @Override
    public boolean isEmpty() {
        Map<K, V> d = fetchDelegate();
        return d == null || d.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        Map<K, V> d = fetchDelegate();
        return d != null && d.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        Map<K, V> d = fetchDelegate();
        return d != null && d.containsValue(value);
    }

    @Override
    public V get(Object key) {
        Map<K, V> d = fetchDelegate();
        return d == null ? null : d.get(key);
    }

    @Override
    public V put(K key, V value) {
        Map<K, V> d = fetchDelegate();
        if (d == null) {
            updateDelegate(Collections.singletonMap(key, value));
            return null;
        } else if (d.size() == 1 && d.containsKey(key)) {
            updateDelegate(Collections.singletonMap(key, value));
            return d.get(key);
        } else {
            updateDelegate(createSmallMap(d));
            return fetchDelegate().put(key, value);
        }
    }

    @Override
    public V remove(Object key) {
        Map<K, V> d = fetchDelegate();
        if (d == null) {
            return null;
        } else if (d.size() == 1) {
            V v = d.get(key);
            if (v == null) return null; else {
                updateDelegate(null);
                return v;
            }
        } else {
            return d.remove(key);
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        Map<K, V> d = fetchDelegate();
        if (d == null) {
            updateDelegate(createSmallMap(m));
        } else if (d.size() == 1) {
            updateDelegate(createSmallMap(d));
            fetchDelegate().putAll(m);
        }
    }

    @Override
    public void clear() {
        updateDelegate(null);
    }

    @Override
    public Set<K> keySet() {
        Map<K, V> d = fetchDelegate();
        return (d == null) ? Collections.<K>emptySet() : d.keySet();
    }

    @Override
    public Collection<V> values() {
        Map<K, V> d = fetchDelegate();
        return (d == null) ? Collections.<V>emptySet() : d.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Map<K, V> d = fetchDelegate();
        return (d == null) ? Collections.<Entry<K, V>>emptySet() : d.entrySet();
    }
}
