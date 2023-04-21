package com.cb.web.wasf;

import com.cb.web.wasf.sample.web.WasfApplicationListener;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * @author CBO
 * @TODO : Implement session listener => application class ? session class ?
 */
public class DefaultSession implements Serializable {

    private static final String SESSION_RAN = "session";

    public static final String SESSION_ID_RPN = "sessionId";

    public static final String SESSION_MAP_SAN = "sessionMap";

    public static final String SESSION_REQUEST_ID_RAN = "sessionRequestId";

    public static final String SESSION_REQUEST_ID_RPN = SESSION_REQUEST_ID_RAN;

    static void putInRequest(HttpServletRequest request, DefaultSession session) {
        request.setAttribute(DefaultSession.SESSION_RAN, session);
    }

    public static DefaultSession getFromRequest(HttpServletRequest request) {
        return (DefaultSession) request.getAttribute(DefaultSession.SESSION_RAN);
    }

    public static DefaultSession getFromPageContext(PageContext context) {
        if (context.getRequest() instanceof HttpServletRequest) {
            return DefaultSession.getFromRequest((HttpServletRequest) context.getRequest());
        } else {
            return null;
        }
    }

    static DefaultSession resolveSession(HttpServletRequest request) {
        String sessionId = request.getParameter(SESSION_ID_RPN);
        DefaultSession session = null;
        if (sessionId != null) {
            session = getSessionsById(request).get(sessionId);
            if (session == null) {
                session = new DefaultSession();
                getSessionsById(request).put(session.getId(), session);
            } else {
                if (!"true".equals(request.getParameter(Request.ASYNC_RPN))) {
                    session.setUnSynchronized(!session.getRequestId().equals(request.getParameter(SESSION_REQUEST_ID_RPN)));
                    session.newRequest();
                }
            }
        } else {
            session = new DefaultSession();
            getSessionsById(request).put(session.getId(), session);
        }
        return session;
    }

    static DefaultSession newSession(HttpServletRequest request, DefaultSession session) {
        Map<String, DefaultSession> sessionsById = (Map<String, DefaultSession>) request.getSession().getAttribute(SESSION_MAP_SAN);
        if (sessionsById != null) {
            sessionsById.remove(session.id);
        }
        DefaultSession newSession = new DefaultSession();
        getSessionsById(request).put(newSession.getId(), newSession);
        return newSession;
    }

    private static Map<String, DefaultSession> getSessionsById(HttpServletRequest request) {
        Map<String, DefaultSession> sessionsById = (Map<String, DefaultSession>) request.getSession().getAttribute(SESSION_MAP_SAN);
        if (sessionsById == null) {
            sessionsById = new HashMap<String, DefaultSession>();
            request.getSession().setAttribute(SESSION_MAP_SAN, sessionsById);
        }
        return sessionsById;
    }

    private String id;

    private String requestId;

    private boolean unSynchronized;

    private State currentState;

    private Principal principal;

    private boolean requestDestruction;

    protected DefaultSession() {
        this.id = UUID.randomUUID().toString();
        this.requestId = this.id;
        this.unSynchronized = false;
        this.requestDestruction = false;
    }

    public String getId() {
        return id;
    }

    public String getRequestId() {
        return requestId;
    }

    boolean isUnSynchronized() {
        return unSynchronized;
    }

    private void setUnSynchronized(boolean unSynchronized) {
        this.unSynchronized = unSynchronized;
    }

    public State getCurrentState() {
        return currentState;
    }

    void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    private void newRequest() {
        this.requestId = UUID.randomUUID().toString();
    }

    public boolean isNew() {
        return id.equals(requestId);
    }

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    public void requestDestruction() {
        this.requestDestruction = true;
    }

    public boolean isRequestDestruction() {
        return requestDestruction;
    }
}
