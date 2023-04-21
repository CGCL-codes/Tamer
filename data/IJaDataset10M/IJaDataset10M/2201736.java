package net.woodstock.rockapi.jsp.listener.debug;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import net.woodstock.rockapi.jsp.listener.BaseListener;

public class SessionListener extends BaseListener implements HttpSessionListener {

    private static int count;

    public void sessionCreated(HttpSessionEvent se) {
        SessionListener.count++;
        this.getLogger().info("Created session for class " + se.getSource().getClass().getName());
        this.getLogger().info("Active sessions " + SessionListener.count);
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        SessionListener.count--;
        this.getLogger().info("Destroyed session for class " + se.getSource().getClass().getName());
        this.getLogger().info("Active sessions " + SessionListener.count);
    }

    public static int getSessionCount() {
        return SessionListener.count;
    }
}
