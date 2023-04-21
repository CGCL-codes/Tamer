package org.apache.roller.util.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.roller.business.runnable.ContinuousWorkerThread;
import org.apache.roller.business.runnable.Job;
import org.apache.roller.config.RollerConfig;
import org.apache.roller.pojos.BookmarkData;
import org.apache.roller.pojos.CommentData;
import org.apache.roller.pojos.FolderData;
import org.apache.roller.pojos.RefererData;
import org.apache.roller.pojos.UserData;
import org.apache.roller.pojos.WeblogCategoryData;
import org.apache.roller.pojos.WeblogEntryData;
import org.apache.roller.pojos.WeblogTemplate;
import org.apache.roller.pojos.WebsiteData;

/**
 * A governing class for Roller cache objects.
 *
 * The purpose of the CacheManager is to provide a level of abstraction between
 * classes that use a cache and the implementations of a cache.  This allows
 * us to create easily pluggable cache implementations.
 * 
 * The other purpose is to provide a single interface for interacting with all
 * Roller caches at the same time.  This is beneficial because as data
 * changes in the system we often need to notify all caches that some part of
 * their cached data needs to be invalidated, and the CacheManager makes that
 * process easier.
 *
 * @author Allen Gilliland
 */
public class CacheManager {

    private static Log mLogger = LogFactory.getLog(CacheManager.class);

    private static final String DEFAULT_FACTORY = "org.apache.roller.util.cache.ExpiringLRUCacheFactoryImpl";

    private static CacheFactory mCacheFactory = null;

    private static Cache lastExpiredCache = null;

    private static Set cacheHandlers = new HashSet();

    private static ContinuousWorkerThread futureInvalidationsThread = null;

    static {
        String classname = RollerConfig.getProperty("cache.defaultFactory");
        try {
            Class factoryClass = Class.forName(classname);
            mCacheFactory = (CacheFactory) factoryClass.newInstance();
        } catch (ClassCastException cce) {
            mLogger.error("It appears that your factory does not implement " + "the CacheFactory interface", cce);
        } catch (Exception e) {
            mLogger.error("Unable to instantiate cache factory [" + classname + "]" + " falling back on default", e);
        }
        if (mCacheFactory == null) try {
            Class factoryClass = Class.forName(DEFAULT_FACTORY);
            mCacheFactory = (CacheFactory) factoryClass.newInstance();
        } catch (Exception e) {
            mLogger.fatal("Failed to instantiate a cache factory", e);
        }
        mLogger.info("Cache Manager Initialized.");
        mLogger.info("Default cache factory = " + mCacheFactory.getClass().getName());
        String lastExpCacheFactory = RollerConfig.getProperty("cache.lastExpired.factory");
        Map lastExpProps = new HashMap();
        if (lastExpCacheFactory != null) {
            lastExpProps.put("factory", lastExpCacheFactory);
        }
        lastExpiredCache = CacheManager.constructCache(null, lastExpProps);
        String customHandlers = RollerConfig.getProperty("cache.customHandlers");
        if (customHandlers != null && customHandlers.trim().length() > 0) {
            String[] cHandlers = customHandlers.split(",");
            for (int i = 0; i < cHandlers.length; i++) {
                try {
                    Class handlerClass = Class.forName(cHandlers[i]);
                    CacheHandler customHandler = (CacheHandler) handlerClass.newInstance();
                    cacheHandlers.add(customHandler);
                } catch (ClassCastException cce) {
                    mLogger.error("It appears that your handler does not implement " + "the CacheHandler interface", cce);
                } catch (Exception e) {
                    mLogger.error("Unable to instantiate cache handler [" + cHandlers[i] + "]", e);
                }
            }
        }
        Integer peerTime = new Integer(5);
        String peerTimeString = RollerConfig.getProperty("cache.futureInvalidations.peerTime");
        try {
            peerTime = new Integer(peerTimeString);
        } catch (NumberFormatException nfe) {
        }
        int threadTime = (peerTime.intValue() * 60 * 1000) - (10 * 1000);
        futureInvalidationsThread = new ContinuousWorkerThread("future invalidations thread", threadTime);
        Job futureInvalidationsJob = new FuturePostingsInvalidationJob();
        Map inputs = new HashMap();
        inputs.put("peerTime", peerTime);
        futureInvalidationsJob.input(inputs);
        futureInvalidationsThread.setJob(futureInvalidationsJob);
        futureInvalidationsThread.start();
    }

    private CacheManager() {
    }

    /**
     * Ask the CacheManager to construct a cache.
     *
     * Normally the CacheManager will use whatever CacheFactory has been
     * chosen for the system via the cache.defaultFactory property.
     * However, it is possible to override the use of the default factory by
     * supplying a "factory" property to this method.  The value should
     * be the full classname for the factory you want to use for constructing
     * the cache.
     *
     * example:
     *   factory -> org.apache.roller.util.cache.LRUCacheFactoryImpl
     *
     * This allows Roller admins the ability to choose a caching strategy to
     * use for the whole system, but override it in certain places where they
     * see fit.  It also allows users to write their own caching modifications
     * and have them used only by specific caches.
     */
    public static Cache constructCache(CacheHandler handler, Map properties) {
        mLogger.debug("Constructing new cache with props " + properties);
        Cache cache = null;
        if (properties != null && properties.containsKey("factory")) {
            String classname = (String) properties.get("factory");
            try {
                Class factoryClass = Class.forName(classname);
                CacheFactory factory = (CacheFactory) factoryClass.newInstance();
                cache = factory.constructCache(properties);
            } catch (ClassCastException cce) {
                mLogger.error("It appears that your factory [" + classname + "] does not implement the CacheFactory interface", cce);
            } catch (Exception e) {
                mLogger.error("Unable to instantiate cache factory [" + classname + "] falling back on default", e);
            }
        }
        if (cache == null) {
            cache = mCacheFactory.constructCache(properties);
        }
        if (handler != null) {
            cacheHandlers.add(handler);
        }
        return cache;
    }

