package com.common.struts.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.apache.struts2.util.StrutsTypeConverter;

/**
 * struts日期转换，用来处理日期格式
 * 
 * @author 孙树林
 * 
 */
public class DateConverter extends StrutsTypeConverter {

    public static final String FORMAT_DATA = "yyyy-MM-dd";

    public static final String FORMAT_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
	 * 从字符串转换成日期
	 * 
	 * @param context
	 * @param values
	 * @param toClass
	 * @return
	 */
    @SuppressWarnings("rawtypes")
    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        if (values == null || values.length == 0) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIME);
        Date date = null;
        String dateString = values[0];
        if (dateString != null) {
            try {
                date = sdf.parse(dateString);
            } catch (ParseException e) {
                date = null;
            }
            if (date == null) {
                sdf = new SimpleDateFormat(FORMAT_DATA);
                try {
                    date = sdf.parse(dateString);
                } catch (ParseException e) {
                    date = null;
                }
            }
        }
        return date;
    }

    /**
	 * 将日期转换成字符串
	 * 
	 * @param context
	 * @param o
	 * @return
	 */
    @SuppressWarnings("rawtypes")
    @Override
    public String convertToString(Map context, Object o) {
        if (o instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIME);
            return sdf.format((Date) o);
        }
        return "";
    }
}
