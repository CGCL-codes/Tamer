package fr.albin.data;

import java.io.File;
import java.util.Locale;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import fr.albin.data.model.Item;

/**
 * Manages the configuration file using apache commons configuration.
 * 
 * @author avigier
 * 
 */
public class MainConfiguration {

    private MainConfiguration(File file) throws ConfigurationException {
        this.conf = new PropertiesConfiguration(file);
    }

    public String getProxyAddress() {
        return this.conf.getString(KEY_PROXY_ADDRESS);
    }

    public int getProxyPort() {
        return this.conf.getInt(KEY_PROXY_PORT);
    }

    public int getConnectionTimeout() {
        return this.conf.getInt(KEY_CONNECTION_TIMEOUT);
    }

    public String getHttpUserAgent() {
        return this.conf.getString(KEY_HTTP_USER_AGENT);
    }

    public String getLocale() {
        return this.conf.getString(KEY_LOCALE, Locale.ENGLISH.toString());
    }

    @SuppressWarnings("unchecked")
    public Class<Item> getItemClass() {
        Class<Item> result = null;
        String className = this.conf.getString(KEY_APPLICATION_ITEM_CLASS);
        try {
            result = (Class<Item>) Class.forName(className);
        } catch (Exception e) {
            LOGGER.fatal("No item class is defined, this is mandatory", e);
        }
        return result;
    }

    public static MainConfiguration getInstance() {
        if (instance == null) {
            try {
                instance = new MainConfiguration(new File(DEFAULT_FILE_NAME));
            } catch (ConfigurationException e) {
                LOGGER.fatal("Cannot load the main configuration file : " + DEFAULT_FILE_NAME + ". EXITING THE APPLICATION !!", e);
                System.exit(1);
            }
        }
        return instance;
    }

    private static final String DEFAULT_FILE_NAME = "conf/conf.properties";

    private static final String KEY_LOCALE = "locale";

    private static final String KEY_PROXY_ADDRESS = "proxy.address";

    private static final String KEY_PROXY_PORT = "proxy.port";

    private static final String KEY_CONNECTION_TIMEOUT = "connection.timeout";

    private static final String KEY_HTTP_USER_AGENT = "http.userAgent";

    private static final String KEY_APPLICATION_ITEM_CLASS = "application.item.class";

    private static Logger LOGGER = Logger.getLogger(MainConfiguration.class);

    private static MainConfiguration instance;

    private PropertiesConfiguration conf;
}
