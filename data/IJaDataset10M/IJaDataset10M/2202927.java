package nauja.utils.jcalendar;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.swing.JComponent;
import javax.swing.JPanel;
import nauja.utils.jcalendar.listeners.CalendarEvent;
import nauja.utils.jcalendar.listeners.DateChangeListener;
import nauja.utils.jcalendar.renderers.CalendarRenderer;
import nauja.utils.jcalendar.renderers.DefaultCalendarRenderer;

/**
 * Graphical calendar.
 * @author Jeremy Morosi
 * @version 1.0
 */
@SuppressWarnings("serial")
public class JCalendar extends JPanel {

    /**
	 * Calendar instance.
	 */
    private Calendar calendar;

    /**
	 * Renderer for calendar.
	 */
    private CalendarRenderer calendarRenderer;

    /**
	 * Initialising constructor.
	 * @param calendar instance of calendar to use.
	 */
    protected JCalendar(final Calendar calendar) {
        super(new BorderLayout());
        this.calendar = calendar;
        this.calendarRenderer = new DefaultCalendarRenderer();
        createComponents();
    }

    /**
	 * Get a JCalendar instance for current date.
	 * @return new JCalendar instance.
	 */
    public static JCalendar getInstance() {
        return getInstance(Calendar.getInstance());
    }

    /**
	 * Get a JCalendar instance for given locale.
	 * @param aLocale a locale for the calendar.
	 * @return new JCalendar instance.
	 */
    public static JCalendar getInstance(final Locale aLocale) {
        return getInstance(Calendar.getInstance(aLocale));
    }

    /**
	 * Get a JCalendar instance for given timezone.
	 * @param zone a time zone for the calendar.
	 * @return new JCalendar instance.
	 */
    public static JCalendar getInstance(final TimeZone zone) {
        return getInstance(Calendar.getInstance(zone));
    }

    /**
	 * Get a JCalendar instance for given timezone and locale.
	 * @param zone a time zone for the calendar.
	 * @param aLocale a locale for the calendar.
	 * @return new JCalendar instance.
	 */
    public static JCalendar getInstance(final TimeZone zone, final Locale aLocale) {
        return getInstance(Calendar.getInstance(zone, aLocale));
    }

    /**
	 * Get a JCalendar instance for given date.
	 * @param date selected date.
	 * @return new JCalendar instance.
	 */
    public static JCalendar getInstance(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getInstance(calendar);
    }

    /**
	 * Get a JCalendar instance for given date.
	 * @param calendar calendar instance.
	 * @return new JCalendar instance.
	 * 
	 */
    public static JCalendar getInstance(final Calendar calendar) {
        return new JCalendar(calendar);
    }

    /**
	 * Function called to create all components.
	 */
    private void createComponents() {
        Date date = calendar.getTime();
        removeAll();
        JComponent componentCalendar = calendarRenderer.getComponent(this, calendar, date);
        add(componentCalendar, BorderLayout.CENTER);
    }

    /**
	 * Goto previous month.
	 */
    public void previousDay() {
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        updateCalendar();
    }

    /**
	 * Goto next day.
	 */
    public void nextDay() {
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        updateCalendar();
    }

    /**
	 * Goto previous month.
	 */
    public void previousMonth() {
        calendar.add(Calendar.MONTH, -1);
        updateCalendar();
    }

    /**
	 * Goto next month.
	 */
    public void nextMonth() {
        calendar.add(Calendar.MONTH, 1);
        updateCalendar();
    }

    /**
	 * Goto previous year.
	 */
    public void previousYear() {
        calendar.add(Calendar.YEAR, -1);
        updateCalendar();
    }

    /**
	 * Goto next year.
	 */
    public void nextYear() {
        calendar.add(Calendar.YEAR, 1);
        updateCalendar();
    }

    /**
	 * Get the date in given format.
	 * @param pattern format for the date.
	 * @return date corresponding to the pattern.
	 */
    public String format(final String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(getDate());
    }

    /**
	 * Update the calendar.
	 */
    public void updateCalendar() {
        createComponents();
        updateUI();
    }

    /**
	 * Add a <code>DateChangeListener</code> to the calendar.
	 * @param listener the <code>DateChangeListener</code> to be added.
	 */
    public void addDateChangeListener(final DateChangeListener listener) {
        listenerList.add(DateChangeListener.class, listener);
    }

    /**
	 * Remove a <code>DateChangeListener</code> from the calendar.
	 * If the listener is the currently set <code>Action</code>
	 * @param listener the listener to be removed.
	 */
    public void removeDateChangeListener(final DateChangeListener listener) {
        listenerList.remove(DateChangeListener.class, listener);
    }

