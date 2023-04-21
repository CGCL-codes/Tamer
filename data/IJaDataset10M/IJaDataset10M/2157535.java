package org.jquantlib.time.calendars;

import static org.jquantlib.time.Weekday.FRIDAY;
import static org.jquantlib.time.Weekday.THURSDAY;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.SEPTEMBER;
import org.jquantlib.time.AbstractCalendar;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

public class SaudiArabia extends DelegateCalendar {

    public enum Market {

        TADAWUL
    }

    ;

    private static final SaudiArabia TADAWUL_CALENDAR = new SaudiArabia(Market.TADAWUL);

    private SaudiArabia(Market market) {
        Calendar delegate;
        switch(market) {
            case TADAWUL:
                delegate = new SaudiArabiaSettlementCalendar();
                break;
            default:
                throw new IllegalArgumentException("unknown market");
        }
        setDelegate(delegate);
    }

    public static SaudiArabia getCalendar(Market market) {
        switch(market) {
            case TADAWUL:
                return TADAWUL_CALENDAR;
            default:
                throw new IllegalArgumentException("unknown market");
        }
    }
}

final class SaudiArabiaSettlementCalendar extends AbstractCalendar {

    public boolean isWeekend(Weekday w) {
        return w == THURSDAY || w == FRIDAY;
    }

    public boolean isBusinessDay(Date date) {
        Weekday w = date.getWeekday();
        int d = date.getDayOfMonth(), dd = date.getDayOfYear();
        Month m = date.getMonthEnum();
        int y = date.getYear();
        if (isWeekend(w) || (d == 23 && m == SEPTEMBER) || (d >= 1 && d <= 6 && m == FEBRUARY && y == 2004) || (d >= 21 && d <= 25 && m == JANUARY && y == 2005) || (d >= 25 && d <= 29 && m == NOVEMBER && y == 2004) || (d >= 14 && d <= 18 && m == NOVEMBER && y == 2005)) return false;
        return true;
    }

    public String getName() {
        return "Tadawul";
    }
}
