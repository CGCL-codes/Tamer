package org.salamandra.web.core.property.converter.adapt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;

public class DateAdapter implements IAdapter<Date, ParseException> {

    private java.text.DateFormat[] dateFormats;

    public DateAdapter(String[] patters) {
        assert patters != null;
        Locale locale = LocaleContextHolder.getLocale();
        int len = patters.length;
        dateFormats = new java.text.DateFormat[len];
        for (int i = 0; i < len; i++) {
            dateFormats[i] = new SimpleDateFormat(patters[i], locale);
        }
    }

    public Date format(Object value) throws ParseException {
        Date date = null;
        int len = dateFormats.length;
        for (int i = 0; i < len; i++) {
            try {
                date = (dateFormats[i].parse(value.toString()));
                break;
            } catch (ParseException e) {
                if (i == (len - 1)) {
                    throw e;
                }
            }
        }
        return date;
    }
}
