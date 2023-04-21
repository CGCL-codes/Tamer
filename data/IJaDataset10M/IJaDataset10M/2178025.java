package org.apache.myfaces.shared_impl.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A bi-level cache based on HashMap for caching objects with minimal sychronization
 * overhead. The limitation is that <code>remove()</code> is very expensive.
 * <p>
 * Access to L1 map is not sychronized, to L2 map is synchronized. New values
 * are first stored in L2. Once there have been more that a specified mumber of
 * misses on L1, L1 and L2 maps are merged and the new map assigned to L1
 * and L2 cleared.
 * </p>
 * <p>
 * IMPORTANT:entrySet(), keySet(), and values() return unmodifiable snapshot collections.
 * </p>
 *
 * @author Anton Koinov (latest modification by $Author: matzew $)
 * @version $Revision: 557350 $ $Date: 2007-07-18 13:19:50 -0500 (Wed, 18 Jul 2007) $
 */
public abstract class BiLevelCacheMap implements Map {

    private static final int INITIAL_SIZE_L1 = 32;

    /** To preinitialize <code>_cacheL1</code> with default values use an initialization block */
    protected Map _cacheL1;

    /** Must be final because it is used for synchronization */
    private final Map _cacheL2;

    private final int _mergeThreshold;

    private int _missCount;

    public BiLevelCacheMap(int mergeThreshold) {
        _cacheL1 = new HashMap(INITIAL_SIZE_L1);
        _cacheL2 = new HashMap(HashMapUtils.calcCapacity(mergeThreshold));
        _mergeThreshold = mergeThreshold;
    }

    public boolean isEmpty() {
        synchronized (_cacheL2) {
            return _cacheL1.isEmpty() && _cacheL2.isEmpty();
        }
    }

    public void clear() {
        synchronized (_cacheL2) {
            _cacheL1 = new HashMap();
            _cacheL2.clear();
        }
    }

    public boolean containsKey(Object key) {
        synchronized (_cacheL2) {
            return _cacheL1.containsKey(key) || _cacheL2.containsKey(key);
        }
    }

    public boolean containsValue(Object value) {
        synchronized (_cacheL2) {
            return _cacheL1.containsValue(value) || _cacheL2.containsValue(value);
        }
    }

    public Set entrySet() {
        synchronized (_cacheL2) {
            mergeIfL2NotEmpty();
            return Collections.unmodifiableSet(_cacheL1.entrySet());
        }
    }

    public Object get(Object key) {
        Map cacheL1 = _cacheL1;
        Object retval = cacheL1.get(key);
        if (retval != null) {
            return retval;
        }
        synchronized (_cacheL2) {
            if (cacheL1 != _cacheL1) {
                if ((retval = _cacheL1.get(key)) != null) {
                    return retval;
                }
            }
            if ((retval = _cacheL2.get(key)) == null) {
                retval = newInstance(key);
                if (retval != null) {
                    put(key, retval);
                    mergeIfNeeded();
                }
            } else {
                mergeIfNeeded();
            }
        }
        return retval;
    }

    public Set keySet() {
        synchronized (_cacheL2) {
            mergeIfL2NotEmpty();
            return Collections.unmodifiableSet(_cacheL1.keySet());
        }
    }

    /**
     * If key is already in cacheL1, the new value will show with a delay,
     * since merge L2->L1 may not happen immediately. To force the merge sooner,
     * call <code>size()<code>.
     */
    public Object put(Object key, Object value) {
        synchronized (_cacheL2) {
            _cacheL2.put(key, value);
            mergeIfNeeded();
        }
        return value;
    }

    public void putAll(Map map) {
        synchronized (_cacheL2) {
            mergeIfL2NotEmpty();
            merge(map);
        }
    }

    /** This operation is very expensive. A full copy of the Map is created */
    public Object remove(Object key) {
        synchronized (_cacheL2) {
            if (!_cacheL1.containsKey(key) && !_cacheL2.containsKey(key)) {
                return null;
            }
            Object retval;
            Map newMap;
            synchronized (_cacheL1) {
                newMap = HashMapUtils.merge(_cacheL1, _cacheL2);
                retval = newMap.remove(key);
            }
            _cacheL1 = newMap;
            _cacheL2.clear();
            _missCount = 0;
            return retval;
        }
    }

    public int size() {
        synchronized (_cacheL2) {
            mergeIfL2NotEmpty();
            return _cacheL1.size();
        }
    }

    public Collection values() {
        synchronized (_cacheL2) {
            mergeIfL2NotEmpty();
            return Collections.unmodifiableCollection(_cacheL1.values());
        }
    }

    private void mergeIfL2NotEmpty() {
        if (!_cacheL2.isEmpty()) {
            merge(_cacheL2);
        }
    }

    private void mergeIfNeeded() {
        if (++_missCount >= _mergeThreshold) {
            merge(_cacheL2);
        }
    }

    private void merge(Map map) {
        Map newMap;
        synchronized (_cacheL1) {
            newMap = HashMapUtils.merge(_cacheL1, map);
        }
        _cacheL1 = newMap;
        _cacheL2.clear();
        _missCount = 0;
    }

    /**
     * Subclasses must implement to have automatic creation of new instances
     * or alternatively can use <code>put<code> to add new items to the cache.<br>
     *
     * Implementing this method is prefered to guarantee that there will be only
     * one instance per key ever created. Calling put() to add items in a multi-
     * threaded situation will require external synchronization to prevent two
     * instances for the same key, which defeats the purpose of this cache
     * (put() is useful when initialization is done during startup and items
     * are not added during execution or when (temporarily) having possibly two
     * or more instances of the same key is not of concern).<br>
     *
     * @param key lookup key
     * @return new instace for the requested key
     */
    protected abstract Object newInstance(Object key);
}
