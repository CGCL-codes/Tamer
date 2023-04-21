package org.caleigo.security;

import java.util.*;
import java.lang.reflect.*;
import org.caleigo.toolkit.log.*;

public class SessionHandler {

    private static SessionHandler sInstance;

    private static Class mSessionClass;

    public static SessionHandler getInstance() {
        if (sInstance == null) sInstance = new SessionHandler();
        return sInstance;
    }

    private int mCurrentSessionID;

    protected HashMap mSessions;

    private SessionHandler() {
        mSessions = new HashMap(20);
        setSessionClass(ISession.DefaultSession.class);
    }

    /** Creates a new session and associates a session objetc with it.
     */
    public synchronized void createSession(UserInfo userInfo) {
        int sessionID = this.generateSessionID();
        mSessions.put(new Integer(sessionID), createSession(userInfo, sessionID));
        userInfo.setSessionID(sessionID);
    }

    public synchronized void destroySession(int sessionID) {
        mSessions.remove(new Integer(sessionID));
    }

    /** Returns an ISessesion object for the provided session id, or <code>null</code>
     * if no valid session exists. Note that invoker of this method should not
     * cache the session object since it may be invalidated at any time.
     */
    public synchronized ISession getSession(int sessionID) {
        ISession session = (ISession) mSessions.get(new Integer(sessionID));
        if (session != null && session.isValid()) return session;
        return null;
    }

    public synchronized boolean isValidSession(UserInfo userInfo, int sessionID) {
        ISession session = (ISession) mSessions.get(new Integer(sessionID));
        if (session != null && session.isValid() && session.getUserInfo().getUserID().compareTo(userInfo.getUserID()) == 0 && session.getUserInfo().getSessionID() == sessionID) return true;
        return false;
    }

    public synchronized void setSessionClass(Class sessionClass) {
        if (!ISession.class.isAssignableFrom(sessionClass)) throw new IllegalArgumentException("The class " + sessionClass + " does not implement ISession");
        mSessionClass = sessionClass;
    }

    protected synchronized int generateSessionID() {
        return ++mCurrentSessionID;
    }

    protected ISession createSession(UserInfo userInfo, int sessionID) {
        try {
            Constructor constructor = mSessionClass.getConstructor(new Class[] { UserInfo.class, Integer.TYPE });
            ISession session = (ISession) constructor.newInstance(new Object[] { userInfo, new Integer(sessionID) });
            return session;
        } catch (Exception e) {
            Log.printError(this, "Could not create session", e);
            throw new RuntimeException("Error while creating a session object", e);
        }
    }
}
