package net.woodstock.rockapi.jsp.servlet.debug;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import net.woodstock.rockapi.sys.SysLogger;

public class DebugHttpSessionListener implements HttpSessionListener {

    public void sessionCreated(HttpSessionEvent event) {
        StringBuilder builder = new StringBuilder();
        builder.append("\nCreated");
        builder.append("\nID      : " + event.getSession().getId());
        builder.append("\nSource  : " + event.getSource().getClass().getCanonicalName());
        builder.append("\nContext : " + event.getSession().getServletContext().getServletContextName());
        SysLogger.getLogger().info(builder.toString());
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        StringBuilder builder = new StringBuilder();
        builder.append("\nDestroyed");
        builder.append("\nID      : " + event.getSession().getId());
        builder.append("\nSource  : " + event.getSource().getClass().getCanonicalName());
        builder.append("\nContext : " + event.getSession().getServletContext().getServletContextName());
        SysLogger.getLogger().info(builder.toString());
    }
}
