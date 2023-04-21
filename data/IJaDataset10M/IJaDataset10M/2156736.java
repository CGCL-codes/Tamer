package org.sourceforge.jemm.client.shared;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.sourceforge.jemm.util.LockManager;

/**
 * An abstract factory that for any key will effectively return a singleton.  No hard reference is held to the 
 * target object, so it is liable for GC if a reference is not held elsewhere.  Instantiation of tke value objects
 * are defered to the subclass.
 * <P/>
 * <B>N.B. Be very careful altering this code, it is designed to be multi-threaded, but the mental hoops required
 * to reason about it are hard, and as always with this kind of code its very hard to test. Here be dragons! </B>
 * 
 * @author Rory Graves
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public abstract class WeakSingletonFactory<K, V> {

    protected final ConcurrentMap<K, WeakReference<V>> keysToWeakRefs;

    protected final Map<WeakReference<V>, K> refsToKeys;

    protected final ReferenceQueue<V> queue;

    protected final Thread queueListener;

    protected volatile boolean shuttingDown = false;

    protected LockManager<K> lockManager = new LockManager<K>();

    public WeakSingletonFactory() {
        keysToWeakRefs = new ConcurrentHashMap<K, WeakReference<V>>();
        refsToKeys = new ConcurrentHashMap<WeakReference<V>, K>();
        queue = new ReferenceQueue<V>();
        queueListener = new Thread(new QueueListener(), "WeakSingletonQueueListener");
        queueListener.setDaemon(true);
        queueListener.start();
    }

    public void put(K key, V value) {
        WeakReference<V> reference = new WeakReference<V>(value, queue);
        refsToKeys.put(reference, key);
        keysToWeakRefs.put(key, reference);
    }

    void removeExpired(Reference<V> expiredRef) {
        final boolean removed;
        K refKey = refsToKeys.remove(expiredRef);
        lockManager.acquire(refKey);
        try {
            removed = keysToWeakRefs.remove(refKey, expiredRef);
        } finally {
            lockManager.release(refKey);
        }
        if (removed) notifyExpired(refKey);
    }

    /**
	 * Returns whether the map currently contains a value associated with the given key
	 * @param key The key to test
	 * @return True if there is a key->value mapping, false otherwise.
	 */
    public boolean contains(final K key) {
        lockManager.acquire(key);
        try {
            WeakReference<V> ref = keysToWeakRefs.get(key);
            return ref != null && ref.get() != null;
        } finally {
            lockManager.release(key);
        }
    }

    public int size() {
        return keysToWeakRefs.size();
    }

    protected abstract void notifyExpired(K k);

    public V get(K key) {
        WeakReference<V> trackedReference = keysToWeakRefs.get(key);
        if (trackedReference == null) return null; else return trackedReference.get();
    }

    public synchronized void remove(K key) {
        WeakReference<V> ref = keysToWeakRefs.remove(key);
        if (ref != null) refsToKeys.remove(ref);
    }

    /**
	 * Stops the listener thread and releases native resources.
	 */
    public void shutdown() {
        shuttingDown = true;
        queueListener.interrupt();
    }

    /**
	 * Retrieve the current value associated with the given key, creating a new one if needed.
	 * @param key The key value.
	 * @return The value associated with key.
	 */
    public V getOrCreate(K key) {
        WeakReference<V> ref = keysToWeakRefs.get(key);
        V value = null;
        if (ref != null) value = ref.get();
        if (value == null) {
            lockManager.acquire(key);
            try {
                ref = keysToWeakRefs.get(key);
                value = ref == null ? null : ref.get();
                if (value != null) return value;
                value = createValue(key);
                WeakReference<V> reference = new WeakReference<V>(value, queue);
                refsToKeys.put(reference, key);
                keysToWeakRefs.put(key, reference);
            } finally {
                lockManager.release(key);
            }
        }
        return value;
    }

    protected abstract V createValue(K key);

    class QueueListener implements Runnable {

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            try {
                while (true) {
                    removeExpired((Reference<V>) queue.remove());
                }
            } catch (InterruptedException e) {
                if (!shuttingDown) throw new RuntimeException("Error occurred whilst waiting on expired queue", e);
            }
        }
    }
}
