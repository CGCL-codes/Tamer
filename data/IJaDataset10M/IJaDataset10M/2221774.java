package org.nightlabs.jfire.jdo.cache;

import org.apache.log4j.Logger;
import org.nightlabs.config.ConfigModule;
import org.nightlabs.config.InitException;
import org.nightlabs.jfire.jdo.cache.bridge.JdoCacheBridgeDefault;

/**
 * @author Marco Schulze - marco at nightlabs dot de
 */
public class CacheCfMod extends ConfigModule {

    private static final long serialVersionUID = 1L;

    /**
	 * LOG4J logger used by this class
	 */
    private static final Logger logger = Logger.getLogger(CacheCfMod.class);

    private String documentation;

    private long notificationIntervalMSec = 0;

    private long cacheSessionContainerActivityMSec = 0;

    private int cacheSessionContainerCount = 0;

    private long waitForChangesTimeoutMin = 0;

    private long waitForChangesTimeoutMax = 0;

    private int freshDirtyObjectIDContainerCount = 0;

    private long freshDirtyObjectIDContainerActivityMSec = 0;

    private String jdoCacheBridgeClassName = null;

    private long notificationDelayAfterTransactionCompletionMSec = -1;

    private int notificationDelayAfterTransactionCompletionThreadPoolCoreSize = 0;

    private int notificationDelayAfterTransactionCompletionThreadPoolMaxSize = 0;

    private long notificationDelayAfterTransactionCompletionThreadPoolKeepAliveMSec = -1;

    public CacheCfMod() {
    }

