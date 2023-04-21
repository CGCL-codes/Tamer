package ssmith.util;

import java.util.Hashtable;

public class MemCache extends Hashtable<String, MemCacheData> {

    private static final long serialVersionUID = 1L;

    private long timeout;

    public MemCache(long _timeout) {
        super();
        timeout = _timeout;
    }

    public boolean containsKey(int i) {
        return containsKey("" + i);
    }

    public boolean containsKey(String url) {
        if (super.containsKey(url)) {
            MemCacheData data = this.getMemCacheData(url);
            if (System.currentTimeMillis() < data.date_added + timeout) {
                return true;
            } else {
                this.remove(url);
            }
        }
        return false;
    }

    public void put(String url, Object data) {
        MemCacheData mc = new MemCacheData(data);
        this.put(url, mc);
    }

    public void put(int i, Object data) {
        put("" + i, data);
    }

    public Object get(String key) {
        MemCacheData data = super.get(key);
        return data.data;
    }

    public Object get(int key) {
        return get("" + key);
    }

    private MemCacheData getMemCacheData(String key) {
        return super.get(key);
    }
}

final class MemCacheData {

    public Object data;

    public long date_added;

    public MemCacheData(Object _data) {
        data = _data;
        date_added = System.currentTimeMillis();
    }
}
