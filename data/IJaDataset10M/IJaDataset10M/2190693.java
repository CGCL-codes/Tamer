package com.simplenix.nicasio.sys;

import com.simplenix.nicasio.hmaint.EntityFactory;
import com.simplenix.nicasio.navig.TrayManager;
import com.simplenix.nicasio.persistence.HibernateUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 * @author fronald
 */
public class InitListener implements ServletContextListener {

    private Logger log = Logger.getLogger(getClass().getName());

    public void contextInitialized(ServletContextEvent sc) {
        log.info("Starting Nicasio...");
        try {
            log.info("Starting Hibernate Persistence...");
            HibernateUtil.getSessionFactory();
            log.info("Hibernate is ok!");
            log.info("Starting Nicasio System Definitions...");
            SystemDef.createInstance(sc.getServletContext());
            log.info("Nicasio System Definitions is ok!");
            log.log(Level.INFO, "Application real path: " + SystemDef.getInstance().getAppPath());
            log.log(Level.INFO, "Application context path: " + SystemDef.getInstance().getContextPath());
            log.info("Starting Tasks of Nicasio Modules...");
            InitTaskManager.getInstance().executeAll();
            log.info("Nicasio Tasks of Modules are ok!");
            log.info("Starting HMaint EntityDefinitions...");
            EntityFactory.getInstance();
            log.info("HMaint EntityDefinitions are ok!");
            log.info("Starting Tray Manager...");
            TrayManager.getInstance();
            log.info("Tray Manager is ok!");
            log.info("Application is started.");
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error on init Nicasio.");
            ex.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent sc) {
        log.info("Shutdown Nicasio...");
        try {
            log.info("Close Hibernate session and session factory...");
            HibernateUtil.getCurrentSession().close();
            HibernateUtil.getSessionFactory().close();
            log.info("Closed.");
            log.info("Running Shutdown tasks of Nicasio Modules...");
            ShutdownTaskManager.getInstance().executeAll();
            log.info("Shutdown Tasks of Modules are ok!");
            log.info("Bye bye.");
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error on shutdown Nicasio.");
            ex.printStackTrace();
        }
    }
}
