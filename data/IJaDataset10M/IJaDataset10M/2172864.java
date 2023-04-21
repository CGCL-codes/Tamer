package org.jcvi.glk.helpers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @param <T> The type of object to store in the cache.
 * @author jsitz
 * @author dkatzel
 */
public class ObjectCache<T> {

    private final Map<String, T> cache;

    private boolean complete;

    /**
     * Creates a new <code>ObjectCache</code>. 
     *
     */
    public ObjectCache() {
        super();
        this.cache = new HashMap<String, T>();
    }

    public boolean available(String name) {
        return this.cache.containsKey(name);
    }

    public T get(String name) {
        return this.cache.get(name);
    }

    public void offer(String name, T object) {
        if (!this.available(name)) {
            this.store(name, object);
        }
    }

    public T store(String name, T object) {
        if (object != null) this.cache.put(name, object);
        return object;
    }

    public Set<T> getAllStoredItems() {
        return new HashSet<T>(this.cache.values());
    }

    public boolean isComplete() {
        return this.complete;
    }

    public void markComplete() {
        this.complete = true;
    }

    public void markIncomplete() {
        this.complete = false;
    }
}
