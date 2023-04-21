package com.guanghua.brick.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 进行日期计算的一些工具类
 * @author Administrator
 *
 */
public class DateUtil {

    /**
	 * before的年代加上一个diff年后是否比after年大
	 * 一般用于计算在多少年之内的判断
	 * @param before
	 * @param after
	 * @param diff
	 * @param format
	 * @return
	 * @throws ParseException
	 */
    public static boolean differenceTowDateLessThanByYear(String before, String after, int diff, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return differenceTowDateLessThanByYear(sdf.parse(before), sdf.parse(after), diff);
    }

    public static boolean differenceTowDateLessThanByYear(String before, String after, int diff) throws ParseException {
        return differenceTowDateLessThanByYear(before, after, diff, "yyyy-MM-dd");
    }

    public static boolean differenceTowDateLessThanByYear(Date before, Date after, int diff) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(before);
        c.add(Calendar.YEAR, diff);
        return c.getTime().before(after);
    }
}
