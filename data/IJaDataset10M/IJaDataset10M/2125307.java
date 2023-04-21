package org.openware.job.cache;

import org.openware.job.data.PersistentManager;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import org.openware.jdf.Factory;
import org.openware.jdf.server.JDFServer;
import org.openware.jdf.Location;
import org.apache.log4j.Category;

public class CacheManager implements ICacheManager {

    private static Category log = Category.getInstance(CacheManager.class.getName());

    private static CacheManager localInstance = null;

    private HashMap remoteCacheManagerUrls = new HashMap();

    private HashMap persistentManagers = new HashMap();

    private int numTries = 3;

    private long maxIdleTime = 60 * 60 * 3 * 1000;

    private CacheManager() {
    }

    /**
     * Get the local instance (the running in the local VM) of 
     * the <code>CacheManager</code>.
     */
    static CacheManager getLocalInstance() {
        if (localInstance == null) {
            localInstance = new CacheManager();
        }
        return localInstance;
    }

    public static void addManager(PersistentManager pmanager) {
        if (CacheManagerBuilder.initialized()) {
            getLocalInstance().addManagerInstance(pmanager);
        }
    }

    public static void removeManager(PersistentManager pmanager) {
        if (CacheManagerBuilder.initialized()) {
            getLocalInstance().removeManagerInstance(pmanager);
        }
    }

    void setMaxIdleTime(int maxIdleTimeMinutes) {
        this.maxIdleTime = (long) maxIdleTimeMinutes * 60 * 1000;
    }

    public synchronized void removeManagerInstance(PersistentManager pmanager) {
        LinkedList list = null;
        list = (LinkedList) persistentManagers.get(pmanager.getName());
        if (list != null) {
            list.remove(pmanager);
        }
        if (log.isDebugEnabled()) {
            log.debug("Removed " + pmanager);
        }
    }

    /**
     * Add a persistent manager to the cache manager.
     */
    public synchronized void addManagerInstance(PersistentManager pmanager) {
        LinkedList list = null;
        list = (LinkedList) persistentManagers.get(pmanager.getName());
        if (list == null) {
            list = new LinkedList();
            persistentManagers.put(pmanager.getName(), list);
        }
        list.add(pmanager);
        if (log.isDebugEnabled()) {
            log.debug("Added " + pmanager + " for " + pmanager.getName());
        }
    }

    /**
     * Update the persistent manager caches that this cache manager
     * is managing.
     *
     * @param  pmanagerName     The persistent manager name for which persistent
     *                          managers need updating.
     * @param  callingPmanager  The persistent manager making the call.  This will
     *                          be <code>null</code> when it is being called from
     *                          a remote url.
     * @param cacheInfo         The cache invalidation information.
     */
    public synchronized void updateLocalCaches(String pmanagerName, CacheInvalidationInfo cacheInfo) {
        LinkedList list = null;
        LinkedList removeList = null;
        long now = System.currentTimeMillis();
        removeList = new LinkedList();
        list = (LinkedList) persistentManagers.get(pmanagerName);
        log.debug("list = " + list);
        if (list != null) {
            Iterator iter = null;
            iter = list.iterator();
            while (iter.hasNext()) {
                PersistentManager pmanager = null;
                long tmpMaxIdleTime = 0;
                pmanager = (PersistentManager) iter.next();
                if (pmanager.getMaxIdleTime() > 0) {
                    tmpMaxIdleTime = pmanager.getMaxIdleTime();
                } else {
                    tmpMaxIdleTime = maxIdleTime;
                }
                if (tmpMaxIdleTime > 0 && (now - pmanager.getLastAccessTime()) <= tmpMaxIdleTime) {
                    if (log.isDebugEnabled()) {
                        log.debug("CacheInfo: " + cacheInfo.toXml());
                        log.debug("Updated " + pmanager);
                    }
                    pmanager.updateCache(cacheInfo);
                } else {
                    removeList.add(pmanager);
                }
            }
        }
        if (removeList.size() > 0) {
            Iterator iter = null;
            iter = removeList.iterator();
            while (iter.hasNext()) {
                PersistentManager pmanager = null;
                pmanager = (PersistentManager) iter.next();
                list.remove(pmanager);
                log.info("Removed " + pmanager + " from CacheManager -- it expired");
            }
        }
    }

