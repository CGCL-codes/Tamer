package org.kaleidofoundry.core.config;

import java.util.EventListener;
import java.util.LinkedHashSet;

/**
 * Configuration changes listener
 * 
 * @author Jerome RADUGET
 */
public interface ConfigurationListener extends EventListener {

    /**
    * @param evt
    * @see Configuration#setProperty(String, java.io.Serializable)
    */
    void propertyUpdate(ConfigurationChangeEvent evt);

    /**
    * @param evt
    * @see Configuration#setProperty(String, java.io.Serializable)
    */
    void propertyCreate(ConfigurationChangeEvent evt);

    /**
    * @param evt
    * @see Configuration#removeProperty(String)
    */
    void propertyRemove(ConfigurationChangeEvent evt);

    /**
    * @param events
    * @see Configuration#fireConfigurationChangesEvents()
    */
    void propertiesChanges(LinkedHashSet<ConfigurationChangeEvent> events);

    /**
    * @param source
    * @see Configuration#unload()
    */
    void configurationUnload(Configuration source);
}
