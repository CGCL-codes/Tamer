package org.jboss.web.tomcat.service.session;

import java.text.ParseException;
import org.jboss.cache.Fqn;
import org.jboss.cache.buddyreplication.BuddyManager;
import org.jboss.logging.Logger;
import org.jboss.metadata.WebMetaData;
import org.mobicents.servlet.sip.core.session.SessionManagerUtil;
import org.mobicents.servlet.sip.core.session.SipApplicationSessionKey;
import org.mobicents.servlet.sip.core.session.SipSessionKey;

/**
 * Listens for distributed caches events, notifying the JBossCacheManager
 * of events of interest. 
 * 
 * @author Brian Stansberry
 * @author <A HREF="mailto:jean.deruelle@gmail.com">Jean Deruelle</A>
 */
public class SipCacheListener extends AbstractCacheListener {

    protected static Logger logger = Logger.getLogger(SipCacheListener.class);

    private static final int SIPSESSION_FQN_INDEX = 0;

    private static final int HOSTNAME_FQN_INDEX = 1;

    private static final int SIPAPPNAME_FQN_INDEX = 2;

    private static final int SIPAPPSESSION_ID_FQN_INDEX = 3;

    private static final int SIPSESSION_ID_FQN_INDEX = 4;

    private static final int SIPAPPSESSION_FQN_SIZE = SIPAPPSESSION_ID_FQN_INDEX + 1;

    private static final int SIPAPPSESSION_POJO_ATTRIBUTE_FQN_INDEX = SIPAPPSESSION_ID_FQN_INDEX + 1;

    private static final int SIPSESSION_FQN_SIZE = SIPSESSION_ID_FQN_INDEX + 1;

    private static final int SIPSESSION_POJO_ATTRIBUTE_FQN_INDEX = SIPSESSION_ID_FQN_INDEX + 1;

    private static final int SIPAPPSESSION_POJO_KEY_FQN_INDEX = SIPAPPSESSION_POJO_ATTRIBUTE_FQN_INDEX + 1;

    private static final int SIPAPPSESSION_POJO_KEY_FQN_SIZE = SIPAPPSESSION_POJO_KEY_FQN_INDEX + 1;

    private static final int SIPSESSION_POJO_KEY_FQN_INDEX = SIPSESSION_POJO_ATTRIBUTE_FQN_INDEX + 1;

    private static final int SIPSESSION_POJO_KEY_FQN_SIZE = SIPSESSION_POJO_KEY_FQN_INDEX + 1;

    private static final int BUDDY_BACKUP_ROOT_OWNER_INDEX = BuddyManager.BUDDY_BACKUP_SUBTREE_FQN.size();

    private static final int BUDDY_BACKUP_ROOT_OWNER_SIZE = BUDDY_BACKUP_ROOT_OWNER_INDEX + 1;

    private static Logger log_ = Logger.getLogger(SipCacheListener.class);

    private JBossCacheWrapper cacheWrapper_;

    private JBossCacheSipManager manager_;

    private String sipApplicationName;

    private String sipApplicationNameHashed;

    private String hostname_;

    private boolean fieldBased_;

    private boolean disdainLocalActivity_;

    SipCacheListener(JBossCacheWrapper wrapper, JBossCacheSipManager manager, String hostname, String sipApplicationName, String sipApplicationNameHashed) {
        cacheWrapper_ = wrapper;
        manager_ = manager;
        hostname_ = hostname;
        this.sipApplicationName = sipApplicationName;
        this.sipApplicationNameHashed = sipApplicationNameHashed;
        int granularity = manager_.getReplicationGranularity();
        fieldBased_ = (granularity == WebMetaData.REPLICATION_GRANULARITY_FIELD);
        disdainLocalActivity_ = (granularity == WebMetaData.REPLICATION_GRANULARITY_SESSION);
    }

    @Override
    public void nodeCreated(Fqn fqn) {
        boolean local = ConvergedSessionReplicationContext.isSipLocallyActive();
        boolean isBuddy = isBuddyFqn(fqn);
        int size = fqn.size();
        if (isFqnSessionRootSized(size, isBuddy) && isFqnForOurSipapp(fqn, isBuddy)) {
            logger.debug("following node created " + fqn.toString() + " with name " + fqn.getName());
        }
        if (!fieldBased_ && local) return;
    }

