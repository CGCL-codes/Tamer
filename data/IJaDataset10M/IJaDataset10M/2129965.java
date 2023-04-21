package mobi.ilabs.cache;

import java.util.HashMap;
import java.util.Map;
import org.restlet.data.Request;

/**
 * A simple cache. XXX Uses very coarse locking, should read
 * read/write differentiated locks, since most of the time
 * it is ok to do parallell reads.
 * 
 * @param <T>
 */
public abstract class Cache<T> {

    /**
     * A map mapping a string key into a cache entry.
     */
    private final Map<String, CacheEntry<T>> map = new HashMap<String, CacheEntry<T>>();

    public int noOfCachedItems() {
        return map.size();
    }

    protected Map<String, CacheEntry<T>> getMap() {
        return map;
    }

    public synchronized boolean containsKey(final String key) {
        return map.containsKey(key);
    }

    public synchronized T get(final Request r, final String key) {
        final CacheEntry<T> e = getEntry(r, key);
        if (e == null) {
            return null;
        } else {
            return e.content;
        }
    }

    protected synchronized CacheEntry<T> getEntry(final Request r, final String key) {
        final CacheEntry<T> entryFromCache = map.get(key);
        if (entryFromCache == null) {
            T obFromBacking = getFromBacking(r, key);
            if (obFromBacking != null) {
                return put(key, obFromBacking);
            } else {
                return null;
            }
        }
        return entryFromCache;
    }

    public synchronized CacheEntry<T> remove(final CacheEntry<T> entry) {
        if (entry != null) {
            map.remove(entry.key);
        }
        return entry;
    }

    public synchronized CacheEntry<T> remove(final String key) {
        if (map.containsKey(key)) {
            return remove(map.get(key));
        } else {
            return null;
        }
    }

    protected abstract boolean evictionIsNecessary(T t);

    protected abstract T getFromBacking(Request r, String key);

    protected CacheEntry<T> evict(final CacheEntry<T> entry) {
        map.remove(entry.key);
        return entry;
    }

    protected abstract CacheEntry<T> getItemToEvict();

    public CacheEntry<T> put(final String key, final T ob) {
        remove(key);
        while (evictionIsNecessary(ob)) {
            evict(getItemToEvict());
        }
        final CacheEntry<T> myEntry = new CacheEntry<T>(key, ob);
        map.put(key, myEntry);
        return myEntry;
    }

    public Cache() {
        super();
    }
}
