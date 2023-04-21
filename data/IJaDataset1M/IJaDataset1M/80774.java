package edu.tsinghua.keg.distributedlda;

import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;

/**
 * <p>Title: Configuration loader for distributed LDA. </p>
 * <p>Description: The configuration loader used in distributed LDA daemon. This loader will read the configurations saved in an XML file named as Config.xml in the user's current directory.</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: KEG, DCST, Tsinghua University</p>
 * @author CHEN Dewei
 * @version 0.1
 */
public class Configs {

    private static final String CONFIG_FILE_NAME = "config.xml";

    private static Logger log = Logger.getLogger(Configs.class);

    private Properties props = null;

    private static Configs configs;

    public static Configs getInstance() {
        if (configs == null) {
            configs = new Configs();
        }
        return configs;
    }

    private Configs() {
        String userHome = System.getProperty("user.dir");
        String fileSeperator = System.getProperty("file.separator");
        String configFileName = userHome + fileSeperator + CONFIG_FILE_NAME;
        try {
            InputStream is = new FileInputStream(configFileName);
            props = new Properties();
            props.loadFromXML(is);
            is.close();
        } catch (Exception e) {
            log.error("Error ocured while reading configurations from config file: " + configFileName, e);
        }
    }

    /**
     * Get a property in string according to the property name.
     * @param propertyName String
     * @return String
     */
    public String getProperty(String propertyName) {
        return props.getProperty(propertyName);
    }

    /**
     * Get a property in string according to the property name. If not found, the default(def) are returned.
     * @param propertyName String
     * @param def String
     * @return String
     */
    public String getProperty(String propertyName, String def) {
        return props.getProperty(propertyName, def);
    }
}
