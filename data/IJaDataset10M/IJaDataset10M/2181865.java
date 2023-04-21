package info.freundorfer.icalendar.properties.relations;

import info.freundorfer.icalendar.ICalendarParseException;
import info.freundorfer.icalendar.datatypes.DataType;
import info.freundorfer.icalendar.datatypes.DateTime;
import info.freundorfer.icalendar.parameters.*;
import info.freundorfer.icalendar.properties.Property;
import java.util.*;

/**
  * @author Josef Freundorfer <http://www.freundorfer.info>
  */
public class RecurrenceId extends Property<RecurrenceId> {

    private DateTime dtValue;

    private info.freundorfer.icalendar.datatypes.Date dValue;

    private Range range;

    private TZID tzid;

    private Value value;

    @Override
    public Property<RecurrenceId> appendParameter(final Parameter<?, ?> parameter) {
        if (parameter instanceof TZID) {
            tzid = (TZID) parameter;
        } else if (parameter instanceof Value) {
            value = (Value) parameter;
        } else if (parameter instanceof Range) {
            range = (Range) parameter;
        } else {
            super.appendParameter(parameter);
        }
        return this;
    }

    @Override
    public String getName() {
        return "RECURRENCE-ID";
    }

    @Override
    public Stack<Parameter<?, ?>> getParameters() {
        final Stack<Parameter<?, ?>> s = super.getParameters();
        if (tzid != null) {
            s.push(tzid);
        }
        if (value != null) {
            s.push(value);
        }
        if (range != null) {
            s.push(range);
        }
        return s;
    }

    @Override
    public List<DataType<?>> getValues() {
        final ArrayList<DataType<?>> r = new ArrayList<DataType<?>>();
        if (dtValue != null) {
            r.add(dtValue);
        }
        if (dValue != null) {
            r.add(dValue);
        }
        return r;
    }

    @Override
    public RecurrenceId parse(final String value) throws ICalendarParseException {
        try {
            dtValue = new DateTime(value);
        } catch (final ICalendarParseException e) {
            dValue = new info.freundorfer.icalendar.datatypes.Date(value);
        }
        return this;
    }
}
