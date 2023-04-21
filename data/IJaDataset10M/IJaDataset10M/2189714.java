package net.sf.ehcache_zen;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;
import net.sf.ehcache.concurrent.LockType;
import net.sf.ehcache.concurrent.Sync;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;
import net.sf.ehcache.constructs.blocking.LockTimeoutException;
import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;
import net.sf.ehcache.statistics.LiveCacheStatisticsData;
import net.sf.ehcache_zen.util.ZenCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A self-populating cache decorator that creates entries on demand and prefers
 * to return a previously cached element that is expired instead of null if an
 * updated element cannot be obtained.
 * 
 * <p/> The cache extends SelfPopulatingCache but modifies the expiry check
 * logic to preferentially return an expired element (if available) instead of
 * null in the case where an updated element cannot be obtained from the
 * CacheEntryFactory.
 * 
 * <p/> <b>To use this class, the cache configuration must be set to
 * <code>eternal=&quot;true&quot;</code>.</b> However, the
 * <code>timeToIdleSeconds</code> and <code>timeToLiveSeconds</code> config
 * parameters will be honored to determine if an element is considered expired
 * during a {@link #get(Object)} call, whereupon an updated element will be
 * requested from the CacheEntryFactory.
 * 
 * NOTE: Due to limitations in the ehcache API that is accessible for extension,
 * ExpiredBetterThanNullSelfPopulatingCache instances cannot distinguish whether
 * cache hits come from the memory store or the disk store and will therefore
 * record all hits as coming from the memory store in its statistics.
 * 
 * <p/> As with SelfPopulatingCache, clients of the cache simply call it without
 * needing knowledge of whether the entry exists in the cache.
 * 
 * <p/> As with SelfPopulatingCache, background refreshes may still be
 * performed, and they operate on the backing cache and do not degrade
 * performance of {@link #get(java.io.Serializable)} calls.
 * 
 * <p/> As with SelfPopulatingCache, thread safety depends on the factory being
 * used. The UpdatingCacheEntryFactory should be made thread safe. In addition
 * users of returned values should not modify their contents.
 * 
 * <p/> This class was written and tested against version 1.7.2 of ehcache-core.
 * 
 * @author Brian Hafer
 */
public class ExpiredBetterThanNullSelfPopulatingCache extends SelfPopulatingCache {

    private static final Logger LOG = LoggerFactory.getLogger(ExpiredBetterThanNullSelfPopulatingCache.class.getName());

    public ExpiredBetterThanNullSelfPopulatingCache(Ehcache cache, CacheEntryFactory factory) throws CacheException {
        super(cache, factory);
    }

    /**
     * Looks up an entry, creating it if not found.
     */
    public Element get(final Object key) throws IllegalStateException, CacheException, LockTimeoutException {
        boolean quiet = ((getTimeToLive() > 0L) || (getTimeToIdle() > 0L));
        if (LOG.isDebugEnabled()) {
            LOG.debug("Using quiet mode for get(" + key + ").");
        }
        Sync lock = getLockForKey(key);
        try {
            acquiredLockForKey(key, lock, LockType.WRITE);
            Element element = (quiet) ? super.getQuiet(key) : cacheGet(key);
            if (element == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Null element found for get(" + key + ").");
                }
                try {
                    if (quiet) {
                        try {
                            ((LiveCacheStatisticsData) getLiveCacheStatistics()).cacheMissNotFound();
                        } catch (Throwable t) {
                            LOG.warn("Could not update miss statistics for cache" + " entry with key \"" + key + "\".", t);
                        }
                    }
                    element = createAndPutEntry(key);
                } catch (Throwable t) {
                    lock.unlock(LockType.WRITE);
                    throw new CacheException("Could not fetch object for cache entry with key \"" + key + "\".", t);
                }
            } else if (isExpired(element)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Expired element found for get(" + key + ").");
                }
                try {
                    if (quiet) {
                        try {
                            ((LiveCacheStatisticsData) getLiveCacheStatistics()).cacheMissExpired();
                        } catch (Throwable t) {
                            LOG.warn("Could not update miss expired statistics for" + " cache entry with key \"" + key + "\".", t);
                        }
                    }
                    element = createAndPutEntry(key);
                } catch (Throwable t) {
                    lock.unlock(LockType.WRITE);
                    LOG.error("Could not update object for cache entry with key \"" + key + "\".", t);
                }
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Valid element found for get(" + key + ").");
                }
                if (quiet) {
                    element.updateAccessStatistics();
                    try {
                        ((LiveCacheStatisticsData) getLiveCacheStatistics()).cacheHitInMemory();
                    } catch (Throwable t) {
                        LOG.warn("Could not update hit statistics for cache entry with" + " key \"" + key + "\".", t);
                    }
                }
                lock.unlock(LockType.WRITE);
            }
            return element;
        } catch (LockTimeoutException e) {
            throw new LockTimeoutException("Timeout after " + timeoutMillis + " waiting on another thread to fetch object for cache" + " entry \"" + key + "\".", e);
        } catch (IllegalStateException e) {
            lock.unlock(LockType.WRITE);
            throw e;
        } catch (CacheException e) {
            lock.unlock(LockType.WRITE);
            throw e;
        } catch (Throwable t) {
            lock.unlock(LockType.WRITE);
            throw new CacheException("Could not fetch object for cache entry with key \"" + key + "\".", t);
        }
    }

    /**
     * Checks whether this cache element has expired.
     * 
     * <p/> The element is expired if:
     * <ol>
     * <li>the idle time is non-zero and has elapsed, regardless of if the cache
     * is eternal; or
     * <li>the time to live is non-zero and has elapsed, regardless of if the
     * cache is eternal; or
     * <li>the value of the element is null.
     * </ol>
     * 
     * @return true if it has expired
     * @throws IllegalStateException
     *             if the cache is not Status.STATUS_ALIVE
     * @throws NullPointerException
     *             if the element is null
     */
    public boolean isExpired(Element element) throws IllegalStateException, NullPointerException {
        if (!cache.getStatus().equals(Status.STATUS_ALIVE)) {
            throw new IllegalStateException("The " + cache.getCacheConfiguration().getName() + " Cache is not alive.");
        }
        if (!isLifespanSet()) {
            return false;
        }
        synchronized (element) {
            long now = System.currentTimeMillis();
            long expirationTime = getExpirationTime(element);
            return now > expirationTime;
        }
    }

    /**
     * Returns the expiration time based on time to live. If the element also
     * has a time to idle setting, the expiration time will vary depending on
     * whether the element is accessed.
     * 
     * The element's eternal setting will be ignored.
     * 
     * @param element
     *            Cache element to examine.
     * @return The time to expiration.
     */
    public long getExpirationTime(Element element) {
        if (!isLifespanSet()) {
            return Long.MAX_VALUE;
        }
        synchronized (element) {
            long expirationTime = 0;
            long ttlExpiry = ZenCacheUtils.getTimeToLiveExpirationTime(element, this);
            long ttiExpiry = ZenCacheUtils.getTimeToIdleExpirationTime(element, this);
            if ((getTimeToLive() != 0) && (getTimeToIdle() == 0)) {
                expirationTime = ttlExpiry;
                if (LOG.isDebugEnabled()) {
                    LOG.debug("TTL expiration is " + expirationTime + " as of " + System.currentTimeMillis() + ".");
                }
            } else if (getTimeToLive() == 0) {
                expirationTime = ttiExpiry;
                if (LOG.isDebugEnabled()) {
                    LOG.debug("TTI expiration is " + expirationTime + " as of " + System.currentTimeMillis() + ".");
                }
            } else {
                expirationTime = Math.min(ttlExpiry, ttiExpiry);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(((Math.min(ttlExpiry, ttiExpiry) == ttlExpiry) ? "TTL" : "TTI") + " (precedence) expiration is " + expirationTime + " as of " + System.currentTimeMillis() + ".");
                }
            }
            return expirationTime;
        }
    }

    /**
     * Returns the TTI configured for this cache, in seconds.
     * 
     * @return The TTI configured for this cache, in seconds.
     */
    public long getTimeToIdle() {
        return cache.getCacheConfiguration().getTimeToIdleSeconds();
    }

    /**
     * Returns the TTL configured for this cache, in seconds.
     * 
     * @return The TTL configured for this cache, in seconds.
     */
    public long getTimeToLive() {
        return cache.getCacheConfiguration().getTimeToLiveSeconds();
    }

    /**
     * Whether any combination of TTL or TTI has been configured for this cache.
     * Ignores eternal.
     * 
     * @return true if set.
     */
    public boolean isLifespanSet() {
        return ((getTimeToIdle() != Integer.MIN_VALUE) || (getTimeToLive() != Integer.MIN_VALUE));
    }

    /**
     * Duplication of BlockingCache.get(Object key) to allow access to backing
     * cache without going through SelfPopulatingCache parent.
     */
    private Element cacheGet(final Object key) throws IllegalStateException, CacheException, LockTimeoutException {
        Sync lock = getLockForKey(key);
        acquiredLockForKey(key, lock, LockType.WRITE);
        Element element = cache.get(key);
        if (element != null) {
            lock.unlock(LockType.WRITE);
        }
        return element;
    }

    /**
     * Duplication of private BlockingCache.acquiredLockForKey(Object key, Sync
     * lock, LockType lockType) method as required by directGet(Object key).
     */
    private void acquiredLockForKey(final Object key, final Sync lock, final LockType lockType) throws LockTimeoutException {
        if (timeoutMillis > 0) {
            try {
                boolean acquired = lock.tryLock(lockType, timeoutMillis);
                if (!acquired) {
                    StringBuffer message = new StringBuffer("Lock timeout. Waited more than ").append(timeoutMillis).append("ms to acquire lock for key ").append(key).append(" on blocking cache ").append(cache.getName());
                    throw new LockTimeoutException(message.toString());
                }
            } catch (InterruptedException e) {
                throw new LockTimeoutException("Got interrupted while trying to acquire lock for key " + key, e);
            }
        } else {
            lock.lock(lockType);
        }
    }

    /**
     * Creates an entry for the specified key using the CacheEntryFactory and
     * places it into the backing cache. The put operation will reset the
     * element access statistics and update the element update statistics.
     */
    private Element createAndPutEntry(final Object key) throws CacheException, Exception {
        Object value = factory.createEntry(key);
        Element element = makeAndCheckElement(key, value);
        put(element);
        return element;
    }
}
