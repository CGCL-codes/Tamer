package net.sf.irunninglog.servlet;

import javax.servlet.http.HttpSessionEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sf.irunninglog.util.Utilities;

/**
 * Listener for events related to changes to the list of active sessions in the
 * web application.  This listener will receive notification and will respond
 * accordingly whenever a session is created or destroyed.
 *
 * @author <a href="mailto:allan_e_lewis@yahoo.com">Allan Lewis</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2005/06/23 01:48:57 $
 * @since iRunningLog 1.0
 */
public class HttpSessionListener implements javax.servlet.http.HttpSessionListener {

    /** <code>Log</code> instance for this class. */
    private static final Log LOG = LogFactory.getLog(HttpSessionListener.class);

    /**
     * Respond to the creation of a session.
     *
     * @param event The notification event
     */
    public void sessionCreated(HttpSessionEvent event) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("sessionCreated: Created a session " + Utilities.toString(event.getSession()));
        }
    }

    /**
     * Respond to the destruction of a session.
     *
     * @param event The notification event
     */
    public void sessionDestroyed(HttpSessionEvent event) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("sessionDestroyed: Destroyed a session " + Utilities.toString(event.getSession()));
        }
    }
}
