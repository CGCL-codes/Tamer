package org.wings.comet;

import org.wings.session.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;

public class CometWingServlet extends WingServlet {

    static final String HANGING_PATH = "/hanging";

    static final String PERIODIC_POLLING_PARAM = "polling";

    Session getSession(HttpServletRequest request) throws ServletException {
        Session session = null;
        final SessionServlet sessionServlet = getSessionServlet(request, null, false);
        if (sessionServlet != null) {
            session = sessionServlet.getSession();
            SessionManager.setSession(session);
        }
        return session;
    }
}
