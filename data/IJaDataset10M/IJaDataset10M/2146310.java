package net.jwpa.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import net.jwpa.controller.Utils;
import net.jwpa.dao.cache.CacheUtils;

public class ContextListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent arg0) {
        try {
            CacheUtils.destroy();
            LogUtil.close();
        } catch (Throwable ex) {
            System.err.println("FATAL: " + ex);
            ex.printStackTrace(System.err);
            throw new RuntimeException(ex);
        }
    }

    public void contextInitialized(ServletContextEvent arg0) {
        try {
            Config.init(arg0.getServletContext());
            CacheUtils.init(arg0.getServletContext());
        } catch (Exception ex) {
            System.err.println("FATAL: " + ex);
            ex.printStackTrace(System.err);
            throw new RuntimeException(ex);
        }
        System.out.println("JWPA " + Utils.VERSION + " ready.");
    }
}
