package org.wings.portlet;

import javax.portlet.RenderRequest;
import javax.swing.event.EventListenerList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.session.Session;
import org.wings.session.SessionManager;

/**
 * This class provides the actual portlet window state as events. The static
 * Strings MAXIMIZED_WS, MINIMIZED_WS and NORMAL_WS are provided for comparing
 * with the actual window state. Also custom window states can be used. To get
 * an instance use getInstance() instead of the cunstrucor which is hidden. For
 * getting notified about a window state change use the
 * addWindowStateChangeListner Method
 * 
 * @author <a href="mailto:marc.musch@mercatis.com">Marc Musch</a>
 * 
 */
public class WindowStateProvider {

    private static final transient Log log = LogFactory.getLog(WindowStateProvider.class);

    /**
	 * The String maximized
	 */
    public static final String MAXIMIZED_WS = "maximized";

    /**
	 * The String minimized
	 */
    public static final String MINIMIZED_WS = "minimized";

    /**
	 * The String normal
	 */
    public static final String NORMAL_WS = "normal";

    /**
	 * Listeners intereted in an event fired if the window state changes
	 */
    private EventListenerList listeners = new EventListenerList();

    /**
	 * The old window state, used so that only events are fired, if the window
	 * state really has changed
	 */
    private String oldWindowState = NORMAL_WS;

    private WindowStateProvider() {
    }

    /**
	 * Use this method to get an instance of this class, the constructir is
	 * hidden.
	 * 
	 * @return Instance of this class
	 */
    public static WindowStateProvider getInstance() {
        Session session = SessionManager.getSession();
        WindowStateProvider pwsp = (WindowStateProvider) session.getProperty(Const.WINGS_SESSION_PROPERTY_WINDOW_STATE_PROVIDER);
        if (pwsp != null) {
            return pwsp;
        } else {
            WindowStateProvider newPwsp = new WindowStateProvider();
            session.setProperty(Const.WINGS_SESSION_PROPERTY_WINDOW_STATE_PROVIDER, newPwsp);
            return newPwsp;
        }
    }

    /**
	 * Adds a new listner for window state changes
	 * 
	 * @param listener -
	 *            the listner for these events
	 */
    public void addWindowStateChangeListener(WindowStateListener listener) {
        listeners.add(WindowStateListener.class, listener);
    }

    /**
	 * Forces the delivering of the window change event, but only if the window
	 * state really has changed
	 */
    public void updateWindowState() {
        Session session = SessionManager.getSession();
        RenderRequest request = (RenderRequest) session.getProperty(Const.WINGS_SESSION_PROPERTY_RENDER_REQUEST);
        if (request != null) {
            String ws = request.getWindowState().toString();
            log.debug("WingS-Portlet-Bridge: providing window state \"" + ws + "\"");
            if (ws != null && !ws.equals(oldWindowState)) {
                oldWindowState = ws;
                notifyWindowState(new WindowStateEvent(this, ws));
            }
        }
    }

    /**
	 * Notifies the listeners
	 * 
	 * @param e -
	 *            the event to deliver
	 */
    private synchronized void notifyWindowState(WindowStateEvent e) {
        for (WindowStateListener l : listeners.getListeners(WindowStateListener.class)) l.windowStateChanged(e);
    }
}
