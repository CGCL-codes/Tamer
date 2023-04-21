package org.oclc.da.ndiipp.struts.core.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.oclc.da.logging.Logger;
import org.oclc.da.ndiipp.helper.SystemHelper;

/**
 * This class is intended to track sessions and clean up when users log out.
 * <P>
 * @author Joseph Nelson
 */
public class SessionManager implements HttpSessionListener {

    /**
     * Logger instance.
     */
    protected Logger logger = Logger.newInstance();

    /**
     * The current servlet context.
     */
    private ServletContext context = null;

    /**
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSessionListener#sessionCreated(
     *      javax.servlet.http.HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent event) {
        logger.trace(this, "sessionCreated", "Entering");
        if (context == null) {
            storeInServletContext(event);
        }
    }

    /**
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(
     *      javax.servlet.http.HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent event) {
        logger.trace(this, "sessionDestroyed", "Entering");
        HttpSession session = event.getSession();
        SystemHelper sh = (SystemHelper) session.getAttribute("SystemHelper");
        if (sh != null) {
            String user = (String) session.getAttribute("user");
            sh.logout();
            logger.trace(this, "sessionDestroyed", user + " has logged out.");
        }
        session.invalidate();
    }

    /**
     * Register self in the servlet context so that servlets and JSP pages can
     * access the session counts.
     * <P>
     * @param event The event associated with the new session.
     */
    private void storeInServletContext(HttpSessionEvent event) {
        logger.trace(this, "storeInServletContext", "Entering");
        HttpSession session = event.getSession();
        context = session.getServletContext();
        context.setAttribute("sessionCounter", this);
    }
}
