package org.nightlabs.jfire.webapp.session;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author marco
 */
public class GlobalSessionManager {

    protected static GlobalSessionManager _sharedInstance;

    /**
	 * This method creates the shared instance of GlobalSessionManager if
	 * it does not yet exist.
	 * This method is called by the JFireWeb framework! You should never
	 * call it directly!
	 *
	 * @return Returns the shared instance of GlobalSessionManager.
	 * @see sharedInstance()
	 */
    public static synchronized GlobalSessionManager createSharedInstance(HttpServletRequest request) {
        if (_sharedInstance == null) _sharedInstance = new GlobalSessionManager();
        HttpSession session = request.getSession();
        GlobalSession globalSession = _sharedInstance.getGlobalSession(session.getId());
        globalSession.registerLocalSession(session);
        return _sharedInstance;
    }

    /**
	 * Use this method to obtain the shared instance of GlobalSessionManager.
	 * It must have been created with createSharedInstance(...) before!
	 * Note, that this might not work if different ClassLoaders are used.
	 *
	 * @return Returns the sharedInstance of GlobalSessionManager.
	 * @see createSharedInstance()
	 * @see createSharedInstance(boolean recreate)
	 */
    public static GlobalSessionManager sharedInstance() {
        if (_sharedInstance == null) throw new IllegalStateException("No sharedInstance of GlobalSessionManager existent! Use createSharedInstance() first and make sure, you're using the correct ClassLoader!");
        return _sharedInstance;
    }

    public GlobalSessionManager() {
    }

    /**
	 * key: String globalSessionID<br/>
	 * value: GlobalSession globalSession
	 */
    protected Map globalSessions = new HashMap();

    /**
	 * Use this method to get the global session by its ID. If there is
	 * no GlobalSession instance registered for the given ID, it will be
	 * created.
	 * @param globalSessionID
	 * @return The instance of GlobalSession for the given ID.
	 */
    public synchronized GlobalSession getGlobalSession(String globalSessionID) {
        GlobalSession globalSession = (GlobalSession) globalSessions.get(globalSessionID);
        if (globalSession == null) {
            globalSession = new GlobalSession(this, globalSessionID);
            globalSessions.put(globalSessionID, globalSession);
        }
        return globalSession;
    }

    private long sessionTimeout = 600;

    /**
	 * default is 10 h.
	 * @return Returns the sessionTimeout in minutes.
	 */
    public long getSessionTimeout() {
        return sessionTimeout;
    }

    /**
	 * Note, that you have to define the local session timeouts
	 * in web.xml:
	 * <code>
	 *   <session-config>
   *     <session-timeout>30</session-timeout>
   *   </session-config>
	 * </code>
	 *
	 * @param sessionTimeout The sessionTimeout in minutes to set.
	 */
    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }
}
