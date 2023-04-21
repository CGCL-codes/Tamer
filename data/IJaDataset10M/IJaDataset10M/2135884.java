package com.projeto.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        ConverterDirector.getInstance().encerraThreads();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
    }
}
