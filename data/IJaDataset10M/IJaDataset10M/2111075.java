package it.gentlewebsite.portal.listeners;

import java.io.UnsupportedEncodingException;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 * @author campanini
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PortalRequestListener implements ServletRequestListener {

    private static final String loggerName = "it.gentlewebsite.portal.listeners.PortalRequestListener";

    public synchronized void requestInitialized(ServletRequestEvent reqEvent) {
        Logger.getLogger(loggerName).debug("requestInitialized called");
        HttpServletRequest request = (HttpServletRequest) reqEvent.getServletRequest();
        try {
            request.setCharacterEncoding("iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            Logger.getLogger(loggerName).error("Problems setting the request Char Encoding", e);
        }
        Logger.getLogger(loggerName).debug("request.isUserInRole(editor): " + request.isUserInRole("editor"));
        Logger.getLogger(loggerName).debug("request.isUserInRole(exteditor): " + request.isUserInRole("exteditor"));
        Logger.getLogger(loggerName).debug("requestInitialized called");
    }

    public void requestDestroyed(ServletRequestEvent arg0) {
        Logger.getLogger(loggerName).debug("requestDestroyed called");
    }
}
