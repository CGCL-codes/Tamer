package my.jutils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ISODateFormat http://www.w3.org/TR/1998/NOTE-datetime-19980827
 * http://en.wikipedia.org/wiki/Iso_date
 * 
 * @author "Luis Alfonso Vega Garcia" <vegacom@gmail.com>
 */
public class ISODateFormat {

    public static final String ISO_DATE_TIME_FMT = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String ISO_DATE_FMT = "yyyy-MM-dd";

    public static String formatDate(Date pdate) {
        Date date = pdate;
        if (date == null) date = new Date();
        return new SimpleDateFormat(ISO_DATE_FMT).format(date);
    }

    public static String formatDateTime(Date pdateTime) {
        Date dateTime = pdateTime;
        if (dateTime == null) dateTime = new Date();
        return new SimpleDateFormat(ISO_DATE_TIME_FMT).format(dateTime);
    }

    public static Date parseDate(String source) throws ParseException {
        Date date = new SimpleDateFormat(ISO_DATE_FMT).parse(source);
        return date;
    }

    public static Date parseDateTime(String source) throws ParseException {
        Date date = new SimpleDateFormat(ISO_DATE_TIME_FMT).parse(source);
        return date;
    }
}
