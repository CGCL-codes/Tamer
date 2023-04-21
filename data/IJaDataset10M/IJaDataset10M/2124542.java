package org.jquantlib.time.calendars;

import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.SEPTEMBER;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.APRIL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/**
 * ! Chinese calendar ! Holidays:
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's day, January 1st</li>
 * <li>Labour Day, first week in May</li>
 * <li>National Day, one week from October 1st</li>
 * </ul>
 * 
 * Other holidays for which no rule is given:
 * <ul>
 * <li>Lunar New Year (data available for 2004 only)</li>
 * <li>Spring Festival</li>
 * <li>Last day of Lunar Year</li>
 * </ul>
 * 
 * @author Tim Swetonic
 * @author Jia Jia
 */
public class China extends DelegateCalendar {

    public enum Market {

        SSE
    }

    private static final China SSE_Calendar = new China(Market.SSE);

    private China(Market market) {
        Calendar delegate;
        switch(market) {
            case SSE:
                delegate = new ChinaSSECalendar();
                break;
            default:
                throw new IllegalArgumentException("unknown market");
        }
        setDelegate(delegate);
    }

    public static China getCalendar(Market market) {
        switch(market) {
            case SSE:
                return SSE_Calendar;
            default:
                throw new IllegalArgumentException("unknown market");
        }
    }

    final class ChinaSSECalendar extends WesternCalendar {

        @Override
        public String getName() {
            return "Shanghai stock exchange";
        }

        public final boolean isBusinessDay(Date date) {
            Weekday w = date.getWeekday();
            int d = date.getDayOfMonth();
            Month m = date.getMonthEnum();
            int y = date.getYear();
            if (isWeekend(w) || (d == 1 && m == JANUARY) || (d == 3 && m == JANUARY && y == 2005) || ((d == 2 || d == 3) && m == JANUARY && y == 2006) || (d <= 3 && m == JANUARY && y == 2007) || (d == 31 && m == DECEMBER && y == 2007) || (d == 1 && m == JANUARY && y == 2008) || (d >= 19 && d <= 28 && m == JANUARY && y == 2004) || (d >= 7 && d <= 15 && m == FEBRUARY && y == 2005) || (((d >= 26 && m == JANUARY) || (d <= 3 && m == FEBRUARY)) && y == 2006) || (d >= 17 && d <= 25 && m == FEBRUARY && y == 2007) || (d >= 6 && d <= 12 && m == FEBRUARY && y == 2008) || (d == 4 && m == APRIL && y <= 2008) || (d >= 1 && d <= 7 && m == MAY && y <= 2007) || (d >= 1 && d <= 2 && m == MAY && y == 2008) || (d == 9 && m == JUNE && y <= 2008) || (d == 15 && m == SEPTEMBER && y <= 2008) || (d >= 1 && d <= 7 && m == OCTOBER && y <= 2007) || (d >= 29 && m == SEPTEMBER && y == 2008) || (d <= 3 && m == OCTOBER && y == 2008)) return false;
            return true;
        }
    }
}
