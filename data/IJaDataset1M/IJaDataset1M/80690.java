package com.sworddance.beans;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import com.sworddance.util.AbstractParameterizedCallableImpl;
import com.sworddance.util.CUtilities;

/**
 * Map that is keyed by class and searches for a value by examining the class hierarchy.
 *
 * Useful for finding helper classes that can be used by subclasses.
 * @param <V>
 *
 */
public class MapByClass<V> implements ConcurrentMap<Class<?>, V> {

    private ConcurrentMap<Class<?>, V> byClassMap = new ConcurrentHashMap<Class<?>, V>();

    public MapByClass() {
    }

    /**
     * this constructor is like this to avoid null varargs eclipse warning messages.
     * @param firstMap (treated same as maps)
     * @param maps
     */
    public MapByClass(Map<Class<?>, V> firstMap, Map<Class<?>, V>... maps) {
        if (firstMap != null && !firstMap.isEmpty()) {
            byClassMap.putAll(firstMap);
        }
        for (Map<Class<?>, V> map : maps) {
            if (map != null && !map.isEmpty()) {
                byClassMap.putAll(map);
            }
        }
    }

    public V get(Object key) {
        V valueByClass = null;
        if (key instanceof Class<?>) {
            Class<?> clazz = (Class<?>) key;
            valueByClass = getRaw(clazz);
            if (valueByClass == null) {
                valueByClass = new InitializeEntry<V>().executeCall(this, clazz);
                if (valueByClass != null) {
                    byClassMap.put(clazz, valueByClass);
                }
            }
        }
        return valueByClass;
    }

    /**
     * @param clazz
     * @return actual value stored in map - the clazz hierarchy structure is not traced.
     */
    public V getRaw(Class<?> clazz) {
        return byClassMap.get(clazz);
    }

    /**
     * @see java.util.Map#clear()
     */
    @Override
    public void clear() {
        this.byClassMap.clear();
    }

    /**
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(Object key) {
        return this.byClassMap.containsKey(key);
    }

    /**
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    @Override
    public boolean containsValue(Object value) {
        return this.byClassMap.containsValue(value);
    }

    /**
     *
     * @see java.util.Map#entrySet()
     */
    @Override
    public Set<Entry<Class<?>, V>> entrySet() {
        return this.byClassMap.entrySet();
    }

    /**
     * @see java.util.Map#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return this.byClassMap.isEmpty();
    }

    /**
     * @see java.util.Map#keySet()
     */
    @Override
    public Set<Class<?>> keySet() {
        return this.byClassMap.keySet();
    }

    /**
     * @see java.util.Map#size()
     */
    @Override
    public int size() {
        return this.byClassMap.size();
    }

    /**
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public V put(Class<?> key, V value) {
        return this.byClassMap.put(key, value);
    }

    /**
     * @see java.util.Map#putAll(java.util.Map)
     */
    @Override
    public void putAll(Map<? extends Class<?>, ? extends V> m) {
        this.byClassMap.putAll(m);
    }

    /**
     * @see java.util.concurrent.ConcurrentMap#putIfAbsent(java.lang.Object, java.lang.Object)
     */
    @Override
    public V putIfAbsent(Class<?> key, V value) {
        return this.byClassMap.putIfAbsent(key, value);
    }

    /**
     * @see java.util.concurrent.ConcurrentMap#remove(java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean remove(Object key, Object value) {
        return remove(key, value);
    }

    /**
     * @see java.util.concurrent.ConcurrentMap#replace(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean replace(Class<?> key, V oldValue, V newValue) {
        return replace(key, oldValue, newValue);
    }

    /**
     * @see java.util.concurrent.ConcurrentMap#replace(java.lang.Object, java.lang.Object)
     */
    @Override
    public V replace(Class<?> key, V value) {
        return replace(key, value);
    }

    /**
     * @see java.util.Map#remove(java.lang.Object)
     */
    @Override
    public V remove(Object key) {
        return this.byClassMap.remove(key);
    }

    /**
     * @see java.util.Map#values()
     */
    @Override
    public Collection<V> values() {
        return this.byClassMap.values();
    }

    @Override
    public String toString() {
        return this.byClassMap.toString();
    }

    static class InitializeEntry<V> extends AbstractParameterizedCallableImpl<V> {

        /**
         * @see com.sworddance.util.ParameterizedCallable#executeCall(java.lang.Object[])
         */
        @Override
        public V executeCall(Object... parameters) {
            MapByClass<V> mapByClass = CUtilities.get(parameters, 0);
            Class<?> clazz = CUtilities.get(parameters, 1);
            V valueByClass = null;
            for (Class<?> interf : clazz.getInterfaces()) {
                valueByClass = mapByClass.getRaw(interf);
                if (valueByClass != null) {
                    break;
                }
            }
            Class<?> superclass = clazz.getSuperclass();
            if (valueByClass == null && superclass != null) {
                valueByClass = mapByClass.getRaw(clazz.getSuperclass());
            }
            if (valueByClass == null && clazz.getSuperclass() != null) {
                valueByClass = mapByClass.get(clazz.getSuperclass());
            }
            if (valueByClass == null) {
                for (Class<?> interf : clazz.getInterfaces()) {
                    valueByClass = mapByClass.get(interf);
                    if (valueByClass != null) {
                        break;
                    }
                }
            }
            return valueByClass;
        }
    }
}
