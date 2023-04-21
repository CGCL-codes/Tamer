package org.yajul.util;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.io.PrintStream;
import java.util.Iterator;
import org.yajul.log.Logger;

/**
 * Cache provides a mechanism for storing a map of objects, which are activated when needed.
 * This is implemented as an in-memory hash lookup table, with a size-limited LRU list.
 * <ul>Options:
 * <li>Parallel activation - This flag allows multiple thread accessing the same cache key to
 * invoke the activator simultaneously.  Only one of the activated objects will be placed
 * in the cache, however.</li>
 * <li>Timeout - Entries will be re-activated (passivated, then activated) when the entry has
 * not been activated since the specified timeout.  This is useful when the contents of the
 * activator (backing store) change, making the cache 'stale'.</li>
 * <li>Statistics - The cache can keep a list of all keys that have been asked for.</li>
 * </ul>
 */
public class Cache {

    /**
     * Activator provides a mechanism for an instance of Cache to activate objects.
     * @see Cache
     */
    public interface Activator {

        /** The object is being passivated because it is the least recently used. */
        public static final int PASSIVATE_LRU = 0;

        /** The object is being passivated because of a timeout. */
        public static final int PASSIVATE_TIMEOUT = 1;

        /** The object is being passivated because the cache entry has been finalized. */
        public static final int PASSIVATE_FINALIZED = 2;

        /** The object is being passivated because the cache has been cleared. */
        public static final int PASSIVATE_CLEAR = 3;

        /**
    	 * Activates the object associated with the specified information (for example, read the object
    	 * from persistent storage).
    	 * @param key - The key object that has enough information for the activator to create a new object.
    	 * @return The newly activated object.
    	 */
        public Object activate(Object key) throws Exception;

        /**
    	 * Passivates the object, indicating that it is no longer in the cache.  The object may want to
    	 * release resources at this point.
    	 * @param key - The key object that has enough information for the activator to create a new object.
    	 * @param obj - The object that is being removed from the cache.
    	 * @param reason - The reason the object is being passivated (PASSIVATE_xxx values).
    	 */
        public void passivate(Object key, Object obj, int reason) throws Exception;
    }

    /** A logger for this class. */
    private static Logger log = Logger.getLogger(Cache.class.getName());

    /** Inner class that is the intermediate object that points to the actual cached objects. **/
    private static class Entry {

        /** A logger for this class. */
        private static Logger log = Logger.getLogger(Entry.class.getName());

        /** The key for this entry. */
        Object key;

        /** The cached object. */
        Object object;

        /** The number of hits on this entry. */
        int hits;

        /** The system time of the last hit on this entry. */
        long lastHit;

        /** The system time of the last activation of this entry. */
        long lastActivation;

        /** The cache that owns this entry. */
        Cache cache;

        Entry(Cache c, Object key) {
            this.cache = c;
            this.key = key;
        }

        public void finalize() {
            if (cache == null) log.debug("No cache backpointer."); else if (object == null) log.debug("No cached object."); else if (key == null) log.debug("No key."); else cache.entryFinalized(this);
            this.key = null;
            this.object = null;
            this.cache = null;
        }
    }

    /**
     * A list of keys, in order of use.  The most recently used is first.
     */
    private LinkedList usedKeys;

    /**
     * A map of the cached object entries (Entry) by key.
     */
    private Map entryMap;

    /**
     * The maximum number of objects this cache will have in it before
     * it removes objects.  This is the maximum size of the LRU list.
     */
    private int maxSize;

    /**
     * The cache timeout, in millilseconds.  If zero, there will be no timeout.
     */
    private long timeout;

    /** 
     * An interface to something that provides acitvation of objects.  This is a 'factory',
     * that produces objects based on a key.
     * @see Activator
     */
    private Activator activator;

    /** If true, parallel activation is allowed.  Otherwise,
     * object activation is serialized. */
    private boolean parallelActivation;

    /** If true, statistics are kept. */
    private boolean keepStats;

    private int activations;

    private int requests;

    private int timeouts;

    private Set allRequests;

    /** Gets the maximum size the cache can grow to */
    public int getMaxSize() {
        return maxSize;
    }

    /** Gets the current size of the cache */
    public int getCurrentSize() {
        return entryMap.size();
    }

    /** Gets the number of times an activation had to be perfomed (a cache miss) */
    public int getActivations() {
        return activations;
    }

    /** Gets the number request made of the cache */
    public int getRequests() {
        return requests;
    }

    /** Gets the number unique keys asked for, this will allways be zero if 'keepStats' is false. */
    public int getUniqueRequests() {
        return allRequests.size();
    }

    /** Gets the number of 'timeout' passivations (zero, unless a timeout was specified). **/
    public int getTimeouts() {
        return timeouts;
    }

    /** Gets the rate at which requested object were available in the cache [0..1] **/
    public double getHitRate() {
        double dreq = (double) requests;
        double dact = (double) activations;
        return ((dreq - dact) * 1.0) / dreq;
    }

    /** Gets the rate at which requested objects timed out [0..1] **/
    public double getTimeoutRate() {
        double dreq = (double) requests;
        double dto = (double) timeouts;
        return ((dreq - dto) * 1.0) / dreq;
    }

    /**
     * Creates a new cache with the specified size.  Object are activated by the given activator interface.
     *
     * @param activator The interface responsible for activating objects on cache misses.
     * @param maxSize The maximum number of objects this cache will have in it before it removes objects. maxSize=0 is allowed.
     */
    public Cache(Activator activator, int maxSize) {
        this(activator, maxSize, 0, true, false, new HashMap());
    }

