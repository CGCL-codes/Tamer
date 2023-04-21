package org.dllearner.core;

import java.util.Collection;
import java.util.LinkedList;
import org.dllearner.core.options.ConfigEntry;
import org.dllearner.core.options.ConfigOption;
import org.dllearner.core.options.DoubleConfigOption;
import org.dllearner.core.options.InvalidConfigOptionValueException;

/**
 * Base class of all components. See also http://dl-learner.org/wiki/Architecture.
 * 
 * @author Jens Lehmann
 *
 */
public abstract class AbstractComponent implements Component {

    /**
	 * Returns the name of this component. By default, "unnamed 
	 * component" is returned, but all implementations of components
	 * are strongly encouraged to provide a static method returning 
	 * the name.
	 * 
	 * Use the DLComponent annotation instead of setting a name through this method.
	 * 
	 * @return The name of this component.
	 */
    @Deprecated
    public static String getName() {
        return "unnamed component";
    }

    /**
	 * Returns all configuration options supported by this component.
	 * @return A list of supported configuration options for this
	 * component.
	 */
    public static Collection<ConfigOption<?>> createConfigOptions() {
        return new LinkedList<ConfigOption<?>>();
    }

    /**
	 * Applies a configuration option to this component. Implementations
	 * of components should use option and value of the config entry to
	 * perform an action (usually setting an internal variable to 
	 * an appropriate value).
	 * 
	 * Since the availability of configurators, it is optional for 
	 * components to implement this method. Instead of using this method
	 * to take an action based on a configuration value, components can
	 * also use the getters defined in the components configurator. 
	 * 
	 * Important note: Never call this method directly. All calls are
	 * done via the {@link org.dllearner.core.ComponentManager}.
	 * 
	 * @see #getConfigurator()
	 * @param <T> Type of the config entry (Integer, String etc.).
	 * @param entry A configuration entry.
	 * @throws InvalidConfigOptionValueException This exception is thrown if the
	 * value of the config entry is not valid. For instance, a config option
	 * may only accept values, which are within intervals 0.1 to 0.3 or 0.5 to 0.8.
	 * If the value is outside of those intervals, an exception is thrown. Note
	 * that many of the common cases are already caught in the constructor of
	 * ConfigEntry (for instance for a {@link DoubleConfigOption} you can specify
	 * an interval for the value). This means that, as a component developer, you
	 * often do not need to implement further validity checks.  
	 */
    protected <T> void applyConfigEntry(ConfigEntry<T> entry) throws InvalidConfigOptionValueException {
    }
}
