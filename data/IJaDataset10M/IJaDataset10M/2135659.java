package de.schlund.pfixxml.targets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.oro.util.CacheLRU;

public class LRUCache<T1, T2> implements SPCache<T1, T2> {

    public static final int DEFAULT_SIZE = 30;

    private CacheLRU cache;

    public LRUCache() {
        cache = new CacheLRU(DEFAULT_SIZE);
    }

    public void createCache(int capacity) {
        if (capacity <= 0) capacity = DEFAULT_SIZE;
        cache = new CacheLRU(capacity);
    }

    @SuppressWarnings("unchecked")
    public Iterator<T1> getIterator() {
        Map<T1, T2> tmphash = new HashMap<T1, T2>();
        synchronized (cache) {
            for (Iterator<T1> iter = cache.keys(); iter.hasNext(); ) {
                T1 k = iter.next();
                tmphash.put(k, (T2) cache.getElement(k));
            }
        }
        return tmphash.keySet().iterator();
    }

    @SuppressWarnings("unchecked")
    public T2 getValue(T1 key) {
        T2 retval;
        synchronized (cache) {
            retval = (T2) cache.getElement(key);
        }
        return retval;
    }

    public void setValue(T1 key, T2 value) {
        synchronized (cache) {
            cache.addElement(key, value);
        }
    }

    public int getCapacity() {
        return cache.capacity();
    }

    public int getSize() {
        return cache.size();
    }
}