    /**
	 * @see org.nightlabs.config.ConfigModule#init()
	 */
    @Override
    public void init() throws InitException {
        documentation = "This is the documentation for the settings in this ConfigModule.\n" + "\n" + "* notificationIntervalMSec: The length of the interval in millisec, in which the\n" + "    NotificationThread will check changed objects and trigger events. Default\n" + "    is 2000 (2 sec). Minimum is 100.\n" + "\n" + "* cacheSessionContainerCount: CacheSessions expire after the client didn't use\n" + "    them for a longer time. To avoid the need to iterate all CacheSessions in\n" + "    order to find out which ones expired, they're grouped in CacheSessionContainers.\n" + "    There is always one active CacheSessionContainer into which a CacheSession\n" + "    is moved, when it is used. This allows to periodically roll the containers\n" + "    and simply throw away a whole expired container. This setting controls, how\n" + "    many containers will be used. So, the maximum age of an unused CacheSession\n" + "    is cacheSessionContainerCount * cacheSessionContainerActivityMSec. Default\n" + "    is 12. Minimum is 2 and maximum 300.\n" + "\n" + "* cacheSessionContainerActivityMSec: How long shall the active CacheSessionContainer\n" + "    be active, before it will be replaced by a new one (and rolled through the\n" + "    LinkedList. Default is 300000 (5 min). Minimum is 1 min and maximum 30 min.\n" + "\n" + "* freshDirtyObjectIDContainerCount: While one client is loading an object, another\n" + "    client might simultaneously modify this object. In this case, the listener from\n" + "    the first client will be established too late (after the change happened). This\n" + "    would cause the first client to miss this modification completely. To avoid this,\n" + "    modifications are considered to be fresh for a certain time. Whenever a client\n" + "    registers a new implicit listener, the system checks whether the target-object has been\n" + "    modified by ANOTHER session during this freshness-period. In contrast to implicit\n" + "    listeners, changes by the same session are included, when registering explicit\n" + "    listeners. If fresh changes match the new listener, it will be triggered immediately.\n" + "    Like for cache sessions, we use a rolling mechanism to enhance performance.\n" + "    This setting controls how many buckets there are. Hence, the time, in which a\n" + "    change is considered to be fresh, is:\n" + "    freshDirtyObjectIDContainerCount * freshDirtyObjectIDContainerActivityMSec\n" + "    Default is 9. Minimum is 2 and maximum is 100.\n" + "\n" + "* freshDirtyObjectIDContainerActivityMSec: How long is the active bucket active,\n" + "    before being replaced by a new one. Default is 20000 millisec. Minimum is 1 sec\n" + "    and maximum is 10 min.\n" + "\n" + "* waitForChangesTimeoutMin: This is the lower limit of what the client can pass\n" + "    to CacheManager.waitForChanges(long waitTimeout). If the client requested\n" + "    a lower value, it will be changed to this limit. Default is 30000 (30 sec).\n" + "    Minumum is 1 sec and maximum 5 min.\n" + "\n" + "* waitForChangesTimeoutMax: Similar to waitForChangesTimeoutMin, the maximum\n" + "    wait timeout can be set, here. Default is 3600000 (1 h). Minimum is\n" + "    waitForChangesTimeoutMin and maximum is 3h.\n" + "\n" + "* jdoCacheBridgeClassName: Depending on the JDO implemention, you're using, you\n" + "    might need to use a specialized JDO-cache-bridge (in nearly all cases, the default\n" + "    one is perfect). This bridge makes sure, the CacheManagerFactory (the core of\n" + "    the cache on the server-side) is notified whenever an object is created, changed\n" + "    or deleted in the datastore.\n" + "    Default: org.nightlabs.jfire.jdo.cache.bridge.JdoCacheBridgeDefault\n" + "\n" + "* notificationDelayAfterTransactionCompletionMSec: In order to ensure that a read\n" + "    access onto a changed object is really getting the new object (and not happening\n" + "    too early and thus still reading the old instance), it is possible to delay the\n" + "    transmission of notifications. This is done by the JdoCacheBridgeDefault (and\n" + "    therefore might not be respected by another cache bridge implementation!).\n" + "    Minimum is 0 (i.e. immediate transmission from the bridge to the CacheManagerFactory).\n" + "    Maximum is 120000 (2 minutes).\n" + "    Default is 2000 (2 sec).\n" + "\n" + "* notificationDelayAfterTransactionCompletionThreadPoolCoreSize: The core pool size of\n" + "    the ThreadPool (i.e. the minimum number of pooled threads) used by the JdoCacheBridgeDefault\n" + "    if notificationDelayAfterTransactionCompletionMSec > 0.\n" + "    Minimum is 1. Maximum is 1000. Default is 10.\n" + "\n" + "* notificationDelayAfterTransactionCompletionThreadPoolMaxSize: The maximum pool size of\n" + "    the ThreadPool (i.e. the maximum number of pooled threads) used by the JdoCacheBridgeDefault\n" + "    if notificationDelayAfterTransactionCompletionMSec > 0.\n" + "    Minimum is notificationDelayAfterTransactionCompletionThreadPoolCoreSize.\n" + "    Maximum is 10000.\n" + "    Default is 2 * notificationDelayAfterTransactionCompletionThreadPoolCoreSize.\n" + "\n" + "* notificationDelayAfterTransactionCompletionThreadPoolKeepAliveMSec: The time for which a thread\n" + "    in the pool waits for a new notification to be done before terminating, if there are more\n" + "    threads existing than defined by the core pool size.\n" + "    Minimum is 0.\n" + "    Maximum is 3600000 (1 hour).\n" + "    Default is 60000 (1 minute).\n";
        if (notificationIntervalMSec < 100) setNotificationIntervalMSec(2 * 1000);
        if (cacheSessionContainerActivityMSec < 60 * 1000 || 30 * 60 * 1000 < cacheSessionContainerActivityMSec) setCacheSessionContainerActivityMSec(5 * 60 * 1000);
        if (cacheSessionContainerCount < 2 || 300 < cacheSessionContainerCount) setCacheSessionContainerCount(12);
        if (freshDirtyObjectIDContainerActivityMSec < 1000 || 10 * 60 * 1000 < freshDirtyObjectIDContainerActivityMSec) setFreshDirtyObjectIDContainerActivityMSec(20000);
        if (freshDirtyObjectIDContainerCount < 2 || 100 < freshDirtyObjectIDContainerCount) setFreshDirtyObjectIDContainerCount(9);
        if (waitForChangesTimeoutMin < 1000 || 5 * 60 * 1000 < waitForChangesTimeoutMin) setWaitForChangesTimeoutMin(30 * 1000);
        if (waitForChangesTimeoutMax < waitForChangesTimeoutMin || 3 * 60 * 60 * 1000 < waitForChangesTimeoutMax) setWaitForChangesTimeoutMax(60 * 60 * 1000);
        if (jdoCacheBridgeClassName == null || "".equals(jdoCacheBridgeClassName)) setJdoCacheBridgeClassName(JdoCacheBridgeDefault.class.getName());
        long cacheSessionLifeTime = cacheSessionContainerCount * cacheSessionContainerActivityMSec;
        if (cacheSessionLifeTime < waitForChangesTimeoutMax) {
            logger.warn("cacheSessionLifeTime (" + cacheSessionLifeTime + " msec = cacheSessionContainerCount (" + cacheSessionContainerCount + ") * cacheSessionContainerActivityMSec (" + cacheSessionContainerActivityMSec + ")) < waitForChangesTimeoutMax (" + waitForChangesTimeoutMax + ")! Adjusting waitForChangesTimeoutMax!");
            setWaitForChangesTimeoutMax(cacheSessionLifeTime - 60000);
        }
        if (waitForChangesTimeoutMax < waitForChangesTimeoutMin) {
            logger.warn("waitForChangesTimeoutMax (" + waitForChangesTimeoutMax + ") < waitForChangesTimeoutMin(" + waitForChangesTimeoutMin + ")! Adjusting waitForChangesTimeoutMin!");
            setWaitForChangesTimeoutMin(waitForChangesTimeoutMax);
        }
        if (notificationDelayAfterTransactionCompletionMSec < 0 || notificationDelayAfterTransactionCompletionMSec > 120000) setNotificationDelayAfterTransactionCompletionMSec(2000);
        if (notificationDelayAfterTransactionCompletionThreadPoolCoreSize < 1 || notificationDelayAfterTransactionCompletionThreadPoolCoreSize > 1000) setNotificationDelayAfterTransactionCompletionThreadPoolCoreSize(10);
        if (notificationDelayAfterTransactionCompletionThreadPoolMaxSize < notificationDelayAfterTransactionCompletionThreadPoolCoreSize || notificationDelayAfterTransactionCompletionThreadPoolMaxSize > 10000) setNotificationDelayAfterTransactionCompletionThreadPoolMaxSize(Math.min(10000, notificationDelayAfterTransactionCompletionThreadPoolCoreSize * 2));
        if (notificationDelayAfterTransactionCompletionThreadPoolKeepAliveMSec < 0 || notificationDelayAfterTransactionCompletionThreadPoolKeepAliveMSec > 60 * 60 * 1000) setNotificationDelayAfterTransactionCompletionThreadPoolKeepAliveMSec(60 * 1000);
        if (logger.isDebugEnabled()) {
            logger.debug("The Cache settings are:");
            logger.debug("      notificationIntervalMSec=" + notificationIntervalMSec);
            logger.debug("      cacheSessionContainerActivityMSec=" + cacheSessionContainerActivityMSec);
            logger.debug("      cacheSessionContainerCount=" + cacheSessionContainerCount);
            logger.debug("      freshDirtyObjectIDContainerActivityMSec=" + freshDirtyObjectIDContainerActivityMSec);
            logger.debug("      freshDirtyObjectIDContainerCount=" + freshDirtyObjectIDContainerCount);
            logger.debug("      waitForChangesTimeoutMin=" + waitForChangesTimeoutMin);
            logger.debug("      waitForChangesTimeoutMax=" + waitForChangesTimeoutMax);
            logger.debug("      jdoCacheBridgeClassName=" + jdoCacheBridgeClassName);
            logger.debug("      notificationDelayAfterTransactionCompletionMSec=" + notificationDelayAfterTransactionCompletionMSec);
            logger.debug("      notificationDelayAfterTransactionCompletionThreadPoolCoreSize=" + notificationDelayAfterTransactionCompletionThreadPoolCoreSize);
            logger.debug("      notificationDelayAfterTransactionCompletionThreadPoolMaxSize=" + notificationDelayAfterTransactionCompletionThreadPoolMaxSize);
            logger.debug("      notificationDelayAfterTransactionCompletionThreadPoolKeepAliveMSec=" + notificationDelayAfterTransactionCompletionThreadPoolKeepAliveMSec);
        }
    }

