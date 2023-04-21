package de.objectcode.soa.common.utils.locator;

import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.helpers.ConfigTree;

/**
 * ValueLocator factory singleton.
 * 
 * This factory provides a convenient way to create ValueLocators for an ActionHandler ConfigTree.
 * 
 * @author junglas
 */
public class ValueLocatorFactory {

    public static final ValueLocatorFactory INSTANCE = new ValueLocatorFactory();

    private ValueLocatorFactory() {
    }

    public IValueLocator createValueLocator(ConfigTree config) throws ConfigurationException {
        String expression = config.getAttribute("expression");
        String objectPath = config.getAttribute("object-path");
        String xpath = config.getAttribute("xpath");
        String defaultValue = config.getAttribute("default-value");
        String value = config.getAttribute("value");
        if ((objectPath == null && expression == null && value == null && xpath == null) || (objectPath != null && value != null)) {
            throw new ConfigurationException("parameter needs either object-path, expression, xpath or value");
        }
        if (objectPath != null) {
            return new ObjectPathValueLocator(objectPath, defaultValue);
        } else if (expression != null) {
            return new MVELValueLocator(expression);
        } else if (xpath != null) {
            return new XPathValueLocator(xpath);
        } else {
            return new StaticValueLocator(value);
        }
    }
}
