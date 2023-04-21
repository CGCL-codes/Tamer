package eu.future.earth.gwt.client.date;

import java.util.Calendar;
import java.util.GregorianCalendar;
import junit.framework.TestCase;

public class TimeCheck extends TestCase {

    public void testTime() {
        GregorianCalendar walker = new GregorianCalendar();
        walker.set(Calendar.HOUR_OF_DAY, 23);
        for (int i = 0; i < 60; i++) {
            System.out.println(walker.getTime());
            walker.add(Calendar.MINUTE, 1);
        }
    }
}
