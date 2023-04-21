package org.idspace.aau.iwis.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.idspace.aau.iwis.dataaccess.HibernateUtil;

/**
 * This listener initializes and closes Hibernate on deployment 
 * and undeployment, instead of the first user request hitting 
 * the application
 * */
public class HibernateListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        HibernateUtil.getSessionFactory();
    }

    public void contextDestroyed(ServletContextEvent event) {
        HibernateUtil.getSessionFactory().close();
    }
}
