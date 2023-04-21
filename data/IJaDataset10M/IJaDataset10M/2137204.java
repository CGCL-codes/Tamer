package edu.indiana.extreme.www.xgws.msgbox;

import java.util.Properties;

public class ConfigurationManager {

    protected Properties configurations = new Properties();

    public ConfigurationManager(String configFileName) {
        try {
            configurations.load(this.getClass().getClassLoader().getResourceAsStream("/" + configFileName));
        } catch (Exception e) {
            throw new RuntimeException("unable to load configurations", e);
        }
    }

    public String getConfig(String configName) {
        return configurations.getProperty(configName);
    }

    public String getConfig(String configName, String defaultVal) {
        return configurations.getProperty(configName, defaultVal);
    }
}
