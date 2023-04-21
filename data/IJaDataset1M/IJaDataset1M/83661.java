package org.dllearner.core.config;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * A configuration option, which allows values of type String. Optionally a set
 * of allowed strings can be set. By default all strings are allowed.
 * 
 * @author Jens Lehmann
 *
 */
public class StringConfigOption extends ConfigOption<String> {

    private Set<String> allowedValues = new TreeSet<String>();

    ;

    public StringConfigOption(String name, String description) {
        super(name, description);
    }

    public StringConfigOption(String name, String description, String defaultValue) {
        super(name, description, defaultValue);
    }

    @Override
    public boolean isValidValue(String value) {
        if (allowedValues.size() == 0 || allowedValues.contains(value)) return true; else return false;
    }

    /**
	 * @return the allowedValues
	 */
    public Set<String> getAllowedValues() {
        return allowedValues;
    }

    /**
	 * @param allowedValues the allowedValues to set
	 */
    public void setAllowedValues(Set<String> allowedValues) {
        this.allowedValues = allowedValues;
    }

    public void setAllowedValues(String[] allowedValues) {
        this.allowedValues = new TreeSet<String>(Arrays.asList(allowedValues));
    }

    @Override
    public boolean checkType(Object object) {
        return (object instanceof String);
    }
}
