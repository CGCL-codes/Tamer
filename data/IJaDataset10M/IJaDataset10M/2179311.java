package jgnash.engine.recurring;

import java.util.Date;

/** A one time only reminder
 * <p>
 * $Id: OneTimeReminder.java 675 2008-06-17 01:36:01Z ccavanaugh $
 * 
 * @author Craig Cavanaugh
 */
public class OneTimeReminder extends Reminder {

    public OneTimeReminder() {
        super();
    }

    /**
	 * @see jgnash.engine.recurring.Reminder#getIterator()
	 */
    public RecurringIterator getIterator() {
        return new OneTimeIterator();
    }

    /**
	 * @see jgnash.engine.recurring.Reminder#getReminderType()
	 */
    public ReminderType getReminderType() {
        return ReminderType.ONETIME;
    }

    private class OneTimeIterator implements RecurringIterator {

        private boolean end = false;

        /**
		 * @see jgnash.engine.recurring.RecurringIterator#next()
		 */
        public Date next() {
            if (getLastDate() == null && end == false) {
                end = true;
                return getStartDate();
            }
            return null;
        }
    }
}