    public void nodeRemoved(Fqn fqn) {
        boolean local = ConvergedSessionReplicationContext.isSipLocallyActive();
        boolean isBuddy = isBuddyFqn(fqn);
        int size = fqn.size();
        if (isFqnSessionRootSized(size, isBuddy) && isFqnForOurSipapp(fqn, isBuddy)) {
            logger.debug("following node removed " + fqn.toString() + " with name " + fqn.getName());
        }
        if (!fieldBased_ && local) return;
        if (isFqnSessionRootSized(size, isBuddy)) {
            if (!local && isFqnForOurSipapp(fqn, isBuddy)) {
                if (isFqnSipApplicationSessionRootSized(size, isBuddy)) {
                    String sessId = getSipApplicationSessionIdFromFqn(fqn, isBuddy);
                    SipApplicationSessionKey sipApplicationSessionKey = new SipApplicationSessionKey(sessId, sipApplicationName);
                    manager_.processRemoteSipApplicationSessionInvalidation(sipApplicationSessionKey);
                } else {
                    String sipAppSessId = getSipApplicationSessionIdFromFqn(fqn, isBuddy);
                    String sessId = getSipSessionIdFromFqn(fqn, isBuddy);
                    try {
                        SipSessionKey sipSessionKey = SessionManagerUtil.parseHaSipSessionKey(sessId, sipAppSessId, sipApplicationName);
                        manager_.processRemoteSipSessionInvalidation(sipSessionKey);
                    } catch (ParseException e) {
                        logger.error("Unexpected exception while parsing the following sip session key " + sessId, e);
                        return;
                    }
                }
            }
        } else if (fieldBased_ && isFqnForOurSipapp(fqn, isBuddy)) {
            if (!local && isFqnPojoKeySized(size, isBuddy)) {
                String sessId = null;
                String attrKey = null;
                if (isFqnSipApplicationSessionRootSized(size, isBuddy)) {
                    sessId = getSipApplicationSessionIdFromFqn(fqn, isBuddy);
                    attrKey = getSipApplicationSessionIdPojoKeyFromFqn(fqn, isBuddy);
                    SipApplicationSessionKey sipApplicationSessionKey = new SipApplicationSessionKey(sessId, sipApplicationName);
                    manager_.processRemoteSipApplicationSessionAttributeRemoval(sipApplicationSessionKey, attrKey);
                } else {
                    String sipAppSessId = getSipApplicationSessionIdFromFqn(fqn, isBuddy);
                    sessId = getSipSessionIdFromFqn(fqn, isBuddy);
                    attrKey = getSipSessionIdPojoKeyFromFqn(fqn, isBuddy);
                    try {
                        SipSessionKey sipSessionKey = SessionManagerUtil.parseHaSipSessionKey(sessId, sipAppSessId, sipApplicationName);
                        manager_.processRemoteSipSessionAttributeRemoval(sipSessionKey, attrKey);
                    } catch (ParseException e) {
                        logger.error("Unexpected exception while parsing the following sip session key " + sessId, e);
                        return;
                    }
                }
            } else if (local && isFqnInPojo(size, isBuddy)) {
                String sessId = null;
                if (isFqnSipApplicationSessionRootSized(size, isBuddy)) {
                    sessId = getSipApplicationSessionIdFromFqn(fqn, isBuddy);
                    SipApplicationSessionKey sipApplicationSessionKey = new SipApplicationSessionKey(sessId, sipApplicationName);
                    manager_.processSipApplicationSessionLocalPojoModification(sipApplicationSessionKey);
                } else {
                    String sipAppSessId = getSipApplicationSessionIdFromFqn(fqn, isBuddy);
                    sessId = getSipSessionIdFromFqn(fqn, isBuddy);
                    try {
                        SipSessionKey sipSessionKey = SessionManagerUtil.parseHaSipSessionKey(sessId, sipAppSessId, sipApplicationName);
                        manager_.processSipSessionLocalPojoModification(sipSessionKey);
                    } catch (ParseException e) {
                        logger.error("Unexpected exception while parsing the following sip session key " + sessId, e);
                        return;
                    }
                }
            }
        }
    }

