package aos.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import aos.server.ServerProperties;

/**
 * This class manages the client properties stored in the client.properties file.
 * 
 * @author Yannic
 *
 */
public class ClientProperties extends Properties {

    private static final String FILENAME = "client.properties";

    public static final String APPLICATION_NAME = "application-name";

    public static final String HOST = "host";

    private static ClientProperties instance = null;

    Logger logger = Logger.getLogger(ServerProperties.class);

    public static Properties getInstance() {
        if (instance == null) {
            instance = new ClientProperties();
        }
        return instance;
    }

    private ClientProperties() {
        FileInputStream inf = null;
        try {
            inf = new FileInputStream(FILENAME);
            super.load(inf);
            logger.info("Client properties loaded from " + FILENAME);
        } catch (FileNotFoundException e) {
            logger.fatal(FILENAME + " has not been found. Unable to load client properties.", e);
            logger.fatal("System exit with error");
            System.exit(-1);
        } catch (IOException e) {
            logger.fatal("Unable to load properties from " + FILENAME + ".");
            logger.fatal("System exit with error");
            System.exit(-1);
        }
    }
}
