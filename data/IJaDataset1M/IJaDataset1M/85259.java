package info.freundorfer.icalendar.properties.changeManagement;

import info.freundorfer.icalendar.ICalendarParseException;
import info.freundorfer.icalendar.datatypes.DataType;
import info.freundorfer.icalendar.datatypes.Integer;
import info.freundorfer.icalendar.properties.Property;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Josef Freundorfer <http://www.freundorfer.info>^
 */
public class SequenceNumber extends Property<SequenceNumber> {

    Integer value;

    @Override
    public String getName() {
        return "SEQUENCE";
    }

    @Override
    public List<DataType<?>> getValues() {
        final List<DataType<?>> lst = new ArrayList<DataType<?>>();
        lst.add(value);
        return lst;
    }

    @Override
    public SequenceNumber parse(final String value) throws ICalendarParseException {
        this.value = new Integer().parse(value);
        return this;
    }
}
