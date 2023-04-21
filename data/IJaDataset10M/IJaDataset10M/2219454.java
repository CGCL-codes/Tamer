package net.taylor.lang;

import java.util.Calendar;
import java.util.Date;
import junit.framework.TestCase;

public class DateUtilTest extends TestCase {

    public void testEndOfDay() {
        Date eod = DateUtil.endOfDay(DateUtil.now());
        System.out.println("EOD: " + eod);
    }

    public void testIsDayOfWeek() {
        boolean result = false;
        result = result || DateUtil.isDayOfWeek(Calendar.SUNDAY);
        System.out.println("Is Sunday: " + result);
        result = result || DateUtil.isDayOfWeek(Calendar.MONDAY);
        System.out.println("Is Monday: " + result);
        result = result || DateUtil.isDayOfWeek(Calendar.TUESDAY);
        System.out.println("Is Tuesday: " + result);
        result = result || DateUtil.isDayOfWeek(Calendar.WEDNESDAY);
        System.out.println("Is Wednesday: " + result);
        result = result || DateUtil.isDayOfWeek(Calendar.THURSDAY);
        System.out.println("Is Thursday: " + result);
        result = result || DateUtil.isDayOfWeek(Calendar.FRIDAY);
        System.out.println("Is Friday: " + result);
        result = result || DateUtil.isDayOfWeek(Calendar.SATURDAY);
        System.out.println("Is Saturday: " + result);
        assertTrue(result);
    }
}
