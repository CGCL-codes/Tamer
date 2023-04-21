package org.jdbf.engine.caching;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.jcache.Cache;
import javax.jcache.CacheAccessFactory;
import javax.jcache.CacheNotAvailableException;
import javax.jcache.ObjectNotFoundException;
import javax.jcache.ObjectExistsException;
import javax.jcache.CacheAccess;
import org.jdbf.castor.Messages;
import org.jdbf.engine.basic.ObjectMapped;
import org.jdbf.engine.basic.PrimaryKey;
import org.jdbf.engine.mapping.BeanDescriptor;
import org.jdbf.engine.mapping.PrimaryKeyMap;
import org.jdbf.engine.repository.RepositoryView;

/**
 * <code>CacheManager</code> handles all operation that are 
 * performed in cache. Cache is created from paramters that are
 * contained in a configuration file. The structure of cache is composed 
 * from region and group. Region is name of database and group is name of 
 * repository view. Region is top of hierarchy, group are contained in region
 * and under group are present objects that are stored. This structure is
 * created in CacheManager.loadCache(Collection) method.   
 * 
 *  
 * @author Giovanni Martone<br>
 * @version $Revision: 1.7 $<br>
 * last changed by $Author: gmartone $
 */
public class CacheManager {

    /**
	 * <p>
	 * Represents CacheManager object
	 * </p>
	 */
    private static CacheManager cacheManager;

    /**
	 * <p>
	 * Represents class name for Logger
	 * </p>
	 */
    private static final String CLASS_NAME = "org.jdbf.engine.caching.CacheManager";

    /**
	 * <p>
	 * Represents Logger object
	 * </p>
	 */
    private Logger logger;

    /**
	 * <p>
	 * Represents CacheIdManager
	 * </p>
	 */
    private CacheIdManagerImpl cacheIdManager;

    /**
	 * Represents factory of Cache
	 */
    private CacheAccessFactory factory;

    /**
	 * 
	 * Creates CacheManager object. This method load information
	 * from configuration file and create cache.
	 * 
	 * @throws CacheException
	 * 
	 */
    protected CacheManager() throws CacheException {
        try {
            logger = Logger.getLogger(CLASS_NAME);
            factory = CacheAccessFactory.getInstance();
            Cache cache = factory.getCache(false);
            cache.open(null);
            cacheIdManager = new CacheIdManagerImpl();
        } catch (CacheNotAvailableException e) {
            throw new CacheException(e);
        } catch (javax.jcache.CacheException e) {
            throw new CacheException(e);
        }
    }

    /**
	 * 
	 * Creates id cache for an object specified in obj.
	 * 
	 * Value of cache id is created on value of primary key.
	 *
	 * @param pk PrimaryKeyMap
	 * @param obj ObjectMapped
	 * @return String cache id
	 * @throws CacheException
	 * @see CacheIdManager#createCacheId(PrimaryKeyMap,ObjectMapped)
	 *  
	 */
    public String createId(PrimaryKeyMap pk, ObjectMapped obj) throws CacheException {
        return cacheIdManager.createCacheId(pk, obj);
    }

    /**
	 * 
	 * Creates id cache for an object.
	 * 
	 * Value of cache id is created on value of primary key
	 * specified in pk parameter.
	 *
	 * @param pk PrimaryKey
	 * @param pkMap PrimaryKeyMap
	 * @return String cache id
	 * @throws CacheException
	 * @see CacheIdManager#createCacheId(PrimaryKey,PrimaryKeyMap)
	 *  
	 */
    public String createId(PrimaryKey pk, PrimaryKeyMap pkMap) throws CacheException {
        return cacheIdManager.createCacheId(pk, pkMap);
    }

