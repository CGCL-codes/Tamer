package com.telstra.ess.configuration.spi.builder;

import com.telstra.ess.EssComponent;
import com.telstra.ess.EssFactoryException;
import com.telstra.ess.configuration.spi.ConfigurationManagerFactory;
import com.telstra.ess.configuration.spi.ConfigurationManagerFactoryBuilder;
import com.telstra.ess.configuration.spi.properties.*;

/**
 * @author c957258
 *
 */
public class SystemConfigurationManagerFactoryBuilder implements ConfigurationManagerFactoryBuilder {

    private static final String CONF_MGR_FACTORY_PROP_NAME = "ess.configuration.factory.classname";

    /**
	 * 
	 */
    public SystemConfigurationManagerFactoryBuilder() {
        super();
    }

    public ConfigurationManagerFactory getConfigurationManagerFactory(EssComponent comp) throws EssFactoryException {
        String factoryClassName = null;
        if ((factoryClassName = System.getProperty(CONF_MGR_FACTORY_PROP_NAME)) == null) {
            return new EssPropertiesConfigurationManagerFactory();
        }
        try {
            Object obj = Class.forName(factoryClassName).newInstance();
            if (obj != null) {
                if (obj instanceof ConfigurationManagerFactory) {
                    return (ConfigurationManagerFactory) obj;
                } else {
                    return new EssPropertiesConfigurationManagerFactory();
                }
            } else {
                return new EssPropertiesConfigurationManagerFactory();
            }
        } catch (Throwable t) {
            return new EssPropertiesConfigurationManagerFactory();
        }
    }
}
