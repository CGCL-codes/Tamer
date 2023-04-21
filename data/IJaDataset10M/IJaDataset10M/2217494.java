package org.jquantlib.time.calendars;

import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

public class Denmark extends WesternCalendar {

    private static Denmark DENMARK = new Denmark();

    private Denmark() {
    }

    public static Denmark getCalendar() {
        return DENMARK;
    }

    public boolean isBusinessDay(Date date) {
        Weekday w = date.getWeekday();
        int d = date.getDayOfMonth(), dd = date.getDayOfYear();
        Month m = date.getMonthEnum();
        int y = date.getYear();
        int em = easterMonday(y);
        if (isWeekend(w) || (dd == em - 4) || (dd == em - 3) || (dd == em) || (dd == em + 25) || (dd == em + 38) || (dd == em + 49) || (d == 1 && m == JANUARY) || (d == 5 && m == JUNE) || (d == 25 && m == DECEMBER) || (d == 26 && m == DECEMBER)) return false;
        return true;
    }

    public String getName() {
        return "Denmark";
    }
}
