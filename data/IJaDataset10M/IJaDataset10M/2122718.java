package com.butterfill.opb.web.listeners;

import com.butterfill.opb.data.OpbDataAccessException;
import com.butterfill.opb.session.OpbSession;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * makes sure there is an Opb session associated with every HTTP session and that
 * appropriate clean-up is performed when the session ends.
 */
public class OpbSessionListener implements HttpSessionListener {

    /**
     * The name of this class.
     */
    public static final String CLASS_NAME = OpbSessionListener.class.getName();

    /**
     * The logger of this class.
     */
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    /**
     * Gets a new OpbSession and saves as an attribute of this HTTP session
     * under the key "opbSession".
     * @param event The session event.
     */
    public void sessionCreated(final HttpSessionEvent event) {
        final String methodName = "sessionCreated(HttpSessionEvent)";
        logger.entering(CLASS_NAME, methodName);
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(event.getSession().getServletContext());
        event.getSession().setAttribute("opbSession", context.getBean("opbSession"));
    }

    /**
     * Ends the OpbSession associated with this session.
     * @param event The session event.
     */
    public void sessionDestroyed(final HttpSessionEvent event) {
        final String methodName = "sessionDestroyed(HttpSessionEvent)";
        logger.entering(CLASS_NAME, methodName);
        OpbSession opbSession = (OpbSession) event.getSession().getAttribute("opbSession");
        if (opbSession == null) {
            logger.logp(Level.WARNING, CLASS_NAME, methodName, "Opb session not found for http session");
        } else {
            try {
                opbSession.endSession();
            } catch (OpbDataAccessException ex) {
                logger.logp(Level.FINER, CLASS_NAME, methodName, "failed to end opb session", ex);
            } catch (Exception ex) {
                logger.logp(Level.WARNING, CLASS_NAME, methodName, "failed to end opb session", ex);
            }
        }
    }
}
