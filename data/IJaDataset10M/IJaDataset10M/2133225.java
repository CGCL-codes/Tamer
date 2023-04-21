package org.kablink.teaming.ehcache;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cache.CacheKey;
import org.kablink.teaming.util.cache.DefinitionCache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

public class DefinitionCacheEventListener implements CacheEventListener {

    private static Log logger = LogFactory.getLog(DefinitionCacheEventListener.class);

    public DefinitionCacheEventListener() {
        if (logger.isDebugEnabled()) logger.debug("Instantiated");
    }

    @Override
    public void dispose() {
        if (logger.isDebugEnabled()) logger.debug("dispose");
    }

    @Override
    public void notifyElementEvicted(Ehcache cache, Element element) {
        if (logger.isDebugEnabled()) logger.debug("notifyElementEvicted [" + getDefinitionIdForLog(element) + "]");
        DefinitionCache.invalidate(getDefinitionId(element));
    }

    @Override
    public void notifyElementExpired(Ehcache cache, Element element) {
        if (logger.isDebugEnabled()) logger.debug("notifyElementExpired [" + getDefinitionIdForLog(element) + "]");
        DefinitionCache.invalidate(getDefinitionId(element));
    }

    @Override
    public void notifyElementPut(Ehcache cache, Element element) throws CacheException {
        if (logger.isDebugEnabled()) logger.debug("notifyElementPut [" + getDefinitionIdForLog(element) + "]");
    }

    @Override
    public void notifyElementRemoved(Ehcache cache, Element element) throws CacheException {
        if (logger.isDebugEnabled()) logger.debug("notifyElementRemoved [" + getDefinitionIdForLog(element) + "]");
        DefinitionCache.invalidate(getDefinitionId(element));
    }

    @Override
    public void notifyElementUpdated(Ehcache cache, Element element) throws CacheException {
        if (logger.isDebugEnabled()) logger.debug("notifyElementUpdated [" + getDefinitionIdForLog(element) + "]");
        DefinitionCache.invalidate(getDefinitionId(element));
    }

    @Override
    public void notifyRemoveAll(Ehcache cache) {
        if (logger.isDebugEnabled()) logger.debug("notifyRemoveAll");
        DefinitionCache.clear();
    }

    public Object clone() throws CloneNotSupportedException {
        if (logger.isDebugEnabled()) logger.debug("clone");
        super.clone();
        return new DefinitionCacheEventListener();
    }

    private String getDefinitionId(Element element) {
        Serializable sk = element.getKey();
        if (sk instanceof CacheKey) {
            org.hibernate.cache.CacheKey ck = (org.hibernate.cache.CacheKey) sk;
            Serializable k = ck.getKey();
            if (k instanceof String) {
                return (String) k;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private String getDefinitionIdForLog(Element element) {
        Serializable sk = element.getKey();
        if (sk instanceof CacheKey) {
            org.hibernate.cache.CacheKey ck = (org.hibernate.cache.CacheKey) sk;
            Serializable k = ck.getKey();
            if (k instanceof String) {
                return (String) k;
            } else {
                return k.getClass().getName();
            }
        } else {
            return sk.getClass().getName();
        }
    }
}
