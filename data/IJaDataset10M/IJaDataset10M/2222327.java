package de.fmannan.addbook.editor.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import de.fmannan.addbook.editor.types.TypesUtil;

public class DateUtil {

    private static final DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

    private static final String INVALID_STRING_DISPLAY = "";

    static {
        dateFormatter.setLenient(false);
    }

    /**
	 * DateUtil is not supposed to be instantiated, since it is a pure
	 * utility class.
	 */
    private DateUtil() {
    }

    /**
	 * Formats a Date to a String in a preset format: dd.MM.yyyy
	 * 
	 * @param date
	 *            which should be formatted to a String
	 * @return a String representing the given {@link Date}
	 */
    public static String formatDate(Date date) {
        if (date != null && TypesUtil.isValidDate(date)) {
            Calendar cal = new GregorianCalendar();
            cal.setTime(date);
            return cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.YEAR);
        }
        return "";
    }

    /**
	 * Parses a String and transforms it into a {@link Date} object.
	 * 
	 * @param dateString
	 *            the Datestring that should be parsed
	 * @return a {@link Date} representing the given String
	 * @throws ParseException
	 */
    public static Date parseDate(String dateString) throws ParseException {
        return (dateString != null && !dateString.equals(INVALID_STRING_DISPLAY)) ? dateFormatter.parse(dateString) : TypesUtil.getInvalidDate();
    }
}
