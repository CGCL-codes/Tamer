package net.sf.jabs.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {

    public int stringToInt(String i) {
        return Integer.parseInt(i == null ? "0" : i);
    }

    public static String logBanner(String caption) {
        StringBuffer sb = new StringBuffer();
        sb.append("\n+-------------------------------------+\n");
        sb.append("|" + caption + "\n");
        sb.append("+-------------------------------------+");
        return sb.toString();
    }

    public static String getCurrentTime() {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
        return df.format(System.currentTimeMillis());
    }

    public String getDate() {
        return getDate(new Timestamp(System.currentTimeMillis()), Constants.DEFAULT_DATE_FORMAT);
    }

    public String getDate(Timestamp t) {
        return getDate(t, Constants.DEFAULT_DATE_FORMAT);
    }

    public String getShortDate(Timestamp t) {
        return getDate(t, Constants.SHORT_DATE_FORMAT);
    }

    public String getTime(Timestamp t) {
        return getDate(t, Constants.DEFAULT_TIME_FORMAT);
    }

    public String getDate(Timestamp t, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(t);
    }

    public String getElapsedTime(Date startTime, Date endTime) {
        long start = startTime.getTime();
        long end = endTime.getTime();
        long elapsed = (end - start) / 1000;
        return elapsedHMS(elapsed);
    }

    public String getElapsedTime(Timestamp startTime, Timestamp endTime) {
        long start = startTime.getTime();
        long end = endTime.getTime();
        long elapsed = (end - start) / 1000;
        return elapsedHMS(elapsed);
    }

    protected String elapsedHMS(long timeInSeconds) {
        long hours, minutes, seconds;
        hours = timeInSeconds / 3600;
        timeInSeconds = timeInSeconds - (hours * 3600);
        minutes = timeInSeconds / 60;
        timeInSeconds = timeInSeconds - (minutes * 60);
        seconds = timeInSeconds;
        String e = MessageFormat.format("{0,number,00}h:{1,number,00}m:{2,number,00}s", hours, minutes, seconds);
        return e;
    }

    public static String formatSize(Object obj, boolean mb) {
        long bytes = -1L;
        if (obj instanceof Long) {
            bytes = ((Long) obj).longValue();
        } else if (obj instanceof Integer) {
            bytes = ((Integer) obj).intValue();
        }
        if (mb) {
            long mbytes = bytes / (1024 * 1024);
            long rest = ((bytes - (mbytes * (1024 * 1024))) * 100) / (1024 * 1024);
            return (mbytes + "." + ((rest < 10) ? "0" : "") + rest + " MB");
        } else {
            if (bytes < 1024) {
                return ("1 KB");
            } else {
                return ((bytes / 1024) + " KB");
            }
        }
    }

    /**
     * Generate a random password.
     * @param n
     * @return
     */
    public static String getPassword(int n) {
        char[] pw = new char[n];
        int c = 'A';
        int r1 = 0;
        for (int i = 0; i < n; i++) {
            r1 = (int) (Math.random() * 3);
            switch(r1) {
                case 0:
                    c = '0' + (int) (Math.random() * 10);
                    break;
                case 1:
                    c = 'a' + (int) (Math.random() * 26);
                    break;
                case 2:
                    c = 'A' + (int) (Math.random() * 26);
                    break;
            }
            pw[i] = (char) c;
        }
        return new String(pw);
    }

    public static boolean isNullOrEmpty(String value) {
        if (value == null) {
            return true;
        }
        if (value.equals("")) {
            return true;
        }
        if (value.equals("null")) {
            return true;
        }
        return false;
    }
}
