package oracle.toplink.essentials.internal.identitymaps;

import java.util.*;
import java.io.*;
import oracle.toplink.essentials.internal.helper.*;

/**
 * <p><b>Purpose</b>: Caches objects, and allows their retrieval  by their primary key.
 * <p><b>Responsibilities</b>:<ul>
 * <li> Store CacheKeys containing objects and possibly writeLockValues
 * <li> Insert & retrieve objects from the cache
 * <li> Allow retrieval and modification of writeLockValue for a cached object.
 * </ul>
 * @see CacheKey
 * @since TOPLink/Java 1.0
 */
public abstract class IdentityMap implements Serializable, Cloneable {

    /** The innitial or maximum size of the cache depending upon the concrete implementation */
    protected int maxSize;

    /** Used to optimize get through avoiding recreation of the cache key each time. */
    protected CacheKey searchKey;

    /**
     *    Instantiate an new IdentityMap with it's maximum size.<p>
     *    <b>NOTE</b>: Subclasses may provide different behaviour for maxSize.
     *    @param anInteger is the maximum size to be allocated for the recevier.
    */
    public IdentityMap(int size) {
        maxSize = size;
        searchKey = new CacheKey(new Vector(1), null, null);
    }

    /**
     * Acquire the deferred lock
     */
    public CacheKey acquireDeferredLock(Vector primaryKey) {
        CacheKey key = null;
        synchronized (this) {
            key = getCacheKey(primaryKey);
            if (key == null) {
                CacheKey cacheKey = createCacheKey(primaryKey, null, null);
                cacheKey.acquireDeferredLock();
                put(cacheKey);
                return cacheKey;
            }
        }
        key.acquireDeferredLock();
        return key;
    }

    /**
     *    Set an exclusive lock on an object in the IdentityMap. This is provided so that when the object is being read from the database
     *     the reader can lock the object while it builds and caches the new object. This will prevent other threads
     *    from accessing this objects but will not stop other threads from inserting other objects into this IdentityMap.
     */
    public CacheKey acquireLock(Vector primaryKey, boolean forMerge) {
        CacheKey key = null;
        synchronized (this) {
            key = getCacheKey(primaryKey);
            if (key == null) {
                CacheKey cacheKey = createCacheKey(primaryKey, null, null);
                cacheKey.acquire(forMerge);
                put(cacheKey);
                return cacheKey;
            }
        }
        key.acquire();
        return key;
    }

    /**
     * INTERNAL:
     * Used to print all the Locks in every identity map in this session.
     * The output of this method will go to log passed in as a parameter.
     */
    public abstract void collectLocks(HashMap threadList);

    /**
     *    Set an exclusive lock on an object in the IdentityMap. This is provided so that when the object is being read from the database
     *     the reader can lock the object while it builds and caches the new object. This will prevent other threads
     *    from accessing this objects but will not stop other threads from inserting other objects into this IdentityMap.
     */
    public CacheKey acquireLockNoWait(Vector primaryKey, boolean forMerge) {
        CacheKey key = null;
        synchronized (this) {
            key = getCacheKey(primaryKey);
            if (key == null) {
                CacheKey cacheKey = createCacheKey(primaryKey, null, null);
                cacheKey.acquire(forMerge);
                put(cacheKey);
                return cacheKey;
            }
        }
        if (key != null) {
            if (!key.acquireNoWait(forMerge)) {
                key = null;
            }
        }
        return key;
    }

    /**
     * INTERNAL:
     * Find the cachekey for the provided primary key and place a readlock on it.
     * This will allow multiple users to read the same object but prevent writes to
     * the object while the read lock is held.
     */
    public CacheKey acquireReadLockOnCacheKey(Vector primaryKey) {
        CacheKey key = null;
        synchronized (this) {
            key = getCacheKey(primaryKey);
            if (key == null) {
                CacheKey cacheKey = createCacheKey(primaryKey, null, null);
                cacheKey.acquireReadLock();
                return cacheKey;
            }
        }
        key.acquireReadLock();
        return key;
    }

    /**
     * INTERNAL:
     * Find the cachekey for the provided primary key and place a readlock on it.
     * This will allow multiple users to read the same object but prevent writes to
     * the object while the read lock is held.
     * If no readlock can be acquired then do not wait but return null.
     */
    public CacheKey acquireReadLockOnCacheKeyNoWait(Vector primaryKey) {
        CacheKey key = null;
        synchronized (this) {
            key = getCacheKey(primaryKey);
            if (key == null) {
                CacheKey cacheKey = createCacheKey(primaryKey, null, null);
                cacheKey.acquireReadLock();
                return cacheKey;
            }
        }
        if (key != null) {
            if (!key.acquireReadLockNoWait()) {
                key = null;
            }
        }
        return key;
    }

    /**
     * INTERNAL:
     * Clones itself.
     */
    public Object clone() {
        Object object = null;
        try {
            object = super.clone();
        } catch (Exception e) {
            ;
        }
        return object;
    }

    /**
     * Return true if an object is indexed in the recevier at the primary key &lt;aVector&gt;
     * @param aVector is the primary key for the object to search for.
     */
    public boolean containsKey(Vector primaryKey) {
        CacheKey wrapper = getCacheKeyWithReadLock(primaryKey);
        if (wrapper == null) {
            return false;
        } else {
            return true;
        }
    }

    public CacheKey createCacheKey(Vector primaryKey, Object object, Object writeLockValue) {
        return createCacheKey(primaryKey, object, writeLockValue, 0);
    }

    public CacheKey createCacheKey(Vector primaryKey, Object object, Object writeLockValue, long readTime) {
        return new CacheKey(primaryKey, object, writeLockValue, readTime);
    }

