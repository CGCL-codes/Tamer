package org.jquantlib.time.calendars;

import org.jquantlib.time.AbstractCalendar;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.util.Date;

/**
 * 
 * Depending on the chosen rule, this calendar has a set of business days given
 * by either the union or the intersection of the sets of business days of the
 * given calendars.
 * 
 * JOIN_HOLIDAYS - A date is a holiday for the joint calendar if it is a
 * holiday for any of the given calendars
 * 
 * JOIN_BUSINESSDAYS - A date is a business day for the joint calendar if it is
 * a business day for any of the given calendars
 * 
 * @author Srinivas Hasti
 * 
 */
public class JointCalendar extends AbstractCalendar {

    public static enum JointCalendarRule {

        JOIN_HOLIDAYS, JOIN_BUSINESSDAYS
    }

    ;

    private JointCalendarRule joinRule;

    private Calendar[] calendars;

    public JointCalendar(JointCalendarRule rule, Calendar... calendar) {
        this.calendars = calendar;
        this.joinRule = rule;
    }

    public String getName() {
        StringBuilder builder = new StringBuilder();
        switch(joinRule) {
            case JOIN_HOLIDAYS:
                builder.append("JoinHolidays(");
                break;
            case JOIN_BUSINESSDAYS:
                builder.append("JoinBusinessDays()");
                break;
            default:
                throw new IllegalStateException("unknown joint calendar rule");
        }
        for (Calendar cal : calendars) {
            builder.append(cal.getName() + ",");
        }
        builder.insert(builder.length() - 1, ")");
        return builder.toString();
    }

    public boolean isBusinessDay(Date d) {
        switch(joinRule) {
            case JOIN_HOLIDAYS:
                for (Calendar cal : calendars) {
                    if (cal.isHoliday(d)) {
                        return false;
                    }
                }
                return true;
            case JOIN_BUSINESSDAYS:
                for (Calendar cal : calendars) {
                    if (!cal.isHoliday(d)) {
                        return true;
                    }
                }
                return false;
            default:
                throw new IllegalStateException("unknown joint calendar rule");
        }
    }

    public boolean isWeekend(Weekday w) {
        switch(joinRule) {
            case JOIN_HOLIDAYS:
                for (Calendar cal : calendars) {
                    if (cal.isWeekend(w)) {
                        return true;
                    }
                }
                return false;
            case JOIN_BUSINESSDAYS:
                for (Calendar cal : calendars) {
                    if (!cal.isWeekend(w)) {
                        return false;
                    }
                }
                return true;
            default:
                throw new IllegalStateException("unknown joint calendar rule");
        }
    }
}
