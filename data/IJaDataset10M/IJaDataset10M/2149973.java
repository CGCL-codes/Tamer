package com.siberhus.commons.converter;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class SqlTimestampTypeConverter implements ITypeConverter<Timestamp> {

    private Locale locale;

    private DateFormat[] formats;

    public void setLocale(Locale locale) {
        this.locale = locale;
        this.formats = getDateFormats();
    }

    /** The default set of date patterns used to parse dates with SimpleDateFormat. */
    private String[] formatStrings = new String[] { "dd/MM/yyyy HH:mm:ss", "dd/MM/yyyy" };

    public void setFormatStrings(String[] formatStrings) {
        this.formatStrings = formatStrings;
    }

    public String[] getFormatStrings() {
        return formatStrings;
    }

    protected DateFormat[] getDateFormats() {
        String[] formatStrings = getFormatStrings();
        SimpleDateFormat[] dateFormats = new SimpleDateFormat[formatStrings.length];
        for (int i = 0; i < formatStrings.length; ++i) {
            dateFormats[i] = new SimpleDateFormat(formatStrings[i], locale);
            dateFormats[i].setLenient(false);
        }
        return dateFormats;
    }

    public Timestamp convert(String input) throws ConvertException {
        return convert(input, Timestamp.class);
    }

    public Timestamp convert(String input, Class<? extends Timestamp> targetType) throws ConvertException {
        java.util.Date date = null;
        for (DateFormat format : this.formats) {
            try {
                date = format.parse(input);
                break;
            } catch (ParseException pe) {
            }
        }
        if (date != null) {
            return new Timestamp(date.getTime());
        } else {
            throw new ConvertException("invalidDate");
        }
    }
}