    /**
	 * Notifies all listeners that day selection has changed.
	 * @param passive indicate if the selection was passive
	 * (made by clicking on a button in calendar's title) or
	 * active (made by clicking on a day of the calendar).
	 */
    protected void fireDayChanged(boolean passive) {
        CalendarEvent event = null;
        if (passive) {
            event = new CalendarEvent(this, CalendarEvent.DAY_CHANGED, getDate());
        } else {
            event = new CalendarEvent(this, CalendarEvent.DATE_SELECTED, getDate());
        }
        fireDateChanged(event);
    }

    /**
	 * Notifies all listeners that month selection has changed.
	 */
    protected void fireMonthChanged() {
        CalendarEvent event = new CalendarEvent(this, CalendarEvent.MONTH_CHANGED, getDate());
        fireDateChanged(event);
    }

    /**
	 * Notifies all listeners that year selection has changed.
	 */
    protected void fireYearChanged() {
        CalendarEvent event = new CalendarEvent(this, CalendarEvent.YEAR_CHANGED, getDate());
        fireDateChanged(event);
    }

    /**
	 * Notifies all listeners that have registered interest for
	 * notification on this event type. The event instance
	 * is lazily created using the <code>event</code> parameter.
	 * @param event the <code>CalendarEvent</code> object.
	 */
    protected void fireDateChanged(final CalendarEvent event) {
        Object[] listeners = listenerList.getListenerList();
        CalendarEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == DateChangeListener.class) {
                if (e == null) {
                    e = new CalendarEvent(event.getComponent(), event.getID(), event.getDate());
                }
                ((DateChangeListener) listeners[i + 1]).dateChanged(e);
            }
        }
    }

    /**
	 * Set the selected day.
	 * @param day selected day.
	 */
    public void setDay(final int day) {
        calendar.set(Calendar.DAY_OF_MONTH, day);
        updateCalendar();
        fireDayChanged(true);
    }

    /**
	 * Set the selected month.
	 * @param month selected month.
	 */
    public void setMonth(final int month) {
        calendar.set(Calendar.MONTH, month);
        updateCalendar();
        fireMonthChanged();
    }

    /**
	 * Set the selected year.
	 * @param year selected year.
	 */
    public void setYear(final int year) {
        calendar.set(Calendar.YEAR, year);
        updateCalendar();
        fireYearChanged();
    }

    /**
	 * Set the selected date.
	 * @param date selected date.
	 */
    public void setDate(final Date date) {
        calendar.setTime(date);
        updateCalendar();
        fireDayChanged(true);
    }

    /**
	 * Modify selected day.
	 * This function act as if the day was selected by user
	 * to select a day on calendar.
	 * @param day new selected day.
	 */
    public void setSelectedDay(final int day) {
        calendar.set(Calendar.DAY_OF_MONTH, day);
        updateCalendar();
        fireDayChanged(false);
    }

    /**
	 * Modify selected month.
	 * This function act as if the month was selected by user
	 * to select a day on calendar.
	 * @param month new selected month.
	 */
    public void setSelectedMonth(final int month) {
        calendar.set(Calendar.MONTH, month);
        updateCalendar();
        fireDayChanged(false);
    }

    /**
	 * Modify selected year.
	 * This function act as if the year was selected by user
	 * to select a day on calendar.
	 * @param year new selected year.
	 */
    public void setSelectedYear(final int year) {
        calendar.set(Calendar.YEAR, year);
        updateCalendar();
        fireDayChanged(false);
    }

    /**
	 * Modify selected date.
	 * This function act as if the date was selected by user
	 * to select a day on calendar.
	 * @param date new selected date.
	 */
    public void setSelectedDate(final Date date) {
        calendar.setTime(date);
        updateCalendar();
        fireDayChanged(false);
    }

    /**
	 * Modify the renderer to use for this calendar.
	 * @param calendarRenderer new renderer.
	 */
    public void setCalendarRenderer(final CalendarRenderer calendarRenderer) {
        this.calendarRenderer = calendarRenderer;
        updateCalendar();
    }

    /**
	 * Get selected day.
	 * @return day.
	 */
    public int getDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
	 * Get selected month.
	 * @return month.
	 */
    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    /**
	 * Get selected year.
	 * @return year.
	 */
    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    /**
	 * Get selected date.
	 * @return date.
	 */
    public Date getDate() {
        return calendar.getTime();
    }

    /**
	 * Get the renderer to use for this calendar.
	 * @return calendar's renderer.
	 */
    public CalendarRenderer getCalendarRenderer() {
        return calendarRenderer;
    }
}
