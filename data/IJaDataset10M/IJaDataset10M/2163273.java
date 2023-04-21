package org.toobsframework.util.cache;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SimpleDispatchObjectCacheImpl extends ConcurrentLRUCache<String, DispatchObjectCacheEntry> implements DispatchObjectCache {

    private static final int DEFAULT_CACHE_SIZE = 200;

    private final ReadWriteLock key;

    private final Lock readLock;

    private final Lock writeLock;

    public SimpleDispatchObjectCacheImpl() {
        super(DEFAULT_CACHE_SIZE);
        key = new ReentrantReadWriteLock(true);
        readLock = key.readLock();
        writeLock = key.writeLock();
    }

    public SimpleDispatchObjectCacheImpl(int cacheSize) {
        super(cacheSize);
        key = new ReentrantReadWriteLock(true);
        readLock = key.readLock();
        writeLock = key.writeLock();
    }

    public DispatchObjectCacheEntry getAndLock(String key) throws InterruptedException {
        DispatchObjectCacheEntry cacheEntry = null;
        boolean isEntryLocked = false;
        readLock.lockInterruptibly();
        cacheEntry = this.get(key);
        if (cacheEntry != null) {
            isEntryLocked = cacheEntry.tryLock();
        }
        try {
            if (cacheEntry == null) {
                readLock.unlock();
                writeLock.lockInterruptibly();
                try {
                    cacheEntry = this.get(key);
                    if (cacheEntry == null) {
                        cacheEntry = new DispatchObjectCacheEntry(key, null);
                        isEntryLocked = cacheEntry.tryLock();
                        this.put(key, cacheEntry);
                    }
                } finally {
                    readLock.lock();
                    writeLock.unlock();
                }
            }
        } finally {
            readLock.unlock();
        }
        if (!isEntryLocked && cacheEntry != null) {
            cacheEntry.lock();
        }
        return cacheEntry;
    }

    public void release(DispatchObjectCacheEntry entry) {
        if (entry == null) {
            return;
        }
        writeLock.lock();
        try {
            entry.unlock();
            this.put(entry.getKey(), entry);
        } finally {
            writeLock.unlock();
        }
    }
}
