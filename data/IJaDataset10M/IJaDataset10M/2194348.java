package org.acegisecurity.providers.cas.cache;

import org.acegisecurity.providers.cas.CasAuthenticationToken;
import org.acegisecurity.providers.cas.StatelessTicketCache;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.util.Assert;

/**
 * Caches tickets using a Spring IoC defined <A
 * HREF="http://ehcache.sourceforge.net">EHCACHE</a>.
 *
 * @author Ben Alex
 * @version $Id: EhCacheBasedTicketCache.java,v 1.8 2005/11/17 00:55:48 benalex Exp $
 */
public class EhCacheBasedTicketCache implements StatelessTicketCache, InitializingBean {

    private static final Log logger = LogFactory.getLog(EhCacheBasedTicketCache.class);

    private Cache cache;

    public CasAuthenticationToken getByTicketId(String serviceTicket) {
        Element element = null;
        try {
            element = cache.get(serviceTicket);
        } catch (CacheException cacheException) {
            throw new DataRetrievalFailureException("Cache failure: " + cacheException.getMessage());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Cache hit: " + (element != null) + "; service ticket: " + serviceTicket);
        }
        if (element == null) {
            return null;
        } else {
            return (CasAuthenticationToken) element.getValue();
        }
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public Cache getCache() {
        return cache;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(cache, "cache mandatory");
    }

    public void putTicketInCache(CasAuthenticationToken token) {
        Element element = new Element(token.getCredentials().toString(), token);
        if (logger.isDebugEnabled()) {
            logger.debug("Cache put: " + element.getKey());
        }
        cache.put(element);
    }

    public void removeTicketFromCache(CasAuthenticationToken token) {
        if (logger.isDebugEnabled()) {
            logger.debug("Cache remove: " + token.getCredentials().toString());
        }
        this.removeTicketFromCache(token.getCredentials().toString());
    }

    public void removeTicketFromCache(String serviceTicket) {
        cache.remove(serviceTicket);
    }
}
