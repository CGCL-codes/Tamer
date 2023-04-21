package org.yagnus.calendar.impl;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import org.yagnus.calendar.BaseCalendarSchedule;

/**
 * This class represents days of week, one can specify multiple days (such as
 * weekdays, or MWF, etc.)
 * 
 * @author Alexia B. Chang
 * 
 */
public class DaysOfWeekSchedule extends DaysOfScheduler {

    public DaysOfWeekSchedule(int... inputWeeks) {
        super(Calendar.DAY_OF_WEEK, inputWeeks);
    }

    public static final DaysOfWeekSchedule weekdays = new DaysOfWeekSchedule(Calendar.MONDAY - 1, Calendar.TUESDAY - 1, Calendar.WEDNESDAY - 1, Calendar.THURSDAY - 1, Calendar.FRIDAY - 1);

    public static final DaysOfWeekSchedule weekend = new DaysOfWeekSchedule(Calendar.SATURDAY - 1, Calendar.SUNDAY - 1);

    public static final DaysOfWeekSchedule mwf = new DaysOfWeekSchedule(Calendar.MONDAY - 1, Calendar.WEDNESDAY - 1, Calendar.THURSDAY - 1);

    public static final DaysOfWeekSchedule tt = new DaysOfWeekSchedule(Calendar.TUESDAY - 1, Calendar.THURSDAY - 1);
}
