package org.singer.type;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.naming.OperationNotSupportedException;

/**
 * Describe an XML schema Date.
 * <p>The format is defined by W3C XML Schema Recommendation and ISO8601
 * i.e <tt>(-)CCYY-MM-DD(Z|(+|-)hh:mm)</tt>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision: 1.1.1.1 $
 */
public class Date extends DateTimeBase {

    /**
     * Flag indicating that we are still looking for a year
     */
    private static final int YEAR_FLAG = 15;

    /**
    * Flag indicating that we are still looking for a month
    */
    private static final int MONTH_FLAG = 7;

    /**
    * Flag indicating that we are still looking for a day
    */
    private static final int DAY_FLAG = 3;

    /**
    * Flag indicating that we are still looking for a timeZone
    */
    private static final int TIMEZONE_FLAG = 1;

    private static final String BAD_DATE = "Bad Date format: ";

    /**
     * The Date Format
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public Date() {
    }

    /**
     * Constructs a XML Schema Date instance given all the values of
     * the different fields.
     * By default a Date is not UTC and is local.
     * @param values an array of shorts that represent the different fields of Time.
     */
    public Date(short[] values) {
        setValues(values);
    }

    /**
     * This constructor is used to convert a long value representing a Date
     * to a new org.exolab.castor.types.Date instance.
     * <p>Note : all the information concerning the time part of
     * the java.util.Date is lost since a W3C Schema Date only represents
     * CCYY-MM-YY
     * @param dateAsLong Date represented in from of a long value.
     */
    public Date(long dateAsLong) {
        this(new java.util.Date(dateAsLong));
    }

