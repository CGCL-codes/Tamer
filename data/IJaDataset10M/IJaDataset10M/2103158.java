package gov.lanl.Utility;

import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;

/**
 * Simple bean to hold build Time History histogram
 * @author  $Author: dwforslund $
 * @version $Revision: 2936 $ $Date: 2004-01-08 18:31:09 -0500 (Thu, 08 Jan 2004) $
 */
public class TimeHistory implements Cloneable {

    private Calendar[] time;

    private int[] values;

    /**
	* Constructor of TimeHistory back in time by "count" days
	*/
    public TimeHistory(int count) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, +1);
        Calendar c1;
        time = new Calendar[count + 1];
        for (int i = count + 1; i > 0; i--) {
            c1 = (Calendar) c.clone();
            time[i - 1] = c1;
            c1.add(Calendar.DAY_OF_MONTH, -1);
            c = c1;
        }
        values = new int[count];
    }

    /**
         * Constructor of TimeHistory for a specific time period
         * @param begin Date for interval
         * @param end Date for interval
         * @param period is the time bin for the histogram
         */
    public TimeHistory(Date begin, Date end, String period) {
        int increment = Calendar.DAY_OF_YEAR;
        if (period.equals("DAY")) {
            increment = Calendar.DAY_OF_YEAR;
        } else if (period.equals("WEEK")) {
            increment = Calendar.WEEK_OF_YEAR;
        } else if (period.equals("MONTH")) {
            increment = Calendar.MONTH;
        } else if (period.equals("YEAR")) {
            increment = Calendar.YEAR;
        }
        if (begin.after(end)) {
            System.err.println("end date precedes begin date! " + begin.toString());
        }
        Calendar c = Calendar.getInstance();
        c.setTime(begin);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        ArrayList times = new ArrayList();
        do {
            times.add(c.clone());
            c.add(increment, 1);
        } while (c.before(endCalendar));
        times.add(c);
        time = new Calendar[times.size()];
        times.toArray(time);
        values = new int[time.length - 1];
    }

    /**
	* insert the values for TimeHistory and put them in bins.
	* @param dates of input
	*/
    public void setValues(String[] dates) {
        for (int i = 0; i < dates.length; i++) {
            Date d = ISODate.HL72Date(dates[i]);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            for (int j = 0; j < time.length - 1; j++) {
                if (!c.before(time[j]) && c.before(time[j + 1])) {
                    values[j] += 1;
                    break;
                }
            }
        }
    }

    /**
	* return the Time array by converting Calendar to Date
	*/
    public Date[] getTimes() {
        trim();
        Date[] d = new Date[time.length - 1];
        for (int i = 0; i < d.length; i++) d[i] = this.time[i].getTime();
        return d;
    }

    /**
	* return computed values
	*/
    public int[] getValues() {
        trim();
        return values;
    }

    public void trim() {
        if (values != null && values.length > 0 && values[values.length - 1] == 0) {
            int emptyCount = 0;
            for (int i = values.length - 1; i >= 0 && values[i] == 0; i--) emptyCount++;
            int[] newValues = new int[this.values.length - emptyCount];
            Calendar[] newTimes = new Calendar[this.time.length - emptyCount];
            System.arraycopy(this.values, 0, newValues, 0, newValues.length);
            System.arraycopy(this.time, 0, newTimes, 0, newTimes.length);
            this.values = newValues;
            this.time = newTimes;
        }
    }

    public static void main(String argv[]) {
        String[] dates = new String[100];
        Calendar c;
        java.util.Random r = new java.util.Random();
        for (int i = 0; i < dates.length; i++) {
            c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, -r.nextInt(30));
            dates[i] = ISODate.Date2HL7(c.getTime());
            System.out.println("input dates: " + dates[i]);
        }
        TimeHistory timeHistory = new TimeHistory(30);
        timeHistory.setValues(dates);
        Date[] d = timeHistory.getTimes();
        int[] v = timeHistory.getValues();
        for (int i = 0; i < d.length; i++) {
            System.out.println("date: " + d[i] + " ," + v[i]);
        }
    }
}
