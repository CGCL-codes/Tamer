package com.liferay.portal.kernel.servlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * <a href="PortletSessionTracker.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * See http://support.liferay.com/browse/LEP-1466.
 * </p>
 *
 * @author Rudy Hilado
 *
 */
public class PortletSessionTracker implements HttpSessionBindingListener, Serializable {

    public static void add(HttpSession session) {
        _instance._add(session);
    }

    public static HttpSessionBindingListener getInstance() {
        return _instance;
    }

    public static void invalidate(String sessionId) {
        _instance._invalidate(sessionId);
    }

    public void valueBound(HttpSessionBindingEvent event) {
    }

    public void valueUnbound(HttpSessionBindingEvent event) {
        invalidate(event.getSession().getId());
    }

    private PortletSessionTracker() {
        _sessions = new HashMap();
    }

    private void _add(HttpSession session) {
        String sessionId = session.getId();
        synchronized (_sessions) {
            Set portletSessions = (Set) _sessions.get(sessionId);
            if (portletSessions == null) {
                portletSessions = new HashSet();
                _sessions.put(sessionId, portletSessions);
            }
            portletSessions.add(session);
        }
    }

    private void _invalidate(String sessionId) {
        synchronized (_sessions) {
            Set portletSessions = (Set) _sessions.get(sessionId);
            if (portletSessions != null) {
                Iterator itr = portletSessions.iterator();
                while (itr.hasNext()) {
                    HttpSession session = (HttpSession) itr.next();
                    try {
                        session.invalidate();
                    } catch (Exception e) {
                    }
                }
            }
            _sessions.remove(sessionId);
        }
    }

    private static PortletSessionTracker _instance = new PortletSessionTracker();

    private transient Map _sessions;
}
