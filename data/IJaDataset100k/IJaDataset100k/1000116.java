package org.equanda.tapestry.useradmin.cache;

import org.equanda.persistence.om.EquandaPersistenceException;
import org.jboss.cache.CacheException;
import org.jboss.cache.Fqn;
import org.jboss.cache.TreeCacheMBean;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.mx.util.MBeanServerLocator;
import org.apache.log4j.Logger;
import javax.management.ObjectName;

/**
 * User admin cache.
 *
 * @author <a href="mailto:andrei@paragon-software.ro">Andrei Chiritescu</a>
 */
public class UserAdminCache {

    private static final Logger log = Logger.getLogger(UserAdminCache.class);

    protected TreeCacheMBean cache;

    protected String cacheServiceName;

    protected void initCache() {
        if (cache == null) {
            if (log.isDebugEnabled()) log.debug("start init cache");
            if (cacheServiceName == null) {
                throw new RuntimeException("Cache service name 'cacheServiceName' is null! Can not initialize.");
            }
            try {
                cache = (TreeCacheMBean) MBeanProxyExt.create(TreeCacheMBean.class, new ObjectName(getCacheServiceName()), MBeanServerLocator.locateJBoss());
                cache.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (log.isDebugEnabled()) log.debug("cache init done");
        }
    }

    public Object get(String name, LoaderType type, String key) throws EquandaPersistenceException {
        if (log.isDebugEnabled()) log.debug("load " + name + " key " + key);
        initCache();
        try {
            return cache.get(buildFqn(name, type), key);
        } catch (CacheException e) {
            throw new EquandaPersistenceException(e);
        }
    }

    public Object set(String name, LoaderType type, String key, Object value) throws EquandaPersistenceException {
        initCache();
        try {
            return cache.put(buildFqn(name, type), key, value);
        } catch (CacheException e) {
            throw new EquandaPersistenceException(e);
        }
    }

    protected Fqn buildFqn(String name, LoaderType type) {
        return new Fqn(type.fqn, name);
    }

    public String getCacheServiceName() {
        return cacheServiceName;
    }

    public void setCacheServiceName(String cacheServiceName) {
        this.cacheServiceName = cacheServiceName;
    }
}
