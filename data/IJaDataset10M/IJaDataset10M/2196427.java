package org.powerstone.workflow.util;

import java.text.*;
import java.sql.*;

public class TimeUtil {

    public static String getID(String prefix) {
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
        String random = new Float(Math.random()).toString().substring(2);
        if (random.indexOf("E") > 0) {
            random = random.substring(0, random.indexOf("E"));
        }
        String back = prefix + format.format(new Timestamp(System.currentTimeMillis())) + random;
        return back;
    }

    public static String getTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
        return format.format(new Timestamp(System.currentTimeMillis()));
    }
}
