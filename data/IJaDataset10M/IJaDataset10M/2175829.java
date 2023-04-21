package ocumed.web.jsf;

import java.util.Date;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a
 * href="http://techieexchange.blogspot.com/2008/02/jsf-session-expiry-timeout-solution.html">
 * jsf session timeout validation </a>
 * 
 * @author TechieExchange
 */
public class WSessionListener implements HttpSessionListener {

    private static final Log log = LogFactory.getLog(WSessionListener.class);

    /**
     * 
     * override
     *
     * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent event) {
        log.info("session created : " + event.getSession().getId() + " at " + new Date());
    }

    /**
     * 
     * override
     *
     * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        session.invalidate();
        log.info("session destroyed :" + session.getId() + " Logging out user...");
        try {
            prepareLogoutInfoAndLogoutActiveUser(session);
        } catch (Exception e) {
            log.info("Error while logging out at session destroyed : " + e.getMessage());
        }
    }

    /**
     * Clean your logout operations.
     * @param httpSession 
     */
    public void prepareLogoutInfoAndLogoutActiveUser(HttpSession httpSession) {
    }
}
