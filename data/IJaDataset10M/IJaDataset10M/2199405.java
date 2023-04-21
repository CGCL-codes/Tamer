package suchmaschine;

import java.util.Properties;
import java.io.*;

/**
 * Propertybag
 * @author stowasser.h
 */
public class IdowaProperties {

    /**
    * Properties field. You can access them.
    */
    public Properties properties;

    private File mConfig;

    String filename = "../such_config.cfg";

    /**
    * Returns DB.database setting from config-file
    * @return String
    */
    public String database() {
        return properties.getProperty("DB.database");
    }

    /**
    * Returns DB.host setting from config-file
    * @return String
    */
    public String host() {
        return properties.getProperty("DB.host");
    }

    /**
    * Returns DB.user setting from config-file
    * @return String
    */
    public String user() {
        return properties.getProperty("DB.user");
    }

    /**
    * Returns DB.password setting from config-file
    * @return String
    */
    public String password() {
        return properties.getProperty("DB.password");
    }

    /**
    * Returns DB.port setting from config-file
    * @return String
    */
    public String port() {
        return properties.getProperty("DB.port");
    }

    /**
    * Returns idletime setting from config-file
    * @return int
    */
    public int idletime() {
        return Integer.parseInt(properties.getProperty("idletime"));
    }

    /**
    * Returns verbose setting from config-file
    * @return int
    */
    public int verbose() {
        return Integer.parseInt(properties.getProperty("verbose"));
    }

    /**
    * Returns datadir setting from config-file
    * @return String
    */
    public String datadir() {
        return properties.getProperty("datadir");
    }

    /**
    * Returns chunksize setting from config-file
    * @return int
    */
    public int chunksize() {
        return Integer.parseInt(properties.getProperty("chunksize"));
    }

    /**
    * Returns mail.from setting from config-file
    * @return String
    */
    public String mailFrom() {
        return properties.getProperty("mail.from");
    }

    /**
    * Returns mail.to setting from config-file
    * @return String
    */
    public String mailTo() {
        return properties.getProperty("mail.to");
    }

    /**
     * Stortes default-settings to config-file
     * @throws java.lang.Exception On Error
     */
    public void storeProperties() throws Exception {
        properties.setProperty("DB.database", "suche_neu");
        properties.setProperty("DB.host", "zatopek.idowa.de");
        properties.setProperty("DB.password", "CONFIG_ME");
        properties.setProperty("DB.user", "stowasser_h");
        properties.setProperty("DB.port", "");
        properties.setProperty("idletime", "500");
        properties.setProperty("verbose", "1");
        properties.setProperty("datadir", "/data/suchmaschine/");
        properties.setProperty("chunksize", "50");
        properties.setProperty("log4j.category.idowa", "DEBUG, slAppender");
        properties.setProperty("log4j.appender.slAppender", "org.apache.log4j.ConsoleAppender");
        properties.setProperty("log4j.appender.slAppender.layout", "org.apache.log4j.PatternLayout");
        properties.setProperty("log4j.appender.slAppender.layout.ConversionPattern", "%-4r [%t] %-5p %c %x - %m%n");
        properties.store(new FileOutputStream(mConfig), "Suchmaschine Konfigurationsdatei");
    }

    /**
     * Loads propertys from config-file
     * @throws java.lang.Exception On error
     */
    public void loadProperties() throws Exception {
        properties.load(new FileInputStream(mConfig));
    }

    /**
     * Constructor.
     * Initialize Properyes by loading from config file
     * Generates standard propertys if file does not exist.
     * @throws java.lang.Exception On Error
     */
    public IdowaProperties() throws Exception {
        properties = new Properties();
        mConfig = new File(filename);
        if (!mConfig.exists()) {
            storeProperties();
        } else {
            loadProperties();
        }
    }
}
