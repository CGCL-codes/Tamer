package com.markatta.hund.wicket;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

/**
 * There is a bug in wicket making the dates from entities that really are
 * <code>Timestamp</code>s beeing formatted as just hh:mm
 *
 * @author johan
 */
public class TimestampConverter implements IConverter {

    public Object convertToObject(String value, Locale locale) {
        if (value == null) {
            return null;
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        try {
            Date date = format.parse(value);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            throw new ConversionException("Cannot parse '" + value + "' using format " + format).setSourceValue(value).setTargetType(Timestamp.class).setConverter(this).setLocale(locale);
        }
    }

    public String convertToString(Object value, Locale locale) {
        if (value == null) {
            return null;
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        return format.format(value);
    }
}
