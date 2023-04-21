package org.openremote.controller.utils;

import org.openremote.controller.Configuration;
import org.openremote.controller.spring.SpringContext;

/**
 * A factory for creating Configuration objects.
 * 
 * @author Dan 2009-6-9
 */
public class ConfigFactory {

    /**
    * Gets the config.
    * 
    * @return the config
    */
    public static Configuration getConfig() {
        return (Configuration) SpringContext.getInstance().getBean("configuration");
    }
}
