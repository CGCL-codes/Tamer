package wr3.util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * <pre>
 * usage:<code>
 *   Datetime.date();
 *   Datetime.time();
 *   Datetime.datetime();
 * </code></pre>
 * @author jamesqiu 2008-11-24
 *
 */
public class Datetime {

    /**
	 * get: "2008-11-24" alike
	 * @return
	 */
    public static String date() {
        return DateFormat.getDateInstance().format(new Date());
    }

    /**
	 * get: "20:30:59" alike
	 * @return
	 */
    public static String time() {
        return DateFormat.getTimeInstance().format(new Date());
    }

    /**
	 * get: "2008-11-24 20:30:59" alike
	 * @return
	 */
    public static String datetime() {
        return DateFormat.getDateTimeInstance().format(new Date());
    }

    /**
	 * get this year
	 * @return int like 2008
	 */
    public static int year() {
        return new GregorianCalendar().get(Calendar.YEAR);
    }

    /**
	 * get this month
	 * @return int in [1..12]
	 */
    public static int month() {
        return new GregorianCalendar().get(Calendar.MONDAY) + 1;
    }

    /**
	 * get this day
	 * @return int in [1..31]
	 */
    public static int day() {
        return new GregorianCalendar().get(Calendar.DATE);
    }

    /**
	 * �õ����µ����һ��
	 * @return
	 */
    public static int lastDay() {
        return lastDay(month());
    }

    /**
	 * �õ�����ĳ�µ����һ��
	 * @param month
	 * @return
	 */
    public static int lastDay(int month) {
        return lastDay(year(), month);
    }

    /**
	 * ȡĳ��ĳ�µ����һ��
	 * @param year
	 * @param month [1..12]
	 * @return
	 */
    public static int lastDay(int year, int month) {
        Calendar cal = new GregorianCalendar(year, month - 1, 1);
        return cal.getActualMaximum(Calendar.DATE);
    }

    public static void main(String[] args) {
        System.out.println(datetime());
    }
}
