package com.jtri.struts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.beanutils.ConversionException;

/**
 * A Converter between Date and String. It uses the pattern defined in the constructor.
 * @author atorres
 */
public class DateConverter implements FormConverter {

    SimpleDateFormat df;

    String type = "D";

    public DateConverter() {
        df = new SimpleDateFormat();
    }

    public DateConverter(String pattern) {
        df = new SimpleDateFormat(pattern);
        df.setLenient(true);
    }

    public Object convert(Class type, Object value) {
        try {
            if (value == null) return null;
            if (type == String.class) {
                return df.format((Date) value);
            } else if (type == Date.class) {
                return value.equals("") ? null : df.parse((String) value);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new ConversionException("Could not convert from " + value.getClass() + " to " + type);
    }

    public Class fromClass() {
        return String.class;
    }

    public Class toClass() {
        return Date.class;
    }
}
