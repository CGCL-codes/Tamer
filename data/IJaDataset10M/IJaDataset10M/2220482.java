package com.googlecode.jue.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * LRU 缓存
 * 
 * @author noah
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3516256638457588915L;

    /**
	 * 最大缓存数
	 */
    private final int maxCapacity;

    /**
	 * 默认加载因子
	 */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
	 * 读写锁
	 */
    private final ReadWriteLock rwlock = new ReentrantReadWriteLock();

    /**
	 * 读锁
	 */
    private final Lock readLock = rwlock.readLock();

    /**
	 * 写锁
	 */
    private final Lock writeLock = rwlock.writeLock();

    /**
	 * 初始化緩存
	 * @param maxCapacity 最大缓存数
	 */
    public LRUCache(int maxCapacity) {
        super(maxCapacity, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
    }

    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        return size() > maxCapacity;
    }

    @Override
    public boolean containsKey(Object key) {
        try {
            readLock.lock();
            return super.containsKey(key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public V get(Object key) {
        try {
            readLock.lock();
            return super.get(key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        try {
            writeLock.lock();
            return super.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public int size() {
        try {
            readLock.lock();
            return super.size();
        } finally {
            readLock.unlock();
        }
    }

    public void clear() {
        try {
            writeLock.lock();
            super.clear();
        } finally {
            writeLock.unlock();
        }
    }

    public Collection<Map.Entry<K, V>> getAll() {
        try {
            readLock.lock();
            return new ArrayList<Map.Entry<K, V>>(super.entrySet());
        } finally {
            readLock.unlock();
        }
    }
}