    /**
     * Allow for the cache to be iterated on.
     */
    public abstract Enumeration elements();

    /**
     * Return the object cached in the identity map or null if it could not be found.
     */
    public Object get(Vector primaryKey) {
        CacheKey cacheKey = getCacheKeyWithReadLock(primaryKey);
        if (cacheKey == null) {
            return null;
        }
        return cacheKey.getObject();
    }

    /**
     * Get the cache key (with object) for the primary key.
     * This reuses the same instance of cache key (searchKey) for all of the get to improve performance.
     */
    protected CacheKey getCacheKey(Vector primaryKey) {
        CacheKey key = null;
        synchronized (this) {
            getSearchKey().setKey(primaryKey);
            key = getCacheKey(getSearchKey());
        }
        return key;
    }

    /**
     * Return the cache key (with object) matching the cache key wrapper of the primary key.
     */
    protected abstract CacheKey getCacheKey(CacheKey cacheKey);

    /**
     * Get the cache key (with object) for the primary key with read lock.
     */
    protected CacheKey getCacheKeyWithReadLock(Vector primaryKey) {
        CacheKey key = getCacheKey(primaryKey);
        if (key != null) {
            key.acquireReadLock();
            key.releaseReadLock();
        }
        return key;
    }

    /**
     * INTERNAL:
     * Returns the class which should be used as an identity map. For JDK1.1.x
     * FullIdentityMap is the default, for JDK1.2 it is SoftCacheWeakIdentityMap.
     *
     * @return java.lang.Class
     */
    public static Class getDefaultIdentityMapClass() {
        return ClassConstants.SoftCacheWeakIdentityMap_Class;
    }

    /**
     * @return The maxSize for the IdentityMap (NOTE: some subclasses may use this differently).
     */
    public int getMaxSize() {
        if (maxSize == -1) {
            maxSize = 100;
        }
        return maxSize;
    }

    protected CacheKey getSearchKey() {
        return searchKey;
    }

    /**
     * Return the number of objects in the receiver.
     */
    public abstract int getSize();

    /**
     * Return the number of actual objects of type myClass in the IdentityMap.
     * Recurse = true will include subclasses of myClass in the count.
     */
    public abstract int getSize(Class myClass, boolean recurse);

    /**
     * Get the wrapper object from the cache key associated with the given primary key,
     * this is used for EJB.
     */
    public Object getWrapper(Vector primaryKey) {
        CacheKey cacheKey = getCacheKeyWithReadLock(primaryKey);
        if (cacheKey == null) {
            return null;
        } else {
            return cacheKey.getWrapper();
        }
    }

    /**
     * Get the write lock value from the cache key associated to the primarykey
     */
    public Object getWriteLockValue(Vector primaryKey) {
        CacheKey cacheKey = getCacheKeyWithReadLock(primaryKey);
        if (cacheKey == null) {
            return null;
        } else {
            return cacheKey.getWriteLockValue();
        }
    }

    /**
     * Initialize the newly allocated instance of this class. This method must be called in order for
     * the IdentityMap to be functional.
     * @param anInteger is the maximum size to be allocated for the recevier.
     */
    public void initialize(int size) {
        setMaxSize(size);
    }

    /**
     * Allow for the cache keys to be iterated on.
     */
    public abstract Enumeration keys();

    /**
     * Store the object in the cache at its primary key.
     * @param primaryKey is the primary key for the object.
     * @param object is the domain object to cache.
     * @param writeLockValue is the current write lock value of object, if null the version is ignored.
     * @param readTime the read time of the object to be stored in the cache
     */
    public abstract CacheKey put(Vector primaryKey, Object object, Object writeLockValue, long readTime);

    /**
     * Store the object in the cache with the cache key.
     * Should be overide by the sub-class
     */
    protected abstract void put(CacheKey cacheKey);

    /**
     * Remove the primary key from the cache.
     */
    public Object remove(Vector primaryKey) {
        CacheKey key = getCacheKey(primaryKey);
        return remove(key);
    }

    /**
     * Remove the cache key from the cache.
     */
    public abstract Object remove(CacheKey cacheKey);

    /**
     * Set the maximum size for the recevier.
     * @param anInteger is the new maximum size.
     */
    protected void setMaxSize(int size) {
        maxSize = size;
    }

    /**
     * INTERNAL:
     * This method will be used to update the max cache size, any objects exceeding the max cache size will
     * be remove from the cache. Please note that this does not remove the object from the identityMap, except in
     * the case of the CacheIdentityMap.
     */
    public synchronized void updateMaxSize(int maxSize) {
        setMaxSize(maxSize);
    }

    protected void setSearchKey(CacheKey searchKey) {
        this.searchKey = searchKey;
    }

    /**
     * Update the wrapper object the cache key associated with the given primary key,
     * this is used for EJB.
     */
    public void setWrapper(Vector primaryKey, Object wrapper) {
        CacheKey cacheKey = getCacheKey(primaryKey);
        if (cacheKey != null) {
            cacheKey.setWrapper(wrapper);
        }
    }

    /**
     * Update the write lock value of the cache key associated with the given primary key,
     */
    public void setWriteLockValue(Vector primaryKey, Object writeLockValue) {
        CacheKey cacheKey = getCacheKey(primaryKey);
        if (cacheKey != null) {
            cacheKey.acquire();
            cacheKey.setWriteLockValue(writeLockValue);
            cacheKey.release();
        }
    }

    public String toString() {
        return oracle.toplink.essentials.internal.helper.Helper.getShortClassName(getClass()) + "[" + getSize() + "]";
    }

    /**
     * This is used to notify the identity map of a locked keys modification to allow updating of weak refs.
     */
    public void updateCacheKey(CacheKey cacheKey) {
        return;
    }
}
