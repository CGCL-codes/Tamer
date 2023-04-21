package org.commonlibrary.lcms.support.cache;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cache.Cache;
import org.hibernate.cache.CacheProvider;
import org.hibernate.cache.Timestamper;
import java.util.Properties;

/**
 * Used for configuring a single ehCache {@link net.sf.ehcache.CacheManager} to be used by
 *  Hiberante's SessionFactory and the rest  of the application.
 * <p/>
 * Code borrowed from http://www.leshazlewood.com/?p=37
 *
 * @author Jeff Wysong
 *         Date: Jul 23, 2008
 *         Time: 4:21:44 PM
 */
public class ExternalEhCacheProvider implements CacheProvider {

    protected final transient Log log = LogFactory.getLog(getClass());

    private static CacheManager cacheManager = null;

    /**
     * This is the method that is called by an external framework (e.g. Spring) to set the
     *  constructed CacheManager for all instances of this class. Therefore, when
     *  Hibernate instantiates this class, the previously statically injected CacheManager
     *  will be used for all hibernate calls to build caches.
     *  @param cacheManager the CacheManager instance to use for a HibernateSession factory using
     *  this class as its cache.provider_class.
     */
    public static void setCacheManager(CacheManager cacheManager) {
        ExternalEhCacheProvider.cacheManager = cacheManager;
    }

    public Cache buildCache(String name, Properties properties) throws CacheException {
        try {
            net.sf.ehcache.Ehcache cache = cacheManager.getEhcache(name);
            if (cache == null) {
                if (log.isWarnEnabled()) {
                    log.warn("Unable to find EHCache configuration for cache named [" + name + "]. Using defaults.");
                }
                cacheManager.addCache(name);
                cache = cacheManager.getEhcache(name);
                if (log.isDebugEnabled()) {
                    log.debug("Started EHCache region '" + name + "'");
                }
            }
            return new net.sf.ehcache.hibernate.EhCache(cache);
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException("Building the cache in the ExternalEhCacheProvider threw an exception!!!", e);
        }
    }

    public long nextTimestamp() {
        return Timestamper.next();
    }

    public void start(Properties properties) throws CacheException {
    }

    public void stop() {
    }

    public boolean isMinimalPutsEnabledByDefault() {
        return false;
    }
}
