package com.generatescape.date;

import java.util.Calendar;
import java.util.Date;

public class DateStuff {

    public static final long DAY_IN_MILIS = 86400000;

    public static final long TEN_DAY_IN_MILIS = 86400000 * 10;

    /**
   * @param args
   */
    public static void main(String[] args) {
        Date date = new Date();
        Date before = new Date(date.getTime() - TEN_DAY_IN_MILIS);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(before);
        DateDiffer dateDiffer = new DateDiffer(System.currentTimeMillis());
        long dif = dateDiffer.diffDayPeriods(rightNow);
    }
}
