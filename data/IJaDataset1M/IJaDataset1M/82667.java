package pl.rmalinowski.sock4log;

import java.net.BindException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * @author Rafal M.Malinowski
 */
public final class Sock4Log {

    /**
     * ; Defalut constructor.
     */
    private Sock4Log() {
    }

    /**
     * Logger instace.
     */
    private static final Logger LOGGER = Logger.getLogger(Sock4Log.class.getName());

    /**
     * Default config file name.
     */
    static final String CONFIG_FILE_NAME = "sock4log.config";

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        try {
            Configuration config = new PropertiesConfiguration(CONFIG_FILE_NAME);
            AppContext.createInstace(config);
            AppContext.getInstance().getLogPublisher();
            AppContext.getInstance().getLogServer().run();
        } catch (ConfigurationException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage(), ex.getCause());
            LOGGER.info("Hint: Check is your " + CONFIG_FILE_NAME + " is in the classpath");
        } catch (RuntimeException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage(), ex.getCause());
        }
    }
}
