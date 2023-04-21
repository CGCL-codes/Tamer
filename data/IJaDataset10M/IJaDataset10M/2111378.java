package net.cwroethel.swing.JPopupCalendar;

import java.awt.*;
import javax.swing.*;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Locale;
import net.cwroethel.swing.JPopupCalendar.*;

/**
 * <p>Panel containing the actual calendar.</p>
 * <p>DateChooser is made up of three parts:
 * <ul>
 * <li> The month selector, which contains the two buttons to select between
 *  months separated by a label showing the current month and year.</li>
 * <li> The day selector displaying the days of the current month in a grid
 * layout.</li>
 * <li> And a button to quickly select todays date (the today selector).</li>
 * </ul>
 * The three components are independent and can be turned on or off, and/or be
 * rearranged within the (BorderLayout) layout of the calendar.
 * @author Will Roethel, http://www.cwroethel.net
 * @version $Revision: 1.8 $
 */
public class DateChooser extends JPanel {

    public static final String TOP = java.awt.BorderLayout.NORTH;

    public static final String CENTER = java.awt.BorderLayout.CENTER;

    public static final String BOTTOM = java.awt.BorderLayout.SOUTH;

    private boolean FORCE_SERIALIZE_COPY = false;

    /**
   * If true the month selector is shown.
   */
    public boolean showMonthSelector = true;

    /**
   * If true the today selector is shown.
   */
    public boolean showTodaySelector = true;

    /**
   * Position for the month selector. Default is DateChooser.TOP ==
   * BorderLayout.NORTH.
   */
    public String monthSelectorPosition = TOP;

    /**
   * Position for the placement of the today selector. Default is
   * DateChooser.BOTTOM == BorderLayout.SOUTH.
   */
    public String todaySelectorPosition = BOTTOM;

    /**
   * Position for the placement of the day selector. Default is
   * DateChooser.CENTER or BorderLayout.CENTER.
   */
    public String daySelectorPosition = CENTER;

    private TodaySelector todaySelector = null;

    private MonthSelector monthSelector = null;

    private DaySelector daySelector = null;

    private TodaySelectorStyle todaySelectorStyle = null;

    private MonthSelectorStyle monthSelectorStyle = null;

    private DaySelectorStyle daySelectorStyle = null;

    private Calendar currentDate = null;

    private Calendar selectedDate = null;

    private Locale locale = null;

    /**
   * Default constructor.
   */
    public DateChooser() {
        locale = Locale.getDefault();
        currentDate = Calendar.getInstance(locale);
    }

    /**
   * Constructor setting a specific locale to manage looks and language.
   * @param locale Locale
   */
    public DateChooser(Locale locale) {
        this.locale = locale;
        currentDate = Calendar.getInstance(locale);
    }

    /**
   * Set the start date for this calendar.
   * @param currentDate Calendar
   */
    public void setDate(Calendar currentDate) {
        this.currentDate = currentDate;
    }

