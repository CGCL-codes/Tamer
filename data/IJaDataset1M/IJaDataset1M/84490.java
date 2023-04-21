package org.avaje.ebean.server.lib.cron;

import java.util.Calendar;
import java.util.StringTokenizer;

/**
 * A cron like syntax for scheduling.
 * 
 * <p>It uses 5 values to indicate the schedule in terms of Hours, Minutes, 
 * Months, DaysOfWeek and DaysOfMonth.
 * Each of the 5 values can include characters '*', ',' and '-' where </P>
 * <ul>
 * <li><B>*</B> - indicates to fire on all Hours, Minutes, Months etc
 * <li><b>0,5,10</b> - indicates to fire on the 0th, 5th and 10th Hour, Minute, Month etc
 * <li><b>6-8,12</b> - indicates to fire on the 6th through to 8th and 12th Hour, Minute, Month etc
 * </ul>
 * <p>
 * e.g. 59 23 * * *              == fire every day at 23:59 <br>
 * e.g. 0-5,15,45 12,13 * * *    == fire in the hours of 12 and 13, and minutes 0 thru 5, 15 and 45.<br>
 * </p>
 * <p> Hours are between 0 and 23, Minutes are between 0 and 59, Months are between 0 and 11, 
 * DaysOfWeek are between 0 and 6 starting with Sunday as 0, DaysOfMonth are between 0 and 30.
 * </p>
 */
public class CronSchedule {

    private String schedule;

    private boolean[] bHours;

    private boolean[] bMinutes;

    private boolean[] bMonths;

    private boolean[] bDaysOfWeek;

    private boolean[] bDaysOfMonth;

    /**
	 * Create the CronSchedule specifying the schedule in cron like format. 
	 *
	 * @param scheduleLine the cron like schedule.  Example 23 59 * * *.
	 */
    public CronSchedule(String scheduleLine) {
        setSchedule(scheduleLine);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof CronSchedule) {
            CronSchedule cs = (CronSchedule) obj;
            if (schedule.equals(cs.getSchedule())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int hc = CronSchedule.class.getName().hashCode();
        hc = hc * 31 + schedule.hashCode();
        return hc;
    }

    private void initBooleanArrays() {
        bHours = new boolean[24];
        bMinutes = new boolean[60];
        bMonths = new boolean[12];
        bDaysOfWeek = new boolean[7];
        bDaysOfMonth = new boolean[31];
        for (int i = 0; i < 60; i++) {
            if (i < 24) bHours[i] = false;
            if (i < 60) bMinutes[i] = false;
            if (i < 12) bMonths[i] = false;
            if (i < 7) bDaysOfWeek[i] = false;
            if (i < 31) bDaysOfMonth[i] = false;
        }
    }

    /**
	 * Set the schedule in cron like format.
	 */
    public void setSchedule(String schedule) {
        this.schedule = schedule;
        initBooleanArrays();
        StringTokenizer tokenizer = new StringTokenizer(schedule);
        int numTokens = tokenizer.countTokens();
        for (int i = 0; tokenizer.hasMoreElements(); i++) {
            String token = tokenizer.nextToken();
            switch(i) {
                case 0:
                    parseToken(token, bMinutes, false);
                    break;
                case 1:
                    parseToken(token, bHours, false);
                    break;
                case 2:
                    parseToken(token, bDaysOfMonth, true);
                    break;
                case 3:
                    parseToken(token, bMonths, true);
                    break;
                case 4:
                    parseToken(token, bDaysOfWeek, false);
                    break;
                case 5:
                    break;
                case 6:
                    break;
                default:
                    break;
            }
        }
        if (numTokens < 5) {
            String msg = "The schedule[" + schedule + "] did not contain enough tokens (5 required) [" + numTokens + "].";
            throw new RuntimeException(msg);
        }
    }

    private void parseToken(String token, boolean[] arrayBool, boolean bBeginInOne) {
        int i;
        try {
            if (token.equals("*")) {
                for (i = 0; i < arrayBool.length; i++) {
                    arrayBool[i] = true;
                }
                return;
            }
            int index = token.indexOf(",");
            if (index > 0) {
                StringTokenizer tokenizer = new StringTokenizer(token, ",");
                while (tokenizer.hasMoreTokens()) {
                    parseToken(tokenizer.nextToken(), arrayBool, bBeginInOne);
                }
                return;
            }
            index = token.indexOf("-");
            if (index > 0) {
                int start = Integer.parseInt(token.substring(0, index));
                int end = Integer.parseInt(token.substring(index + 1));
                if (bBeginInOne) {
                    start--;
                    end--;
                }
                for (int j = start; j <= end; j++) arrayBool[j] = true;
                return;
            }
            index = token.indexOf("/");
            if (index > 0) {
                int each = Integer.parseInt(token.substring(index + 1));
                for (int j = 0; j < arrayBool.length; j += each) arrayBool[j] = true;
                return;
            } else {
                int iValue = Integer.parseInt(token);
                if (bBeginInOne) {
                    iValue--;
                }
                arrayBool[iValue] = true;
                return;
            }
        } catch (Exception e) {
            String msg = "The schedule[" + schedule + "] had a problem parsing a token [" + token + "].";
            throw new RuntimeException(msg, e);
        }
    }

    /**
	 * Return true if the schedule matches to this minute.
	 */
    public boolean isScheduledToRunNow(Calendar thisMinute) {
        return (bHours[thisMinute.get(Calendar.HOUR_OF_DAY)] && bMinutes[thisMinute.get(Calendar.MINUTE)] && bMonths[thisMinute.get(Calendar.MONTH)] && bDaysOfWeek[thisMinute.get(Calendar.DAY_OF_WEEK) - 1] && bDaysOfMonth[thisMinute.get(Calendar.DAY_OF_MONTH) - 1]);
    }

    /**
	 * return the schedule in cron String format.
	 */
    public String getSchedule() {
        return schedule;
    }
}
