package medical.wicket.app;

import java.io.File;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.util.Log4jConfigurator;
import org.modelibra.util.PathLocator;
import org.mortbay.jetty.Server;

/**
 * Jetty server startup class. 
 */
public class Start {

    private static Log log = LogFactory.getLog(Start.class);

    private static final String CLASS_DIRECTORY = "classes";

    private static final String CONFIG_DIRECTORY = "config";

    private static final String JETTY_CONFIG = "jetty-config.xml";

    private static final String LOG_CONFIG = "log4j.xml";

    private static String findJettyConfigPath() {
        String jettyConfigPath = null;
        PathLocator pathLocator = new PathLocator();
        String log4jConfigPath = pathLocator.findAbsolutePath(Start.class, CLASS_DIRECTORY, CONFIG_DIRECTORY + File.separator + LOG_CONFIG);
        Log4jConfigurator log4jConfigurator = new Log4jConfigurator(log4jConfigPath);
        log4jConfigurator.configure();
        jettyConfigPath = pathLocator.findAbsolutePath(Start.class, CLASS_DIRECTORY, CONFIG_DIRECTORY + File.separator + JETTY_CONFIG);
        return jettyConfigPath;
    }

    public static void main(String[] args) {
        Server jettyServer = null;
        try {
            URL jettyConfig = new URL("file:" + findJettyConfigPath());
            if (jettyConfig != null) {
                jettyServer = new Server(jettyConfig);
                jettyServer.start();
            }
        } catch (Exception e) {
            log.fatal("Could not start the Jetty server: " + e);
        }
    }
}
