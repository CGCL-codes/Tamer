package org.gruposp2p.dnie.client.util;

import java.util.Date;
import com.google.gwt.i18n.client.DateTimeFormat;

public class DateUtils {

    /**
     * Método que devuelve un Date a partir de un String con formato "yyyy-MM-ddTHH:mm:ss"
     *
     * @param dateString fecha en formato String
     * @return Date fecha en formato Date
     * @throws import java.text.ParseException;
     */
    public static Date getDateFromString(String dateString) {
        DateTimeFormat formatter = DateTimeFormat.getFormat("yyyy-MM-ddTHH:mm:ss");
        return formatter.parse(dateString);
    }

    /**
     * Método que devuelve un String con formato "yyyy-MM-dd'T'HH:mm:ss a partir de un Date"
     *
     * @param Date fecha en formato Date
     * @return dateString fecha en formato String
     * @throws import java.text.ParseException;
     */
    public static String getStringFromDate(Date date) {
        DateTimeFormat formatter = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(date);
    }

    public static String getSimpleStringFromDate(Date date) {
        DateTimeFormat formatter = DateTimeFormat.getFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
}
