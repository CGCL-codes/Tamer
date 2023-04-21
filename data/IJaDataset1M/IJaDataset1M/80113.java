package org.opencms.main;

import org.opencms.db.CmsUserSettings;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsProject;
import org.opencms.file.CmsRequestContext;
import org.opencms.file.CmsUser;
import org.opencms.security.CmsRole;
import org.opencms.security.CmsSecurityException;
import org.opencms.util.CmsRequestUtil;
import org.opencms.util.CmsStringUtil;
import org.opencms.util.CmsUUID;
import org.opencms.workplace.CmsWorkplaceManager;
import org.opencms.workplace.tools.CmsToolManager;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUtils;
import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.commons.logging.Log;

/**
 * Keeps track of the sessions running on the OpenCms server and
 * provides a session info storage which is used to get an overview
 * about currently authenticated OpenCms users, as well as sending broadcasts between users.<p> 
 *  
 * For each authenticated OpenCms user, a {@link org.opencms.main.CmsSessionInfo} object
 * holds the information about the users status.<p>
 *
 * When a user session is invalidated, the user info will be removed.
 * This happens when a user log out, or when his session times out.<p>
 * 
 * <b>Please Note:</b> The current implementation does not provide any permission checking,
 * so all users can access the methods of this manager. Permission checking
 * based on the current users OpenCms context may be added in a future OpenCms release.<p>
 * 
 * @author Alexander Kandzior 
 *
 * @version $Revision: 1.18 $ 
 * 
 * @since 6.0.0 
 */
