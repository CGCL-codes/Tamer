package org.parosproxy.paros.core.scanner;

import org.apache.commons.configuration.Configuration;
import org.parosproxy.paros.network.HttpMessage;

/**
 * This interface must be implemented by a Plugin for running the checks. The
 * AbstractHostPlugin, AbstractAppPlugin, AbstractAppParamPlugin implement this
 * interface and is a good starting point for writing new plugins.
 * 
 */
public interface Plugin extends Runnable {

    /**
	 * Unique Paros ID of this plugin.
	 * 
	 * @return
	 */
    public int getId();

    /**
	 * Plugin name. This is the human readable plugin name for display.
	 * 
	 * @return
	 */
    public String getName();

    /**
	 * Code name is the plugin name used for dependency naming. By default this
	 * is the class name (without the package prefix).
	 * 
	 * @return
	 */
    public String getCodeName();

    /**
	 * Default description of this plugin.
	 * 
	 * @return
	 */
    public String getDescription();

    public void init(HttpMessage msg, HostProcess parent);

    public void scan();

    /**
	 * Array of dependency of this plugin. This plugin will start running until
	 * all the dependency completed running. The dependency is the class name.
	 * 
	 * @return null if there is no dependency.
	 */
    public String[] getDependency();

    /**
	 * Enable/disable this plugin.
	 * 
	 * @param enabled
	 */
    public void setEnabled(boolean enabled);

    /**
	 * Return if this plugin is enabled.
	 * 
	 * @return true = enabled.
	 */
    public boolean isEnabled();

    /**
	 * The Category of this plugin. See Category.
	 * 
	 * @return
	 */
    public int getCategory();

    /**
	 * Default solution returned by this plugin.
	 * 
	 * @return
	 */
    public String getSolution();

    /**
	 * Reference document provided by this plugin.
	 * 
	 * @return
	 */
    public String getReference();

    /**
	 * Plugin must implement this to notify when completed.
	 * 
	 */
    public void notifyPluginCompleted(HostProcess parent);

    /**
	 * Always true - if plugin is visible to the framework.
	 * 
	 * @return
	 */
    public boolean isVisible();

    public void setConfig(Configuration config);

    public Configuration getConfig();

    public void createParamIfNotExist();
}
