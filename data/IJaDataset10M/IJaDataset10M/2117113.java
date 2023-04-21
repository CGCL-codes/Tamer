package org.dllearner.core.options;

/**
 * A config entry is a configuration option and a value for the option.
 * 
 * @author Jens Lehmann
 * 
 */
public class ConfigEntry<T> {

    private ConfigOption<T> option;

    private T value;

    public ConfigEntry(ConfigOption<T> option, T value) throws InvalidConfigOptionValueException {
        if (!option.isValidValue(value)) {
            throw new InvalidConfigOptionValueException(option, value);
        } else {
            this.option = option;
            this.value = value;
        }
    }

    public ConfigOption<T> getOption() {
        return option;
    }

    public String getOptionName() {
        return option.getName();
    }

    public T getValue() {
        return value;
    }

    /**
	 * Get a string to save into a configuration file.
	 * 
	 * @return a formatted string
	 */
    public String toConfString(String componentName) {
        if (option.getName().equalsIgnoreCase("positiveExamples")) {
            return option.getValueFormatting(value);
        } else if (option.getName().equalsIgnoreCase("negativeExamples")) {
            return option.getValueFormatting(value);
        }
        return componentName.toString() + "." + option.getName() + " = " + option.getValueFormatting(value);
    }

    @Override
    public String toString() {
        return option.name + " = " + value;
    }
}
