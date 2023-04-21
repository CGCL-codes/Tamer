package cz.langteacher.plugin.iface;

import cz.langteacher.model.Setting;

/**
 * DAO of settings for plugin. Each plugin can persist data in form: String pluginID, String value. 
 * @author libor
 *
 */
public interface IDataPluginConfig {

    /**
	 * Return setting for given key
	 * @param key
	 * @return value for given key
	 */
    String getSetting(String pluginID);

    /**
	 * Insert new value of property
	 * @param key
	 * @param value
	 */
    void setSetting(String pluginID, String value);

    /**
	 * Insert new setting represent by {@link Setting}
	 * @param pluginConfig
	 */
    void setSetting(Setting pluginConfig);
}