    /**
	 * @return Returns the cacheSessionContainerActivityMSec.
	 */
    public long getCacheSessionContainerActivityMSec() {
        return cacheSessionContainerActivityMSec;
    }

    /**
	 * @param cacheSessionContainerActivityMSec The cacheSessionContainerActivityMSec to set.
	 */
    public void setCacheSessionContainerActivityMSec(long cacheSessionContainerActivityMSec) {
        this.cacheSessionContainerActivityMSec = cacheSessionContainerActivityMSec;
        setChanged();
    }

    /**
	 * @return Returns the cacheSessionContainerCount.
	 */
    public int getCacheSessionContainerCount() {
        return cacheSessionContainerCount;
    }

    /**
	 * @param cacheSessionContainerCount The cacheSessionContainerCount to set.
	 */
    public void setCacheSessionContainerCount(int cacheSessionContainerCount) {
        this.cacheSessionContainerCount = cacheSessionContainerCount;
        setChanged();
    }

    /**
	 * @return Returns the documentation.
	 */
    public String getDocumentation() {
        return documentation;
    }

    /**
	 * @param documentation The documentation to set.
	 */
    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    /**
	 * @return Returns the notificationIntervalMSec.
	 */
    public long getNotificationIntervalMSec() {
        return notificationIntervalMSec;
    }

