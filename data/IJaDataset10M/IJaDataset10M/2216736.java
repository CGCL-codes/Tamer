package com.gampire.pc.config;

import com.gampire.pc.json.JSONBeanFactory;

public class Configuration {

    private static final String CONFIGURATION_FILE_PATH = "data/configuration.txt";

    private static Configuration instance;

    public static Configuration getInstance() {
        return instance;
    }

    private Configuration(ConfigurationBean configurationBean) {
        super();
        this.configurationBean = configurationBean;
    }

    private ConfigurationBean configurationBean;

    static {
        ConfigurationBean configurationBean = (ConfigurationBean) JSONBeanFactory.createBean(ConfigurationBean.class, CONFIGURATION_FILE_PATH);
        instance = new Configuration(configurationBean);
    }

    public boolean isShadowMode() {
        return configurationBean.isShadowMode();
    }

    public boolean isSilentMode() {
        return configurationBean.isSilentMode();
    }

    public boolean isQuickTimeOnly() {
        return configurationBean.isQuickTimeOnly();
    }
}