public class CmsSessionManager {

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsSessionManager.class);

    /** Lock object for synchronized session count updates. */
    private Object m_lockSessionCount;

    /** Counter for the currently active sessions. */
    private int m_sessionCountCurrent;

    /** Counter for all sessions created so far. */
    private int m_sessionCountTotal;

    /** Session storage provider instance. */
    private I_CmsSessionStorageProvider m_sessionStorageProvider;

    /**
     * Creates a new instance of the OpenCms session manager.<p>
     */
    protected CmsSessionManager() {
        m_lockSessionCount = new Object();
    }

    /**
     * Returns the broadcast queue for the given OpenCms session id.<p>
     * 
     * @param sessionId the OpenCms session id to get the broadcast queue for
     * 
     * @return the broadcast queue for the given OpenCms session id
     */
    public Buffer getBroadcastQueue(String sessionId) {
        CmsSessionInfo sessionInfo = getSessionInfo(getSessionUUID(sessionId));
        if (sessionInfo == null) {
            return BufferUtils.synchronizedBuffer(new CircularFifoBuffer(CmsSessionInfo.QUEUE_SIZE));
        }
        return sessionInfo.getBroadcastQueue();
    }

    /**
     * Returns the number of sessions currently authenticated in the OpenCms security system.<p>
     *
     * @return the number of sessions currently authenticated in the OpenCms security system
     */
    public int getSessionCountAuthenticated() {
        if (m_sessionStorageProvider == null) {
            return 0;
        }
        return m_sessionStorageProvider.getSize();
    }

    /**
     * Returns the number of current sessions, including the sessions of not authenticated guest users.<p>
     * 
     * @return the number of current sessions, including the sessions of not authenticated guest users
     */
    public int getSessionCountCurrent() {
        return m_sessionCountCurrent;
    }

    /**
     * Returns the number of total sessions generated so far, including already destroyed sessions.<p>
     * 
     * @return the number of total sessions generated so far, including already destroyed sessions
     */
    public int getSessionCountTotal() {
        return m_sessionCountTotal;
    }

    /**
     * Returns the complete user session info of a user from the session storage,
     * or <code>null</code> if this session id has no session info attached.<p>
     *
     * @param sessionId the OpenCms session id to return the session info for
     * 
     * @return the complete user session info of a user from the session storage
     */
    public CmsSessionInfo getSessionInfo(CmsUUID sessionId) {
        if (m_sessionStorageProvider == null) {
            return null;
        }
        return m_sessionStorageProvider.get(sessionId);
    }

    /**
     * Returns the OpenCms user session info for the given request, 
     * or <code>null</code> if no user session is available.<p>
     * 
     * @param req the current request
     * 
     * @return the OpenCms user session info for the given request, or <code>null</code> if no user session is available
     */
    public CmsSessionInfo getSessionInfo(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            String sessionId = req.getHeader(CmsRequestUtil.HEADER_JSESSIONID);
            return sessionId == null ? null : getSessionInfo(sessionId);
        }
        return getSessionInfo(session);
    }

    /**
     * Returns the OpenCms user session info for the given http session, 
     * or <code>null</code> if no user session is available.<p>
     * 
     * @param session the current http session
     * 
     * @return the OpenCms user session info for the given http session, or <code>null</code> if no user session is available
     */
    public CmsSessionInfo getSessionInfo(HttpSession session) {
        if (session == null) {
            return null;
        }
        CmsUUID sessionId = (CmsUUID) session.getAttribute(CmsSessionInfo.ATTRIBUTE_SESSION_ID);
        return (sessionId == null) ? null : getSessionInfo(sessionId);
    }

    /**
     * Returns the complete user session info of a user from the session storage,
     * or <code>null</code> if this session id has no session info attached.<p>
     *
     * @param sessionId the OpenCms session id to return the session info for,
     * this must be a String representation of a {@link CmsUUID}
     * 
     * @return the complete user session info of a user from the session storage
     * 
     * @see #getSessionInfo(CmsUUID)
     */
    public CmsSessionInfo getSessionInfo(String sessionId) {
        return getSessionInfo(getSessionUUID(sessionId));
    }

    /**
     * Returns all current session info objects.<p>
     *  
     * @return all current session info objects
     */
    public List getSessionInfos() {
        if (m_sessionStorageProvider == null) {
            return Collections.EMPTY_LIST;
        }
        return m_sessionStorageProvider.getAll();
    }

    /**
     * Returns a list of all active session info objects for the specified user.<p>
     * 
     * An OpenCms user can have many active sessions. 
     * This is e.g. possible when two people have logged in to the system using the
     * same username. Even one person can have multiple sessions if he
     * is logged in to OpenCms with several browser windows at the same time.<p>
     * 
     * @param userId the id of the user
     *  
     * @return a list of all active session info objects for the specified user
     */
    public List getSessionInfos(CmsUUID userId) {
        if (m_sessionStorageProvider == null) {
            return Collections.EMPTY_LIST;
        }
        return m_sessionStorageProvider.getAllOfUser(userId);
    }

    /**
     * Sends a broadcast to all sessions of all currently authenticated users.<p>
     * 
     * @param cms the OpenCms user context of the user sending the broadcast
     * 
     * @param message the message to broadcast
     */
    public void sendBroadcast(CmsObject cms, String message) {
        if (CmsStringUtil.isEmptyOrWhitespaceOnly(message)) {
            return;
        }
        CmsBroadcast broadcast = new CmsBroadcast(cms.getRequestContext().currentUser(), message);
        Iterator i = m_sessionStorageProvider.getAll().iterator();
        while (i.hasNext()) {
            CmsSessionInfo sessionInfo = (CmsSessionInfo) i.next();
            if (m_sessionStorageProvider.get(sessionInfo.getSessionId()) != null) {
                sessionInfo.getBroadcastQueue().add(broadcast);
            }
        }
    }

    /**
     * Sends a broadcast to the specified user session.<p>
     * 
     * @param cms the OpenCms user context of the user sending the broadcast
     * 
     * @param message the message to broadcast
     * @param sessionId the OpenCms session uuid target (receiver) of the broadcast
     */
    public void sendBroadcast(CmsObject cms, String message, String sessionId) {
        if (CmsStringUtil.isEmptyOrWhitespaceOnly(message)) {
            return;
        }
        CmsSessionInfo sessionInfo = m_sessionStorageProvider.get(new CmsUUID(sessionId));
        if (sessionInfo != null) {
            sessionInfo.getBroadcastQueue().add(new CmsBroadcast(cms.getRequestContext().currentUser(), message));
        }
    }

    /**
     * Sends a broadcast to all sessions of a given user.<p>
     * 
     * The user sending the message may be a real user like 
     * <code>cms.getRequestContext().currentUser()</code> or
     * <code>null</code> for a system message.<p>
     * 
     * @param fromUser the user sending the broadcast
     * @param message the message to broadcast
     * @param toUser the target (receiver) of the broadcast
     */
    public void sendBroadcast(CmsUser fromUser, String message, CmsUser toUser) {
        if (CmsStringUtil.isEmptyOrWhitespaceOnly(message)) {
            return;
        }
        CmsBroadcast broadcast = new CmsBroadcast(fromUser, message);
        List userSessions = getSessionInfos(toUser.getId());
        Iterator i = userSessions.iterator();
        while (i.hasNext()) {
            CmsSessionInfo sessionInfo = (CmsSessionInfo) i.next();
            if (m_sessionStorageProvider.get(sessionInfo.getSessionId()) != null) {
                sessionInfo.getBroadcastQueue().add(broadcast);
            }
        }
    }

    /**
     * Switches the current user to the given user. The session info is rebuild as if the given user
     * performs a login at the workplace.
     * 
     * @param cms the current CmsObject
     * @param req the current request
     * @param user the user to switch to
     * 
     * @throws CmsException if something goes wrong
     */
    public void switchUser(CmsObject cms, HttpServletRequest req, CmsUser user) throws CmsException {
        OpenCms.getRoleManager().checkRole(cms, CmsRole.ROOT_ADMIN.forOrgUnit(user.getOuFqn()));
        CmsSessionInfo info = getSessionInfo(req);
        HttpSession session = req.getSession(false);
        if ((info == null) || (session == null)) {
            throw new CmsException(Messages.get().container(Messages.ERR_NO_SESSIONINFO_SESSION_0));
        }
        if (!OpenCms.getRoleManager().hasRole(cms, user.getName(), CmsRole.WORKPLACE_USER)) {
            throw new CmsSecurityException(Messages.get().container(Messages.ERR_NO_WORKPLACE_PERMISSIONS_0));
        }
        CmsUserSettings settings = new CmsUserSettings(user);
        String ouFqn = user.getOuFqn();
        CmsProject userProject = cms.readProject(ouFqn + OpenCms.getWorkplaceManager().getDefaultUserSettings().getStartProject());
        try {
            userProject = cms.readProject(settings.getStartProject());
        } catch (Exception e) {
        }
        String userSiteRoot = settings.getStartSite();
        CmsRequestContext context = new CmsRequestContext(user, userProject, null, userSiteRoot, null, null, null, 0, null, null, ouFqn);
        session.removeAttribute(CmsWorkplaceManager.SESSION_WORKPLACE_SETTINGS);
        CmsSessionInfo newInfo = new CmsSessionInfo(context, info.getSessionId(), info.getMaxInactiveInterval());
        addSessionInfo(newInfo);
        cms.getRequestContext().setSiteRoot(userSiteRoot);
        cms.getRequestContext().setCurrentProject(userProject);
        cms.getRequestContext().setOuFqn(user.getOuFqn());
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer output = new StringBuffer();
        Iterator i = m_sessionStorageProvider.getAll().iterator();
        output.append("[CmsSessions]:\n");
        while (i.hasNext()) {
            CmsSessionInfo sessionInfo = (CmsSessionInfo) i.next();
            output.append(sessionInfo.getSessionId().toString());
            output.append(" : ");
            output.append(sessionInfo.getUserId().toString());
            output.append('\n');
        }
        return output.toString();
    }

    /**
     * Updates all session info objects, so that invalid projects 
     * are replaced by the Online project.<p>
     * 
     * @param cms the cms context
     */
    public void updateSessionInfos(CmsObject cms) {
        List userSessions = getSessionInfos();
        Iterator i = userSessions.iterator();
        while (i.hasNext()) {
            CmsSessionInfo sessionInfo = (CmsSessionInfo) i.next();
            CmsUUID projectId = sessionInfo.getProject();
            try {
                cms.readProject(projectId);
            } catch (CmsException e) {
                sessionInfo.setProject(CmsProject.ONLINE_PROJECT_ID);
                addSessionInfo(sessionInfo);
            }
        }
    }

    /**
     * Adds a new session info into the session storage.<p>
     *
     * @param sessionInfo the session info to store for the id
     */
    protected void addSessionInfo(CmsSessionInfo sessionInfo) {
        m_sessionStorageProvider.put(sessionInfo);
    }

    /**
     * Returns the UUID representation for the given session id String.<p>
     * 
     * @param sessionId the session id String to return the  UUID representation for
     * 
     * @return the UUID representation for the given session id String
     */
    protected CmsUUID getSessionUUID(String sessionId) {
        return new CmsUUID(sessionId);
    }

    /**
     * Sets the storage provider.<p>
     * 
     * @param sessionStorageProvider the storage provider implementation
     */
    protected void initialize(I_CmsSessionStorageProvider sessionStorageProvider) {
        m_sessionStorageProvider = sessionStorageProvider;
        m_sessionStorageProvider.initialize();
    }

    /**
     * Called by the {@link OpenCmsListener} when a http session is created.<p>
     * 
     * @param event the http session event
     * 
     * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
     * @see OpenCmsListener#sessionCreated(HttpSessionEvent)
     */
    protected void sessionCreated(HttpSessionEvent event) {
        synchronized (m_lockSessionCount) {
            m_sessionCountCurrent = (m_sessionCountCurrent <= 0) ? 1 : (m_sessionCountCurrent + 1);
            m_sessionCountTotal++;
            if (LOG.isInfoEnabled()) {
                LOG.info(Messages.get().getBundle().key(Messages.LOG_SESSION_CREATED_2, new Integer(m_sessionCountTotal), new Integer(m_sessionCountCurrent)));
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.get().getBundle().key(Messages.LOG_SESSION_CREATED_1, event.getSession().getId()));
        }
    }

    /**
     * Called by the {@link OpenCmsListener} when a http session is destroyed.<p>
     * 
     * @param event the http session event
     * 
     * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
     * @see OpenCmsListener#sessionDestroyed(HttpSessionEvent)
     */
    protected void sessionDestroyed(HttpSessionEvent event) {
        synchronized (m_lockSessionCount) {
            m_sessionCountCurrent = (m_sessionCountCurrent <= 0) ? 0 : (m_sessionCountCurrent - 1);
            if (LOG.isInfoEnabled()) {
                LOG.info(Messages.get().getBundle().key(Messages.LOG_SESSION_DESTROYED_2, new Integer(m_sessionCountTotal), new Integer(m_sessionCountCurrent)));
            }
        }
        CmsSessionInfo sessionInfo = getSessionInfo(event.getSession());
        CmsUUID userId = null;
        if (sessionInfo != null) {
            userId = sessionInfo.getUserId();
            m_sessionStorageProvider.remove(sessionInfo.getSessionId());
        }
        if ((userId != null) && (getSessionInfos(userId).size() == 0)) {
            OpenCmsCore.getInstance().getLockManager().removeTempLocks(userId);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.get().getBundle().key(Messages.LOG_SESSION_DESTROYED_1, event.getSession().getId()));
        }
    }

    /**
     * Removes all stored session info objects.<p>
     * 
     * @throws Exception if something goes wrong 
     */
    protected void shutdown() throws Exception {
        if (m_sessionStorageProvider != null) {
            m_sessionStorageProvider.shutdown();
        }
    }

    /**
     * Updates the the OpenCms session data used for quick authentication of users.<p>
     *
     * This is required if the user data (current group or project) was changed in
     * the requested document.<p>
     *
     * The user data is only updated if the user was authenticated to the system.
     *
     * @param cms the current OpenCms user context
     * @param req the current request
     */
    protected void updateSessionInfo(CmsObject cms, HttpServletRequest req) {
        if (!cms.getRequestContext().isUpdateSessionEnabled()) {
            return;
        }
        if (cms.getRequestContext().getUri().equals(CmsToolManager.VIEW_JSPPAGE_LOCATION)) {
            return;
        }
        if (!cms.getRequestContext().currentUser().isGuestUser()) {
            CmsSessionInfo sessionInfo = getSessionInfo(req);
            if (sessionInfo != null) {
                sessionInfo.update(cms.getRequestContext());
                addSessionInfo(sessionInfo);
            } else {
                HttpSession session = req.getSession(false);
                if (session != null) {
                    sessionInfo = new CmsSessionInfo(cms.getRequestContext(), new CmsUUID(), session.getMaxInactiveInterval());
                    session.setAttribute(CmsSessionInfo.ATTRIBUTE_SESSION_ID, sessionInfo.getSessionId().clone());
                    addSessionInfo(sessionInfo);
                }
            }
        }
    }

    /**
     * Validates the sessions stored in this manager and removes 
     * any sessions that have become invalidated.<p>
     */
    protected void validateSessionInfos() {
        if (m_sessionStorageProvider == null) {
            return;
        }
        m_sessionStorageProvider.validate();
    }
}
