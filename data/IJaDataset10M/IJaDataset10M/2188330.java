package br.com.wepa.webapps.orca.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import br.com.wepa.webapps.logger.TraceLogger;
import br.com.wepa.webapps.orca.logica.persistencia.PersistenceDelegator;

/**
 * @author Fabricio
 *
 */
public class ContextListener implements ServletContextListener {

    protected static TraceLogger logger = new TraceLogger(ContextListener.class);

    private static final String BARRA = "################################################################" + "################################################################";

    private static final String EOL = System.getProperty("line.separator");

    ;

    public void contextInitialized(ServletContextEvent evt) {
        logger.log(EOL + BARRA + EOL + EOL + "               Iniciando Orca" + EOL + EOL + BARRA + EOL + EOL);
        PersistenceDelegator.getHibernatePersistence().connect();
        logger.log(EOL + BARRA + EOL + EOL + "              Orca Inicialzado com Sucesso " + EOL + EOL + BARRA + EOL + EOL);
    }

    public void contextDestroyed(ServletContextEvent evt) {
        try {
            logger.log(EOL + BARRA + EOL + EOL + "               Finalizando Orca" + EOL + EOL + BARRA + EOL + EOL);
            PersistenceDelegator.getHibernatePersistence().disconnect();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        logger.log(EOL + BARRA + EOL + EOL + "              Orca Finalizado" + EOL + EOL + BARRA + EOL + EOL);
    }
}
