package jaxlib.cache;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jaxlib.col.Iterators;
import jaxlib.col.concurrent.ConcurrentUnfairLRUHashMap;
import jaxlib.col.concurrent.ConcurrentXMap;
import jaxlib.lang.Ints;
import jaxlib.ref.ConcurrentSoftValueHashMap;

/**
 * A simple in-memory cache.
 *
 * @see MemoryCacheProperties
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: MemoryCache.java 3008 2011-10-31 01:39:24Z joerg_wassmer $
 */
@SuppressWarnings({ "serial", "unchecked", "element-type-mismatch" })
public class MemoryCache<K, V> extends Cache<K, V> {

    private static final int CONCURRENCY = 16;

    private static final Object INVALID = new Object();

    @CheckForNull
    private final CacheEntryValidator entryValidator;

    @Nonnull
    private final DefaultCacheStatistics cacheStatistics;

    /**
   * Used only for tracking the largest size.
   */
    @CheckForNull
    private final AtomicInteger trackedSize;

    @CheckForNull
    private ConcurrentMap<K, Object> map;

    public MemoryCache(final CacheSystem cacheSystem, final String name, CacheProperties properties) {
        super(cacheSystem, name, properties);
        this.cacheStatistics = new DefaultCacheStatistics();
        properties = super.properties;
        this.entryValidator = properties.cacheEntryValidator;
        if (properties instanceof MemoryCacheProperties) {
            final MemoryCacheProperties mp = (MemoryCacheProperties) properties;
            final int capacity = mp.capacity;
            if (mp.isSoft()) {
                this.map = new ConcurrentSoftValueHashMap<K, Object>(mp.initialCapacity, 0.75f, CONCURRENCY);
                this.trackedSize = null;
            } else if (capacity == Integer.MAX_VALUE) {
                this.map = new ConcurrentHashMap<K, Object>(mp.initialCapacity, 0.75f, CONCURRENCY);
                this.trackedSize = new AtomicInteger();
            } else {
                this.map = new ConcurrentUnfairLRUHashMap<K, Object>(mp.initialCapacity, 0.75f, capacity);
                this.trackedSize = null;
            }
        } else {
            this.map = new ConcurrentHashMap<K, Object>(CONCURRENCY, 0.75f, CONCURRENCY);
            this.trackedSize = new AtomicInteger();
        }
    }

    @Override
    public void clear() {
        final ConcurrentMap<K, ?> map = this.map;
        if (map != null) {
            map.clear();
            if (this.trackedSize != null) this.trackedSize.set(0);
        }
    }

    @Override
    public final boolean containsKey(final Object key) {
        final ConcurrentMap<K, ?> map = this.map;
        return (map != null) && map.containsKey(key);
    }

    @Override
    public CacheStatistics getCacheStatistics() {
        return this.cacheStatistics;
    }

    @Override
    public final V get(final Object key) throws IOException {
        final ConcurrentMap<K, ?> map = this.map;
        if (map == null) return null;
        final Object x = map.get(key);
        if (x == null) {
            this.cacheStatistics.missesBecauseAbsent.incrementAndGet();
            return null;
        }
        if (this.entryValidator == null) {
            this.cacheStatistics.hitsInMemoryStore.incrementAndGet();
            return (V) x;
        }
        final Entry<K, V> e = (Entry) x;
        if (e.value == null) {
            this.cacheStatistics.missesBecauseAbsent.incrementAndGet();
            return null;
        }
        Object oldAttachment = e.get();
        if (oldAttachment == INVALID) return null;
        Object newAttachment = this.entryValidator.validateCacheEntry(this, (K) key, e.value, oldAttachment);
        if (newAttachment == null) {
            oldAttachment = null;
            e.set(INVALID);
            map.remove(key);
            this.cacheStatistics.missesBecauseInvalid.incrementAndGet();
            return null;
        }
        if ((oldAttachment != newAttachment) && !e.compareAndSet(oldAttachment, newAttachment) && (e.get() == INVALID)) {
            oldAttachment = null;
            newAttachment = null;
            map.remove(key);
            this.cacheStatistics.missesBecauseInvalid.incrementAndGet();
            return null;
        }
        oldAttachment = null;
        newAttachment = null;
        this.cacheStatistics.hitsInMemoryStore.incrementAndGet();
        return e.value;
    }

    @Override
    public final int getCapacity() {
        final ConcurrentMap<K, ?> map = this.map;
        if (map == null) return 0;
        if (map instanceof ConcurrentXMap) return ((ConcurrentXMap) map).capacity();
        final int v = this.cacheStatistics.getLargestSize();
        return (v == 0) ? 0 : (v <= 16) ? CONCURRENCY : Ints.ceilToPowerOf2(v);
    }

    @Override
    public Iterator<K> keys() {
        final ConcurrentMap<K, ?> map = this.map;
        if (map == null) return Iterators.empty();
        return map.keySet().iterator();
    }

    @Override
    public boolean put(final K key, final V value) throws IOException {
        final ConcurrentMap<K, Object> map = ensureOpen();
        if (this.entryValidator == null) {
            if (map.put(key, value) == null) afterAdd(map);
            return true;
        } else {
            final Object attachment = this.entryValidator.validateCacheEntry(this, key, value, null);
            if (attachment == null) return false;
            if (map.put(key, new Entry<K, V>(key, value, attachment)) == null) afterAdd(map);
            return true;
        }
    }

    @Override
    public boolean putIfAbsent(final K key, final V value) throws IOException {
        final ConcurrentMap<K, Object> map = ensureOpen();
        if (this.entryValidator == null) {
            if (map.putIfAbsent(key, value) != null) return false;
            afterAdd(map);
            return true;
        } else {
            final Object attachment = this.entryValidator.validateCacheEntry(this, key, value, null);
            if ((attachment == null) || (map.putIfAbsent(key, new Entry<K, V>(key, value, attachment)) != null)) return false;
            afterAdd(map);
            return true;
        }
    }

    @Override
    public boolean remove(final Object key) throws IOException {
        final ConcurrentMap<K, ?> map = this.map;
        if ((map == null) || (map.remove(key) == null)) return false;
        afterRemove();
        return true;
    }

    @Override
    public final int size() {
        final ConcurrentMap<K, ?> map = this.map;
        return (map != null) ? map.size() : 0;
    }

    @Override
    protected void closeImpl() throws IOException {
        ConcurrentMap<K, ?> map = this.map;
        if (map != null) {
            this.map = null;
            map.clear();
            map = null;
            if (this.trackedSize != null) this.trackedSize.set(0);
        }
    }

    private void afterAdd(final ConcurrentMap<?, ?> map) {
        this.cacheStatistics.updateLargestSize((this.trackedSize == null) ? map.size() : this.trackedSize.incrementAndGet());
    }

    private void afterRemove() {
        if (this.trackedSize != null) this.trackedSize.decrementAndGet();
    }

    private ConcurrentMap<K, Object> ensureOpen() throws ClosedChannelException {
        final ConcurrentMap<K, Object> map = this.map;
        if ((map == null) || !isOpen()) throw new ClosedChannelException();
        return map;
    }

    private static final class Entry<K, V> extends AtomicReference<Object> {

        final K key;

        final V value;

        Entry(final K key, final V value) {
            super();
            this.key = key;
            this.value = value;
        }

        Entry(final K key, final V value, @Nullable final Object attachment) {
            super(attachment);
            this.key = key;
            this.value = value;
        }
    }
}
