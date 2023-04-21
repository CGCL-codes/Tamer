package uk.co.christhomson.sibyl.cache.connectors;

import java.util.Map;
import uk.co.christhomson.sibyl.exception.CacheException;

public interface CacheConnector {

    public Object get(String cacheName, Object key) throws CacheException;

    public Map<?, ?> getAll(String cacheName) throws CacheException;

    public void put(String cacheName, Object key, Object value) throws CacheException;

    public void putAll(String cacheName, Map<?, ?> data) throws CacheException;

    public void clear(String cacheName) throws CacheException;

    public void clearAll() throws CacheException;

    public int getCacheSize(String cacheName) throws CacheException;

    public String getQueryLanguageDescription();

    public Map<?, ?> query(String cacheName, String query) throws CacheException;
}
