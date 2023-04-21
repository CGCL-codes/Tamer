package org.jboss.web.tomcat.service.session.distributedcache.impl.jbc;

import java.util.Map;
import org.apache.log4j.Logger;
import org.jboss.cache.Fqn;
import org.jboss.cache.buddyreplication.BuddyManager;
import org.jboss.cache.notifications.annotation.NodeCreated;
import org.jboss.cache.notifications.annotation.NodeModified;
import org.jboss.cache.notifications.annotation.NodeRemoved;
import org.jboss.cache.notifications.event.NodeCreatedEvent;
import org.jboss.cache.notifications.event.NodeModifiedEvent;
import org.jboss.cache.notifications.event.NodeRemovedEvent;
import org.jboss.metadata.web.jboss.ReplicationGranularity;
import org.jboss.web.tomcat.service.session.distributedcache.spi.LocalDistributableConvergedSessionManager;
import org.jboss.web.tomcat.service.session.distributedcache.spi.LocalDistributableSessionManager;

/**
 * Listens for distributed caches events, notifying the JBossCacheManager
 * of events of interest. 
 * 
 * @author Brian Stansberry
 * @author <A HREF="mailto:jean.deruelle@gmail.com">Jean Deruelle</A>
 */
@org.jboss.cache.notifications.annotation.CacheListener(sync = false)
public class SipCacheListener extends CacheListenerBase {

    protected static Logger logger = Logger.getLogger(SipCacheListener.class);

    protected static final int SIPSESSION_FQN_INDEX = 0;

    protected static final int HOSTNAME_FQN_INDEX = 1;

    protected static final int SIPAPPNAME_FQN_INDEX = 2;

    protected static final int SIPAPPSESSION_ID_FQN_INDEX = 3;

    protected static final int SIPSESSION_ID_FQN_INDEX = 4;

    protected static final int SIPAPPSESSION_FQN_SIZE = SIPAPPSESSION_ID_FQN_INDEX + 1;

    protected static final int SIPAPPSESSION_POJO_ATTRIBUTE_FQN_INDEX = SIPAPPSESSION_ID_FQN_INDEX + 1;

    protected static final int SIPSESSION_FQN_SIZE = SIPSESSION_ID_FQN_INDEX + 1;

    protected static final int SIPSESSION_POJO_ATTRIBUTE_FQN_INDEX = SIPSESSION_ID_FQN_INDEX + 1;

    protected static final int SIPAPPSESSION_POJO_KEY_FQN_INDEX = SIPAPPSESSION_POJO_ATTRIBUTE_FQN_INDEX + 1;

    protected static final int SIPAPPSESSION_POJO_KEY_FQN_SIZE = SIPAPPSESSION_POJO_KEY_FQN_INDEX + 1;

    protected static final int SIPSESSION_POJO_KEY_FQN_INDEX = SIPSESSION_POJO_ATTRIBUTE_FQN_INDEX + 1;

    protected static final int SIPSESSION_POJO_KEY_FQN_SIZE = SIPSESSION_POJO_KEY_FQN_INDEX + 1;

    protected static final int BUDDY_BACKUP_ROOT_OWNER_INDEX = BuddyManager.BUDDY_BACKUP_SUBTREE_FQN.size();

    protected static final int BUDDY_BACKUP_ROOT_OWNER_SIZE = BUDDY_BACKUP_ROOT_OWNER_INDEX + 1;

    protected static final int SIPSESSION_POJO_INTERNAL_FQN_INDEX = SIPSESSION_ID_FQN_INDEX + 1;

    protected static final int SIPSESSION_POJO_INTERNAL_FQN_SIZE = SIPSESSION_POJO_INTERNAL_FQN_INDEX + 1;

    protected static final int SIPAPPSESSION_POJO_INTERNAL_FQN_INDEX = SIPAPPSESSION_ID_FQN_INDEX + 1;

    protected static final int SIPAPPSESSION_POJO_INTERNAL_FQN_SIZE = SIPAPPSESSION_POJO_INTERNAL_FQN_INDEX + 1;

    private String sipApplicationNameHashed;

    private String sipApplicationName;

    private boolean fieldBased_;

    private boolean attributeBased_;

    private JBossCacheWrapper wrapper;

    SipCacheListener(JBossCacheWrapper wrapper, LocalDistributableSessionManager manager, String contextHostPath, ReplicationGranularity granularity, String sipApplicationName, String sipApplicationNameHashed) {
        super(manager, contextHostPath);
        if (granularity == ReplicationGranularity.FIELD) fieldBased_ = true; else if (granularity == ReplicationGranularity.ATTRIBUTE) attributeBased_ = true;
        this.sipApplicationName = sipApplicationName;
        this.sipApplicationNameHashed = sipApplicationNameHashed;
        if (logger.isDebugEnabled()) {
            logger.debug("SipCacheListener sipApplicationName : " + sipApplicationName + ", sipApplicationNameHashed " + sipApplicationNameHashed + ", granularity " + granularity + ", contextHostPath " + contextHostPath);
        }
    }

