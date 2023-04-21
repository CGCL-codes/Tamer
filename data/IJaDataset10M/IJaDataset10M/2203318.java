package de.hdtconsulting.yahoo.finance.server.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface IYCache<K, V> {

    public abstract void clear();

    public abstract boolean containsKey(K key);

    public abstract boolean containsValue(V value);

    public abstract V get(K key);

    public abstract Set<java.util.Map.Entry<K, V>> entrySet();

    public abstract boolean isEmpty();

    public abstract Set<K> keySet();

    public abstract V put(K key, V value);

    public abstract void putAll(Map<? extends K, ? extends V> m);

    public abstract V remove(K key);

    public abstract int size();

    public abstract Collection<V> values();
}
