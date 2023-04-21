package org.maveryx.util;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStamp {

    public static String getTimeStamp() {
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ssZ");
        StringBuffer date = format.format(currentDate, new StringBuffer(), new FieldPosition(0));
        return date.toString();
    }
}
