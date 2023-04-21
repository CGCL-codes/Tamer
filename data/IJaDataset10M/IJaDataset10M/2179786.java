package net.cygeek.tech.client.util;

import com.gwtext.client.widgets.form.DateField;
import java.util.Date;

/**
 * Author: Thilina Hasantha
 * Date: Dec 10, 2008
 * Time: 7:50:18 AM
 */
public class ODateFormat {

    public static DateField sdf = new DateField("SDF", "", 230);

    public static long getTimeInMills(Date d, String time) {
        String[] x = time.split(" ");
        int year = d.getYear();
        int month = d.getMonth();
        int day = d.getDate();
        String[] x1 = x[0].split("\\:");
        int hour = Integer.parseInt(x1[0]);
        int min = Integer.parseInt(x1[1]);
        Date xd = new Date();
        xd.setYear(year);
        xd.setMonth(month);
        xd.setDate(day);
        xd.setHours(hour);
        xd.setMinutes(min);
        xd.setSeconds(0);
        return xd.getTime();
    }

    public static String getTimeFromString(String time) {
        Date d = new Date(Long.parseLong(time));
        return getDecimalCorrectedFront(d.getHours()) + ":" + getDecimalCorrectedFront(d.getMinutes());
    }

    public static Date getDateFromString(String time) {
        return new Date(Long.parseLong(time));
    }

    public static String getDecimalCorrectedFront(int k) {
        String s = "";
        if (k < 10) {
            s = "0" + k;
        } else {
            s = "" + k;
        }
        return s;
    }

    private static String getDecimalCorrectedBack(int k) {
        String s = "";
        if (k < 10) {
            s = k + "0";
        } else {
            s = "" + k;
        }
        return s;
    }
}
