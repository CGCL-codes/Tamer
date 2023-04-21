package nauja.utils.jcalendar.renderers;

import java.util.Calendar;
import java.util.Date;
import javax.swing.JComponent;
import nauja.utils.jcalendar.JCalendar;
import nauja.utils.jcalendar.components.DefaultDaysNamesPanel;

/**
 * Renderer for days names in calendar's body.
 * @author Jeremy Morosi
 * @version 1.0
 */
public class DefaultCalendarDaysNamesRenderer implements CalendarRenderer {

    /**
	 * {@inheritDoc}
	 */
    @Override
    public JComponent getComponent(final JCalendar parent, final Calendar calendar, final Date date) {
        return new DefaultDaysNamesPanel();
    }
}
