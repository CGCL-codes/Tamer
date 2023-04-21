package persistence;

import domain.ServerConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * This class imports HTTPDragon's configuration settings.
 * @author HTTPDragon Team
 */
@SuppressWarnings("LoggerStringConcat")
public class ImportConfig {

    private static final Logger logger = Logger.getLogger("HTTPDragon Log");

    private ServerConfig currentConfig;

    public ImportConfig(File configFile) {
        Properties dprops = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(configFile);
            dprops.loadFromXML(is);
            is.close();
        } catch (FileNotFoundException ex) {
            logger.warning(configFile.getPath() + " was not found.");
        } catch (IOException ex) {
            logger.warning("Couldn't open " + configFile.getPath());
        } catch (Exception ex) {
            logger.warning("Couldn't open " + configFile.getPath());
        }
        currentConfig = new ServerConfig(dprops.getProperty("http.run", "true").toLowerCase().equals("true") ? true : false, dprops.getProperty("http.path", "./html"), dprops.getProperty("http.index", "index.html"), Integer.parseInt(dprops.getProperty("http.port", "80")), dprops.getProperty("https.run", "false").toLowerCase().equals("true") ? true : false, dprops.getProperty("https.path", "./html"), dprops.getProperty("https.index", "index.html"), Integer.parseInt(dprops.getProperty("https.port", "443")), dprops.getProperty("https.keyFile", "serverkey"), dprops.getProperty("php.run", "false").toLowerCase().equals("true") ? true : false, dprops.getProperty("php.exec", null));
    }

    /**
     * This method returns the currently loaded configuration.
     * @return ServerConfig The current server configuration.
     */
    public ServerConfig getConfig() {
        return currentConfig;
    }
}
