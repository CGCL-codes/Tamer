package org.damour.base.server.hibernate;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Properties;
import net.sf.ehcache.CacheManager;
import org.damour.base.server.Logger;
import org.hibernate.cache.Cache;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.EhCache;
import org.hibernate.cache.Timestamper;
import org.hibernate.util.ConfigHelper;

public class CacheProvider implements org.hibernate.cache.CacheProvider {

    private CacheManager manager;

    public Cache buildCache(String name, Properties properties) throws CacheException {
        try {
            net.sf.ehcache.Cache cache = manager.getCache(name);
            if (cache == null) {
                manager.addCache(name);
                cache = manager.getCache(name);
                Logger.log("Starting EHCache region: [" + name + "]; using defaults.");
            }
            return new EhCache(cache);
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
    }

    /**
   * Returns the next timestamp.
   */
    public long nextTimestamp() {
        return Timestamper.next();
    }

    public void start(Properties properties) throws CacheException {
        if (manager != null) {
            Logger.log("Attempt to restart an already started EhCacheProvider. Use sessionFactory.close() " + " between repeated calls to buildSessionFactory. Using previously created EhCacheProvider." + " If this behaviour is required, consider using net.sf.ehcache.hibernate.SingletonEhCacheProvider.");
            return;
        }
        try {
            manager = new CacheManager(new ByteArrayInputStream(getEhcacheXMLString().getBytes()));
        } catch (net.sf.ehcache.CacheException e) {
            if (e.getMessage().startsWith("Cannot parseConfiguration CacheManager. Attempt to create a new instance of " + "CacheManager using the diskStorePath")) {
                throw new CacheException("Attempt to restart an already started EhCacheProvider. Use sessionFactory.close() " + " between repeated calls to buildSessionFactory. Consider using net.sf.ehcache.hibernate.SingletonEhCacheProvider.", e);
            } else {
                throw e;
            }
        }
    }

    private URL loadResource(String configurationResourceName) {
        URL url = ConfigHelper.locateConfig(configurationResourceName);
        Logger.log("Creating EhCacheProvider from a specified resource: " + configurationResourceName + " Resolved to URL: " + url);
        return url;
    }

    /**
   * Callback to perform any necessary cleanup of the underlying cache implementation during SessionFactory.close().
   */
    public void stop() {
        if (manager != null) {
            manager.shutdown();
            manager = null;
        }
    }

    public boolean isMinimalPutsEnabledByDefault() {
        return false;
    }

    public static String getEhcacheXMLString() {
        return "<ehcache><diskStore path=\"/tmp/sometests.com" + System.currentTimeMillis() + "\"/>" + "<defaultCache maxElementsInMemory=\"65536\" eternal=\"false\" overflowToDisk=\"false\" timeToIdleSeconds=\"7200\" timeToLiveSeconds=\"7200\" diskPersistent=\"false\" diskExpiryThreadIntervalSeconds=\"120\"/>" + "</ehcache>";
    }

    public CacheManager getManager() {
        return manager;
    }

    public void setManager(CacheManager manager) {
        this.manager = manager;
    }
}
