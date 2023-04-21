package org.nakedobjects.nos.client.web.servlet;

import org.nakedobjects.noa.security.Session;
import org.nakedobjects.noa.security.SessionManager;
import org.nakedobjects.nof.core.security.ExplorationSession;
import org.nakedobjects.nof.core.security.PasswordAuthenticationRequest;
import org.nakedobjects.nos.client.web.html.HtmlComponentFactory;
import org.nakedobjects.nos.client.web.request.Context;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class SystemAccess {

    private static final String NOF_SESSION_ATTRIBUTE = "nof-context";

    private static final Logger LOG = Logger.getLogger(SystemAccess.class);

    private static SystemAccess instance;

    public static void addSession(HttpSession session) {
        instance.sessions.add(session);
        LOG.debug("session started " + session);
    }

    public static Session authenticate(PasswordAuthenticationRequest passwordAuthenticationRequest) {
        if (showExploration()) {
            Session session = new ExplorationSession();
            Context context = new Context(new HtmlComponentFactory());
            context.setSession(session);
            return session;
        }
        return getManager().authenticate(passwordAuthenticationRequest);
    }

    public static boolean authenticateUser() {
        return instance.authenticateUser;
    }

    public static void endRequest(final Session session) {
        getManager().disconnect(session);
    }

    public static String listSessions() {
        StringBuffer str = new StringBuffer();
        Iterator it = instance.sessions.iterator();
        while (it.hasNext()) {
            HttpSession session = (HttpSession) it.next();
            String id = session.getId();
            str.append(id);
            str.append(" \t");
            long creationTime = session.getCreationTime();
            str.append(new Date(creationTime));
            str.append(" \t");
            long lastAccessedTime = session.getLastAccessedTime();
            str.append(new Date(lastAccessedTime));
            str.append(" \t");
            Session nofSession = (Session) session.getAttribute("NOF_SESSION_ATTRIBUTE");
            if (nofSession != null) {
                str.append(nofSession.getUserName());
            }
            str.append("\n");
        }
        return str.toString();
    }

    public static void logoffUser(final Session session) {
        getManager().logoff(session);
    }

    public static SessionManager getManager() {
        if (instance == null) {
            throw new IllegalStateException("Server initialisation failed, or not defined as a context listener");
        }
        return instance.manager;
    }

    public static boolean showExploration() {
        return instance.showExploration;
    }

    public static void startRequest(final Session session) {
        getManager().connect(session);
    }

    public static void removeSession(HttpSession session) {
        LOG.debug("session ended " + session);
        instance.sessions.remove(session);
        Context context = (Context) session.getAttribute(NOF_SESSION_ATTRIBUTE);
        Session nofSession = context.getSession();
        if (nofSession != null) {
            logoffUser(nofSession);
        }
    }

    private final SessionManager manager;

    private final boolean showExploration;

    private final boolean authenticateUser;

    private final List sessions = new ArrayList();

    public SystemAccess(final SessionManager manager) {
        this(manager, true, false);
    }

    public SystemAccess(final SessionManager manager, final boolean authenticateUser, final boolean showExploration) {
        this.manager = manager;
        this.authenticateUser = authenticateUser;
        this.showExploration = showExploration;
        SystemAccess.instance = this;
    }
}
