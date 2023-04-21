package info.freundorfer.icalendar.properties.changeManagement;

import info.freundorfer.icalendar.ICalendarParseException;
import info.freundorfer.icalendar.datatypes.DataType;
import info.freundorfer.icalendar.datatypes.DateTime;
import info.freundorfer.icalendar.properties.Property;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Josef Freundorfer <http://www.freundorfer.info>
 */
public class DateTimeCreated extends Property<DateTimeCreated> {

    private DateTime value;

    @Override
    public String getName() {
        return "CREATED";
    }

    @Override
    public List<DataType<?>> getValues() {
        final List<DataType<?>> lst = new ArrayList<DataType<?>>();
        lst.add(value);
        return lst;
    }

    @Override
    public DateTimeCreated parse(final String value) throws ICalendarParseException {
        this.value = new DateTime(value);
        return this;
    }
}
