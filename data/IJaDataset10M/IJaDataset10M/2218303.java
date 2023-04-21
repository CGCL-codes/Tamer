package net.sourceforge.sql.function.date;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import net.sourceforge.sql.function.SqlFunctionException;
import net.sourceforge.sql.function.date.DateTimeUtil.DatePart;

/**
 * Returns a new datetime value based on adding an interval to the specified
 * date
 * 
 * @author ekonstantinov
 */
public class DateAddFunction {

    /**
	 * 
	 * @param part
	 *            specifies on which {@code DatePart} of the date to return a
	 *            new value
	 * @param number
	 *            value used to increment part. If you specify a value that is
	 *            not an integer, the fractional part of the value is discarded.
	 *            For example, if you specify day for datepart and 1.75 for
	 *            number, date is incremented by 1.
	 * @param date
	 *            the GMT time
	 * @return Date result of date part addtion
	 * 
	 * @see DatePart
	 * @see DateDiffFunction
	 */
    public static Date execute(DatePart part, Double number, Date date) {
        Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.setTime(date);
        int intNumber = number.intValue();
        switch(part) {
            case year:
            case yyyy:
            case yy:
                {
                    calendar.add(Calendar.YEAR, intNumber);
                    break;
                }
            case quarter:
            case qq:
            case q:
                {
                    calendar.add(Calendar.MONTH, intNumber * 3);
                    break;
                }
            case month:
            case m:
            case mm:
                {
                    calendar.add(Calendar.MONTH, intNumber);
                    break;
                }
            case dayofyear:
            case dy:
            case y:
            case day:
            case dd:
            case d:
                {
                    calendar.add(Calendar.DAY_OF_MONTH, intNumber);
                    break;
                }
            case week:
            case wk:
            case ww:
                {
                    calendar.add(Calendar.WEEK_OF_MONTH, intNumber);
                    break;
                }
            case hour:
            case hh:
                {
                    calendar.add(Calendar.HOUR_OF_DAY, intNumber);
                    break;
                }
            case minute:
            case mi:
            case n:
                {
                    calendar.add(Calendar.MINUTE, intNumber);
                    break;
                }
            case second:
            case ss:
            case s:
                {
                    calendar.add(Calendar.SECOND, intNumber);
                    break;
                }
            case millisecond:
            case ms:
                {
                    calendar.add(Calendar.MILLISECOND, intNumber);
                    break;
                }
            default:
                {
                    throw new SqlFunctionException(new IllegalArgumentException());
                }
        }
        return calendar.getTime();
    }
}