    /**
	 * 
	 * Return a ObjectMapped from cache.
	 * 
	 * Location in cache of this object is specified 
	 * from region and group paramaters.
	 * If it returns null, object with cache id specified in 
	 * cacheId, is not present in cache. 
	 *
	 * @param cacheId cache id
	 * @param region
	 * @param group
	 * @return ObjectMapped
	 * @throws CacheException
	 *  
	 */
    public ObjectMapped getFromCache(String cacheId, String region, String group) throws CacheException {
        try {
            logger.log(Level.INFO, Messages.format("CacheManager.get", cacheId));
            CacheAccess access = factory.getAccess(region);
            logger.log(Level.INFO, Messages.format("CacheManager.getok", cacheId));
            return (ObjectMapped) access.get(cacheId, group, null);
        } catch (ObjectNotFoundException e) {
            logger.log(Level.INFO, Messages.format("CacheManager.getko", cacheId));
            return null;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    /**
	 * 
	 * Return an instance of this object.
	 * 
	 * @return CacheManager
	 * @throws CacheException
	 *  
	 */
    public static synchronized CacheManager getInstance() throws CacheException {
        if (cacheManager == null) {
            cacheManager = new CacheManager();
        }
        return cacheManager;
    }

    /**
	 * 
	 * Invalidate in cache the object that has cache id specified 
	 * in id and is present in region specified in region.
	 * 
	 * @param id cache id
	 * @param region
	 * @return boolean true if object has been invalidate,false otherwise
	 * @throws CacheException
	 *  
	 */
    public boolean invalidateObject(String id, String region) throws CacheException {
        boolean isInvalidate = false;
        try {
            CacheAccess access = factory.getAccess(region);
            if (access.isPresent(id)) {
                access.invalidate(id);
                isInvalidate = true;
                logger.log(Level.INFO, Messages.format("CacheManager.remove", id));
            }
        } catch (Exception e) {
            throw new CacheException(e);
        }
        return isInvalidate;
    }

    /**
	 * 
	 * Loads cache,creating regions and groups.
	 *  
	 * Region is the name of database connection and group 
	 * is the name of repository view 
	 * 
	 * @param views Collection
	 * @throws CacheException
	 * 
	 */
    public void loadCache(Collection views) throws CacheException {
        try {
            Iterator iter = views.iterator();
            Cache cache = factory.getCache();
            while (iter.hasNext()) {
                CacheAccess access = null;
                RepositoryView view = (RepositoryView) iter.next();
                BeanDescriptor beanDesc = view.getBeanDescriptor();
                boolean isCacheable = beanDesc.isCacheable();
                String region = beanDesc.getDatabaseName();
                String group = beanDesc.getRepositoryViewName();
                if (isCacheable) {
                    try {
                        factory.defineRegion(region);
                    } catch (ObjectExistsException e) {
                    } finally {
                        access = factory.getAccess(region);
                    }
                    access.defineGroup(group);
                }
            }
            int size = cache.getAttributes().getMemoryCacheSize();
            logger.log(Level.INFO, Messages.format("CacheManager.size", new Integer(size)));
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    /**
	 * Put in cache the object specified in object with key specified in id.
	 * 
	 * Location where object is stored is specified by region and group 
	 * parameters
	 * 
	 * @param id String cache id
	 * @param object ObjectMapped
	 * @param region 
	 * @param group
	 * @throws CacheException
	 * 
	 */
    public void putInCache(String id, ObjectMapped object, String region, String group) throws CacheException {
        try {
            CacheAccess access = factory.getAccess(region);
            if (!access.isPresent(id)) {
                access.put(id, group, object);
                logger.log(Level.INFO, Messages.format("CacheManager.put", id));
            }
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    /**
	 * Replace object specified in object with key specified in id.
	 * 
	 * Location where object is stored is specified by region and group 
	 * parameters
	 * 
	 * @param id String cache id
	 * @param object ObjectMapped
	 * @param region 
	 * @param group
	 * @throws CacheException
	 * 
	 */
    public void replaceObject(String id, ObjectMapped object, String region, String group) throws CacheException {
        try {
            CacheAccess access = factory.getAccess(region);
            if (access.isPresent(id)) access.replace(id, group, object);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }
}
