package de.dirkdittmar.flickr.group.comment.spring;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import de.dirkdittmar.flickr.group.comment.PropertyNames;
import de.dirkdittmar.flickr.group.comment.cache.EmptyPhotoCacheImpl;
import de.dirkdittmar.flickr.group.comment.cache.PhotoCache;
import de.dirkdittmar.flickr.group.comment.cache.PhotoCacheImpl;
import de.dirkdittmar.flickr.group.comment.domain.PhotoCacheObject;

public class MemoryCacheFactoryBean implements FactoryBean {

    private static final Logger log = Logger.getLogger(MemoryCacheFactoryBean.class);

    private PhotoCache photoCache;

    private Properties systemProperties;

    public void init() {
        int cacheSize = Integer.valueOf(systemProperties.getProperty(PropertyNames.MEMORY_CACHE_SIZE));
        boolean active = Boolean.valueOf(systemProperties.getProperty(PropertyNames.MEMORY_CACHE_ACTIVE));
        if (active) {
            Map<URI, PhotoCacheObject> map = new LRUMap<URI, PhotoCacheObject>(cacheSize);
            this.photoCache = new PhotoCacheImpl(map);
        } else {
            this.photoCache = new EmptyPhotoCacheImpl();
        }
    }

    public void setProperties(Properties properties) {
        this.systemProperties = properties;
    }

    @Override
    public Object getObject() throws Exception {
        return photoCache;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class getObjectType() {
        return PhotoCache.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private static class LRUMap<K, V> extends LinkedHashMap<K, V> {

        private static final long serialVersionUID = -7008089257931917066L;

        private int maxCapacity;

        public LRUMap(int maxCapacity) {
            super(maxCapacity, 0.75f, true);
            this.maxCapacity = maxCapacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            boolean remove = size() > this.maxCapacity;
            if (remove) {
                log.debug("have to drop the oldest Element in the Cache...");
            }
            return remove;
        }
    }
}
