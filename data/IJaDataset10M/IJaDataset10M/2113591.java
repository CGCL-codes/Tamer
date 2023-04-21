package com.metasolutions.util;

import java.io.Serializable;

/**
 * An optimized HashMap which is not part of the Collections Framework.  This
 * HashMap has been designed for execution speed over all other concerns.  This
 * HashMap uses a "power of 2" implementation with a singly-linked collision
 * list.  This implemenation uses 0 divisions, 1 multiplication in the constructor,
 * and 1 multiplication per rehash() operation.<p>
 * 
 * The HashMap can be futher optimized by using hashcodes instead of keys.  This
 * is an admittedly dangerous operation:  the array returned from getKeys() would
 * be missing keys if they were not specified in a put() operation.  As a rule of
 * thumb, if you would need to use a getKeys() or getHashCodes() operation, you
 * should fill the map with all keys, or all hashcodes, respectively.  Note that
 * getHashCodes will always return all hashcodes, whether keys or hashcodes were
 * used.<p>
 * 
 * This implementation is thread-safe, but it should be noted that the Collection
 * classes provided in the new <tt>java.util.concurrent</tt> package will undoubtedly
 * be a superior solution for multithreaded applications.
 *
 * @author	Shawn Curry
 * @version 0.9	May 29, 2005
 */
public class SimpleHashMap implements Serializable {

    private Entry[] table = null;

    private int size = 0;

    private int capacity = 0x02;

    private int threshold = 0x02;

    private static final float LOAD_FACTOR = 0.75f;

    public SimpleHashMap() {
        table = new Entry[capacity];
    }

    public SimpleHashMap(int cap) {
        while (capacity < cap) capacity <<= 1;
        threshold = (int) (capacity * LOAD_FACTOR);
        if (cap >= threshold) {
            capacity <<= 1;
            threshold <<= 1;
        }
        table = new Entry[capacity];
    }

    private synchronized void rehash() {
        capacity <<= 1;
        threshold = (int) (capacity * LOAD_FACTOR);
        if (capacity == 0x01000000) throw new ArrayIndexOutOfBoundsException("MAP OVERFLOW");
        Entry[] oldtab = table;
        Entry[] newtab = new Entry[capacity];
        int mask = capacity - 1;
        for (int i = oldtab.length; i-- > 0; ) {
            for (Entry e = oldtab[i]; e != null; ) {
                Entry old = e;
                e = e.next;
                int index = old.hash & mask;
                old.next = newtab[index];
                newtab[index] = old;
            }
        }
        table = newtab;
    }

    public synchronized void clear() {
        for (int i = capacity; i-- > 0; ) table[i] = null;
        size = 0;
    }

    public synchronized int size() {
        return size;
    }

    public synchronized Object put(Object key, Object value) {
        if (size >= threshold) rehash();
        Entry[] tab = table;
        int hash = key.hashCode();
        int index = hash & (tab.length - 1);
        for (Entry e = tab[index]; e != null; e = e.next) {
            if (hash == e.hash) {
                Object retval = e.value;
                e.value = value;
                return retval;
            }
        }
        tab[index] = new Entry(key, value, hash, tab[index]);
        size++;
        return null;
    }

    public synchronized Object put(int hash, Object value) {
        if (size >= threshold) rehash();
        Entry[] tab = table;
        int index = hash & (tab.length - 1);
        for (Entry e = tab[index]; e != null; e = e.next) {
            if (hash == e.hash) {
                Object retval = e.value;
                e.value = value;
                return retval;
            }
        }
        tab[index] = new Entry(null, value, hash, tab[index]);
        size++;
        return null;
    }

    public synchronized Object get(Object key) {
        return get(key.hashCode());
    }

    public synchronized Object get(int hash) {
        Entry[] tab = table;
        int index = hash & (tab.length - 1);
        for (Entry e = tab[index]; e != null; e = e.next) if (hash == e.hash) return e.value;
        return null;
    }

    public synchronized Object remove(Object key) {
        return remove(key.hashCode());
    }

    public synchronized Object remove(int hash) {
        int index = hash & (table.length - 1);
        Entry e = table[index];
        if (e == null) return null;
        if (hash == e.hash) {
            table[index] = e.next;
            size--;
            return e.value;
        }
        do {
            Entry old = e;
            e = e.next;
            if (e == null) return null;
            if (hash == e.hash) {
                old.next = e.next;
                size--;
                return e.value;
            }
        } while (e != null);
        return null;
    }

    public synchronized int[] getHashCodes() {
        int[] keys = new int[size];
        Entry[] tab = table;
        int i = capacity, j = size;
        while (i > 0 && j > 0) {
            for (Entry e = tab[--i]; e != null; e = e.next) keys[--j] = e.hash;
        }
        return keys;
    }

    public synchronized Object[] getKeys() {
        Object[] keys = new Object[size];
        Entry[] tab = table;
        int i = capacity, j = size;
        while (i > 0 && j > 0) {
            for (Entry e = tab[--i]; e != null; e = e.next) keys[--j] = e.key;
        }
        return keys;
    }

    private static class Entry implements Serializable {

        Object key;

        Object value;

        int hash;

        Entry next;

        protected Entry(Object key, Object value, int hash, Entry next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }
}
