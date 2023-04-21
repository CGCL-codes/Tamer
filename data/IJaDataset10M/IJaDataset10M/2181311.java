package com.fddtool.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * This is a proxy map that should be used when ne needs to control access to
 * methods declared in <code>Map</code> interface. None of the methods are
 * implemented. Override this class to implement only the required methods.
 * 
 * @author SKhramtc
 * @version $Revision: 1.3 $, $Date: 2009/04/01 00:56:51 $
 * @lastModifiedBy $Author: sergei19 $
 */
public abstract class ProxyMap<K, V> implements Map<K, V> {

    /**
     * Constructor for ProxyMap.
     */
    public ProxyMap() {
        super();
    }

    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean containsKey(Object key) {
        return false;
    }

    public boolean containsValue(Object value) {
        return false;
    }

    public V get(Object key) {
        return null;
    }

    public V put(K key, V value) {
        return null;
    }

    public V remove(Object key) {
        return null;
    }

    public void putAll(Map<? extends K, ? extends V> t) {
    }

    public void clear() {
    }

    public Set<K> keySet() {
        return null;
    }

    public Collection<V> values() {
        return null;
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return null;
    }
}
