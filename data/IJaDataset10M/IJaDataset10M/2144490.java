package info.freundorfer.icalendar.properties.calendar;

import info.freundorfer.icalendar.ICalendarParseException;
import info.freundorfer.icalendar.datatypes.DataType;
import info.freundorfer.icalendar.datatypes.Text;
import info.freundorfer.icalendar.properties.Property;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Josef Freundorfer <http://www.freundorfer.info>
 */
public class CalendarScale extends Property<CalendarScale> {

    public Text value;

    @Override
    public String getName() {
        return "CALSCALE";
    }

    @Override
    public List<DataType<?>> getValues() {
        List<DataType<?>> lst = new ArrayList<DataType<?>>();
        lst.add(value);
        return lst;
    }

    @Override
    public CalendarScale parse(String value) throws ICalendarParseException {
        this.value = new Text(value);
        return this;
    }
}
