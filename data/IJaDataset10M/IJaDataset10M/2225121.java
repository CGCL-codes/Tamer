package com.codename1.io;

import com.codename1.ui.Display;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * A cache map is essentially a hashtable that indexes entries based on age and is
 * limited to a fixed size. Hence when an entry is placed into the cache map and the
 * cache size needs to increase, the least referenced entry is removed.
 * A cache hit is made both on fetching and putting, hence frequently fetched elements
 * will never be removed from a sufficiently large cache.
 * Cache can work purely in memory or swap data into storage based on user definitions.
 * Notice that this class isn't threadsafe.
 *
 * @author Shai Almog
 */
public class CacheMap {

    private int cacheSize = 10;

    private Hashtable memoryCache = new Hashtable();

    private Hashtable weakCache = new Hashtable();

    private int storageCacheSize = 0;

    private Vector storageCacheContent = new Vector();

    /**
     * Indicates the size of the memory cache after which the cache won't grow further
     * Size is indicated by number of elements stored and not by KB or similar benchmark!
     *
     * @return the cacheSize
     */
    public int getCacheSize() {
        return cacheSize;
    }

    /**
     * Indicates the size of the memory cache after which the cache won't grow further
     * Size is indicated by number of elements stored and not by KB or similar benchmark!
     * 
     * @param cacheSize the cacheSize to set
     */
    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    /**
     * Puts the given key/value pair in the cache
     *
     * @param key the key
     * @param value the value
     */
    public void put(Object key, Object value) {
        if (cacheSize <= memoryCache.size()) {
            Enumeration e = memoryCache.keys();
            long oldest = System.currentTimeMillis();
            Object oldestKey = null;
            Object[] oldestValue = null;
            while (e.hasMoreElements()) {
                Object currentKey = e.nextElement();
                Object[] currentValue = (Object[]) memoryCache.get(currentKey);
                long currentAge = ((Long) currentValue[0]).longValue();
                if (currentAge <= oldest) {
                    oldest = currentAge;
                    oldestKey = currentKey;
                    oldestValue = currentValue;
                }
            }
            placeInStorageCache(oldestKey, oldest, oldestValue[1]);
            weakCache.put(oldestKey, Display.getInstance().createSoftWeakRef(oldestValue[1]));
            memoryCache.remove(oldestKey);
        }
        memoryCache.put(key, new Object[] { new Long(System.currentTimeMillis()), value });
    }

    /**
     * Deletes a cached entry
     * 
     * @param key entry to remove from the cache
     */
    public void delete(String key) {
        memoryCache.remove(key);
        weakCache.remove(key);
    }

    /**
     * Returns the object matching the given key
     *
     * @param key key object
     * @return value from a previous put or null
     */
    public Object get(Object key) {
        Object[] o = (Object[]) memoryCache.get(key);
        if (o != null) {
            return o[1];
        }
        Object ref = weakCache.get(key);
        if (ref != null) {
            ref = Display.getInstance().extractHardRef(ref);
            if (ref != null) {
                put(key, ref);
                return ref;
            }
        }
        if (storageCacheSize > 0) {
            for (int iter = 0; iter < storageCacheContent.size(); iter++) {
                Object[] obj = (Object[]) storageCacheContent.elementAt(iter);
                if (obj[1].equals(key)) {
                    Vector v = (Vector) Storage.getInstance().readObject("$CACHE$" + iter);
                    Object val = v.elementAt(0);
                    put(key, val);
                    return val;
                }
            }
        }
        return null;
    }

    /**
     * Clears the caches for this cache object
     */
    public void clearAllCache() {
        clearMemoryCache();
        clearStorageCache();
    }

    /**
     * Clears the memory cache
     */
    public void clearMemoryCache() {
        memoryCache.clear();
        weakCache.clear();
    }

    private void placeInStorageCache(Object key, long lastAccessed, Object value) {
        if (storageCacheSize < 1) {
            return;
        }
        if (storageCacheContent.size() < storageCacheSize) {
            placeInStorageCache(storageCacheContent.size(), key, lastAccessed, value);
        } else {
            long smallest = Long.MAX_VALUE;
            int offset = 0;
            for (int iter = 0; iter < storageCacheSize; iter++) {
                Object[] index = (Object[]) storageCacheContent.elementAt(iter);
                long current = ((Long) index[0]).longValue();
                if (smallest > current) {
                    smallest = current;
                    offset = iter;
                }
            }
            placeInStorageCache(offset, key, lastAccessed, value);
        }
    }

    private void placeInStorageCache(int offset, Object key, long lastAccessed, Object value) {
        Vector v = new Vector();
        v.addElement(value);
        Long l = new Long(lastAccessed);
        v.addElement(l);
        v.addElement(key);
        Storage.getInstance().writeObject("$CACHE$" + offset, v);
        if (storageCacheContent.size() > offset) {
            storageCacheContent.setElementAt(new Object[] { l, key }, offset);
        } else {
            storageCacheContent.insertElementAt(new Object[] { l, key }, offset);
        }
    }

    private Vector fetchFromStorageCache(int offset) {
        return (Vector) Storage.getInstance().readObject("$CACHE$" + offset);
    }

    /**
     * Clears the storage cache
     */
    public void clearStorageCache() {
        if (storageCacheSize > 0) {
            for (int iter = 0; iter < storageCacheSize; iter++) {
                Storage.getInstance().deleteStorageFile("$CACHE$" + iter);
            }
        }
    }

    /**
     * Indicates the size of the storage cache after which the cache won't grow further
     * Size is indicated by number of elements stored and not by KB or similar benchmark!
     *
     * @return the storageCacheSize
     */
    public int getStorageCacheSize() {
        return storageCacheSize;
    }

    /**
     * Indicates the size of the storage cache after which the cache won't grow further
     * Size is indicated by number of elements stored and not by KB or similar benchmark!
     *
     * @param storageCacheSize the storageCacheSize to set
     */
    public void setStorageCacheSize(int storageCacheSize) {
        this.storageCacheSize = storageCacheSize;
        for (int iter = 0; iter < storageCacheSize; iter++) {
            Vector v = fetchFromStorageCache(iter);
            if (v != null) {
                storageCacheContent.insertElementAt(new Object[] { v.elementAt(1), v.elementAt(2) }, iter);
            }
        }
    }
}