    public void nodeModified(Fqn fqn) {
        boolean local = ConvergedSessionReplicationContext.isSipLocallyActive();
        boolean isBuddy = isBuddyFqn(fqn);
        int size = fqn.size();
        if (isFqnSessionRootSized(size, isBuddy) && isFqnForOurSipapp(fqn, isBuddy)) {
            logger.debug("following node modified " + fqn.toString() + " with name " + fqn.getName());
        }
        if (!fieldBased_ && local) return;
        if (isFqnSessionRootSized(size, isBuddy)) {
            if (!local && isFqnForOurSipapp(fqn, isBuddy)) {
                handleSessionRootModification(fqn, isBuddy);
            }
        } else if (fieldBased_ && local && isFqnForOurSipapp(fqn, isBuddy) && isFqnInPojo(size, isBuddy)) {
            String sessId = null;
            if (isFqnSipApplicationSessionRootSized(size, isBuddy)) {
                sessId = getSipApplicationSessionIdFromFqn(fqn, isBuddy);
                SipApplicationSessionKey sipApplicationSessionKey = new SipApplicationSessionKey(sessId, sipApplicationName);
                manager_.processSipApplicationSessionLocalPojoModification(sipApplicationSessionKey);
            } else {
                String sipAppSessId = getSipApplicationSessionIdFromFqn(fqn, isBuddy);
                sessId = getSipSessionIdFromFqn(fqn, isBuddy);
                try {
                    SipSessionKey sipSessionKey = SessionManagerUtil.parseHaSipSessionKey(sessId, sipAppSessId, sipApplicationName);
                    manager_.processSipSessionLocalPojoModification(sipSessionKey);
                } catch (ParseException e) {
                    logger.error("Unexpected exception while parsing the following sip session key " + sessId, e);
                    return;
                }
            }
        }
    }

    private void handleSessionRootModification(Fqn fqn, boolean isBuddy) {
        if (!isFqnForOurSipapp(fqn, isBuddy)) return;
        Integer version = (Integer) cacheWrapper_.get(fqn, JBossCacheService.VERSION_KEY);
        if (version != null) {
            String sessId = getSipApplicationSessionIdFromFqn(fqn, isBuddy);
            boolean isSipApplicationSession = true;
            if (!isFqnSipApplicationSessionRootSized(fqn.size(), isBuddy)) {
                isSipApplicationSession = false;
            }
            SipApplicationSessionKey key = new SipApplicationSessionKey(sessId, sipApplicationName);
            ClusteredSipApplicationSession sipApplicationSession = manager_.findLocalSipApplicationSession(key, false);
            if (sipApplicationSession == null) {
                return;
            } else if (sipApplicationSession.isNewData(version.intValue())) {
                sipApplicationSession.setOutdatedVersion(version.intValue());
                if (isSipApplicationSession) {
                    if (log_.isDebugEnabled()) {
                        log_.debug("Refreshing sip application session that was loaded on this node but modified on remote node (update expiration timer too) " + sipApplicationSession);
                    }
                    manager_.loadSipApplicationSession(key, false);
                }
                if (log_.isDebugEnabled()) {
                    log_.debug("nodeDirty(): session in-memory data is " + "invalidated with id: " + sessId + " and version: " + version.intValue());
                }
            } else if (isBuddy) {
                ;
            } else {
                log_.info("Possible concurrency problem: Replicated version id " + version + " matches in-memory version for session " + sessId);
                sipApplicationSession.setOutdatedVersion(version.intValue());
            }
            if (!isSipApplicationSession) {
                String sipSessionId = getSipSessionIdFromFqn(fqn, isBuddy);
                ClusteredSipSession session = null;
                try {
                    SipSessionKey sipSessionKey = SessionManagerUtil.parseHaSipSessionKey(sipSessionId, sessId, sipApplicationName);
                    session = manager_.findLocalSipSession(sipSessionKey, false, sipApplicationSession);
                    if (session == null) {
                        return;
                    } else if (session.isNewData(version.intValue())) {
                        session.setOutdatedVersion(version.intValue());
                        if (log_.isDebugEnabled()) {
                            log_.debug("nodeDirty(): session in-memory data is " + "invalidated with id: " + sipSessionId + " and version: " + version.intValue());
                        }
                    } else if (isBuddy) {
                        ;
                    } else {
                        log_.info("Possible concurrency problem: Replicated version id " + version + " matches in-memory version for session " + sipSessionId);
                        session.setOutdatedVersion(version.intValue());
                    }
                } catch (ParseException e) {
                    logger.error("An unexpected exception happened while parsing the sip session id : " + sessId, e);
                }
            }
        }
    }

    private boolean isFqnForOurSipapp(Fqn fqn, boolean isBuddy) {
        try {
            if (sipApplicationNameHashed.equals(fqn.get(isBuddy ? BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPAPPNAME_FQN_INDEX : SIPAPPNAME_FQN_INDEX)) && hostname_.equals(fqn.get(isBuddy ? BUDDY_BACKUP_ROOT_OWNER_SIZE + HOSTNAME_FQN_INDEX : HOSTNAME_FQN_INDEX)) && ConvergedJBossCacheService.SIPSESSION.equals(fqn.get(isBuddy ? BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPSESSION_FQN_INDEX : SIPSESSION_FQN_INDEX))) return true;
        } catch (IndexOutOfBoundsException e) {
        }
        return false;
    }

