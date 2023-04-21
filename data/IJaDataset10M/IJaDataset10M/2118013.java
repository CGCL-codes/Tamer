package com.softwarementors.extjs.djn.servlet.config;

import com.softwarementors.extjs.djn.DirectJNgineException;
import com.softwarementors.extjs.djn.servlet.ServletRegistryConfigurator;

public class RegistryConfigurationException extends DirectJNgineException {

    private static final long serialVersionUID = -7479927238508694423L;

    protected RegistryConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static RegistryConfigurationException forUnableToInstantiateRegistryConfigurator(Class<? extends ServletRegistryConfigurator> configuratorClass, Throwable cause) {
        assert configuratorClass != null;
        assert cause != null;
        return new RegistryConfigurationException("Unable to create a servlet registry configurator of type '" + configuratorClass + "'", cause);
    }
}
