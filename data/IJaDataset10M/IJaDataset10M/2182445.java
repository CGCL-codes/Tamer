package org.toobsframework.social.config;

import java.util.Properties;

public class Config {

    protected Properties properties;

    public Config() {
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String paramValue = this.getProperty(key, null);
        if (paramValue == null || paramValue.length() == 0) {
            return defaultValue;
        }
        if (paramValue.equalsIgnoreCase("true")) {
            return true;
        }
        if (paramValue.equalsIgnoreCase("yes")) {
            return true;
        }
        if (paramValue.equalsIgnoreCase("on")) {
            return true;
        }
        if (paramValue.equalsIgnoreCase("1")) {
            return true;
        }
        return false;
    }

    public boolean isComponentEnabled(String componentId) {
        return getBooleanProperty("component." + componentId, true);
    }

    public boolean isLayoutEnabled(String componentId) {
        return getBooleanProperty("layout." + componentId, true);
    }
}
