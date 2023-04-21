package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Time {

    public static String nowTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(new Date().getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(c.getTime());
    }

    public static String GetTimeToString() {
        Calendar now = Calendar.getInstance();
        String ImageName = Integer.toString(now.get(Calendar.YEAR)) + Integer.toString(now.get(Calendar.MONTH)) + Integer.toString(now.get(Calendar.DAY_OF_MONTH)) + Integer.toString(now.get(Calendar.HOUR_OF_DAY)) + Integer.toString(now.get(Calendar.MINUTE)) + Integer.toString(now.get(Calendar.SECOND)) + Integer.toString(now.get(Calendar.MILLISECOND));
        return ImageName;
    }

    public static String GetTimeToFormatString() {
        Calendar now = Calendar.getInstance();
        String ImageName = Format.IntToString(now.get(Calendar.YEAR), 4) + Format.IntToString(now.get(Calendar.MONTH), 2) + Format.IntToString(now.get(Calendar.DAY_OF_MONTH), 2) + Format.IntToString(now.get(Calendar.HOUR_OF_DAY), 2) + Format.IntToString(now.get(Calendar.MINUTE), 2) + Format.IntToString(now.get(Calendar.SECOND), 2) + Format.IntToString(now.get(Calendar.MILLISECOND), 3);
        return ImageName;
    }
}
