package org.citycarshare.application;

import java.util.*;
import java.sql.*;
import java.lang.reflect.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.citycarshare.persistence.*;
import org.citycarshare.util.Utils;

/**
 * Manages the read-only persistent data at the application level.
 * Responsible for managing the memory usage and loading and unloading
 * application data on demand.
 * The same references to applcation objects are probably being 
 * shared by multiple threads, so beware of concurrency issues.
 * 
 * Note that this design binds the application to a single JVM.
 * @author Brian "Dexter" Allen, vector@acm.org
 * @version $Id: ObjectCache.java,v 1.3 2002/07/13 02:31:53 dexter_allen Exp $
 */
public class ObjectCache {

    public static ObjectCache getInstance() {
        if (ourInstance == null) {
            ourInstance = new ObjectCache();
        }
        return ourInstance;
    }

    /**
     * @see getCollectionByIds
     */
    public void acquireCollectionByIds(Class argClass, Collection argIds, Collection outObjects) {
        Utils.ensureNotNull(outObjects, "outObjects (target Collection)");
        Utils.ensureNotNull(argClass, "argClass");
        if (argIds == null) {
            return;
        }
        Iterator i = argIds.iterator();
        Integer currId;
        Object currObj;
        while (i.hasNext()) {
            currId = (Integer) i.next();
            currObj = getObjectById(argClass, currId.intValue());
            outObjects.add(currObj);
        }
    }

    /**
     * @return a new Collection of objects of the specified class
     * whose IDs are in the argIds collection.  If an Id appears
     * more than once, only one corresponding object will be in 
     * the returned Collection.  If argIds is null, return will be
     * null.
     */
    public Collection getCollectionByIds(Class argClass, Collection argIds) {
        Collection retVal = new LinkedList();
        acquireCollectionByIds(argClass, argIds, retVal);
        return retVal;
    }

    public Object getObjectByCode(Class argClass, String argCode) {
        ensureCache(argClass);
        Map codeCacheMap = getCodeCacheForClass(argClass);
        Object retVal = codeCacheMap.get(argCode);
        if (retVal == null) {
            String errMsg = "Unable to find " + argClass.getName() + " by code " + argCode + "\nAll Map entries for this class:\n";
            errMsg += Utils.dumpMap(codeCacheMap);
            ourLog.warn(errMsg);
        }
        return retVal;
    }

    public Object getObjectById(Class argClass, int argId) {
        if (argId <= 0) {
            return null;
        }
        ensureCache(argClass);
        Map cacheMap = getCacheForClass(argClass);
        Integer id = new Integer(argId);
        Object retVal = cacheMap.get(id);
        if (retVal == null) {
            String errMsg = "Unable to find " + argClass.getName() + " by id " + argId + "\nAll Map entries for this class:\n";
            errMsg += Utils.dumpMap(cacheMap);
            ourLog.warn(errMsg);
        }
        return retVal;
    }

    public void acquireAllObjects(Class argClass, Collection outAll) {
        if (outAll == null) {
            throw new IllegalArgumentException("outAll must be non-null");
        }
        ensureCache(argClass);
        Map cacheMap = getCacheForClass(argClass);
        if (cacheMap == null) {
            String errMsg = "Unable to retrieve Cache for class: " + argClass.getName() + ".";
            ourLog.fatal(errMsg);
            throw new IllegalStateException(errMsg);
        }
        Set ids = cacheMap.keySet();
        Iterator i = ids.iterator();
        Integer currId;
        while (i.hasNext()) {
            currId = (Integer) i.next();
            outAll.add(cacheMap.get(currId));
        }
    }

    public MembershipGroup getMembershipGroupById(int argId) {
        return (MembershipGroup) getObjectById(MembershipGroup.class, argId);
    }

    /**
     * Provides a collection of all the Pods found in the
     * database.
     * @deprecated
     */
    public void acquireAllPods(Collection outCollection) {
        acquireAllObjects(Pod.class, outCollection);
    }

    /**
     * @return returns a Pod object based on the specified
     * argId POD_ID.
     */
    public Pod getPodById(int argId) {
        return (Pod) getObjectById(Pod.class, argId);
    }