    /**
     * Register a CacheHandler to listen for object invalidations.
     *
     * This is here so that it's possible to to add classes which would respond
     * to object invalidations without necessarily having to create a cache.
     *
     * An example would be a handler designed to notify other machines in a 
     * cluster when an object has been invalidated, or possibly the search
     * index management classes are interested in knowing when objects are
     * invalidated.
     */
    public static void registerHandler(CacheHandler handler) {
        mLogger.debug("Registering handler " + handler);
        if (handler != null) {
            cacheHandlers.add(handler);
        }
    }

    public static void invalidate(WeblogEntryData entry) {
        mLogger.debug("invalidating entry = " + entry.getAnchor());
        setLastExpiredDate(entry.getWebsite().getHandle());
        Iterator handlers = cacheHandlers.iterator();
        while (handlers.hasNext()) {
            ((CacheHandler) handlers.next()).invalidate(entry);
        }
    }

    public static void invalidate(WebsiteData website) {
        mLogger.debug("invalidating website = " + website.getHandle());
        setLastExpiredDate(website.getHandle());
        Iterator handlers = cacheHandlers.iterator();
        while (handlers.hasNext()) {
            ((CacheHandler) handlers.next()).invalidate(website);
        }
    }

    public static void invalidate(BookmarkData bookmark) {
        mLogger.debug("invalidating bookmark = " + bookmark.getId());
        setLastExpiredDate(bookmark.getWebsite().getHandle());
        Iterator handlers = cacheHandlers.iterator();
        while (handlers.hasNext()) {
            ((CacheHandler) handlers.next()).invalidate(bookmark);
        }
    }

    public static void invalidate(FolderData folder) {
        mLogger.debug("invalidating folder = " + folder.getId());
        setLastExpiredDate(folder.getWebsite().getHandle());
        Iterator handlers = cacheHandlers.iterator();
        while (handlers.hasNext()) {
            ((CacheHandler) handlers.next()).invalidate(folder);
        }
    }

    public static void invalidate(CommentData comment) {
        mLogger.debug("invalidating comment = " + comment.getId());
        setLastExpiredDate(comment.getWeblogEntry().getWebsite().getHandle());
        Iterator handlers = cacheHandlers.iterator();
        while (handlers.hasNext()) {
            ((CacheHandler) handlers.next()).invalidate(comment);
        }
    }

    public static void invalidate(RefererData referer) {
        mLogger.debug("invalidating referer = " + referer.getId());
        Iterator handlers = cacheHandlers.iterator();
        while (handlers.hasNext()) {
            ((CacheHandler) handlers.next()).invalidate(referer);
        }
    }

    public static void invalidate(UserData user) {
        mLogger.debug("invalidating user = " + user.getUserName());
        Iterator handlers = cacheHandlers.iterator();
        while (handlers.hasNext()) {
            ((CacheHandler) handlers.next()).invalidate(user);
        }
    }

    public static void invalidate(WeblogCategoryData category) {
        mLogger.debug("invalidating category = " + category.getId());
        setLastExpiredDate(category.getWebsite().getHandle());
        Iterator handlers = cacheHandlers.iterator();
        while (handlers.hasNext()) {
            ((CacheHandler) handlers.next()).invalidate(category);
        }
    }

    public static void invalidate(WeblogTemplate template) {
        mLogger.debug("invalidating template = " + template.getId());
        setLastExpiredDate(template.getWebsite().getHandle());
        Iterator handlers = cacheHandlers.iterator();
        while (handlers.hasNext()) {
            ((CacheHandler) handlers.next()).invalidate(template);
        }
    }

    /**
     * Flush the entire cache system.
     */
    public static void clear() {
        lastExpiredCache.clear();
        CacheHandler handler = null;
        Iterator handlers = cacheHandlers.iterator();
        while (handlers.hasNext()) {
            handler = (CacheHandler) handlers.next();
            handler.clear();
        }
    }

    /**
     * Flush a single cache handler.
     */
    public static void clear(String handlerClass) {
        lastExpiredCache.clear();
        CacheHandler handler = null;
        Iterator handlers = cacheHandlers.iterator();
        while (handlers.hasNext()) {
            handler = (CacheHandler) handlers.next();
            if (handler.getClass().getName().equals(handlerClass)) {
                handler.clear();
            }
        }
    }

    public static void setLastExpiredDate(String weblogHandle) {
        lastExpiredCache.put("lastExpired:" + weblogHandle, new Date());
    }

    /**
     * Get the date of the last time the specified weblog was invalidated.
     */
    public static Date getLastExpiredDate(String weblogHandle) {
        return (Date) lastExpiredCache.get("lastExpired:" + weblogHandle);
    }

    /**
     * Compile stats from all registered handlers.
     *
     * This is basically a hacky version of instrumentation which is being
     * thrown in because we don't have a full instrumentation strategy yet.
     * This is here with the full expectation that it will be replaced by
     * something a bit more elaborate, like JMX.
     */
    public static Map getStats() {
        Map allStats = new HashMap();
        CacheHandler handler = null;
        Iterator handlers = cacheHandlers.iterator();
        while (handlers.hasNext()) {
            handler = (CacheHandler) handlers.next();
            allStats.put(handler.getClass().getName(), handler.getStats());
        }
        return allStats;
    }

    /**
     * Place to do any cleanup tasks for cache system.
     */
    public static void shutdown() {
        if (futureInvalidationsThread != null) {
            futureInvalidationsThread.interrupt();
        }
    }
}
