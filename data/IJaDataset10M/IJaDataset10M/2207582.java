package net.sourceforge.rcache.decorator;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import net.sourceforge.rcache.Cache;

/**
 * <p>Decorator for Cache instances that adds an MRU list containing hard
 * references to the most recently used entries.</p>
 *
 * <p>This decorator prevents the most recently used entries from being
 * garbage collected.</p>
 *
 * @author Rodrigo Ruiz
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of cached values
 */
public final class MruGuard<K, V> implements Cache<K, V> {

    /**
   * The wrapped cache.
   */
    private final Cache<K, V> cache;

    /**
   * Map containing the most recently used entries.
   */
    private final MruMap<V> mru;

    /**
   * Creates an instance.
   *
   * @param cache      The cache instance to wrap
   * @param mruSize    The number of items to maintain in the MRU list
   */
    public MruGuard(Cache<K, V> cache, int mruSize) {
        this.cache = cache;
        this.mru = new MruMap<V>(mruSize);
    }

    /**
   * {@inheritDoc}
   */
    public V get(Object key) {
        V value = this.cache.get(key);
        this.mru.put(value);
        return value;
    }

    /**
   * {@inheritDoc}
   */
    public V put(K key, V value) {
        this.mru.put(value);
        value = this.cache.put(key, value);
        return value;
    }

    /**
   * {@inheritDoc}
   */
    public V remove(Object key) {
        V value = this.cache.remove(key);
        this.mru.remove(value);
        return value;
    }

    /**
   * {@inheritDoc}
   */
    public void clear() {
        this.mru.clear();
        this.cache.clear();
    }

    /**
   * Gets the maximum size of the MRU list.
   *
   * @return the MRU list max size
   */
    public int getMaxSize() {
        return this.mru.maxSize();
    }

    /**
   * Gets an iterator for the internal MRU list.
   * <p>
   * This method is for test purposes only.
   *
   * @return An iterator for the MRU list
   */
    protected Iterator<V> mruIterator() {
        return this.mru.keySet().iterator();
    }

    /**
   * Fixed-size map for MRU management.
   */
    private static final class MruMap<V> extends LinkedHashMap<V, Object> {

        /**
     * <code>serialVersionUID</code> attribute.
     */
        private static final long serialVersionUID = -5316259183944411057L;

        /**
     * Load factor for the internal map.
     */
        private static final float MRU_LOAD_FACTOR = 0.75f;

        /**
     * Size of the map.
     */
        private final int maxSize;

        /**
     * Creates an instance.
     *
     * @param maxSize  The max size of the map
     */
        public MruMap(int maxSize) {
            super(2 * maxSize, MRU_LOAD_FACTOR, true);
            this.maxSize = maxSize;
        }

        /**
     * Enforces the max size of the map.
     *
     * {@inheritDoc}
     */
        @Override
        protected boolean removeEldestEntry(Map.Entry<V, Object> entry) {
            return this.size() > maxSize;
        }

        /**
     * Puts a value in this MRU map.
     *
     * @param value The value to put
     */
        public void put(V value) {
            if (value != null) {
                synchronized (this) {
                    super.put(value, null);
                }
            }
        }

        /**
     * Gets the max size of this map.
     *
     * @return The max size of this map
     */
        public int maxSize() {
            return this.maxSize;
        }
    }
}
