package net.jamcache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

class LocalMemoryCache implements Cache {

    private final CacheReplacementStrategy strategy;

    private final Map<String, CacheEntry> entries;

    LocalMemoryCache(CacheReplacementStrategy strategy) {
        this.strategy = strategy;
        this.entries = new WeakHashMap<String, CacheEntry>();
    }

    @Override
    public List<CacheEntry> get(List<String> keys) {
        List<CacheEntry> result = new ArrayList<CacheEntry>(keys.size());
        for (String key : keys) {
            result.add(entries.get(key));
        }
        return result;
    }

    @Override
    public CacheEntry get(String key) {
        return entries.get(key);
    }

    @Override
    public boolean set(CacheEntry entry, int expiry) {
        if (!strategy.apply(entries.values())) return false;
        entries.put(entry.getKey(), entry);
        return true;
    }

    @Override
    public boolean add(CacheEntry entry, int expiry) {
        if (!strategy.apply(entries.values())) return false;
        entries.put(entry.getKey(), entry);
        return true;
    }

    @Override
    public boolean append(CacheEntry entry, int expiry) {
        return false;
    }

    @Override
    public boolean delete(String key) {
        return false;
    }

    @Override
    public boolean prepend(CacheEntry entry, int expiry) {
        return false;
    }

    @Override
    public boolean replace(CacheEntry entry, int expiry) {
        return false;
    }
}
