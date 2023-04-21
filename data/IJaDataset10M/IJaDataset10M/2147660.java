package com.meterware.servletunit;

import javax.servlet.ServletContext;
import java.util.*;
import java.util.Hashtable;

class ServletUnitContext {

    private SessionListenerDispatcher _listenerDispatcher;

    private ServletContext _servletContext;

    ServletUnitContext(String contextPath, ServletContext servletContext, SessionListenerDispatcher dispatcher) {
        _servletContext = servletContext;
        _contextPath = (contextPath != null ? contextPath : "");
        _listenerDispatcher = dispatcher;
    }

    Set getSessionIDs() {
        return _sessions.keySet();
    }

    /**
     * Returns an appropriate session for a request. If no cached session is
     * @param sessionId
     * @param session the session cached by previous requests. May be null.
     * @param create
     * @return
     */
    ServletUnitHttpSession getValidSession(String sessionId, ServletUnitHttpSession session, boolean create) {
        if (session == null && sessionId != null) {
            session = getSession(sessionId);
        }
        if (session != null && session.isInvalid()) {
            session = null;
        }
        if (session == null && create) {
            session = newSession();
        }
        return session;
    }

    /**
     * Returns the session with the specified ID, if any.
     **/
    ServletUnitHttpSession getSession(String id) {
        return (ServletUnitHttpSession) _sessions.get(id);
    }

    /**
     * Creates a new session with a unique ID.
     **/
    ServletUnitHttpSession newSession() {
        ServletUnitHttpSession result = new ServletUnitHttpSession(_servletContext, _listenerDispatcher);
        _sessions.put(result.getId(), result);
        _listenerDispatcher.sendSessionCreated(result);
        return result;
    }

    /**
     * Returns the contextPath
     */
    String getContextPath() {
        return _contextPath;
    }

    private Hashtable _sessions = new Hashtable();

    private String _contextPath = null;
}
