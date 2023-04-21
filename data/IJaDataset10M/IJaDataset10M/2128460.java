package org.pblue.appengine.data.caching;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class CacheList<E> extends ArrayList<E> implements Cacheable {

    private Object cacheKey;

    private Set<Object> dependencies = new HashSet<Object>();

    public CacheList() {
        super();
    }

    public CacheList(Collection<? extends E> pCollection) {
        super(pCollection);
    }

    public Object getCacheKey() {
        return this.cacheKey;
    }

    public void setCacheKey(Object pCacheKey) {
        this.cacheKey = pCacheKey;
    }

    public Set<Object> getDependencies() {
        return this.dependencies;
    }

    public void setDependencies(Set<Object> pDependencies) {
        this.dependencies = pDependencies;
    }

    public void addDependency(Cacheable dependency) {
        this.dependencies.add(dependency.getCacheKey());
        if (Cache.cache().containsKey(this.getCacheKey())) {
            Cache.add(this);
        }
    }
}
