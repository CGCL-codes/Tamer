package info.freundorfer.icalendar.parameters;

import info.freundorfer.icalendar.ICalendarParseException;
import info.freundorfer.icalendar.datatypes.CalendarUserAddress;
import info.freundorfer.icalendar.util.ParsableArrayList;
import java.util.List;

/**
 * DELEGATED-FROM Parameter According RFC 5545: 3.2.4
 *
 * @author Josef Freundorfer <http://www.freundorfer.info>
 */
public class Delegators extends Parameter<Delegators, CalendarUserAddress> {

    public static final String propertyName = "DELEGATED-FROM";

    private ParsableArrayList<CalendarUserAddress> value;

    @Override
    public String getName() {
        return propertyName;
    }

    @Override
    public ParsableArrayList<CalendarUserAddress> getValues() {
        return this.value;
    }

    public void setValue(List<CalendarUserAddress> newValue) {
        this.value = new ParsableArrayList<CalendarUserAddress>(newValue, true);
    }

    @Override
    public Delegators parse(String input) throws ICalendarParseException {
        this.value = new ParsableArrayList<CalendarUserAddress>(true).parse(CalendarUserAddress.class, input);
        if (this.value.isEmpty()) {
            throw new ICalendarParseException(propertyName + " needs at least one parameter.", -1);
        }
        return this;
    }
}
