package info.freundorfer.icalendar.properties.datetime;

import info.freundorfer.icalendar.ICalendarParseException;
import info.freundorfer.icalendar.datatypes.DataType;
import info.freundorfer.icalendar.datatypes.DateTime;
import info.freundorfer.icalendar.properties.Property;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Josef Freundorfer <http://www.freundorfer.info>
 */
public class DateTimeCompleted extends Property<DateTimeCompleted> {

    private DateTime dt;

    @Override
    public String getName() {
        return "COMPLETED";
    }

    @Override
    public List<DataType<?>> getValues() {
        final List<DataType<?>> l = new ArrayList<DataType<?>>();
        l.add(dt);
        return l;
    }

    @Override
    public DateTimeCompleted parse(final String value) throws ICalendarParseException {
        dt = new DateTime(value);
        return this;
    }
}