    /**
     * This constructor is used to convert a java.util.Date into
     * a new org.exolab.castor.types.Date
     * <p>Note : all the information concerning the time part of
     * the java.util.Date is lost since a W3C Schema Date only represents
     * CCYY-MM-YY
     */
    public Date(java.util.Date dateRef) {
        GregorianCalendar tempCalendar = new GregorianCalendar();
        tempCalendar.setTime(dateRef);
        setCentury((short) (tempCalendar.get(Calendar.YEAR) / 100));
        try {
            setYear((short) (tempCalendar.get(Calendar.YEAR) % 100));
            setMonth((short) (tempCalendar.get(Calendar.MONTH) + 1));
            setDay((short) (tempCalendar.get(Calendar.DAY_OF_MONTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a date from a string
     * @param date the string representing the date
     */
    public Date(String date) throws java.text.ParseException {
        this();
        parseDateInternal(date, this);
    }

    /**
     * Sets all the fields by reading the values in an array
     * <p>if a Time Zone is specificied it has to be set by using
     * {@link DateTimeBase#setZone(short, short) setZone}.
     * @param values an array of shorts with the values
     * the array is supposed to be of length 4 and ordered like
     * the following:
     * <ul>
     *      <li>century</li>
     *      <li>year</li>
     *      <li>month</li>
     *      <li>day</li>
     * </ul>
     *
     */
    public void setValues(short[] values) {
        if (values.length != 4) throw new IllegalArgumentException("Date#setValues: not the right number of values");
        try {
            this.setCentury(values[0]);
            this.setYear(values[1]);
            this.setMonth(values[2]);
            this.setDay(values[3]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an array of short with all the fields that describe
     * this Date type.
     * <p>Note:the time zone is not included.
     * @return  an array of short with all the fields that describe
     * this Date type.
     */
    public short[] getValues() {
        short[] result = null;
        result = new short[4];
        result[0] = this.getCentury();
        result[1] = this.getYear();
        result[2] = this.getMonth();
        result[3] = this.getDay();
        return result;
    }

    /**
     * converts this Date into a local java Date.
     * @return a local date representing this Date.
     */
    public java.util.Date toDate() {
        java.util.Date date = null;
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        if (isUTC()) {
            SimpleTimeZone timeZone = new SimpleTimeZone(0, "UTC");
            int offset = 0;
            offset = (int) ((this.getZoneMinute() + this.getZoneHour() * 60) * 60 * 1000);
            offset = isZoneNegative() ? -offset : offset;
            timeZone.setRawOffset(offset);
            timeZone.setID(TimeZone.getAvailableIDs(offset)[0]);
            df.setTimeZone(timeZone);
        }
        try {
            date = df.parse(this.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    /**
     * Converts this date into a long value.
     * @return This date instance as a long value.
     */
    public long toLong() {
        java.util.Date date = toDate();
        return date.getTime();
    }

    /**
     * convert this Date to a string
     * The format is defined by W3C XML Schema recommendation and ISO8601
     * i.e (+|-)CCYY-MM-DD
     * @return a string representing this Date
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        if (isNegative()) result.append('-');
        result.append(this.getCentury());
        if (result.length() == 1) result.insert(0, 0);
        if ((this.getYear() / 10) == 0) result.append(0);
        result.append(this.getYear());
        result.append('-');
        if ((this.getMonth() / 10) == 0) result.append(0);
        result.append(this.getMonth());
        result.append('-');
        if ((this.getDay() / 10) == 0) result.append(0);
        result.append(this.getDay());
        if (isUTC()) {
            if ((this.getZoneHour() == 0) && (this.getZoneMinute() == 0)) result.append('Z'); else {
                StringBuffer timeZone = new StringBuffer();
                if (isZoneNegative()) timeZone.append('-'); else timeZone.append('+');
                if ((this.getZoneHour() / 10) == 0) timeZone.append(0);
                timeZone.append(this.getZoneHour());
                timeZone.append(':');
                if ((this.getZoneMinute() / 10) == 0) timeZone.append(0);
                timeZone.append(this.getZoneMinute());
                result.append(timeZone.toString());
                timeZone = null;
            }
        }
        return result.toString();
    }

    /**
     * parse a String and convert it into an java.lang.Object
     * @param str the string to parse
     * @return an Object represented by the string
     * @throws ParseException a parse exception is thrown if the string to parse
     *                        does not follow the rigth format (see the description
     *                        of this class)
     */
    public static Object parse(String str) throws ParseException {
        return parseDate(str);
    }

    /**
    * parse a String and convert it into a Date.
    * @param str the string to parse
    * @return the Date represented by the string
    * @throws ParseException a parse exception is thrown if the string to parse
    *                        does not follow the rigth format (see the description
    *                        of this class)
    */
    public static Date parseDate(String str) throws ParseException {
        Date result = new Date();
        return parseDateInternal(str, result);
    }

    private static Date parseDateInternal(String str, Date result) throws ParseException {
        if (str == null) throw new IllegalArgumentException("The string to be parsed must not " + "be null.");
        if (result == null) result = new Date();
        char[] chars = str.toCharArray();
        int idx = 0;
        if (chars[idx] == '-') {
            idx++;
            result.setNegative();
        }
        boolean hasNumber = false;
        boolean has2Digits = false;
        short number = 0;
        short number2 = 0;
        int flags = YEAR_FLAG;
        while (idx < chars.length) {
            char ch = chars[idx++];
            try {
                switch(ch) {
                    case '-':
                        if (flags == YEAR_FLAG) {
                            if ((number != 0) || (number2 != 0)) {
                                if (has2Digits) result.setCentury(number); else throw new ParseException(BAD_DATE + str + "\nThe Century field must have 2 digits.", idx);
                                result.setYear(number2);
                                number2 = -1;
                                flags = MONTH_FLAG;
                            } else throw new ParseException(BAD_DATE + str + "\n'0000' is not allowed as a year.", idx);
                        } else if (flags == MONTH_FLAG) {
                            if ((has2Digits) && (number2 == -1)) {
                                result.setMonth(number);
                                flags = DAY_FLAG;
                            } else throw new ParseException(BAD_DATE + str + "\nThe month field must have 2 digits.", idx);
                        } else if ((flags == DAY_FLAG) && (number2 == -1)) {
                            if (has2Digits) {
                                result.setUTC();
                                result.setZoneNegative(true);
                                result.setDay(number);
                                flags = TIMEZONE_FLAG;
                            } else throw new ParseException(BAD_DATE + str + "\nThe day field must have 2 digits.", idx);
                        } else throw new ParseException(BAD_DATE + str + "\n '-' " + DateTimeBase.WRONGLY_PLACED, idx);
                        hasNumber = false;
                        has2Digits = false;
                        break;
                    case 'Z':
                        if (flags != DAY_FLAG) throw new ParseException("'Z' " + DateTimeBase.WRONGLY_PLACED, idx); else result.setUTC();
                        break;
                    case '+':
                        if (flags != DAY_FLAG) throw new ParseException("'+' " + DateTimeBase.WRONGLY_PLACED, idx); else {
                            if ((has2Digits) && (number2 == -1)) {
                                result.setDay(number);
                                result.setUTC();
                                flags = TIMEZONE_FLAG;
                                hasNumber = false;
                                has2Digits = false;
                            } else throw new ParseException(BAD_DATE + str + "\nThe day field must have 2 digits.", idx);
                        }
                        break;
                    case ':':
                        if (flags != TIMEZONE_FLAG) throw new ParseException(BAD_DATE + str + "\n':' " + DateTimeBase.WRONGLY_PLACED, idx);
                        number2 = number;
                        number = -1;
                        flags = 0;
                        hasNumber = false;
                        has2Digits = false;
                        break;
                    default:
                        if (('0' <= ch) && (ch <= '9')) {
                            if (hasNumber) {
                                if (has2Digits) number2 = (short) ((number2 * 10) + (ch - 48)); else {
                                    number = (short) ((number * 10) + (ch - 48));
                                    has2Digits = true;
                                }
                            } else {
                                hasNumber = true;
                                number = (short) (ch - 48);
                            }
                        } else throw new ParseException(str + ": Invalid character: " + ch, idx);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            if (flags != DAY_FLAG && flags != 0) throw new ParseException(BAD_DATE + str + "\nA date must follow the pattern CCYY-MM-DD(Z|((+|-)hh:mm)).", idx); else if (flags == DAY_FLAG) {
                if ((has2Digits) && (number2 == -1)) result.setDay(number); else throw new ParseException(BAD_DATE + str + "\nThe day field must have 2 digits.", idx);
            } else if (flags == 0) {
                if (number != -1) result.setZone(number2, number); else throw new ParseException(str + "\n In a time zone, the minute field must always be present.", idx);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public short getHour() {
        String err = "Date: couldn't access to the Hour field.";
        System.out.println(err);
        return -1;
    }

    public short getMinute() {
        String err = "Date: couldn't access to the Minute field.";
        System.out.println(err);
        return -1;
    }

    public short getSeconds() {
        String err = "Date: couldn't access to the Second field.";
        System.out.println(err);
        return -1;
    }

    public short getMilli() {
        String err = "Date: couldn't access to the Millisecond field.";
        System.out.println(err);
        return -1;
    }

    public void setHour(short hour) {
        String err = "Date: couldn't access to the Hour field.";
        System.out.println(err);
    }

    public void setMinute(short minute) {
        String err = "Date: couldn't access to the Minute field.";
        System.out.println(err);
    }

    public void setSecond(short second) {
        String err = "Date: couldn't access to the second field.";
        System.out.println(err);
    }

    public void setMilliSecond(short millisecond) {
        String err = "Date: couldn't access to the Millisecond field.";
        System.out.println(err);
    }
}
