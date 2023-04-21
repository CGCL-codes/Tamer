package de.fuh.xpairtise.server;

import java.util.HashMap;
import de.fuh.xpairtise.common.LogConstants;
import de.fuh.xpairtise.common.XPLog;
import de.fuh.xpairtise.common.network.NetworkException;
import de.fuh.xpairtise.common.replication.IReplicatedListMaster;
import de.fuh.xpairtise.common.replication.UnexpectedReplicationState;
import de.fuh.xpairtise.common.replication.elements.ReplicatedXPSession;
import de.fuh.xpairtise.common.util.GroupCheckHelper;
import de.fuh.xpairtise.server.network.ServerSideCommunicationFactory;

/**
 * This class handles a collaboration session map together with the master list.
 * This allows the clients of this class to query sessions by their IDs.
 */
public class XPSessionManager {

    private IReplicatedListMaster<ReplicatedXPSession> xpSessionList;

    private HashMap<String, ReplicatedXPSession> xpSessionMap;

    /**
   * Creates a new collaboration session manager instance for the specified
   * master list instance.
   * 
   * @param masterListName
   *          the master list's name
   * @throws NetworkException
   */
    public XPSessionManager(String masterListName) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_XPSESSION_MANAGER + "new instance for master list: " + masterListName);
        }
        xpSessionMap = new HashMap<String, ReplicatedXPSession>();
        xpSessionList = ServerSideCommunicationFactory.getInstance().createReplicatedList(masterListName);
    }

    /**
   * Registers a new collaboration session instance if not already registered.
   * The session's ID has to be unique to be added.
   * 
   * @param xpSession
   *          the <code>ReplicatedXPSession</code> instance to be registered
   * @param makePersistent
   *          <code>true</code> the session metadata should be stored on the
   *          server
   * @throws UnexpectedReplicationState
   *           if the session already exists
   */
    public synchronized void registerXPSession(ReplicatedXPSession xpSession, boolean makePersistent) throws UnexpectedReplicationState {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_XPSESSION_MANAGER + "registerXPSession() call for xpSessionId: " + xpSession.getXpSessionId() + ", topic: " + xpSession.getTopic());
        }
        if (!xpSessionMap.containsKey(xpSession.getXpSessionId())) {
            xpSessionList.add(xpSession);
            xpSessionMap.put(xpSession.getXpSessionId(), xpSession);
        } else {
            throw new UnexpectedReplicationState("xpSession " + xpSession.getXpSessionId() + " (topic \"" + xpSession.getTopic() + "\") already registered");
        }
    }

    /**
   * Returns the collaboration session instance by the specified ID.
   * 
   * @param xpSessionId
   *          the ID of the searched session
   * @return the <code>ReplicatedXPSession</code> instance that matches the
   *         specified ID or <code>null</code> otherwise
   */
    public ReplicatedXPSession getXPSessionById(String xpSessionId) {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_XPSESSION_MANAGER + "getXPSessionById() call for xpSessionId: " + xpSessionId);
        }
        ReplicatedXPSession xpSession = xpSessionMap.get(xpSessionId);
        return xpSession;
    }

    /**
   * Returns the collaboration session instance by the specified topic.
   * 
   * @param xpSessionTopic
   *          the topic of the searched session
   * @return the <code>ReplicatedXPSession</code> instance that matches the
   *         specified topic or <code>null</code> otherwise
   */
    public ReplicatedXPSession getXPSessionByTopic(String xpSessionTopic, String userGroup) {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_XPSESSION_MANAGER + "getXPSessionByTopic() call for session topic: " + xpSessionTopic);
        }
        ReplicatedXPSession xpSession = null;
        for (ReplicatedXPSession s : xpSessionMap.values()) {
            if (s.getTopic().equals(xpSessionTopic) && GroupCheckHelper.isUserInGroup(s.getUserGroup(), userGroup)) {
                xpSession = s;
            }
        }
        return xpSession;
    }

    /**
   * Returns the collaboration session instance by the specified user name.
   * 
   * @param userName
   *          the name of the member who participates in the searched session
   * @return the <code>ReplicatedXPSession</code> instance that the specified
   *         user participates in or <code>null</code> otherwise
   */
    public ReplicatedXPSession getXPSessionByUsername(String userName, String userGroup) {
        for (ReplicatedXPSession s : xpSessionMap.values()) {
            if (s.containsUser(userName) && GroupCheckHelper.isUserInGroup(s.getUserGroup(), userGroup)) {
                return s;
            }
        }
        return null;
    }

    /**
   * Returns true if the collaboration session is registered.
   * 
   * @param xpSessionId
   *          the session's ID to be checked
   * 
   * @return <code>true</code> if the session is registered, otherwise
   *         <code>false</code>
   */
    public boolean isXPSessionRegistered(String xpSessionId) {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_XPSESSION_MANAGER + "isXPSessionRegistered() call for xpSessionId: " + xpSessionId);
        }
        return xpSessionMap.containsKey(xpSessionId);
    }

    /**
   * Updates an existing collaboration session instance.
   * 
   * @param xpSession
   *          the session to be updated
   * 
   * @throws UnexpectedReplicationState
   *           if the session is unknown
   */
    public synchronized void updateXPSession(ReplicatedXPSession xpSession) throws UnexpectedReplicationState {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_XPSESSION_MANAGER + "updateXPSession() call for xpSessionId: " + xpSession.getXpSessionId() + ", topic: " + xpSession.getTopic());
        }
        if (isXPSessionRegistered(xpSession.getXpSessionId())) {
            xpSessionList.update(xpSession);
            xpSessionMap.put(xpSession.getXpSessionId(), xpSession);
        } else {
            throw new UnexpectedReplicationState("session " + xpSession.getXpSessionId() + " not existing");
        }
    }

    /**
   * Removes the collaboration session from the registration.
   * 
   * @param xpSession
   *          the session to be removed
   * 
   * @throws UnexpectedReplicationState
   *           if the session is unknown
   */
    public synchronized void removeXPSession(ReplicatedXPSession xpSession) throws UnexpectedReplicationState {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_XPSESSION_MANAGER + "removeXPSession() call for xpSessionId: " + xpSession.getXpSessionId() + ", topic: " + xpSession.getTopic());
        }
        if (isXPSessionRegistered(xpSession.getXpSessionId())) {
            xpSessionList.remove(xpSession);
            xpSessionMap.remove(xpSession.getXpSessionId());
        } else {
            throw new UnexpectedReplicationState("session " + xpSession.getXpSessionId() + " not existing");
        }
    }
}