    /**
   * Create and pack the GUI components into the container. This is separated
   * from the constructor since the style definitions can be changed to
   * customize the look and feel. All style changes have to be applied before
   * calling pack for these to be visible.
   */
    public void pack() {
        try {
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
   * Get access to the style definitions for the Today button.
   * @return TodaySelectorStyle
   */
    public TodaySelectorStyle getTodaySelectorStyle() {
        if (todaySelectorStyle == null) {
            todaySelectorStyle = new TodaySelectorStyle();
        }
        return todaySelectorStyle;
    }

    /**
   * Get the class instance defining the style of the month selector.
   * @return MonthSelectorStyle
   */
    public MonthSelectorStyle getMonthSelectorStyle() {
        if (monthSelectorStyle == null) {
            monthSelectorStyle = new MonthSelectorStyle();
        }
        return monthSelectorStyle;
    }

    /**
   * Get the class instance defining the settings for the day selector.
   * @return DaySelectorStyle
   */
    public DaySelectorStyle getDaySelectorStyle() {
        if (daySelectorStyle == null) {
            daySelectorStyle = new DaySelectorStyle();
        }
        return daySelectorStyle;
    }

    /**
   * resets the selected and current dates.
   */
    public void resetDates() {
        selectedDate = null;
        currentDate = Calendar.getInstance();
    }

    /**
   * Reset the selected date to null. Usefull when an instance of DateChooser
   * is used several times but not expected to store a selected date between
   * uses.
   */
    public void resetSelectedDate() {
        selectedDate = null;
    }

    /**
   * Override the style settings for the today button with a set of user
   * defined settings.
   * @param selectorStyle TodaySelectorStyle
   */
    public void setTodaySelectorStyle(TodaySelectorStyle selectorStyle) {
        todaySelectorStyle = selectorStyle;
    }

    /**
   * Override the style settings for the today button with a set of user
   * defined settings.
   * @param selectorStyle MonthSelectorStyle
   */
    public void setMonthSelectorStyle(MonthSelectorStyle selectorStyle) {
        monthSelectorStyle = selectorStyle;
    }

    /**
   * Override the day selector styles with a user defined new selector.
   * @param selectorStyle DaySelectorStyle
   */
    public void setDaySelectorStyle(DaySelectorStyle selectorStyle) {
        daySelectorStyle = selectorStyle;
    }

    /**
   * Set up the all the objects.
   * @throws Exception
   */
    private void jbInit() throws Exception {
        setLayout(new BorderLayout());
        if (this.FORCE_SERIALIZE_COPY) {
            getMonthSelectorStyle().forceSerializeCopy(true);
            getDaySelectorStyle().forceSerializeCopy(true);
            getTodaySelectorStyle().forceSerializeCopy(true);
        }
        getMonthSelectorStyle().setLocale(locale);
        getDaySelectorStyle().setLocale(locale);
        daySelector = new DaySelector(getDaySelectorStyle());
        daySelector.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getID() == DateChooserAction.calendarButtonSelected) {
                    currentDate.set(java.util.Calendar.DAY_OF_MONTH, ((DaySelector) e.getSource()).selectedDay);
                    selectedDate = (Calendar) currentDate.clone();
                    getParent().setVisible(false);
                }
            }
        });
        add(daySelector, daySelectorPosition);
        if (showMonthSelector) {
            monthSelector = new MonthSelector(getMonthSelectorStyle());
            monthSelector.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (e.getID() == DateChooserAction.previousMonthSelected) {
                        currentDate.add(java.util.Calendar.MONTH, -1);
                        update();
                    }
                    if (e.getID() == DateChooserAction.nextMonthSelected) {
                        currentDate.add(java.util.Calendar.MONTH, 1);
                        update();
                    }
                }
            });
            add(monthSelector, monthSelectorPosition);
        }
        if (showTodaySelector) {
            todaySelector = new TodaySelector(getTodaySelectorStyle());
            todaySelector.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (e.getID() == DateChooserAction.todaySelected) {
                        currentDate.setTime(Calendar.getInstance().getTime());
                        selectedDate = (Calendar) currentDate.clone();
                        getParent().setVisible(false);
                    }
                }
            });
            add(todaySelector, todaySelectorPosition);
        }
        update();
    }

    /**
   * update the view to display settings for the new date.
   */
    public void update() {
        setVisible(false);
        daySelector.paintCalendarPane(currentDate);
        if (showMonthSelector) {
            String yearString = " " + currentDate.get(java.util.Calendar.YEAR);
            monthSelector.setMonthName(getMonthSelectorStyle().getMonthName(currentDate.get(java.util.Calendar.MONTH)) + yearString);
        }
        setVisible(true);
    }

    /**
   * Returns the selected date.
   * @return Calendar
   */
    public Calendar getDate() {
        return (Calendar) selectedDate;
    }

    /**
   * Force a deep copy of the swing component templates instead of the
   * native clone, even if the clone method supports the use of it in
   * this context.
   * @param bool boolean
   */
    public void forceSerializeCopy(boolean bool) {
        this.FORCE_SERIALIZE_COPY = bool;
    }
}