    /**
     * Update both the local and remote caches (remote in a load balanced
     * deployment).  This method spawns a separate thread to do the actual
     * processing so that it returns immediately.
     *
     * @param  callingPmanager  The persistent manager calling this method.
     *                          This is needed so that we don't update the
     *                          cache of the persistent manager (because it
     *                          doesn't need it).
     * @param  cacheInfo        The information needed to update the caches.
     */
    public static void updateCaches(PersistentManager callingPmanager, CacheInvalidationInfo cacheInfo) {
        if (CacheManagerBuilder.initialized()) {
            if (cacheInfo.hasInfo()) {
                Thread thread = null;
                if (log.isDebugEnabled()) {
                    log.debug("Spawning thread to update caches");
                }
                thread = new Thread(new CacheManagerThread(callingPmanager, cacheInfo, getLocalInstance()));
                thread.start();
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Nothing to update");
                }
            }
        } else {
            callingPmanager.updateCache(cacheInfo);
            if (log.isDebugEnabled()) {
                log.debug("CacheManager not initialized");
            }
        }
    }

    /**
     * This method is called by the <code>CacherManagerThread</code> to 
     * actually call each instance of <code>ICacherManagerThread</code> 
     * to update the cache.
     */
    public synchronized void doUpdate(String callingPmanagerName, CacheInvalidationInfo cacheInfo) {
        Iterator iter = null;
        if (log.isDebugEnabled()) {
            log.debug("Updating for " + callingPmanagerName);
            log.debug("cacheInfo: " + cacheInfo.toXml());
        }
        if (remoteCacheManagerUrls.size() > 0) {
            iter = remoteCacheManagerUrls.keySet().iterator();
            if (log.isDebugEnabled()) {
                if (!iter.hasNext()) {
                    log.debug("No remote cache manager urls");
                }
            }
            while (iter.hasNext()) {
                ICacheManager cm = null;
                String url = null;
                boolean success = false;
                int tried = 0;
                url = (String) iter.next();
                try {
                    for (tried = 0; tried < numTries && !success; tried++) {
                        if (log.isDebugEnabled()) {
                            log.info("numtries for 'doUpdate': " + (tried + 1) + " for " + url);
                        }
                        try {
                            cm = (ICacheManager) remoteCacheManagerUrls.get(url);
                            cm.updateLocalCaches(callingPmanagerName, cacheInfo);
                            success = true;
                        } catch (ClassCastException e) {
                            if (log.isDebugEnabled()) {
                                log.debug("Remote instance hasn't been created yet.");
                            }
                            createInstance(url);
                        } catch (Exception e) {
                            if (log.isDebugEnabled()) {
                                log.debug("Couldn't update cache in " + url + ": " + e.toString());
                            }
                            createInstance(url);
                        }
                        if (!success) {
                            Thread.sleep(500);
                        }
                    }
                    if (!success) {
                        log.error("Could not update CacheManager at " + url);
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("Updated " + url);
                        }
                    }
                } catch (InterruptedException e) {
                    log.info("Thread was interrupted after " + tried + " times");
                }
            }
        }
    }

    private synchronized void createInstance(String url) {
        try {
            ICacheManager cm = null;
            Location location = null;
            location = new Location(url);
            if (JDFServer.getInstance().getServerLocation().equals(location)) {
                cm = (ICacheManager) JDFServer.getInstance().getObject(JDF_ALIAS);
                if (log.isDebugEnabled()) {
                    log.debug("Got the local instance from " + url);
                }
            } else {
                cm = (ICacheManager) Factory.create("org.openware.job.cache.CacheManager", url + JDF_ALIAS);
                if (log.isDebugEnabled()) {
                    log.debug("Created remote instance for " + url);
                }
            }
            remoteCacheManagerUrls.put(url, cm);
        } catch (Exception e) {
            log.error("Can't add url: " + e.toString());
            remoteCacheManagerUrls.put(url, url);
        }
    }

    public synchronized void addUrl(String url) {
        remoteCacheManagerUrls.put(url, url);
    }
}

class CacheManagerThread implements Runnable {

    private static Category log = Category.getInstance(CacheManagerThread.class.getName());

    private PersistentManager callingPmanager = null;

    private CacheInvalidationInfo cacheInfo = null;

    private ICacheManager cmanager = null;

    CacheManagerThread(PersistentManager callingPmanager, CacheInvalidationInfo cacheInfo, ICacheManager cmanager) {
        this.callingPmanager = callingPmanager;
        this.cacheInfo = cacheInfo;
        this.cmanager = cmanager;
    }

    public void run() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Starting CacheManagerThread");
            }
            cmanager.doUpdate(callingPmanager.getName(), cacheInfo);
            if (log.isDebugEnabled()) {
                log.debug("Stopping CacheManagerThread");
            }
        } catch (Exception e) {
            log.error("Error updating caches", e);
        }
    }
}
