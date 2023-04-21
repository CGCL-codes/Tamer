package org.tripcom.security.memory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A single process implementation of the <tt>SharedMemory</tt> interface.
 * <p>
 * This class realizes a single process memory, that is a memory whose data is
 * maintained in the local memory space of the current process. Apart from the
 * distribution feature, this implementation implements all the features of the
 * <tt>SharedMemory</tt> interface, including handling of entry expiration and
 * thread safety. Write operations are checked in order to detect multiple
 * writes for a same entry, key pair, with different values; in this case, an
 * exception is raised.
 * </p>
 * 
 * @author Francesco Corcoglioniti &lt;francesco.corcoglioniti@cefriel.it&gt;
 */
public class LocalMemory implements Memory {

    /** The scheduler instance used for handling entry expiration. */
    private ScheduledExecutorService scheduler;

    /** An index (entry id, entry). */
    private Map<String, Record> entryIndex;

    /** An ordered index (expire time, entry). */
    private SortedMap<Long, Record> expirationIndex;

    /** The future handle of the next purge, if scheduled, otherwise null. */
    private Future<?> purgeFuture;

    /** The time of the next purge operation if scheduled, otherwise 0L. */
    private long scheduledPurgeTime;

    /**
     * Create a new local memory, using an internal scheduler.
     */
    public LocalMemory() {
        this(Executors.newScheduledThreadPool(1));
    }

