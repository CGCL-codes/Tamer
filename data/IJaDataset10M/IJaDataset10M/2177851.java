package net.sourceforge.freejava.proxy.java.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import net.sourceforge.freejava.proxy.AbstractProxy;

public class ProxyMap<K, V> extends AbstractProxy<Map<K, V>> implements Map<K, V> {

    public ProxyMap(Map<K, V> proxy) {
        super(proxy);
    }

    public void clear() {
        proxy.clear();
    }

    public boolean containsKey(Object key) {
        return proxy.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return proxy.containsValue(value);
    }

    public Set<Entry<K, V>> entrySet() {
        return proxy.entrySet();
    }

    public V get(Object key) {
        return proxy.get(key);
    }

    public boolean isEmpty() {
        return proxy.isEmpty();
    }

    public Set<K> keySet() {
        return proxy.keySet();
    }

    public V put(K key, V value) {
        return proxy.put(key, value);
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        proxy.putAll(m);
    }

    public V remove(Object key) {
        return proxy.remove(key);
    }

    public int size() {
        return proxy.size();
    }

    public Collection<V> values() {
        return proxy.values();
    }
}
