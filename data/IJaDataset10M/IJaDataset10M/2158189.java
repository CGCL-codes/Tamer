package examples.employee.entity;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Company {

    public static final Calendar FOUNDED_CALENDAR = GregorianCalendar.getInstance();

    public static final long FOUNDED_TIME;

    static {
        FOUNDED_CALENDAR.set(1971, 0, 1);
        FOUNDED_TIME = FOUNDED_CALENDAR.getTimeInMillis();
    }
}