    private static boolean isFqnSessionRootSized(int size, boolean isBuddy) {
        return size == (isBuddy ? BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPAPPSESSION_FQN_SIZE : SIPAPPSESSION_FQN_SIZE) || size == (isBuddy ? BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPSESSION_FQN_SIZE : SIPSESSION_FQN_SIZE);
    }

    private static boolean isFqnPojoKeySized(int size, boolean isBuddy) {
        return size == (isBuddy ? BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPAPPSESSION_POJO_KEY_FQN_SIZE : SIPAPPSESSION_POJO_KEY_FQN_SIZE) || size == (isBuddy ? BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPSESSION_POJO_KEY_FQN_SIZE : SIPSESSION_POJO_KEY_FQN_SIZE);
    }

    private static boolean isFqnSipApplicationSessionRootSized(int size, boolean isBuddy) {
        return size == (isBuddy ? BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPAPPSESSION_FQN_SIZE : SIPAPPSESSION_FQN_SIZE);
    }

    private static boolean isFqnSipApplicationPojoKeySized(int size, boolean isBuddy) {
        return size == (isBuddy ? BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPAPPSESSION_POJO_KEY_FQN_SIZE : SIPAPPSESSION_POJO_KEY_FQN_SIZE);
    }

    private static boolean isFqnInPojo(int size, boolean isBuddy) {
        return size >= (isBuddy ? BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPAPPSESSION_POJO_KEY_FQN_SIZE : SIPAPPSESSION_POJO_KEY_FQN_SIZE) || size >= (isBuddy ? BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPSESSION_POJO_KEY_FQN_SIZE : SIPSESSION_POJO_KEY_FQN_SIZE);
    }

    private static String getSipApplicationSessionIdFromFqn(Fqn fqn, boolean isBuddy) {
        if (isBuddy) {
            return (String) fqn.get(BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPAPPSESSION_ID_FQN_INDEX);
        } else {
            return (String) fqn.get(SIPAPPSESSION_ID_FQN_INDEX);
        }
    }

    private static String getSipSessionIdFromFqn(Fqn fqn, boolean isBuddy) {
        if (isBuddy) {
            return (String) fqn.get(BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPSESSION_ID_FQN_INDEX);
        } else {
            return (String) fqn.get(SIPSESSION_ID_FQN_INDEX);
        }
    }

    private static String getSipApplicationSessionIdPojoKeyFromFqn(Fqn fqn, boolean isBuddy) {
        if (isBuddy) {
            if (fqn.size() == BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPAPPSESSION_POJO_KEY_FQN_SIZE) {
                return (String) fqn.get(BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPAPPSESSION_POJO_KEY_FQN_INDEX);
            } else {
                return (String) fqn.get(BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPSESSION_POJO_KEY_FQN_INDEX);
            }
        } else {
            if (fqn.size() == SIPAPPSESSION_POJO_KEY_FQN_SIZE) {
                return (String) fqn.get(SIPAPPSESSION_POJO_KEY_FQN_INDEX);
            } else {
                return (String) fqn.get(SIPSESSION_POJO_KEY_FQN_INDEX);
            }
        }
    }

    private static String getSipSessionIdPojoKeyFromFqn(Fqn fqn, boolean isBuddy) {
        if (isBuddy) {
            if (fqn.size() == BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPAPPSESSION_POJO_KEY_FQN_SIZE) {
                return (String) fqn.get(BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPAPPSESSION_POJO_KEY_FQN_INDEX);
            } else {
                return (String) fqn.get(BUDDY_BACKUP_ROOT_OWNER_SIZE + SIPSESSION_POJO_KEY_FQN_INDEX);
            }
        } else {
            if (fqn.size() == SIPAPPSESSION_POJO_KEY_FQN_SIZE) {
                return (String) fqn.get(SIPAPPSESSION_POJO_KEY_FQN_INDEX);
            } else {
                return (String) fqn.get(SIPSESSION_POJO_KEY_FQN_INDEX);
            }
        }
    }

    private static boolean isBuddyFqn(Fqn fqn) {
        try {
            return BuddyManager.BUDDY_BACKUP_SUBTREE.equals(fqn.get(0));
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
    * Extracts the owner portion of an buddy subtree Fqn.
    * 
    * @param fqn An Fqn that is a child of the buddy backup root node.
    */
    private static String getBuddyOwner(Fqn fqn) {
        return (String) fqn.get(BUDDY_BACKUP_ROOT_OWNER_INDEX);
    }
}
