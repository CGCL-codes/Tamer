package org.tamacat.util;

public class LimitedCacheLRU<K, V extends LimitedCacheObject> extends CacheLRU<K, V> {

    protected long expire;

    public LimitedCacheLRU(int maxSize, long expire) {
        super(maxSize);
        this.expire = expire;
    }

    @Override
    public V get(K key) {
        V obj = super.get(key);
        if (obj != null) {
            if (obj.isCacheExpired(expire)) {
                super.remove(key);
                return null;
            }
        }
        return obj;
    }
}
