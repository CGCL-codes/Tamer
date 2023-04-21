package org.hardtokenmgmt.ws.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.jcs.engine.control.CompositeCacheManager;
import org.apache.log4j.Logger;
import org.hardtokenmgmt.common.vo.Cacheable;
import org.hardtokenmgmt.core.settings.BasicGlobalSettings;

/**
 * Class used to cache all data using a java cache library
 * to improve performance fetching data taking a long time
 * to calculate.
 * 
 * The cache is configured using the properties of
 * the global properties that is applied to all organizations
 * organization specific properties isn't supported.
 * 
 * After altering the properties will a restart of application server
 * be required.
 * 
 * @author Philip Vendil 4 apr 2011
 *
 * @version $Id$
 */
public class CacheManager {

    private static Logger log = Logger.getLogger(CacheManager.class);

    private static final String defaultConfig = "jcs.default=\n" + "jcs.default.cacheattributes=org.apache.jcs.engine.CompositeCacheAttributes\n" + "jcs.default.cacheattributes.MaxObjects=10000\n" + "jcs.default.cacheattributes.MemoryCacheName=org.apache.jcs.engine.memory.lru.LRUMemoryCache\n" + "jcs.default.elementattributes.IsEternal=true\n" + "jcs.default.elementattributes.IsRemote=true\n" + "jcs.default.elementattributes.IsLateral=true\n";

    private static CacheManager instance = null;

    private static JCS cache = null;

    private static int checkedOut = 0;

    private CacheManager(BasicGlobalSettings allGS) {
        CompositeCacheManager ccm = CompositeCacheManager.getUnconfiguredInstance();
        try {
            ccm.configure(getConfiguration(allGS));
            cache = JCS.getInstance("htmf");
        } catch (CacheException e) {
            log.error("Error configuring JCS cache", e);
        } catch (IOException e) {
            log.error("Error configuring JCS cache", e);
        }
    }

    /**
	 * Fetches the instance of the CacheManager.
	 * 
	 * @param allGS the global settings applied for all organizations.
	 * @return the CacheManager singleton.
	 */
    public static CacheManager getInstance(BasicGlobalSettings allGS) {
        synchronized (CacheManager.class) {
            if (instance == null) {
                instance = new CacheManager(allGS);
            }
        }
        synchronized (instance) {
            CacheManager.checkedOut++;
        }
        return instance;
    }

    /**
	 * Retrieves an object from the the cache 
	 * @param groupName the group name (type of object) to fetch from, never null.
	 * @param key the key of object to fetch.
	 * @return the cached object or null if object doesn't exist in cache�.
	 */
    public Cacheable get(String groupName, String key) {
        return (Cacheable) cache.getFromGroup(key, groupName);
    }

    /**
     * Puts an object on cache.
     * 
     * @param object the object to store in cache.
     * @param removeFirst if the object should be removed before it's updated.
     */
    public void put(Cacheable object, boolean removeFirst) {
        try {
            if (removeFirst) {
                cache.remove(object.genCacheKey(), object.genCacheGroup());
            }
            cache.putInGroup(object.genCacheKey(), object.genCacheGroup(), object);
        } catch (CacheException e) {
            log.error("Error storing object in JCS cache", e);
        }
    }

    /**
     * Puts an object on cache, removes the object before it is updated.
     * 
     * @param object the object to store in cache.
     */
    public void put(Cacheable object) {
        put(object, true);
    }

    /**
     * Method to remove an object from cache
     * @param object to remove.
     */
    public void remove(Cacheable object) {
        cache.remove(object.genCacheKey(), object.genCacheGroup());
    }

    /**
     * Retrieves all cached objects belonging to a group.
     * @param groupName the group (usually object type) to remove all objects for, never null.
     * @return returns a list of cacheable object beloning to the group, never null.
     */
    public List<Cacheable> getObjectsInGroup(String groupName) {
        ArrayList<Cacheable> retval = new ArrayList<Cacheable>();
        for (Object key : cache.getGroupKeys(groupName)) {
            retval.add((Cacheable) cache.getFromGroup(key, groupName));
        }
        return retval;
    }

    /**
     * Removes all object belonging to the specified group.
     * 
     * @param groupName the group (usually object type) to remove all objects for.
     */
    public void removeAllFromGroup(String groupName) {
        for (Object key : cache.getGroupKeys(groupName)) {
            cache.remove(key, groupName);
        }
    }

    private Properties getConfiguration(BasicGlobalSettings allGS) throws IOException {
        Properties retval = new Properties();
        retval.load(new ByteArrayInputStream(defaultConfig.getBytes()));
        Properties allGSProps = allGS.getProperties();
        for (String propName : allGSProps.stringPropertyNames()) {
            retval.setProperty(propName, allGSProps.getProperty(propName));
        }
        return retval;
    }
}
