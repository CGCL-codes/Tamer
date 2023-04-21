package org.slasoi.sa.pss4slam.core;

import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Handler of AdvertisementsService configuration
 * 
 * @author Miguel Rojas (UDO)
 * 
 */
public class PSS4SlamConfig {

    private static final Logger LOGGER = Logger.getLogger(PSS4SlamConfig.class);

    /**
     * Default constructor
     */
    protected PSS4SlamConfig() {
        init();
    }

    /**
     * Singleton pattern
     */
    public static PSS4SlamConfig getInstance() {
        return self;
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    /**
     * Initializes the Configuration for the AdvertisementsService
     */
    protected void init() {
        try {
            String path = System.getenv("SLASOI_HOME");
            if (path != null && !path.equals("")) {
                String hb = path + System.getProperty("file.separator") + "service-advertisement/pss4slam/server.conf";
                LOGGER.info("PSS4SlamConfig :: loading configuration from '" + hb + "'");
                props = new Properties();
                props.load(new FileInputStream(hb));
            } else {
                LOGGER.info("PSS4SlamConfig :: loading configuration from 'classpath:/server.conf'");
                props = new Properties();
                props.load(PSS4SlamConfig.class.getResourceAsStream("/server.conf"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e);
        }
    }

    protected Properties props;

    protected static PSS4SlamConfig self = new PSS4SlamConfig();
}
