package jgnash.engine.recurring;

import java.util.*;
import jgnash.util.DateUtils;

/** A daily reminder
 * <p>
 * $Id: DailyReminder.java 675 2008-06-17 01:36:01Z ccavanaugh $
 * 
 * @author Craig Cavanaugh
 */
public class DailyReminder extends Reminder {

    /**
     * @see jgnash.engine.recurring.Reminder#getIterator()
     */
    public RecurringIterator getIterator() {
        return new DailyIterator();
    }

    /**
     * @see jgnash.engine.recurring.Reminder#getReminderType()
     */
    public ReminderType getReminderType() {
        return ReminderType.DAILY;
    }

    private class DailyIterator implements RecurringIterator {

        private Calendar calendar = Calendar.getInstance();

        public DailyIterator() {
            if (getLastDate() != null) {
                calendar.setTime(getLastDate());
            } else {
                calendar.setTime(getStartDate());
                calendar.add(Calendar.DATE, getIncrement() * -1);
            }
        }

        /**
         * @see jgnash.engine.recurring.RecurringIterator#next()
         */
        public Date next() {
            calendar.add(Calendar.DATE, getIncrement());
            Date date = calendar.getTime();
            if (getEndDate() == null) {
                return date;
            } else if (DateUtils.before(date, getEndDate())) {
                return date;
            }
            return null;
        }
    }
}
