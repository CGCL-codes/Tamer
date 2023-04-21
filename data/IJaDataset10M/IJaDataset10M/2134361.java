package com.hp.hpl.jena.datatypes.xsd.impl;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.graph.impl.LiteralLabel;

/**
 * Base class for all date/time/duration type representations.
 * Includes support functions for parsing and comparing dates.
 * 
 * @author <a href="mailto:der@hplb.hpl.hp.com">Dave Reynolds</a>
 * @version $Revision: 1.6 $ on $Date: 2006/03/22 13:53:23 $
 */
public class XSDAbstractDateTimeType extends XSDDatatype {

    /**
     * Constructor
     */
    public XSDAbstractDateTimeType(String typename) {
        super(typename);
    }

    /**
     * Compares two instances of values of the given datatype.
     * This ignores lang tags and just uses the java.lang.Number 
     * equality.
     */
    public boolean isEqual(LiteralLabel value1, LiteralLabel value2) {
        return value1.getValue().equals(value2.getValue());
    }

    /** Mask to indicate whether year is present */
    public static final short YEAR_MASK = 0x1;

    /** Mask to indicate whether month is present */
    public static final short MONTH_MASK = 0x2;

    /** Mask to indicate whether day is present */
    public static final short DAY_MASK = 0x4;

    /** Mask to indicate whether time is present */
    public static final short TIME_MASK = 0x8;

    /** Mask to indicate all date/time are present */
    public static final short FULL_MASK = 0xf;

    protected static final int CY = 0, M = 1, D = 2, h = 3, m = 4, s = 5, ms = 6, msscale = 8, utc = 7, hh = 0, mm = 1;

    protected static final int TOTAL_SIZE = 9;

    protected static final int YEAR = 2000;

    protected static final int MONTH = 01;

    protected static final int DAY = 15;

    /**
      * Parses time hh:mm:ss.sss and time zone if any
      *
      * @param start
      * @param end
      * @param data
      * @exception RuntimeException
      */
    protected void getTime(String buffer, int start, int end, int[] data, int[] timeZone) throws RuntimeException {
        int stop = start + 2;
        data[h] = parseInt(buffer, start, stop);
        if (buffer.charAt(stop++) != ':') {
            throw new RuntimeException("Error in parsing time zone");
        }
        start = stop;
        stop = stop + 2;
        data[m] = parseInt(buffer, start, stop);
        if (buffer.charAt(stop++) != ':') {
            throw new RuntimeException("Error in parsing time zone");
        }
        start = stop;
        stop = stop + 2;
        data[s] = parseInt(buffer, start, stop);
        if (stop == end) return;
        start = stop;
        int milisec = buffer.charAt(start) == '.' ? start : -1;
        int sign = findUTCSign(buffer, start, end);
        if (milisec != -1) {
            start = sign < 0 ? end : sign;
            int msEnd = start;
            while (buffer.charAt(msEnd - 1) == '0') msEnd--;
            data[ms] = parseInt(buffer, milisec + 1, msEnd);
            data[msscale] = msEnd - milisec - 1;
        }
        if (sign > 0) {
            if (start != sign) throw new RuntimeException("Error in parsing time zone");
            getTimeZone(buffer, data, sign, end, timeZone);
        } else if (start != end) {
            throw new RuntimeException("Error in parsing time zone");
        }
    }

    /**
      * Parses date CCYY-MM-DD
      *
      * @param start
      * @param end
      * @param data
      * @exception RuntimeException
      */
    protected int getDate(String buffer, int start, int end, int[] date) throws RuntimeException {
        start = getYearMonth(buffer, start, end, date);
        if (buffer.charAt(start++) != '-') {
            throw new RuntimeException("CCYY-MM must be followed by '-' sign");
        }
        int stop = start + 2;
        date[D] = parseInt(buffer, start, stop);
        return stop;
    }

    /**
      * Parses date CCYY-MM
      *
      * @param start
      * @param end
      * @param data
      * @exception RuntimeException
      */
    protected int getYearMonth(String buffer, int start, int end, int[] date) throws RuntimeException {
        if (buffer.charAt(0) == '-') {
            start++;
        }
        int i = indexOf(buffer, start, end, '-');
        if (i == -1) throw new RuntimeException("Year separator is missing or misplaced");
        int length = i - start;
        if (length < 4) {
            throw new RuntimeException("Year must have 'CCYY' format");
        } else if (length > 4 && buffer.charAt(start) == '0') {
            throw new RuntimeException("Leading zeros are required if the year value would otherwise have fewer than four digits; otherwise they are forbidden");
        }
        date[CY] = parseIntYear(buffer, i);
        if (buffer.charAt(i) != '-') {
            throw new RuntimeException("CCYY must be followed by '-' sign");
        }
        start = ++i;
        i = start + 2;
        date[M] = parseInt(buffer, start, i);
        return i;
    }

    /**
      * Shared code from Date and YearMonth datatypes.
      * Finds if time zone sign is present
      *
      * @param end
      * @param date
      * @exception RuntimeException
      */
    protected void parseTimeZone(String buffer, int start, int end, int[] date, int[] timeZone) throws RuntimeException {
        if (start < end) {
            int sign = findUTCSign(buffer, start, end);
            if (sign < 0) {
                throw new RuntimeException("Error in month parsing");
            } else {
                getTimeZone(buffer, date, sign, end, timeZone);
            }
        }
    }

