package org.wynnit.minows;

import java.util.EventListener.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Date;
import javax.ejb.*;
import javax.naming.*;

/**
 *
 * @author steve
 */
public class MyLifeCycleEventExample implements ServletContextListener {

    /** Creates a new instance of MyLifeCycleEventExample */
    public MyLifeCycleEventExample() {
    }

    ServletContext servletContext;

    public void contextInitialized(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        try {
            HouseKeepingLocal keeper = lookupHouseKeepingBean();
            ActionLocal al = lookupActionBean();
            keeper.StartTimer(1000, "HouseKeeper");
            al.startTimer(5000, "Actions");
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }

    protected void log(String msg) {
        System.out.println("[" + getClass().getName() + "] " + msg);
    }

    private org.wynnit.minows.ActionLocal lookupActionBean() {
        try {
            javax.naming.Context c = new javax.naming.InitialContext();
            org.wynnit.minows.ActionLocalHome rv = (org.wynnit.minows.ActionLocalHome) c.lookup("java:comp/env/ejb/ActionBean");
            return rv.create();
        } catch (javax.naming.NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        } catch (javax.ejb.CreateException ce) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ce);
            throw new RuntimeException(ce);
        }
    }

    private org.wynnit.minows.HouseKeepingLocal lookupHouseKeepingBean() {
        try {
            javax.naming.Context c = new javax.naming.InitialContext();
            org.wynnit.minows.HouseKeepingLocalHome rv = (org.wynnit.minows.HouseKeepingLocalHome) c.lookup("java:comp/env/ejb/HouseKeepingBean");
            return rv.create();
        } catch (javax.naming.NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        } catch (javax.ejb.CreateException ce) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ce);
            throw new RuntimeException(ce);
        }
    }
}
