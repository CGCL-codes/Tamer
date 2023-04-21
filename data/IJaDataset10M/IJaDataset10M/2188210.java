package com.powerhua.core.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;
import javax.servlet.ServletRequest;

public class CommonUtils {

    public static String getStringFromReq(ServletRequest map, String key) {
        String result = "";
        String obj = map.getParameter(key);
        if (obj != null) {
            result = obj;
        }
        return result;
    }

    public static String getDateTimeFormat(Timestamp d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(d);
    }

    public static String getDateFormat(Timestamp d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d);
    }

    public static String getUuid() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString().toUpperCase();
        return uuidStr;
    }

    public static int converStr2Int(String num) {
        int intNum = 0;
        if (num == null) {
            return intNum;
        }
        try {
            intNum = Integer.parseInt(num);
        } catch (Exception e) {
            e.printStackTrace();
            return intNum;
        }
        return intNum;
    }
}
