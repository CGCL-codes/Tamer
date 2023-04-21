package com.habitton.startweb;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoaderListener;

/**
 * Listener de arranque que crea el contexto de Spring
 *  e inicializa listas est�ticas de los combos
 */
public class StartupListener extends ContextLoaderListener implements ServletContextListener {

    private static final Log log = LogFactory.getLog(StartupListener.class);

    public void contextInitialized(ServletContextEvent event) {
        if (log.isInfoEnabled()) log.info("Inicializando el contexto...");
        super.contextInitialized(event);
        if (log.isInfoEnabled()) log.info("Contexto obtenido...");
    }
}
