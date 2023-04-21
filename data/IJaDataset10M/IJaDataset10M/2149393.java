package net.infordata.ifw2.web.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        LocaleContext.init(sc, sc.getInitParameter("ifw2.acceptedLocales"), sc.getInitParameter("ifw2.defaultLocale"));
        SecurityContext.init(sc, sc.getInitParameter("ifw2.acceptedRoles"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
