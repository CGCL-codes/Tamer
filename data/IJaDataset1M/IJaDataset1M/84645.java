package carassius.GUI.Calendar;

import carassius.BLL.GregorianCalendarWithEvents;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.GregorianCalendar;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author siebz0r
 */
public class YearSpinner extends JSpinner implements PropertyChangeListener {

    private GregorianCalendarWithEvents _calendar;

    public YearSpinner(int year) {
        this(new GregorianCalendarWithEvents(year, new GregorianCalendar().get(GregorianCalendar.MONTH), new GregorianCalendar().get(GregorianCalendar.DATE)));
    }

    public YearSpinner(GregorianCalendarWithEvents date) {
        super(new SpinnerNumberModel(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.YEAR) - 100, date.get(GregorianCalendar.YEAR) + 100, 1));
        _calendar = date;
        _calendar.addPropertyChangeListener("year", this);
    }

    public YearSpinner() {
        this(new GregorianCalendarWithEvents());
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Integer) {
            super.setValue(value);
            _calendar.set(GregorianCalendar.YEAR, (Integer) value);
        } else {
            super.setValue(getValue());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.setValue(evt.getNewValue());
    }
}
