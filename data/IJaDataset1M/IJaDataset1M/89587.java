package org.acegisecurity.providers.dao.cache;

import org.acegisecurity.providers.dao.UserCache;
import org.acegisecurity.userdetails.UserDetails;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.util.Assert;

/**
 * Caches <code>User</code> objects using a Spring IoC defined <A
 * HREF="http://ehcache.sourceforge.net">EHCACHE</a>.
 *
 * @author Ben Alex
 * @version $Id: EhCacheBasedUserCache.java,v 1.9 2005/11/29 13:10:11 benalex Exp $
 */
public class EhCacheBasedUserCache implements UserCache, InitializingBean {

    private static final Log logger = LogFactory.getLog(EhCacheBasedUserCache.class);

    private Cache cache;

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public Cache getCache() {
        return cache;
    }

    public UserDetails getUserFromCache(String username) {
        Element element = null;
        try {
            element = cache.get(username);
        } catch (CacheException cacheException) {
            throw new DataRetrievalFailureException("Cache failure: " + cacheException.getMessage());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Cache hit: " + (element != null) + "; username: " + username);
        }
        if (element == null) {
            return null;
        } else {
            return (UserDetails) element.getValue();
        }
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(cache, "cache mandatory");
    }

    public void putUserInCache(UserDetails user) {
        Element element = new Element(user.getUsername(), user);
        if (logger.isDebugEnabled()) {
            logger.debug("Cache put: " + element.getKey());
        }
        cache.put(element);
    }

    public void removeUserFromCache(UserDetails user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Cache remove: " + user.getUsername());
        }
        this.removeUserFromCache(user.getUsername());
    }

    public void removeUserFromCache(String username) {
        cache.remove(username);
    }
}