    private ObjectCache() {
        LoadQuery loader = null;
        try {
            loader = org.citycarshare.persistence.MembershipGroupLoadQuery.getInstance();
            myLoaderMap.put(MembershipGroup.class.getName(), loader);
            loader = org.citycarshare.persistence.PodLoadQuery.getInstance();
            myLoaderMap.put(Pod.class.getName(), loader);
            loader = org.citycarshare.persistence.RegionLoadQuery.getInstance();
            myLoaderMap.put(Region.class.getName(), loader);
            loader = org.citycarshare.persistence.VehicleTypeLoadQuery.getInstance();
            myLoaderMap.put(VehicleType.class.getName(), loader);
        } catch (SQLException e) {
            ourLog.error("problem creating query", e);
        }
    }

    private Cachable buildCachable(Class argClass, Data argData) {
        Cachable retVal = null;
        try {
            Class[] ctorArgsSpec = new Class[] { argData.getClass() };
            Constructor ctor = argClass.getConstructor(ctorArgsSpec);
            Object[] ctorArgs = new Object[] { argData };
            retVal = (Cachable) ctor.newInstance(ctorArgs);
        } catch (Exception e) {
            ourLog.error("buildCachable error", e);
        }
        return retVal;
    }

    private Map getCodeCacheForClass(Class argClass) {
        Map retVal = (Map) myMapOfCodeCacheMaps.get(argClass.getName());
        if (retVal == null) {
            String errMsg = "Unable to find codeCacheMap for " + argClass.getName() + ".  This is expected " + "during system startup.";
            ourLog.info(errMsg);
        }
        return retVal;
    }

    private Map getCacheForClass(Class argClass) {
        Map retVal = (Map) myMapOfCacheMaps.get(argClass.getName());
        if (retVal == null) {
            String errMsg = "Unable to find CacheMap for " + argClass.getName() + ".  This is expected " + "during system startup.";
            ourLog.info(errMsg);
        }
        return retVal;
    }

    private LoadQuery getLoaderForClass(Class argClass) {
        LoadQuery retVal = (LoadQuery) myLoaderMap.get(argClass.getName());
        if (retVal == null) {
            String errMsg = "Unable to find LoadQuery for " + argClass.getName() + ".  Got null." + "This is serious-- somebody thinks a class " + "should be cached here but we don't " + "have a loader for it.";
            ourLog.error(errMsg);
            throw new IllegalStateException(errMsg);
        }
        return retVal;
    }

    private void ensureCache(Class argClass) {
        Map cache = getCacheForClass(argClass);
        Map codeCache = getCodeCacheForClass(argClass);
        if (cache == null || codeCache == null) {
            loadCacheMap(argClass);
        }
        cache = getCacheForClass(argClass);
        codeCache = getCodeCacheForClass(argClass);
        if (cache == null || codeCache == null) {
            throw new IllegalStateException("Unable to load cache for " + argClass.getName());
        }
    }

    private void loadCacheMap(Class argClass) {
        LoadQuery loader = getLoaderForClass(argClass);
        if (loader == null) {
            throw new IllegalStateException("Unable to find loader for " + argClass.getName());
        }
        Map dataCache = DataService.getInstance().getAllData(loader);
        if (dataCache == null || dataCache.isEmpty()) {
            String errMsg = "No objects of type " + argClass.getName() + " were found in " + " the datbase.  Perhaps a problem with " + "the DataLoad class?";
            throw new IllegalStateException(errMsg);
        }
        Set ids = dataCache.keySet();
        Iterator i = ids.iterator();
        Integer currId;
        Cachable obj;
        Map objCache = new HashMap();
        Map objCodeCache = new HashMap();
        while (i.hasNext()) {
            currId = (Integer) i.next();
            Data data = (Data) dataCache.get(currId);
            obj = buildCachable(argClass, data);
            if (obj == null) {
                ourLog.warn("buildCachable(" + argClass.getName() + ", data[" + data + "]) " + "returned a null object.");
            }
            objCache.put(currId, obj);
            objCodeCache.put(obj.getCode(), obj);
        }
        myMapOfCacheMaps.put(argClass.getName(), objCache);
        myMapOfCodeCacheMaps.put(argClass.getName(), objCodeCache);
    }

    private static ObjectCache ourInstance = null;

    private static Log ourLog = LogFactory.getLog(ObjectCache.class.getName());

    private Map myMapOfCacheMaps = new HashMap();

    private Map myMapOfCodeCacheMaps = new HashMap();

    private Map myLoaderMap = new HashMap();
}
