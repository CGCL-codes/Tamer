package org.jostraca.comp.apache.oro.util;

import java.util.*;

/**
 * This is the base class for all cache implementations provided in the
 * org.jostraca.comp.apache.oro.util package.  To derive a subclass from GenericCache
 * only the ... methods
 * need be overridden.
 * Although 4 subclasses of GenericCache are provided with this
 * package, users may not derive subclasses from this class.
 * Rather, users should create their own implmentations of the
 * {@link Cache} interface.
 *
 * @author <a href="mailto:oro-dev@jakarta.apache.org">Daniel F. Savarese</a>
 * @version @version@
 * @since 1.0
 * @see Cache
 * @see CacheLRU
 * @see CacheFIFO
 * @see CacheFIFO2
 * @see CacheRandom
 */
public abstract class GenericCache implements Cache, java.io.Serializable {

    /**
   * The default capacity to be used by the GenericCache subclasses
   * provided with this package.  Its value is 20.
   */
    public static final int DEFAULT_CAPACITY = 20;

    int _numEntries;

    GenericCacheEntry[] _cache;

    HashMap _table;

    /**
   * The primary constructor for GenericCache.  It has default
   * access so it will only be used within the package.  It initializes
   * _table to a Hashtable of capacity equal to the capacity argument,
   * _cache to an array of size equal to the capacity argument, and
   * _numEntries to 0.
   * <p>
   * @param capacity The maximum capacity of the cache.
   */
    GenericCache(int capacity) {
        _numEntries = 0;
        _table = new HashMap(capacity);
        _cache = new GenericCacheEntry[capacity];
        while (--capacity >= 0) _cache[capacity] = new GenericCacheEntry(capacity);
    }

    public abstract void addElement(Object key, Object value);

    public synchronized Object getElement(Object key) {
        Object obj;
        obj = _table.get(key);
        if (obj != null) return ((GenericCacheEntry) obj)._value;
        return null;
    }

    public final Iterator keys() {
        return _table.keySet().iterator();
    }

    /**
   * Returns the number of elements in the cache, not to be confused with
   * the {@link #capacity()} which returns the number
   * of elements that can be held in the cache at one time.
   * <p>
   * @return  The current size of the cache (i.e., the number of elements
   *          currently cached).
   */
    public final int size() {
        return _numEntries;
    }

    /**
   * Returns the maximum number of elements that can be cached at one time.
   * <p>
   * @return The maximum number of elements that can be cached at one time.
   */
    public final int capacity() {
        return _cache.length;
    }

    public final boolean isFull() {
        return (_numEntries >= _cache.length);
    }
}