    /**
      * Parses time zone: 'Z' or {+,-} followed by  hh:mm
      *
      * @param data
      * @param sign
      * @exception RuntimeException
      */
    protected void getTimeZone(String buffer, int[] data, int sign, int end, int[] timeZone) throws RuntimeException {
        data[utc] = buffer.charAt(sign);
        if (buffer.charAt(sign) == 'Z') {
            if (end > (++sign)) {
                throw new RuntimeException("Error in parsing time zone");
            }
            return;
        }
        if (sign <= (end - 6)) {
            int stop = ++sign + 2;
            timeZone[hh] = parseInt(buffer, sign, stop);
            if (buffer.charAt(stop++) != ':') {
                throw new RuntimeException("Error in parsing time zone");
            }
            timeZone[mm] = parseInt(buffer, stop, stop + 2);
            if (stop + 2 != end) {
                throw new RuntimeException("Error in parsing time zone");
            }
        } else {
            throw new RuntimeException("Error in parsing time zone");
        }
    }

    /**
      * Computes index of given char within StringBuffer
      *
      * @param start
      * @param end
      * @param ch     character to look for in StringBuffer
      * @return index of ch within StringBuffer
      */
    protected int indexOf(String buffer, int start, int end, char ch) {
        for (int i = start; i < end; i++) {
            if (buffer.charAt(i) == ch) {
                return i;
            }
        }
        return -1;
    }

    public static final boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    public static final int getDigit(char ch) {
        return isDigit(ch) ? ch - '0' : -1;
    }

    /**
      * Return index of UTC char: 'Z', '+', '-'
      *
      * @param start
      * @param end
      * @return index of the UTC character that was found
      */
    protected int findUTCSign(String buffer, int start, int end) {
        int c;
        for (int i = start; i < end; i++) {
            c = buffer.charAt(i);
            if (c == 'Z' || c == '+' || c == '-') {
                return i;
            }
        }
        return -1;
    }

    /**
      * Given start and end position, parses string value
      *
      * @param value  string to parse
      * @param start  Start position
      * @param end    end position
      * @return  return integer representation of characters
      */
    protected int parseInt(String buffer, int start, int end) throws NumberFormatException {
        int radix = 10;
        int result = 0;
        int digit = 0;
        int limit = -Integer.MAX_VALUE;
        int multmin = limit / radix;
        int i = start;
        do {
            digit = getDigit(buffer.charAt(i));
            if (digit < 0) throw new NumberFormatException("'" + buffer.toString() + "' has wrong format");
            if (result < multmin) throw new NumberFormatException("'" + buffer.toString() + "' has wrong format");
            result *= radix;
            if (result < limit + digit) throw new NumberFormatException("'" + buffer.toString() + "' has wrong format");
            result -= digit;
        } while (++i < end);
        return -result;
    }

    protected int parseIntYear(String buffer, int end) {
        int radix = 10;
        int result = 0;
        boolean negative = false;
        int i = 0;
        int limit;
        int multmin;
        int digit = 0;
        if (buffer.charAt(0) == '-') {
            negative = true;
            limit = Integer.MIN_VALUE;
            i++;
        } else {
            limit = -Integer.MAX_VALUE;
        }
        multmin = limit / radix;
        while (i < end) {
            digit = getDigit(buffer.charAt(i++));
            if (digit < 0) throw new NumberFormatException("'" + buffer.toString() + "' has wrong format");
            if (result < multmin) throw new NumberFormatException("'" + buffer.toString() + "' has wrong format");
            result *= radix;
            if (result < limit + digit) throw new NumberFormatException("'" + buffer.toString() + "' has wrong format");
            result -= digit;
        }
        if (negative) {
            if (i > 1) return result; else throw new NumberFormatException("'" + buffer.toString() + "' has wrong format");
        }
        return -result;
    }

    public String dateToString(int[] date) {
        StringBuffer message = new StringBuffer(25);
        append(message, date[CY], 4);
        message.append('-');
        append(message, date[M], 2);
        message.append('-');
        append(message, date[D], 2);
        message.append('T');
        append(message, date[h], 2);
        message.append(':');
        append(message, date[m], 2);
        message.append(':');
        append(message, date[s], 2);
        message.append('.');
        appendFractionalTime(message, date[ms], date[msscale]);
        append(message, (char) date[utc], 0);
        return message.toString();
    }

    /** Append the fraction time part of a date/time vector to
      * a string buffer.
      */
    public static void appendFractionalTime(StringBuffer buff, int fsec, int scale) {
        String msString = Integer.toString(fsec);
        int pad = scale - msString.length();
        while (pad > 0) {
            buff.append('0');
            pad--;
        }
        int trunc = msString.length();
        while (msString.charAt(trunc - 1) == '0') trunc--;
        buff.append(msString.substring(0, trunc));
    }

    protected void append(StringBuffer message, int value, int nch) {
        if (value < 0) {
            message.append('-');
            value = -value;
        }
        if (nch == 4) {
            if (value < 10) message.append("000"); else if (value < 100) message.append("00"); else if (value < 1000) message.append("0");
            message.append(value);
        } else if (nch == 2) {
            if (value < 10) message.append('0');
            message.append(value);
        } else {
            if (value != 0) message.append((char) value);
        }
    }

    /**
      * Return a minimal datatype for this object. Used to handle
      * cases where a single java object can represent multiple
      * specific types and where we want narrow the type used.
      * For example, a BigDecimal may narrow to a simple xsd:int. 
      * Currently only used to narrow gener XSDDateTime objects
      * to the minimal XSD date/time type.
      */
    public RDFDatatype getNarrowedDatatype(Object value) {
        if (value instanceof XSDDateTime) {
            return ((XSDDateTime) value).getNarrowedDatatype();
        } else {
            return this;
        }
    }
}
