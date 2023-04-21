package org.drftpd.master.cron;

import java.util.Date;

/**
 * There are 5 methods defined in this interface.
 * 4 of the methods are tied together: hour, day, month, and year
 * Week is excluded
 * When processing these methods, only the largest change method will be called
 * E.g., Jan 3rd, 12:00 AM, ONLY resetDay() is called
 * E.g., Jan 1st, 12:00 AM, ONLY resetYear() is called
 * E.g., Jan 2nd, 2:00 PM, ONLY resetHour() is called
 * On top of this, when a week resets, it will be called as well
 * So in resetMonth() in your interface, most will make sure it calls resetDay()
 * @author zubov
 */
public interface TimeEventInterface {

    public abstract void resetDay(Date d);

    public abstract void resetWeek(Date d);

    public abstract void resetMonth(Date d);

    public abstract void resetYear(Date d);

    public abstract void resetHour(Date d);
}
