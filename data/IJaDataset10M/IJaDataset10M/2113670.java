package gpsxml;

import gpsxml.io.ConfigReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Returns a single access point for the configuration file values.
 * @author PLAYER, Keith Ralph
 */
public class ParameterMapSingleton {

    static Logger logger = Logger.getLogger(gpsxml.ParameterMapSingleton.class.getName());

    private static final String CONFIGPATH = "configuration/config.txt";

    private static Map<String, String> parameterMapInstance = null;

    /**
     * Creates a new instance of ParameterMapSingleton
     */
    public ParameterMapSingleton() {
    }

    /**
     *  Returns the configuration parameters for this application.
     *  throws File IO exceptions.
     */
    public static Map<String, String> getParameterMapInstance() {
        if (parameterMapInstance == null) {
            try {
                ConfigReader configReader = new ConfigReader();
                parameterMapInstance = configReader.getParameters(CONFIGPATH);
            } catch (FileNotFoundException ex) {
                logger.error("Config file not found");
            } catch (IOException ex) {
                logger.error("Problem with the config file", ex);
            }
        }
        return parameterMapInstance;
    }
}