    /**
     * Create a new local memory, using the scheduler specified.
     * 
     * @param scheduler the scheduler to use for asynchronous writes (not null).
     */
    public LocalMemory(ScheduledExecutorService scheduler) {
        if (scheduler == null) {
            throw new NullPointerException();
        }
        this.entryIndex = new HashMap<String, Record>();
        this.expirationIndex = new TreeMap<Long, Record>();
        this.scheduler = scheduler;
        this.purgeFuture = null;
        this.scheduledPurgeTime = 0L;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized Serializable write(String key, Serializable value, long expireTimeout) {
        if ((key == null) || (value == null)) {
            throw new NullPointerException("key and value cannot be null");
        }
        Record entry = lookupEntryAndSetExpireTime(key, expireTimeout);
        return entry.write(value);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void writeAsync(final String key, final Serializable value, final long expireTimeout) {
        if ((key == null) || (value == null)) {
            throw new NullPointerException("key and value cannot be null");
        }
        scheduler.execute(new Runnable() {

            public void run() {
                Record entry = lookupEntryAndSetExpireTime(key, expireTimeout);
                entry.write(value);
            }
        });
    }

    /**
     * {@inheritDoc} Note: this implementation ignores the
     * completenessGuarantee. Each request is guaranteed to retrieve the value
     * associated to the given entry and key, if it exists; therefore, the
     * behavior is always like the one achievable with completenessGuarantee =
     * true.
     */
    public synchronized Serializable read(String key, long expireTimeout) {
        if (key == null) {
            throw new NullPointerException("key cannot be null");
        }
        Record entry = lookupEntryAndSetExpireTime(key, expireTimeout);
        return entry.read();
    }

    /**
     * Retrieve or create an entry for the specified key, setting or updating
     * its expire time. This method create a new entry if it doesn't exist.
     * Expire time is updated and set to the requested time (computed starting
     * from the given timeout) only if it is after the current one.
     * 
     * @param key the identifier of the entry to retrieve or create.
     * @param expireTimeout the requested expire timeout (expire time = current
     *            time + timeout).
     * @return the new or existing entry for the id specified, with the expire
     *         time updated as requested.
     */
    private synchronized Record lookupEntryAndSetExpireTime(String key, long expireTimeout) {
        assert (key != null) && (expireTimeout >= 0);
        long requestedExpireTime = System.currentTimeMillis() + expireTimeout;
        while (expirationIndex.containsKey(requestedExpireTime)) {
            requestedExpireTime++;
        }
        Record result = entryIndex.get(key);
        if (result == null) {
            result = new Record(key, requestedExpireTime);
            entryIndex.put(key, result);
            expirationIndex.put(requestedExpireTime, result);
            schedulePurgeIfNecessary();
        } else {
            long oldExpireTime = result.getExpireTime();
            if (requestedExpireTime > oldExpireTime) {
                result.setExpireTime(requestedExpireTime);
                expirationIndex.remove(oldExpireTime);
                expirationIndex.put(requestedExpireTime, result);
                schedulePurgeIfNecessary();
            }
        }
        return result;
    }

    /**
     * Schedule a new purge operation if needed. This method schedules a new
     * purge operation in correspondence of the nearest expire time. If a purge
     * operation was already scheduled, it is re-scheduled (or canceled, if it
     * is no more needed). This method uses the scheduler instance provided to
     * the memory during initialization; all the details of task creation and
     * cancel are handled internally by the method.
     */
    private synchronized void schedulePurgeIfNecessary() {
        long nextPurgeTime = 0;
        if (!expirationIndex.isEmpty()) {
            nextPurgeTime = expirationIndex.keySet().iterator().next();
        }
        if (nextPurgeTime == scheduledPurgeTime) {
            return;
        }
        if (purgeFuture != null) {
            purgeFuture.cancel(false);
        }
        scheduledPurgeTime = nextPurgeTime;
        purgeFuture = null;
        if (nextPurgeTime != 0) {
            purgeFuture = scheduler.schedule(new Runnable() {

                public void run() {
                    synchronized (this) {
                        purgeFuture = null;
                        purge();
                        schedulePurgeIfNecessary();
                    }
                }
            }, Math.max(0L, nextPurgeTime - System.currentTimeMillis()), TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Remove all the expired entries, updating the internal indexes.
     */
    private synchronized void purge() {
        long currentTime = System.currentTimeMillis();
        for (Iterator<Map.Entry<Long, Record>> i = expirationIndex.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<Long, Record> current = i.next();
            if (current.getKey() > currentTime) {
                break;
            }
            entryIndex.remove(current.getValue().getKey());
            i.remove();
        }
    }

    /**
     * Simple, in-memory implementation of an entry.
     * <p>
     * This class implements a simple entry stored in main memory. In the
     * distributed version of the shared memory, this entry will be actually
     * stored in a tuple space.
     * </p>
     * 
     * @author Francesco Corcoglioniti
     *         &lt;francesco.corcoglioniti@cefriel.it&gt;
     */
    public class Record {

        /** The immutable identifier of this memory record. */
        private String key;

        /** The absolute expire time for this memory record. */
        private long expireTime;

        /** A map containing the key-value pairs for this memory record. */
        private Serializable value;

        /**
         * Create a new memory record with the identifier and expire time
         * specified.
         * 
         * @param key an immutable key (not null).
         * @param expireTime the initial absolute expire time for the record.
         */
        public Record(String key, long expireTime) {
            this(key, expireTime, null);
        }

        /**
         * Create a new memory record with the identifier and expire time
         * specified.
         * 
         * @param key an immutable key (not null).
         * @param expireTime the initial absolute expire time for the record.
         * @param value the initial value of this memory record.
         */
        public Record(String key, long expireTime, Serializable value) {
            assert (key != null);
            this.key = key;
            this.expireTime = expireTime;
            this.value = value;
        }

        /**
         * Return the identifier of this memory record.
         * 
         * @return the immutable entry identifier.
         */
        public String getKey() {
            return key;
        }

        /**
         * Retrieve the absolute expire time of the memory record.
         * 
         * @return expireTime the expire time for this entry.
         */
        public long getExpireTime() {
            return expireTime;
        }

        /**
         * Set the absolute expire time of the memory record.
         * 
         * @param expireTime the expire time for this entry.
         */
        public void setExpireTime(long expireTime) {
            assert (expireTime >= 0);
            this.expireTime = expireTime;
        }

        /**
         * Set the value associated to the key.
         * 
         * @param value the new value to associate to the memory record (not
         *            null).
         * @return the previous value associated to the memory record.
         */
        public Serializable write(Serializable value) {
            assert (value != null);
            Serializable oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        /**
         * Read the value associated to the key for this memory record.
         * 
         * @return the value associated to the key.
         */
        public Serializable read() {
            return value;
        }
    }
}
