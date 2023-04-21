package org.mobicents.slee.runtime.sbbentity;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import javax.slee.ServiceID;
import org.apache.log4j.Logger;
import org.jboss.cache.Fqn;
import org.jboss.cache.notifications.annotation.CacheListener;
import org.jboss.cache.notifications.annotation.NodeRemoved;
import org.jboss.cache.notifications.event.NodeRemovedEvent;
import org.mobicents.cache.MobicentsCache;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.sbbentity.SbbEntityID;

/**
 * 
 * @author martins
 *
 */
@CacheListener(sync = false)
public class SbbEntityLockFacility {

    private static final Logger logger = Logger.getLogger(SbbEntityLockFacility.class);

    private boolean doTraceLogs = logger.isTraceEnabled();

    /**
	 * 
	 */
    private final ConcurrentHashMap<SbbEntityID, ReentrantLock> locks = new ConcurrentHashMap<SbbEntityID, ReentrantLock>();

    /**
	 * 
	 */
    public SbbEntityLockFacility(SleeContainer container) {
        MobicentsCache cache = container.getCluster().getMobicentsCache();
        if (!cache.isLocalMode()) {
            cache.getJBossCache().addCacheListener(this);
        }
    }

    /**
	 * 
	 * @param sbbEntityId
	 * @return
	 */
    public ReentrantLock get(SbbEntityID sbbEntityId) {
        ReentrantLock lock = locks.get(sbbEntityId);
        if (lock == null) {
            final ReentrantLock newLock = new ReentrantLock();
            lock = locks.putIfAbsent(sbbEntityId, newLock);
            if (lock == null) {
                if (doTraceLogs) {
                    logger.trace(Thread.currentThread() + " put of lock " + newLock + " for " + sbbEntityId);
                }
                lock = newLock;
            }
        }
        return lock;
    }

    /**
	 * 
	 * @param sbbEntityId
	 * @return
	 */
    public ReentrantLock remove(SbbEntityID sbbEntityId) {
        if (doTraceLogs) {
            logger.trace(Thread.currentThread() + " removed lock for " + sbbEntityId);
        }
        return locks.remove(sbbEntityId);
    }

    /**
	 * 
	 * @return
	 */
    public Set<SbbEntityID> getSbbEntitiesWithLocks() {
        return locks.keySet();
    }

    @NodeRemoved
    public void onNodeRemovedEvent(NodeRemovedEvent event) {
        if (!event.isOriginLocal() && !event.isPre()) {
            Fqn<?> fqn = event.getFqn();
            if (doTraceLogs) {
                logger.trace("onNodeRemovedEvent( fqn = " + fqn + ", size = " + fqn.size() + " )");
            }
            if (fqn.get(0).equals(SbbEntityFactoryCacheData.SBB_ENTITY_FACTORY_FQN_NAME)) {
                int fqnSize = fqn.size();
                if (fqnSize < 3) {
                    return;
                }
                SbbEntityID sbbEntityID = null;
                if (fqnSize == 3) {
                    ServiceID serviceID = (ServiceID) fqn.get(1);
                    String convergenceName = (String) fqn.get(2);
                    sbbEntityID = new RootSbbEntityID(serviceID, convergenceName);
                    if (doTraceLogs) {
                        logger.trace("Root sbb entity " + sbbEntityID + " was remotely removed, ensuring there is no local lock");
                    }
                } else {
                    if (!fqn.get(fqnSize - 3).equals(SbbEntityCacheData.CHILD_RELATIONs_CHILD_NODE_NAME)) {
                        return;
                    }
                    ServiceID serviceID = (ServiceID) fqn.get(1);
                    String convergenceName = (String) fqn.get(2);
                    sbbEntityID = new RootSbbEntityID(serviceID, convergenceName);
                    int i = 3;
                    while (fqnSize >= i + 3) {
                        String childRelationName = (String) fqn.get(i + 1);
                        String childId = (String) fqn.get(i + 2);
                        sbbEntityID = new NonRootSbbEntityID(sbbEntityID, childRelationName, childId);
                        i += 3;
                    }
                    if (doTraceLogs) {
                        logger.trace("Non root sbb entity " + sbbEntityID + " was remotely removed, ensuring there is no local lock");
                    }
                }
                if (locks.remove(sbbEntityID) != null) {
                    if (doTraceLogs) {
                        logger.trace("Remotely removed lock for " + sbbEntityID);
                    }
                }
            }
        }
    }
}
