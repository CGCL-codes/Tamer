package com.w20e.socrates.servlet;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Basic implementation using cookies.
 */
public class SessionManager {

    /**
	 * Initialize this class' logging.
	 */
    private static final Logger LOGGER = Logger.getLogger(SessionManager.class.getName());

    /**
	 * Check for existence of a valid Session.
	 * 
	 * @param req
	 *            Request
	 * @return boolean indicating whether a Session exists.
	 */
    public final boolean hasSession(final HttpServletRequest req) {
        if (req.getSession(false) != null) {
            return true;
        }
        return false;
    }

    /**
	 * Create a fresh Session.
	 * 
	 * @param req
	 *            Request
	 * @return a fresh Session.
	 */
    public final HttpSession createSession(final HttpServletRequest req) {
        if (hasSession(req) && req.isRequestedSessionIdValid()) {
            try {
                req.getSession().invalidate();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Couldn't invalidate session: " + e.getMessage());
            }
        }
        HttpSession session = req.getSession();
        session.setAttribute("runnerCtx", null);
        return session;
    }

    /**
	 * Get the session for this request. The session SHOULD exist!
	 * 
	 * @param req
	 *            a <code>HttpServletRequest</code> value
	 * @return a <code>HttpSession</code> value
	 */
    public final HttpSession getSession(final HttpServletRequest req) {
        return req.getSession(false);
    }
}
