package br.revista.listener;

import br.revista.util.JPAUtil;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class JPAStartupListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent arg0) {
        JPAUtil.startUp();
    }

    public void contextDestroyed(ServletContextEvent arg0) {
    }
}
