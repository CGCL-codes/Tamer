package phex.udp.hostcache;

import phex.common.AbstractManager;
import phex.common.ThreadPool;
import phex.utils.NLogger;
import phex.utils.NLoggerNames;

/**
 *
 */
public class UdpHostCacheManager extends AbstractManager {

    private UdpHostCacheContainer udpHostCacheContainer;

    /**
     * Lock to make sure not more then one thread request is running in parallel
     * otherwise it could happen that we create thread after thread while each
     * one takes a long time to come back.
     */
    private boolean isThreadRequestRunning = false;

    private static class Holder {

        protected static final UdpHostCacheManager manager = new UdpHostCacheManager();
    }

    public static UdpHostCacheManager getInstance() {
        return UdpHostCacheManager.Holder.manager;
    }

    public UdpHostCacheContainer getUdpHostCacheContainer() {
        return udpHostCacheContainer;
    }

    /**
     * Indicates if we are a udp host cache. 
     */
    public boolean isUdpHostCache() {
        return false;
    }

    /**
     * Starts a query for more hosts in an extra thread.
     */
    public synchronized void invokeQueryCachesRequest() {
        if (isThreadRequestRunning) {
            return;
        }
        isThreadRequestRunning = true;
        Runnable runner = new QueryCachesRunner();
        ThreadPool.getInstance().addJob(runner, "UdpHostCacheQuery-" + Integer.toHexString(runner.hashCode()));
    }

    /**
     * This method is called in order to initialize the manager. This method
     * includes all tasks that must be done to intialize all the several manager.
     * Like instantiating the singleton instance of the manager. Inside
     * this method you can't rely on the availability of other managers.
     * @return true is initialization was successful, false otherwise.
     */
    public boolean initialize() {
        udpHostCacheContainer = new UdpHostCacheContainer();
        NLogger.info(NLoggerNames.UDP_HOST_CACHE, "Starting Udp Host Cache Manager");
        return true;
    }

    /**
     * This method is called in order to perform post initialization of the
     * manager. This method includes all tasks that must be done after initializing
     * all the several managers. Inside this method you can rely on the
     * availability of other managers.
     * @return true is initialization was successful, false otherwise.
     */
    public boolean onPostInitialization() {
        return true;
    }

    /**
     * This method is called after the complete application including GUI completed
     * its startup process. This notification must be used to activate runtime
     * processes that needs to be performed once the application has successfully
     * completed startup.
     */
    public void startupCompletedNotify() {
    }

    /**
     * This method is called in order to cleanly shutdown the manager. It
     * should contain all cleanup operations to ensure a nice shutdown of Phex.
     * It is called before the GUI closes.
     */
    public void shutdown() {
        NLogger.info(NLoggerNames.UDP_HOST_CACHE, " UDP HOST CACHE MANAGER " + " SHUTTING DOWN. Writing caches to file");
        udpHostCacheContainer.saveCachesToFile();
    }

    private final class QueryCachesRunner implements Runnable {

        public void run() {
            udpHostCacheContainer.queryMoreHosts();
            isThreadRequestRunning = false;
        }
    }
}