    /**
	 * @param notificationIntervalMSec The notificationIntervalMSec to set.
	 */
    public void setNotificationIntervalMSec(long notificationIntervalMSec) {
        this.notificationIntervalMSec = notificationIntervalMSec;
        setChanged();
    }

    /**
	 * @return Returns the waitForChangesTimeoutMax.
	 */
    public long getWaitForChangesTimeoutMax() {
        return waitForChangesTimeoutMax;
    }

    /**
	 * @param waitForChangesTimeoutMax The waitForChangesTimeoutMax to set.
	 */
    public void setWaitForChangesTimeoutMax(long waitForChangesTimeoutMax) {
        this.waitForChangesTimeoutMax = waitForChangesTimeoutMax;
        setChanged();
    }

    /**
	 * @return Returns the waitForChangesTimeoutMin.
	 */
    public long getWaitForChangesTimeoutMin() {
        return waitForChangesTimeoutMin;
    }

    /**
	 * @param waitForChangesTimeoutMin The waitForChangesTimeoutMin to set.
	 */
    public void setWaitForChangesTimeoutMin(long waitForChangesTimeoutMin) {
        this.waitForChangesTimeoutMin = waitForChangesTimeoutMin;
        setChanged();
    }

    /**
	 * @return Returns the jdoCacheBridgeClassName.
	 */
    public String getJdoCacheBridgeClassName() {
        return jdoCacheBridgeClassName;
    }

    /**
	 * @param jdoCacheBridgeClassName The jdoCacheBridgeClassName to set.
	 */
    public void setJdoCacheBridgeClassName(String jdoCacheBridgeClassName) {
        this.jdoCacheBridgeClassName = jdoCacheBridgeClassName;
        setChanged();
    }

    public int getFreshDirtyObjectIDContainerCount() {
        return freshDirtyObjectIDContainerCount;
    }

    public void setFreshDirtyObjectIDContainerCount(int freshDirtyObjectIDContainerCount) {
        this.freshDirtyObjectIDContainerCount = freshDirtyObjectIDContainerCount;
        setChanged();
    }

    public long getFreshDirtyObjectIDContainerActivityMSec() {
        return freshDirtyObjectIDContainerActivityMSec;
    }

    public void setFreshDirtyObjectIDContainerActivityMSec(long freshDirtyObjectIDContainerActivityMSec) {
        this.freshDirtyObjectIDContainerActivityMSec = freshDirtyObjectIDContainerActivityMSec;
        setChanged();
    }

    public long getNotificationDelayAfterTransactionCompletionMSec() {
        return notificationDelayAfterTransactionCompletionMSec;
    }

    public void setNotificationDelayAfterTransactionCompletionMSec(long notificationDelayAfterTransactionCompletionMSec) {
        this.notificationDelayAfterTransactionCompletionMSec = notificationDelayAfterTransactionCompletionMSec;
        setChanged();
    }

    public int getNotificationDelayAfterTransactionCompletionThreadPoolCoreSize() {
        return notificationDelayAfterTransactionCompletionThreadPoolCoreSize;
    }

    public void setNotificationDelayAfterTransactionCompletionThreadPoolCoreSize(int notificationDelayAfterTransactionCompletionThreadPoolCoreSize) {
        this.notificationDelayAfterTransactionCompletionThreadPoolCoreSize = notificationDelayAfterTransactionCompletionThreadPoolCoreSize;
        setChanged();
    }

    public long getNotificationDelayAfterTransactionCompletionThreadPoolKeepAliveMSec() {
        return notificationDelayAfterTransactionCompletionThreadPoolKeepAliveMSec;
    }

    public void setNotificationDelayAfterTransactionCompletionThreadPoolKeepAliveMSec(long notificationDelayAfterTransactionCompletionThreadPoolKeepAliveMSec) {
        this.notificationDelayAfterTransactionCompletionThreadPoolKeepAliveMSec = notificationDelayAfterTransactionCompletionThreadPoolKeepAliveMSec;
        setChanged();
    }

    public int getNotificationDelayAfterTransactionCompletionThreadPoolMaxSize() {
        return notificationDelayAfterTransactionCompletionThreadPoolMaxSize;
    }

    public void setNotificationDelayAfterTransactionCompletionThreadPoolMaxSize(int notificationDelayAfterTransactionCompletionThreadPoolMaxSize) {
        this.notificationDelayAfterTransactionCompletionThreadPoolMaxSize = notificationDelayAfterTransactionCompletionThreadPoolMaxSize;
        setChanged();
    }
}
