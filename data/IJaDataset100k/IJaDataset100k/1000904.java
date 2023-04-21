package org.tcpfile.cache;

import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.tcpfile.utils.PeriodChecker;

/**
 * @author stivo
 * 
 * Basic cache:
 * Use this instead of WeakHashMap! Weak references are automatically collected as soon as the object has 
 * only this reference.
 * Soft References are just collected when there is low memory and the Object is not referenced another time.
 * Can ensure a minimum amount of referenced objects.
 * Also: get is not synchronized for performance reasons.
 * 
 * Inspired by:
 * http://archive.devx.com/java/free/articles/Kabutz01/Kabutz01-2.asp
 * 
 * @param <K> Keys
 * @param <V> Values
 */
public class SynchronizedSoftHashMap<K, V> extends AbstractMap<K, V> {

    private final Map<K, SoftReference<V>> map = new HashMap<K, SoftReference<V>>();

    private final PeriodChecker removeOldPeriod = new PeriodChecker(1000);

    private final LinkedList<V> hardRef = new LinkedList<V>();

    int maxSize = 0;

    /**
	 * Creates a Map with Soft references.
	 * Soft references are unlike Weak references not garbage collected as soon as they are anywhere else accessed,
	 * they are just collected if the memory is running low.
	 */
    public SynchronizedSoftHashMap() {
    }

    /**
	 * Ensures that the last size accessed Objects are kept in memory! Use with caution
	 * @param size
	 */
    public SynchronizedSoftHashMap(int size) {
        this.maxSize = size;
    }

    public int removeOld() {
        if (removeOldPeriod.check()) {
            synchronized (map) {
                ArrayList<K> remove = new ArrayList<K>();
                for (K k : map.keySet()) {
                    if (map.get(k).get() == null) remove.add(k);
                }
                for (K k : remove) {
                    map.remove(k);
                }
                return remove.size();
            }
        }
        return 0;
    }

    public V put(K k, V t) {
        synchronized (map) {
            removeOld();
            SoftReference<V> softie = new SoftReference<V>(t);
            map.size();
            map.put(k, softie);
            return t;
        }
    }

    public V get(Object k) {
        SoftReference<V> soft = map.get(k);
        if (soft == null) return null;
        V v = soft.get();
        if (v != null) {
            if (maxSize > 0) {
                hardRef.addFirst(v);
                if (hardRef.size() > maxSize) hardRef.removeLast();
            }
            return v;
        }
        synchronized (map) {
            map.remove(k);
        }
        return null;
    }

    @Override
    public boolean containsKey(Object arg0) {
        return map.get(arg0) != null;
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Soft references should not be iterated");
    }

    public void clear() {
        map.clear();
    }

    @Override
    @Deprecated
    public boolean containsValue(Object arg0) {
        throw new UnsupportedOperationException("Does not make sense");
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    @Deprecated
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    @Deprecated
    public void putAll(Map<? extends K, ? extends V> arg0) {
        throw new UnsupportedOperationException("Does not make sense");
    }

    @Override
    public V remove(Object arg0) {
        try {
            return map.remove(arg0).get();
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    @Deprecated
    public Collection<V> values() {
        throw new UnsupportedOperationException("Does not make Sense");
    }
}
