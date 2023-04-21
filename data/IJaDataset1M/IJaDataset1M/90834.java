package org.kablink.teaming.module.ldap;

import javax.naming.NamingException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import org.kablink.teaming.domain.LdapConnectionConfig;
import org.kablink.teaming.domain.LdapSyncException;
import org.kablink.teaming.module.ldap.LdapModule;
import org.kablink.teaming.module.ldap.LdapSchedule;
import org.kablink.teaming.module.ldap.LdapSyncResults;
import org.kablink.teaming.module.ldap.LdapSyncResults.SyncStatus;
import org.kablink.teaming.web.util.WebHelper;

/**
 * This class is used to start a thread which will perform an ldap sync.
 * @author jwootton
 *
 */
public class LdapSyncThread extends Thread {

    private LdapSyncResults m_ldapSyncResults;

    private PortletSession m_session = null;

    private String m_id;

    private LdapModule m_ldapModule;

    private boolean m_syncUsersAndGroups;

    private boolean m_syncGuids;

    /**
	 * Create an LdapSyncThread object.
	 */
    public static LdapSyncThread createLdapSyncThread(RenderRequest request, String id, LdapModule ldapModule, boolean syncUsersAndGroups, boolean syncGuids) {
        LdapSyncThread ldapSyncThread;
        PortletSession session;
        session = WebHelper.getRequiredPortletSession(request);
        if (session == null) return null;
        ldapSyncThread = new LdapSyncThread(session, id, ldapModule, syncUsersAndGroups, syncGuids);
        ldapSyncThread.setPriority(Thread.MIN_PRIORITY);
        session.setAttribute(id, ldapSyncThread, PortletSession.APPLICATION_SCOPE);
        return ldapSyncThread;
    }

    /**
	 * Return the LdapSyncThread object with the given id.
	 */
    public static LdapSyncThread getLdapSyncThread(PortletRequest request, String id) {
        LdapSyncThread ldapSyncThread = null;
        PortletSession session;
        if (id == null) return null;
        session = request.getPortletSession(false);
        if (session == null) return null;
        ldapSyncThread = (LdapSyncThread) session.getAttribute(id, PortletSession.APPLICATION_SCOPE);
        return ldapSyncThread;
    }

    /**
	 * Return the LdapSyncResults object for the given LdapSyncThread id.
	 */
    public static LdapSyncResults getLdapSyncResults(PortletRequest request, String id) {
        LdapSyncThread ldapSyncThread;
        LdapSyncResults ldapSyncResults = null;
        ldapSyncThread = getLdapSyncThread(request, id);
        if (ldapSyncThread != null) ldapSyncResults = ldapSyncThread.getLdapSyncResults();
        return ldapSyncResults;
    }

    /**
	 * Class constructor. (1 of 1)
	 */
    private LdapSyncThread(PortletSession session, String id, LdapModule ldapModule, boolean syncUsersAndGroups, boolean syncGuids) {
        super(id);
        m_id = id;
        m_session = session;
        m_ldapModule = ldapModule;
        m_syncUsersAndGroups = syncUsersAndGroups;
        m_syncGuids = syncGuids;
        m_ldapSyncResults = new LdapSyncResults(id);
    }

    /**
	 * Execute the code that will perform the ldap sync.
	 */
    public void doLdapSync() {
        boolean enabled;
        LdapSyncResults syncResults;
        LdapSchedule schedule;
        syncResults = getLdapSyncResults();
        schedule = m_ldapModule.getLdapSchedule();
        enabled = schedule.getScheduleInfo().isEnabled();
        schedule.getScheduleInfo().setEnabled(false);
        m_ldapModule.setLdapSchedule(schedule);
        try {
            m_ldapModule.syncAll(m_syncUsersAndGroups, m_syncGuids, syncResults);
            if (syncResults.getStatus() != SyncStatus.STATUS_SYNC_ALREADY_IN_PROGRESS) {
                syncResults.completed();
            }
        } catch (LdapSyncException ldapSyncEx) {
            LdapConnectionConfig ldapConfig;
            NamingException ne;
            String errorDesc;
            ldapConfig = ldapSyncEx.getLdapConfig();
            ne = ldapSyncEx.getNamingException();
            if (ne.getCause() != null) errorDesc = ne.getCause().getLocalizedMessage() != null ? ne.getCause().getLocalizedMessage() : ne.getCause().getMessage(); else errorDesc = ne.getLocalizedMessage() != null ? ne.getLocalizedMessage() : ne.getMessage();
            syncResults.error(errorDesc, ldapConfig.getId());
        } finally {
            if (enabled) {
                schedule.getScheduleInfo().setEnabled(enabled);
                m_ldapModule.setLdapSchedule(schedule);
            }
        }
    }

    /**
	 * Return the LdapSyncResults object associated with this thread.
	 */
    public LdapSyncResults getLdapSyncResults() {
        return m_ldapSyncResults;
    }

    /**
     * Remove this LdapSyncThread object from the session.
     */
    public void removeFromSession() {
        if (m_session != null) m_session.removeAttribute(m_id);
    }

    /**
     * Implement the Thread::run method.  Do the work of the ldap sync.
     */
    public void run() {
        doLdapSync();
    }
}
