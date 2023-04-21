package com.rome.syncml.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 类DateUtil.java的实现描述：TODO 类实现描述 
 * @author 11 2011-11-5 下午05:05:32
 */
public class DateUtil {

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public static Date getDate(String str) {
        try {
            return df.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }
}
