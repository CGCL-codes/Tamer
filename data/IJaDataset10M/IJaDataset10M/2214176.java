package info.metlos.jcdc.config;

import info.metlos.jcdc.config.extensionpoints.ConfigurationExtensionPoint;
import info.metlos.jcdc.views.IVisualisable;

/**
 * Objects that implement this interface can be configured using user interface.
 * Plugins that implement this should register with
 * {@link ConfigurationExtensionPoint}. When the UI creates the configuration
 * dialog, it examines all the plugins registered with that extension point and
 * uses all the IVisuallyConfigurable to create the configuration UI.
 * 
 * @author metlos
 * 
 * @version $Id: IVisuallyConfigurable.java 89 2007-06-12 20:21:39Z metlos $
 */
public interface IVisuallyConfigurable extends IConfigurable, IVisualisable {

    /**
	 * @return the section in a configuration options tree this configurable belongs to.
	 */
    public ConfigurationSection getSection();
}
