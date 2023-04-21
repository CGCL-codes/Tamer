package org.settings4j;

/**
 * Create a new instance of Settings-Interface
 * 
 * @author hbrabenetz
 */
public interface SettingsFactory {

    /**
     * The default implementation makes a new {@link org.settings4j.settings.DefaultSettings}(String name) instance
     * 
     * @param name
     * @return
     */
    SettingsInstance makeNewSettingsInstance();
}
