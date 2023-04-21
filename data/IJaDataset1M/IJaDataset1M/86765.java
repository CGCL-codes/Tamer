package com.socialnetworkshirts.twittershirts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;
import java.io.IOException;

/**
 * @author mbs
 */
public class Configuration {

    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    private static final Configuration INSTANCE = new Configuration();

    private static Properties properties = null;

    public static Configuration getInstance() {
        return INSTANCE;
    }

    public String getSpreadshirtAPIKey() {
        return getProperties().getProperty("spreadshirtAPIKey");
    }

    public String getSpreadshirtSecret() {
        return getProperties().getProperty("spreadshirtSecret");
    }

    public String getSpreadshirtAPI() {
        return getProperties().getProperty("spreadshirtAPI");
    }

    public String getSpreadshirtShop() {
        return getProperties().getProperty("spreadshirtShop");
    }

    public String getTwitterUserId() {
        return getProperties().getProperty("twitterUserId");
    }

    public String getTwitterPassword() {
        return getProperties().getProperty("twitterPassword");
    }

    private Properties getProperties() {
        if (properties == null) {
            try {
                Properties properties = new Properties();
                properties.load(this.getClass().getResourceAsStream("/configuration.properties"));
                this.properties = properties;
            } catch (IOException e) {
                log.error("Could not load properties!", e);
            }
        }
        return properties;
    }
}
