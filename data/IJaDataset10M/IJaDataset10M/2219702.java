package net.fortuna.ical4j.model.property;

import java.text.ParseException;
import net.fortuna.ical4j.model.DateList;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.util.ParameterValidator;
import net.fortuna.ical4j.model.parameter.Value;

/**
 * Defines a EXDATE iCalendar component property.
 * 
 * @author benf
 */
public class ExDate extends Property {

    private DateList dates;

    /**
	 * Default constructor.
	 */
    public ExDate() {
        super(EXDATE);
        dates = new DateList(Value.DATE_TIME);
    }

    /**
	 * @param aList
	 *            a list of parameters for this component
	 * @param aValue
	 *            a value string for this component
	 * @throws ParseException
	 *             where the specified value string is not a valid
	 *             date-time/date representation
	 */
    public ExDate(final ParameterList aList, final String aValue) throws ParseException {
        super(EXDATE, aList);
        setValue(aValue);
    }

    /**
	 * @param dList
	 *            a list of dates
	 */
    public ExDate(final DateList dList) {
        super(EXDATE);
        dates = dList;
    }

    /**
	 * @param aList
	 *            a list of parameters for this component
	 * @param dList
	 *            a list of dates
	 */
    public ExDate(final ParameterList aList, final DateList dList) {
        super(EXDATE, aList);
        dates = dList;
    }

    /**
	 * @return Returns the dates.
	 */
    public final DateList getDates() {
        Value v = (Value) getParameters().getParameter("VALUE");
        if (v != null) dates.setType(v);
        return dates;
    }

    /**
	 * @see net.fortuna.ical4j.model.Property#validate()
	 */
    public final void validate() throws ValidationException {
        ParameterValidator.getInstance().validateOneOrLess(Parameter.VALUE, getParameters());
        Parameter valueParam = getParameters().getParameter(Parameter.VALUE);
        if (valueParam != null && !Value.DATE_TIME.equals(valueParam) && !Value.DATE.equals(valueParam)) {
            throw new ValidationException("Parameter [" + Parameter.VALUE + "] is invalid");
        }
        ParameterValidator.getInstance().validateOneOrLess(Parameter.TZID, getParameters());
    }

    public final void setValue(final String aValue) throws ParseException {
        dates = new DateList(aValue, (Value) getParameters().getParameter(Parameter.VALUE));
    }

    public final String getValue() {
        return getDates().toString();
    }
}
