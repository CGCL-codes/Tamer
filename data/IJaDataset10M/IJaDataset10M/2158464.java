package org.vrspace.util;

import java.util.*;
import java.lang.ref.*;

/**
Soft cache. Does not prevent cached object from being reclaimed by gc.
*/
public class Cache {

    Hashtable cache = new Hashtable();

    ReferenceQueue queue = new ReferenceQueue();

    boolean working = false;

    /**
  Stores an object to the cache.
  */
    public synchronized void put(Object key, Object value) {
        cache.put(key, new CacheEntry(key, value));
        if (!working) {
            working = true;
            (new Thread(new Cleanup(), "Cache cleanup")).start();
        }
    }

    /**
  Retreives an object from the cache
  */
    public Object get(Object key) {
        Object ret = null;
        CacheEntry ref = (CacheEntry) cache.get(key);
        if (ref != null) {
            ret = ref.get();
        }
        return ret;
    }

    /**
  Removes an object from the cache
  */
    public synchronized void remove(Object key) {
        cache.remove(key);
        if (size() == 0) {
            working = false;
        }
    }

    /**
  */
    public int size() {
        return cache.size();
    }

    /**
  Extends Reference with a key
  */
    private class CacheEntry extends WeakReference {

        Object key;

        CacheEntry(Object key, Object value) {
            super(value, queue);
            this.key = key;
        }
    }

    /**
  Cleanup procedure. Removes entries enqueued by the garbage collector.
  */
    class Cleanup implements Runnable {

        public void run() {
            working = true;
            while (working) {
                try {
                    CacheEntry entry = (CacheEntry) queue.remove();
                    cache.remove(entry.key);
                    if (size() == 0) {
                        working = false;
                    }
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public static void main(String[] args) {
        Cache cache = new Cache();
        for (int i = 0; i < 1000; i++) {
            cache.put(new Integer(i), new Object());
        }
        System.out.println("Cache size: " + cache.size());
        System.gc();
        Util.sleep(100);
        System.out.println("Cache size: " + cache.size());
    }
}
