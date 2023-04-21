package net.fortuna.ical4j.model.property;

import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.util.ParameterValidator;

/**
 * Defines a TZNAME iCalendar component property.
 * 
 * @author benf
 */
public class TzName extends Property {

    private String value;

    /**
	 * Default constructor.
	 */
    public TzName() {
        super(TZNAME);
        setEscapable(true);
    }

    /**
	 * @param aValue
	 *            a value string for this component
	 */
    public TzName(final String aValue) {
        super(TZNAME);
        setValue(aValue);
        setEscapable(true);
    }

    /**
	 * @param aList
	 *            a list of parameters for this component
	 * @param aValue
	 *            a value string for this component
	 */
    public TzName(final ParameterList aList, final String aValue) {
        super(TZNAME, aList);
        setValue(aValue);
        setEscapable(true);
    }

    /**
	 * @see net.fortuna.ical4j.model.Property#validate()
	 */
    public final void validate() throws ValidationException {
        ParameterValidator.getInstance().validateOneOrLess(Parameter.LANGUAGE, getParameters());
    }

    public final void setValue(final String aValue) {
        this.value = aValue;
    }

    public final String getValue() {
        return value;
    }
}
