package net.fortuna.ical4j.model.property;

import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.util.DateTimeFormat;
import net.fortuna.ical4j.util.DurationFormat;
import net.fortuna.ical4j.util.ParameterValidator;
import java.util.Date;

/**
 * Defines a TRIGGER iCalendar component property.
 * 
 * @author benf
 */
public class Trigger extends Property {

    private long duration;

    /**
	 * The value type can be set to a DATE-TIME value type, in which case the
	 * value MUST specify a UTC formatted DATE-TIME value.
	 */
    private Date dateTime;

    /**
	 * Default constructor.
	 */
    public Trigger() {
        super(TRIGGER);
        dateTime = new Date();
    }

    /**
	 * @param aList
	 *            a list of parameters for this component
	 * @param aValue
	 *            a value string for this component
	 */
    public Trigger(final ParameterList aList, final String aValue) {
        super(TRIGGER, aList);
        setValue(aValue);
    }

    /**
	 * @param aDuration
	 *            a duration in milliseconds
	 */
    public Trigger(final long aDuration) {
        super(TRIGGER);
        duration = aDuration;
    }

    /**
	 * @param aList
	 *            a list of parameters for this component
	 * @param aDuration
	 *            a duration in milliseconds
	 */
    public Trigger(final ParameterList aList, final long aDuration) {
        super(TRIGGER, aList);
        duration = aDuration;
    }

    /**
	 * @param aDate
	 *            a date representation of a date-time
	 */
    public Trigger(final Date aDate) {
        super(TRIGGER);
        dateTime = aDate;
    }

    /**
	 * @param aList
	 *            a list of parameters for this component
	 * @param aDate
	 *            a date representation of a date-time
	 */
    public Trigger(final ParameterList aList, final Date aDate) {
        super(TRIGGER, aList);
        dateTime = aDate;
    }

    /**
	 * @see net.fortuna.ical4j.model.Property#validate()
	 */
    public final void validate() throws ValidationException {
        if (getDateTime() != null) {
            ParameterValidator.getInstance().validateOne(Parameter.VALUE, getParameters());
            Parameter valueParam = getParameters().getParameter(Parameter.VALUE);
            if (valueParam == null || !Value.DATE_TIME.equals(valueParam.getValue())) {
                throw new ValidationException("Parameter [" + Parameter.VALUE + "=" + valueParam.getValue() + "] is invalid");
            }
        } else {
            ParameterValidator.getInstance().validateOneOrLess(Parameter.VALUE, getParameters());
            Parameter valueParam = getParameters().getParameter(Parameter.VALUE);
            if (valueParam != null && !Value.DURATION.equals(valueParam.getValue())) {
                throw new ValidationException("Parameter [" + Parameter.VALUE + "=" + valueParam.getValue() + "] is invalid");
            }
            ParameterValidator.getInstance().validateOneOrLess(Parameter.RELATED, getParameters());
        }
    }

    /**
	 * @return Returns the dateTime.
	 */
    public final Date getDateTime() {
        return dateTime;
    }

    /**
	 * @return Returns the duration.
	 */
    public final long getDuration() {
        return duration;
    }

    public final void setValue(final String aValue) {
        try {
            dateTime = DateTimeFormat.getInstance().parse(aValue);
            duration = 0;
        } catch (Exception e) {
            duration = DurationFormat.getInstance().parse(aValue);
            dateTime = null;
        }
    }

    public final String getValue() {
        if (getDateTime() != null) {
            return DateTimeFormat.getInstance().format(getDateTime());
        } else {
            return DurationFormat.getInstance().format(getDuration());
        }
    }

    /**
	 * @param dateTime
	 *            The dateTime to set.
	 */
    public final void setDateTime(final Date dateTime) {
        this.dateTime = dateTime;
        duration = 0;
    }

    /**
	 * @param duration
	 *            The duration to set.
	 */
    public final void setDuration(final long duration) {
        this.duration = duration;
        dateTime = null;
    }
}
