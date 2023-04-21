package br.net.woodstock.rockframework.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import br.net.woodstock.rockframework.web.config.WebLog;

public abstract class AbstractServletContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        WebLog.getInstance().getLog().info("Destroying context " + event.getServletContext().getServletContextName());
    }

    @Override
    public void contextInitialized(final ServletContextEvent event) {
        WebLog.getInstance().getLog().info("Initializing context " + event.getServletContext().getServletContextName());
    }
}
