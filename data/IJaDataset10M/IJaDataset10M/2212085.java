package net.fortuna.ical4j.model.parameter;

import net.fortuna.ical4j.model.Parameter;

/**
 * Defines a Language parameter.
 * 
 * @author benfortuna
 */
public class Language extends Parameter {

    private String value;

    /**
	 * @param aValue
	 *            a string representation of a Language
	 */
    public Language(final String aValue) {
        super(LANGUAGE);
        this.value = aValue;
    }

    public String getValue() {
        return value;
    }
}
