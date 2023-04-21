package com.idocbox.common.lang;

import java.util.Calendar;
import java.util.Date;

/**
 * date util help to process date related task.
 * @author C.H Li wiseneuron@gmail.com
 * @since 0.0.1.1
 */
public class DateUtil {

    /**
	 * compute (d2 - d1), unit is second.
	 * 
	 * @param d1
	 *            start date.
	 * @param d2
	 *            end date.
	 * @return (d2 - d1)
	 */
    public static long computeTime(Date d1, Date d2) {
        long t = d2.getTime() - d1.getTime();
        Float ts = (t / 1000F);
        return ts.longValue();
    }

    /**
	 * roll date .
	 * @param theDate date to roll.
	 * @param num     number to roll
	 * @param type    Y:year, M:month, D:day.
	 * @return rolled date.
	 */
    public static Date rollDate(Date theDate, int num, String type) {
        Date d = null;
        Calendar theCal = Calendar.getInstance();
        Calendar theCal2 = Calendar.getInstance();
        theCal.setTime(theDate);
        theCal2.setTime(theDate);
        if ("Y".equals(type)) {
            theCal.roll(Calendar.YEAR, num);
        } else if ("M".equals(type)) {
            theCal.roll(Calendar.MONTH, num);
            if (theCal2.after(theCal) && num > 0) {
                theCal.roll(Calendar.YEAR, 1);
            }
            if (theCal.after(theCal2) && num < 0) {
                theCal.roll(Calendar.YEAR, -1);
            }
        } else if ("D".equals(type)) {
            theCal.roll(Calendar.DAY_OF_YEAR, num);
            if (theCal2.after(theCal) && num > 0) {
                theCal.roll(Calendar.YEAR, 1);
            }
            if (theCal.after(theCal2) && num < 0) {
                theCal.roll(Calendar.YEAR, -1);
            }
        }
        d = theCal.getTime();
        return d;
    }

    /**
	 * convert date to time stamp.
	 * @param d
	 * @return
	 */
    public static String stamp(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        StringBuffer buf = new StringBuffer();
        buf.append(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        buf.append(StringUtil.fillBefore(month, 2, '0'));
        buf.append(c.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        buf.append(StringUtil.fillBefore(hour, 2, '0'));
        String min = String.valueOf(c.get(Calendar.MINUTE));
        buf.append(StringUtil.fillBefore(min, 2, '0'));
        String seconds = String.valueOf(c.get(Calendar.SECOND));
        buf.append(StringUtil.fillBefore(seconds, 2, '0'));
        return buf.toString();
    }
}
