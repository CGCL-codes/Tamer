package org.codehaus.groovy.grails.plugins.searchable.compass.config;

import org.compass.core.config.CompassConfiguration;
import java.util.Map;

/**
 * Configures Compass
 *
 * @author Maurice Nicholson
 */
public interface SearchableCompassConfigurator {

    /**
     * Configure Compass ready for it to be built
     * 
     * @param compassConfiguration runtime configuration instance
     * @param configurationContext a context allowing flexible parameter passing
     */
    void configure(CompassConfiguration compassConfiguration, Map configurationContext);
}
