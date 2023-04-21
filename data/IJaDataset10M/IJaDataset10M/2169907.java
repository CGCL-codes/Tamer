package com.jyams.util;

import org.apache.struts2.json.bridge.FieldBridge;

public class DateFormatFieldBridge implements FieldBridge {

    @Override
    public String objectToString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Long) {
            return DateTimeUtils.convertLongToString((Long) obj, "yyyy-MM-dd HH:mm:ss");
        }
        return obj.toString();
    }
}