    protected boolean isFqnForOurSipapp(Fqn<String> fqn, boolean isBuddy) {
        try {
            String appName = (String) fqn.get(isBuddy ? BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPAPPNAME_FQN_INDEX : SIPAPPNAME_FQN_INDEX);
            if (sipApplicationNameHashed.equals(appName)) return true;
        } catch (IndexOutOfBoundsException e) {
        }
        return false;
    }

    @NodeCreated
    public void nodeCreated(NodeCreatedEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("following node created " + event.getFqn().toString());
        }
    }

    @NodeRemoved
    public void nodeRemoved(NodeRemovedEvent event) {
        if (event.isPre()) return;
        if (logger.isDebugEnabled()) {
            logger.debug("following node removed " + event.getFqn().toString());
        }
        boolean local = event.isOriginLocal();
        if (!fieldBased_ && local) return;
        @SuppressWarnings("unchecked") Fqn<String> fqn = event.getFqn();
        boolean isBuddy = isBuddyFqn(fqn);
        if (!local && isFqnSessionRootSized(fqn.size(), isBuddy) && isFqnForOurSipapp(fqn, isBuddy)) {
            String sipAppSessId = getSipApplicationSessionIdFromFqn(fqn, isBuddy);
            String sessId = null;
            if (isFqnSipApplicationSessionRootSized(fqn.size(), isBuddy)) {
                ((LocalDistributableConvergedSessionManager) manager_).notifyRemoteSipApplicationSessionInvalidation(sipAppSessId);
            } else {
                sessId = getSipSessionIdFromFqn(fqn, isBuddy);
                ((LocalDistributableConvergedSessionManager) manager_).notifyRemoteSipSessionInvalidation(sipAppSessId, sessId);
            }
        } else if (local && !isBuddy && (isPossibleSipApplicationSessionInternalPojoFqn(fqn) || isPossibleSipSessionInternalPojoFqn(fqn)) && isFqnForOurSipapp(fqn, isBuddy)) {
            String sipAppSessId = getSipApplicationSessionIdFromFqn(fqn, isBuddy);
            String sessId = null;
            if (isFqnSipApplicationSessionRootSized(fqn.size(), isBuddy)) {
                ((LocalDistributableConvergedSessionManager) manager_).notifySipApplicationSessionLocalAttributeModification(sipAppSessId);
            } else {
                sessId = getSipSessionIdFromFqn(fqn, isBuddy);
                ((LocalDistributableConvergedSessionManager) manager_).notifySipSessionLocalAttributeModification(sipAppSessId, sessId);
            }
        }
    }

    @NodeModified
    public void nodeModified(NodeModifiedEvent event) {
        if (event.isPre()) return;
        boolean local = event.isOriginLocal();
        if (!fieldBased_ && local) return;
        @SuppressWarnings("unchecked") Fqn<String> fqn = event.getFqn();
        boolean isBuddy = isBuddyFqn(fqn);
        if (!local && isFqnSessionRootSized(fqn.size(), isBuddy) && isFqnForOurSipapp(fqn, isBuddy)) {
            if ((fqn.size() == 6 && fqn.get(5).toString().equals(AbstractJBossCacheService.VERSION_KEY.toString())) || (fqn.size() == 5 && fqn.get(4).toString().equals(AbstractJBossCacheService.VERSION_KEY.toString()))) {
                if (logger.isDebugEnabled()) {
                    logger.debug("following node modified " + event.getFqn().toString());
                }
                @SuppressWarnings("unchecked") Map<Object, Object> data = event.getData();
                Integer version = (Integer) data.get(AbstractJBossCacheService.VERSION_KEY.toString());
                if (version != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("version attribute found " + version + " in " + fqn + " using parent fqn " + fqn.getParent());
                    }
                    String sipAppSessionId = getSipApplicationSessionIdFromFqn(fqn.getParent(), isBuddy);
                    String sipSessionId = null;
                    boolean isSipApplicationSession = true;
                    if (!isFqnSipApplicationSessionRootSized(fqn.getParent().size(), isBuddy)) {
                        sipSessionId = getSipSessionIdFromFqn(fqn.getParent(), isBuddy);
                        isSipApplicationSession = false;
                    }
                    if (logger.isDebugEnabled()) {
                        logger.debug("isSipAppSession " + isSipApplicationSession + " in " + fqn.getParent());
                    }
                    String owner = null;
                    Long timestamp = Long.valueOf(-1);
                    boolean updated = false;
                    if (isSipApplicationSession) {
                        updated = ((LocalDistributableConvergedSessionManager) manager_).sipApplicationSessionChangedInDistributedCache(sipAppSessionId, owner, version.intValue(), timestamp, null);
                    } else {
                        updated = ((LocalDistributableConvergedSessionManager) manager_).sipSessionChangedInDistributedCache(sipAppSessionId, sipSessionId, owner, version.intValue(), timestamp, null);
                    }
                    if (!updated && !isBuddy) {
                        logger.warn("Possible concurrency problem: Replicated version id " + version + " is less than or equal to in-memory version for session app id " + sipAppSessionId + " and session id " + sipSessionId);
                    }
                }
            }
        } else if (local && !isBuddy && (isPossibleSipApplicationSessionInternalPojoFqn(fqn) || isPossibleSipSessionInternalPojoFqn(fqn)) && isFqnForOurSipapp(fqn, isBuddy)) {
            String sipAppSessId = getSipApplicationSessionIdFromFqn(fqn, isBuddy);
            String sessId = null;
            if (isFqnSipApplicationSessionRootSized(fqn.size(), isBuddy)) {
                ((LocalDistributableConvergedSessionManager) manager_).notifySipApplicationSessionLocalAttributeModification(sipAppSessId);
            } else {
                sessId = getSipSessionIdFromFqn(fqn, isBuddy);
                ((LocalDistributableConvergedSessionManager) manager_).notifySipSessionLocalAttributeModification(sipAppSessId, sessId);
            }
        }
    }

    protected static boolean isFqnSessionRootSized(int size, boolean isBuddy) {
        return size == (isBuddy ? BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPAPPSESSION_FQN_SIZE : SIPAPPSESSION_FQN_SIZE) || size == (isBuddy ? BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPSESSION_FQN_SIZE : SIPSESSION_FQN_SIZE);
    }

    protected static boolean isFqnSipApplicationSessionRootSized(int size, boolean isBuddy) {
        return size == (isBuddy ? BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPAPPSESSION_FQN_SIZE : SIPAPPSESSION_FQN_SIZE);
    }

    protected static String getSipApplicationSessionIdFromFqn(Fqn fqn, boolean isBuddy) {
        if (isBuddy) {
            return (String) fqn.get(BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPAPPSESSION_ID_FQN_INDEX);
        } else {
            return (String) fqn.get(SIPAPPSESSION_ID_FQN_INDEX);
        }
    }

    protected static String getSipSessionIdFromFqn(Fqn fqn, boolean isBuddy) {
        if (isBuddy) {
            return (String) fqn.get(BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPSESSION_ID_FQN_INDEX);
        } else {
            return (String) fqn.get(SIPSESSION_ID_FQN_INDEX);
        }
    }

    /**
	 * Check if the fqn is big enough to be in the internal pojo area but isn't
	 * in the regular attribute area.
	 * 
	 * Structure in the cache is:
	 * 
	 * /SIPSESSION 
	 * ++ /contextPath_hostname
	 * ++++ /sipApplicationName
	 * ++++++ /id 
	 * ++++++++ /sipsessionid 
	 * ++++++++++ /ATTRIBUTE
	 * ++++++++++ /_JBossInternal_ 
	 * ++++++++++++ etc etc
	 * 
	 * If the Fqn size is big enough to be "etc etc" or lower, but the 4th level
	 * is not "ATTRIBUTE", it must be under _JBossInternal_. We discriminate
	 * based on != ATTRIBUTE to avoid having to code to the internal PojoCache
	 * _JBossInternal_ name.
	 * 
	 * @param fqn
	 * @return
	 */
    public static boolean isPossibleSipSessionInternalPojoFqn(Fqn<String> fqn) {
        return (fqn.size() > SIPSESSION_POJO_INTERNAL_FQN_SIZE && FieldBasedJBossCacheService.ATTRIBUTE.equals(fqn.get(SIPSESSION_POJO_INTERNAL_FQN_INDEX)) == false);
    }

    /**
	 * Check if the fqn is big enough to be in the internal pojo area but isn't
	 * in the regular attribute area.
	 * 
	 * Structure in the cache is:
	 * 
	 * /SIPSESSION 
	 * ++ /contextPath_hostname
	 * ++++ /sipApplicationName
	 * ++++++ /id 
	 * ++++++++ /ATTRIBUTE
	 * ++++++++ /_JBossInternal_ 
	 * ++++++++++ etc etc
	 * 
	 * If the Fqn size is big enough to be "etc etc" or lower, but the 4th level
	 * is not "ATTRIBUTE", it must be under _JBossInternal_. We discriminate
	 * based on != ATTRIBUTE to avoid having to code to the internal PojoCache
	 * _JBossInternal_ name.
	 * 
	 * @param fqn
	 * @return
	 */
    public static boolean isPossibleSipApplicationSessionInternalPojoFqn(Fqn<String> fqn) {
        return (fqn.size() > SIPAPPSESSION_POJO_INTERNAL_FQN_SIZE && FieldBasedJBossCacheService.ATTRIBUTE.equals(fqn.get(SIPAPPSESSION_POJO_INTERNAL_FQN_INDEX)) == false);
    }
}
