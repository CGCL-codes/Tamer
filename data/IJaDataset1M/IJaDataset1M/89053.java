package net.sf.fileexchange.util.http;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class HTTPDateFormat {

    private final SimpleDateFormat rfc1123Format;

    private final SimpleDateFormat rfc850Format;

    private final SimpleDateFormat asctimeFormat0;

    private final SimpleDateFormat asctimeFormat1;

    public HTTPDateFormat() {
        rfc1123Format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        rfc850Format = new SimpleDateFormat("EEEEEEEEE, dd-MMM-yy HH:mm:ss zzz");
        asctimeFormat0 = new SimpleDateFormat("EEE MMM  d HH:mm:ss yyyy");
        asctimeFormat1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
        rfc1123Format.setTimeZone(TimeZone.getTimeZone("GMT"));
        rfc850Format.setTimeZone(TimeZone.getTimeZone("GMT"));
        asctimeFormat0.setTimeZone(TimeZone.getTimeZone("GMT"));
        asctimeFormat1.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    Date parseRFC1233Date(String dateToParse) {
        return parseWith(dateToParse, rfc1123Format);
    }

    Date parseWith(String dateToParse, DateFormat format) {
        ParsePosition position = new ParsePosition(0);
        Date date = format.parse(dateToParse, position);
        if (position.getIndex() != dateToParse.length()) return null;
        return date;
    }

    /**
	 * 
	 * @param dateToParse
	 *            the date to parse.
	 * @return the parsed date or null, if string does not represent a date in
	 *         the HTTP date format.
	 */
    public Date parse(String dateToParse) {
        Date date = parseWith(dateToParse, rfc1123Format);
        if (date != null) return date;
        date = parseWith(dateToParse, rfc850Format);
        if (date != null) return date;
        date = parseWith(dateToParse, asctimeFormat0);
        if (date != null) return date;
        return parseWith(dateToParse, asctimeFormat1);
    }

    public String format(Date date) {
        return rfc1123Format.format(date);
    }
}
