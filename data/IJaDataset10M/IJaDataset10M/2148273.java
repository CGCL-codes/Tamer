package net.fortuna.ical4j.model.property;

import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.Property;

/**
 * Defines a TZID iCalendar component property.
 * 
 * @author benf
 */
public class TzId extends Property {

    private static final String PREFIX = "/";

    private String value;

    /**
	 * Default constructor.
	 */
    public TzId() {
        super(TZID);
        setEscapable(true);
    }

    /**
	 * @param aValue
	 *            a value string for this component
	 */
    public TzId(final String aValue) {
        super(TZID);
        setValue(aValue);
        setEscapable(true);
    }

    /**
	 * @param aList
	 *            a list of parameters for this component
	 * @param aValue
	 *            a value string for this component
	 */
    public TzId(final ParameterList aList, final String aValue) {
        super(TZID, aList);
        setValue(aValue);
        setEscapable(true);
    }

    public final void setValue(final String aValue) {
        this.value = aValue;
    }

    public final String getValue() {
        return value;
    }
}
