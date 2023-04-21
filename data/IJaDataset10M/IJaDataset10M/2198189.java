package net.sf.dozer.util.mapping.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.commons.beanutils.Converter;

/**
 * Internal class for converting Supported Data Types --> Calendar. Supported source data types include Date, Calendar,
 * String, Objects that return a long from their toString(). Only intended for internal use.
 * 
 * @author tierney.matt
 */
public class CalendarConverter implements Converter {

    private DateFormat dateFormat;

    public CalendarConverter(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Object convert(Class destClass, Object srcObj) {
        Calendar result = new GregorianCalendar();
        Class srcFieldClass = srcObj.getClass();
        if (java.util.Date.class.isAssignableFrom(srcFieldClass)) {
            result.setTime((java.util.Date) srcObj);
        } else if (Calendar.class.isAssignableFrom(srcFieldClass)) {
            Calendar c = (Calendar) srcObj;
            result.setTime(c.getTime());
        } else if (dateFormat != null && String.class.isAssignableFrom(srcFieldClass)) {
            try {
                result.setTime(new Date(dateFormat.parse((String) srcObj).getTime()));
            } catch (ParseException e) {
                throw new ConversionException("Unable to parse source object using specified date format", e);
            }
        } else {
            try {
                result.setTime(new Date(Long.parseLong(srcObj.toString())));
            } catch (NumberFormatException e) {
                throw new ConversionException("Unable to determine time in millis of source object", e);
            }
        }
        return result;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }
}
