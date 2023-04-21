package photorganizer.common.cache;

import java.util.Map;
import photorganizer.common.util.LRUMap;

public class MemoryCache<K, V> implements Cache<K, V> {

    private final Map<K, V> map;

    private boolean closed;

    public MemoryCache(int capacity) {
        map = new LRUMap<K, V>(capacity);
    }

    public V get(K key) {
        check();
        return map.get(key);
    }

    public void put(K key, V value) {
        check();
        map.put(key, value);
    }

    public void remove(K key) {
        check();
        map.remove(key);
    }

    public void close() {
        closed = true;
    }

    private void check() {
        if (closed) {
            throw new IllegalStateException("This cache is closed");
        }
    }
}
