package net.sourceforge.powerswing.util.date;

public class CacheMap {

    private LeakyHashMap hash;

    private CacheMissListener missListener;

    public CacheMap(int maxsize, CacheMissListener missListener, LeakyHashMap.DiscardListener discardListener) {
        this.hash = new LeakyHashMap(maxsize, discardListener);
        this.missListener = missListener;
    }

    public CacheMap(int maxsize, CacheMissListener missListener) {
        this(maxsize, missListener, null);
    }

    public Object put(Object key, Object value) {
        return hash.put(key, value);
    }

    public Object get(Object key) {
        Object value = hash.get(key);
        if (value == null) {
            value = missListener.cacheMiss(key);
            hash.put(key, value);
        }
        return value;
    }

    public Object remove(Object key) {
        return hash.remove(key);
    }

    public boolean isEmpty() {
        return hash.isEmpty();
    }

    public void clear() {
        hash.clear();
    }

    public int size() {
        return hash.size();
    }

    public int maxSize() {
        return hash.maxSize();
    }
}
