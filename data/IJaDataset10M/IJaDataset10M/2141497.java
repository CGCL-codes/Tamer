package org.openaion.gameserver.utils.collections;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rolandas
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class LastUsedCache<K extends Comparable, V> implements ICache<K, V>, Serializable {

    private static final long serialVersionUID = 3674312987828041877L;

    Map<K, Item> map = Collections.synchronizedMap(new HashMap<K, Item>());

    Item startItem = new Item();

    Item endItem = new Item();

    int maxSize;

    Object syncRoot = new Object();

    static class Item {

        public Item(Comparable k, Object v, long e) {
            key = k;
            value = v;
            expires = e;
        }

        public Item() {
        }

        public Comparable key;

        public Object value;

        public long expires;

        public Item previous;

        public Item next;
    }

    void removeItem(Item item) {
        synchronized (syncRoot) {
            item.previous.next = item.next;
            item.next.previous = item.previous;
        }
    }

    void insertHead(Item item) {
        synchronized (syncRoot) {
            item.previous = startItem;
            item.next = startItem.next;
            startItem.next.previous = item;
            startItem.next = item;
        }
    }

    void moveToHead(Item item) {
        synchronized (syncRoot) {
            item.previous.next = item.next;
            item.next.previous = item.previous;
            item.previous = startItem;
            item.next = startItem.next;
            startItem.next.previous = item;
            startItem.next = item;
        }
    }

    public LastUsedCache(int maxObjects) {
        maxSize = maxObjects;
        startItem.next = endItem;
        endItem.previous = startItem;
    }

    public CachePair[] getAll() {
        CachePair p[] = new CachePair[maxSize];
        int count = 0;
        synchronized (syncRoot) {
            Item cur = startItem.next;
            while (cur != endItem) {
                p[count] = new CachePair(cur.key, cur.value);
                count++;
                cur = cur.next;
            }
        }
        CachePair np[] = new CachePair[count];
        System.arraycopy(p, 0, np, 0, count);
        return np;
    }

    /**
	 *  Gets a value by key. Returns null if not found or expired
	 */
    public V get(K key) {
        Item cur = map.get(key);
        if (cur == null) return null;
        if (System.currentTimeMillis() > cur.expires) {
            map.remove(cur.key);
            removeItem(cur);
            return null;
        }
        if (cur != startItem.next) moveToHead(cur);
        return (V) cur.value;
    }

    public void put(K key, V obj) {
        put(key, obj, -1);
    }

    /**
	 *  Adds or renews a cache item pair
	 */
    public void put(K key, V value, long validTime) {
        Item cur = map.get(key);
        if (cur != null) {
            cur.value = value;
            if (validTime >= 0) cur.expires = System.currentTimeMillis() + validTime; else cur.expires = Long.MAX_VALUE;
            moveToHead(cur);
            return;
        }
        if (map.size() >= maxSize && maxSize != 0) {
            cur = endItem.previous;
            map.remove(cur.key);
            removeItem(cur);
        }
        long expires = 0;
        if (validTime >= 0) expires = System.currentTimeMillis() + validTime; else expires = Long.MAX_VALUE;
        Item item = new Item(key, value, expires);
        insertHead(item);
        map.put(key, item);
    }

    public void remove(K key) {
        Item cur = map.get(key);
        if (cur == null) return;
        map.remove(key);
        removeItem(cur);
    }

    public int size() {
        return map.size();
    }
}
