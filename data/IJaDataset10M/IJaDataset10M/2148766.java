package org.datanucleus.store.types.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.datanucleus.exceptions.NucleusDataStoreException;

/**
 * Class to handle the conversion between java.util.Date and a String form.
 */
public class DateStringConverter implements TypeConverter<Date, String> {

    private static final ThreadLocal<FormatterInfo> formatterThreadInfo = new ThreadLocal<FormatterInfo>() {

        protected FormatterInfo initialValue() {
            return new FormatterInfo();
        }
    };

    static class FormatterInfo {

        SimpleDateFormat formatter;
    }

    private DateFormat getFormatter() {
        FormatterInfo formatInfo = formatterThreadInfo.get();
        if (formatInfo.formatter == null) {
            formatInfo.formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        }
        return formatInfo.formatter;
    }

    public Date toMemberType(String str) {
        if (str == null) {
            return null;
        }
        try {
            return getFormatter().parse(str);
        } catch (ParseException pe) {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Date.class.getName()), pe);
        }
    }

    public String toDatastoreType(Date date) {
        return date != null ? getFormatter().format(date) : null;
    }
}
