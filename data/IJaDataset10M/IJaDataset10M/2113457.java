package net.sf.sql2java.common.configuration;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;

public abstract class Configurer<T> {

    private Configuration configuration;

    public abstract void configure(T subject) throws ConfigurationException;

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
