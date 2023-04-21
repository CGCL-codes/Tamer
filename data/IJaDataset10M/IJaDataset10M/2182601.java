package org.misrical.util;

import java.util.Calendar;
import static java.lang.Math.floor;

/**
 * @author mustafa
 *
 */
public class Calculations {

    /**
   * Returns the Julian day number that begins at noon of
   * this day, Positive year signifies A.D., negative year B.C.
   * The year after 1 B.C. was 1 A.D.
   * ref :
   *    http://www.astro.uu.nl/~strous/AA/en/reken/juliaansedag.html
   *    
   * @param cal - The target date to be converted to Julian value.
   * @return Returns the double value signifying the Julian date.
   */
    public static Double toJulianValue(Calendar cal) {
        Double returnVal = Double.NaN;
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        double day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        day += hour / 24D;
        month++;
        if (month < 3) {
            month += 12;
            year--;
        }
        double equation1 = 2 - floor(year / 100) + floor(year / 400);
        returnVal = floor(1461 * (year + 4716) / 4) + floor(153 * (month + 1) / 5) + day + equation1 - 1524.5;
        return returnVal;
    }

    /**
   * Returns the Gregorian date for a given Julian date. 
   * 
   * @param julianDay - the double value representing a Julian day.
   * @return Returns the Calendar object representing the Gregorian date.
   * 
   * The details of the algorithm are as follows:
   * 
   * day   = eq8 + julianDay - eq1 + 0.5
   * time = (day % 1) * 24
   * hourOfDay = floor(time)
   * minuteOfHour = (time % 1) * 60
   * ref:
   *    http://www.astro.uu.nl/~strous/AA/en/reken/juliaansedag.html
   */
    public static Calendar toGregorianDate(double julianDay) {
        Calendar returnVal = Calendar.getInstance();
        Double eq1 = floor(julianDay + 0.5);
        Double eq2 = eq1 + 68569;
        Double eq3 = floor(4 * eq2 / 146097);
        Double eq4 = eq2 - floor((146097 * eq3 + 3) / 4);
        Double eq5 = floor(4000 * (eq4 + 1) / 1461001);
        Double eq6 = eq4 - floor(1461 * eq5 / 4) + 31;
        Double eq7 = floor(80 * eq6 / 2447);
        Double eq8 = eq6 - floor(2447 * eq7 / 80);
        Double eq9 = floor(eq7 / 11);
        int year = (int) (100 * (eq3 - 49) + eq5 + eq9);
        int month = (int) (eq7 + 2 - 12 * eq9);
        Double dayTime = eq8 + julianDay - eq1 + 0.5;
        double time = dayTime % 1 * 24;
        int day = dayTime.intValue();
        int hourOfDay = (int) floor(time);
        int minute = (int) ((time % 1) * 60);
        returnVal.clear();
        returnVal.set(year, month - 1, day, hourOfDay, minute);
        return returnVal;
    }
}
