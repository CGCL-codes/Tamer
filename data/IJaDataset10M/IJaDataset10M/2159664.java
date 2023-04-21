package com.danga.memcached;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.jsp.PageContext;
import java.io.Serializable;
import java.net.URLClassLoader;
import java.net.URL;

/**
 * Original please visit <a href="http://sourceforge.net/projects/memcachetaglib">memcachetaglib in sf</a><p/>
 * This is cache config provide the fast cache access as cachetag in jsp.<p>
 *
 * @author <a href="mailto:cytown@gmail.com">Cytown</a>
 * @version 1.09
 */
public class CacheConfig<T extends Serializable> {

    private static transient Log log = LogFactory.getLog(CacheConfig.class);

    private String key;

    private int time;

    private String group;

    protected Cache<T> cache = null;

    protected boolean turbo = false;

    protected CacheManager admin = null;

    /**
     * Default construction.
     */
    public CacheConfig() {
    }

    /**
     * Construtcion with key and time
     * @param key the cache key
     * @param time the timeout time in seconds.
     */
    public CacheConfig(String key, int time) {
        this.key = key;
        this.time = time;
    }

    /**
     * Set the key
     * @param key cache key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Set the timeout time
     * @param time in seconds
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * Set use turbo scope
     * @param turbo true if turbo scope
     */
    public void setTurbo(boolean turbo) {
        this.turbo = turbo;
    }

    /**
     * Set the group of the cache
     * @param group group name
     */
    public void setGroup(String group) {
        this.group = group;
    }

    protected final boolean check() {
        if (key == null || key.equals("")) {
            log.error("The key can not be empty!");
            return false;
        }
        if (time <= 5) {
            log.warn("The key:[" + key + "] time is " + time + "!");
        }
        return true;
    }

    protected final void initCache() {
        cache = new Cache<T>(key);
        cache.setTime(time);
    }

    /**
     * Get the cached object, in most case, just call this.
     * @return Cached object
     */
    public T get() {
        return get((ClassLoader) null);
    }

    /**
     * Get the cached object.
     * @param o the caller to get classloader
     * @return Cached object
     */
    public T get(Object o) {
        return get(o.getClass());
    }

    /**
     * Get the cached object.
     * @param clazz the class to get classloader
     * @return Cached object
     */
    public T get(Class clazz) {
        return get(clazz.getClassLoader());
    }

    /**
     * Get the cached object.
     * @param classloader the classloader
     * @return Cached object
     */
    public T get(ClassLoader classloader) {
        if (!check() || !initAdmin()) return null;
        initCache();
        if (classloader != null) cache.setClassLoader(classloader);
        cache = admin.getCache(cache, PageContext.APPLICATION_SCOPE, turbo);
        if (cache != null) {
            return cache.getObject();
        }
        return null;
    }

    protected final boolean initAdmin() {
        if (admin == null) {
            try {
                admin = CacheManager.getInstance(this.group);
            } catch (IllegalStateException e) {
                log.error("Can't find the cache server, cache disabled!");
                return false;
            }
        }
        return true;
    }

    /**
     * Set the object to be cached.
     * @param o cached object
     */
    public void set(T o) {
        if (!check() || !initAdmin()) return;
        initCache();
        cache.setObject(o);
        admin.setCache(cache, PageContext.APPLICATION_SCOPE, turbo);
    }

    /**
     * Clear the cache with the key.
     */
    public void delete() {
        if (!check() || !initAdmin()) return;
        initCache();
        admin.deleteCache(cache, PageContext.APPLICATION_SCOPE, turbo);
    }
}
