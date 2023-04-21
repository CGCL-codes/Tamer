package ar.fi.uba.apolo.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class FechaUtil {

    public static Calendar stringToCalendar(String fecha) {
        Calendar cal = new GregorianCalendar();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        StringTokenizer st = new StringTokenizer(fecha, "-");
        if (st.hasMoreTokens()) {
            year = Integer.valueOf(st.nextToken()).intValue();
            month = Integer.valueOf(st.nextToken()).intValue() - 1;
            date = Integer.valueOf(st.nextToken()).intValue();
        }
        return new GregorianCalendar(year, month, date);
    }

    public static Calendar stringToCalendar2(String fecha) {
        Calendar cal = new GregorianCalendar();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        StringTokenizer st = new StringTokenizer(fecha, "-");
        if (st.hasMoreTokens()) {
            date = Integer.valueOf(st.nextToken()).intValue();
            month = Integer.valueOf(st.nextToken()).intValue() - 1;
            year = Integer.valueOf(st.nextToken()).intValue();
        }
        return new GregorianCalendar(year, month, date);
    }
}
