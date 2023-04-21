package org.objectwiz.core.ui.component;

import java.util.Map;
import org.objectwiz.core.Application;

/**
 * Component provider.
 *
 * Implementation is tied to a UI technology and knows how to build actual
 * UI components for that technology, given the {@link GenericUIComponent} and
 * its configuration options.
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public abstract class UIComponentProvider {

    /**
     * Returns true if the given generic component is supported by this provider.
     */
    public abstract boolean isSupported(GenericUIComponent genericComponent);

    /**
     * Instanciates the actual component that implements the given
     * {@link GenericUIComponent} in the technology supported by this provider.
     *
     * @param application           The underlying application
     * @param genericComponent      The generic component.
     * @param options               Instanciation options. See generic component
     *                              reference for more information about the available
     *                              options for each component.
     */
    public abstract UIComponent createUIComponent(Application application, GenericUIComponent genericComponent, Map options);

    /**
     * Implementation helper for parsing configuration options.
     * @param options           The configuration options.
     * @param optionClass       The expected class
     * @param optionName        The name of the option to fetch.
     * @return the option if it is set or null otherwise.
     */
    public <E> E getOption(Map options, Class<E> optionClass, String optionName) {
        return getOption(options, optionClass, optionName, null);
    }

    /**
     * Implementation helper for parsing configuration options.
     * Same as {@link #getOption(Map,Class,String)} but supports a default value
     * (i.e. returns <code>defaultValue</code> in case the value is not defined).
     */
    public <E> E getOption(Map options, Class<E> optionClass, String optionName, E defaultValue) {
        if (options == null || options.get(optionName) == null) {
            return defaultValue;
        }
        Object optionValue = options.get(optionName);
        if (!optionClass.isInstance(optionValue)) {
            throw new RuntimeException("Invalid class, got: '" + optionValue.getClass() + "', expected: " + optionClass);
        }
        return optionValue == null ? defaultValue : (E) optionValue;
    }

    /**
     * Implementation helper for parsing configuration options.
     * Same as {@link #getOption(Map,Class,String)} but requires the option
     * to be present i.e. will fail if it not present.
     * @throws RuntimeException if the required option is not present.
     */
    public <E> E requireOption(Map options, Class<E> optionClass, String optionName) {
        Object value = getOption(options, optionClass, optionName);
        if (value == null) {
            throw new RuntimeException("Required option: " + optionName);
        }
        return (E) value;
    }
}