    /**
     * Creates a new cache with the specified size.  Object are activated by the given activator interface.
     *
     * @param activator The interface responsible for activating objects on cache misses.
     * @param maxSize The maximum number of objects this cache will have in it before it removes objects. maxSize=0 is allowed.
     * @param parallelActivation Iff false, threads will wait for an object to be activated if it is already being activated when get() is called.  For any non-trival activation, it will always be faster to set this to false.'
     * @param keepStats If true, cache hit rate statistics will be kept.
     * @param map The map that will be used to keep all of the entries.
     */
    public Cache(Activator activator, int maxSize, long timeout, boolean parallelActivation, boolean keepStats, Map map) {
        this.activator = activator;
        this.maxSize = maxSize;
        this.parallelActivation = parallelActivation;
        this.keepStats = keepStats;
        this.timeout = timeout;
        do_clearStats();
        log.debug("maxSize = " + maxSize + " timeout = " + timeout);
        entryMap = map;
        usedKeys = new LinkedList();
        allRequests = new HashSet();
    }

    /**
     * Increments the request count
     */
    private synchronized void logRequest() {
        requests++;
    }

    /**
     * Logs the activation of the key for statistics
     */
    private void logActivation(Object key, Entry entry) {
        synchronized (this) {
            activations++;
            if (timeout > 0) {
                entry.lastActivation = System.currentTimeMillis();
            }
            if (keepStats) allRequests.add(key);
        }
    }

    /**
     * Gets the object associated to the given key.  Activates a new one if necessary.
     * @param key - The key
     * @return Object - The cached object.
     */
    public Object get(Object key) throws Exception {
        logRequest();
        Entry entry = null;
        if (parallelActivation) {
            synchronized (entryMap) {
                entry = find(key);
            }
            if (entry == null) entry = new Entry(this, key);
            if (entry.object == null) activate(key, entry); else usedKey(key);
        } else {
            synchronized (entryMap) {
                entry = find(key);
                if (entry == null) {
                    entry = new Entry(this, key);
                    if (maxSize > 0) addToCollection(key, entry);
                }
            }
            synchronized (entry) {
                if (entry.object == null) {
                    logActivation(key, entry);
                    entry.object = activator.activate(key);
                }
            }
        }
        return entry.object;
    }

    /**
     * Finds the object with the specified key.  Returns null if one doesn't exits.
     */
    private Entry find(Object key) {
        Entry e = (Entry) entryMap.get(key);
        if (e != null) {
            e.hits++;
            e.lastHit = System.currentTimeMillis();
            if (timeout > 0) {
                if (e.lastActivation > 0) {
                    long age = e.lastHit - e.lastActivation;
                    if (age > timeout) {
                        timeouts++;
                        try {
                            passivate(key, Activator.PASSIVATE_TIMEOUT);
                        } catch (Exception ex) {
                            throw new Error("Unexpected exception during passivation: " + ex.getMessage());
                        }
                        return null;
                    }
                }
            }
        }
        return e;
    }

    /**
     * Activates the object with the specified key and adds it to the collection
     * @param key - The key.
     * @param entry - The cache entry.
     */
    private void activate(Object key, Entry entry) throws Exception {
        logActivation(key, entry);
        entry.object = activator.activate(key);
        if (maxSize > 0) addToCollection(key, entry);
    }

    /**
     * Adds the specified key/value pair to the cache
     * @param key - The key.
     * @param entry - The cache entry.
     */
    private synchronized void addToCollection(Object key, Entry entry) throws Exception {
        if (entryMap.get(key) != null) return;
        if (entryMap.size() >= maxSize) {
            passivate(usedKeys.removeLast(), Activator.PASSIVATE_LRU);
        }
        usedKeys.addFirst(key);
        entryMap.put(key, entry);
    }

    /**
     * Called when a key is used. Moves it to the front of the usedKeys list
     */
    private synchronized void usedKey(Object key) {
        if (maxSize > 0) {
            usedKeys.remove(key);
            usedKeys.addFirst(key);
        }
    }

    /**
	* Clears the cache contents and resets the stats.
	*/
    public synchronized void clear() throws Exception {
        Iterator iter = entryMap.keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            Entry e = (Entry) entryMap.get(key);
            e.cache = null;
            activator.passivate(key, e.object, Activator.PASSIVATE_CLEAR);
        }
        entryMap.clear();
        usedKeys.clear();
        clearStats();
    }

    /**
	* If true, statistics info is kept.
	*/
    public synchronized void setKeepStats(boolean keepStats) {
        keepStats = keepStats;
    }

    /**
	* If true, statistics info is kept.
	*/
    public synchronized boolean getKeepStats() {
        return keepStats;
    }

    /**
	* Clears the statistics
	*/
    public synchronized void clearStats() {
        allRequests.clear();
        do_clearStats();
    }

    private void do_clearStats() {
        activations = 0;
        requests = 0;
        timeouts = 0;
    }

    /**
	 * Entry has been finalized.
	 */
    void entryFinalized(Entry e) {
        try {
            if (activator == null) {
                if (log.isDebugEnabled()) log.debug("Entry finalized, but activator is null: not passivating.");
                return;
            }
            passivate(e.key, Activator.PASSIVATE_FINALIZED);
        } catch (Exception ex) {
            log.unexpected(ex);
        }
    }

    private void internal_passivate(Entry e, int reason) throws Exception {
        if (e.key == null) return;
        usedKeys.remove(e.key);
        if (e.object != null) activator.passivate(e.key, e.object, reason);
    }

    /**
     * Removes an object from the cache.
     * @param key - The key for the object in the cache.
     * @param reason - The reason for the passivation (Activator.PASSIVATE_xxx values).
     */
    public void passivate(Object key, int reason) throws Exception {
        synchronized (entryMap) {
            Entry e = (Entry) entryMap.remove(key);
            if (e != null) internal_passivate(e, reason);
        }
    }
}
