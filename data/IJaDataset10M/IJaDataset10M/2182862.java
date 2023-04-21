package model.tests;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateTest {

    public static void main(String[] args) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, 2009);
        date.set(Calendar.MONTH, 0);
        date.set(Calendar.DATE, 2);
        Calendar calStart = Calendar.getInstance();
        calStart.setFirstDayOfWeek(Calendar.MONDAY);
        calStart.setTime(date.getTime());
        calStart.add(Calendar.WEEK_OF_YEAR, -1);
        calStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setFirstDayOfWeek(Calendar.MONDAY);
        calEnd.setTime(date.getTime());
        calEnd.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Calendar test = new GregorianCalendar();
        test.setFirstDayOfWeek(Calendar.MONDAY);
        test.set(Calendar.YEAR, 2009);
        test.set(Calendar.MONTH, 0);
        test.set(Calendar.DATE, 4);
        Calendar test2 = Calendar.getInstance();
        test2.set(Calendar.YEAR, 2009);
        test2.set(Calendar.MONTH, 0);
        test2.set(Calendar.DATE, 2);
        System.out.println(calStart.getTime() + " <-> " + calEnd.getTime());
        System.out.println("<--" + test.getTime());
        if (calStart.getTime().before(test.getTime()) && calEnd.getTime().after(test.getTime())) {
            System.out.println("Hier");
        }
    }
}
