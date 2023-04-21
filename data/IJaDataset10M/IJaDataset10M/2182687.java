package org.dbe.composer.wfengine.util;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * This class represents a time as defined by the xsd:time datatype.  An object of this type
 * can be constructed from an xsd:time formatted string, or from a Date or Calendar java
 * object.
 */
public class SdlSchemaTime extends SdlSchemaDateTime {

    /**
     * Creates a schema time object given a xsd:time formatted String.  This string is
     * typically gotten from a value found in an XML document.
     *
     * @param aSchemaTimeStr A time formatted in XSD format (subset of ISO 8601).
     */
    public SdlSchemaTime(String aSchemaTimeStr) throws ParseException {
        super();
        initFromSchemaTime(aSchemaTimeStr);
    }

    /**
     * Creates a schema time object from a java Date object.
     *
     * @param aDate
     */
    public SdlSchemaTime(Date aDate) {
        super(aDate);
        mCalendar.getTimeInMillis();
        mCalendar.set(Calendar.YEAR, 2004);
        mCalendar.set(Calendar.MONTH, 0);
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
    }

    /**
     * Creates a schema date object from a java Calendar object.
     *
     * @param aCalendar
     */
    public SdlSchemaTime(Calendar aCalendar) {
        super(aCalendar);
        mCalendar.getTimeInMillis();
        mCalendar.set(Calendar.YEAR, 2004);
        mCalendar.set(Calendar.MONTH, 0);
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
    }

    /**
     * Initialize the internal Calendar object by parsing the given xsd:time formatted string.
     *
     * @param aTimeString A xsd:time formatted string.
     * @throws ParseException
     */
    protected void initFromSchemaTime(String aTimeString) throws ParseException {
        try {
            StringTokenizer tokenizer = new StringTokenizer(aTimeString, ":-+.Z");
            ArrayList list = new ArrayList(10);
            for (; tokenizer.hasMoreTokens(); list.add(tokenizer.nextToken())) ;
            String hourStr = (String) list.get(0);
            String minuteStr = (String) list.get(1);
            String secondStr = (String) list.get(2);
            String fracSecondStr = null;
            String tzHr = null;
            String tzMin = null;
            char tzDir = '+';
            if (list.size() == 4 || list.size() == 6) {
                fracSecondStr = (String) list.get(3);
                if (list.size() == 6) {
                    tzHr = (String) list.get(4);
                    tzMin = (String) list.get(5);
                    tzDir = aTimeString.charAt(aTimeString.length() - 6);
                }
            }
            if (list.size() == 5) {
                tzHr = (String) list.get(3);
                tzMin = (String) list.get(4);
                tzDir = aTimeString.charAt(aTimeString.length() - 6);
            }
            int millis = 0;
            if (fracSecondStr != null) {
                millis = (int) (1000F * new Float("0." + fracSecondStr).floatValue());
            }
            mCalendar.setTimeZone(createTimeZone(tzHr, tzMin, tzDir));
            mCalendar.set(Calendar.HOUR_OF_DAY, new Integer(hourStr).intValue());
            mCalendar.set(Calendar.MINUTE, new Integer(minuteStr).intValue());
            mCalendar.set(Calendar.SECOND, new Integer(secondStr).intValue());
            mCalendar.set(Calendar.MILLISECOND, millis);
            mCalendar.getTimeInMillis();
            mCalendar.setTimeZone(sUTCTimeZone);
        } catch (Exception e) {
            throw new ParseException(MessageFormat.format("Error parsing a xsd:time [{0}]: {1}", new Object[] { aTimeString, e.getMessage() }), -1);
        }
    }

    /**
     * Outputs the current Time object in ISO8601 extended time format.
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buff = new StringBuffer();
        synchronized (buff) {
            appendValue(buff, mCalendar.get(Calendar.HOUR_OF_DAY));
            buff.append(":");
            appendValue(buff, mCalendar.get(Calendar.MINUTE));
            buff.append(":");
            appendValue(buff, mCalendar.get(Calendar.SECOND));
            if (mCalendar.get(Calendar.MILLISECOND) > 0) {
                buff.append(".");
                appendMillis(buff, mCalendar.get(Calendar.MILLISECOND));
            }
            buff.append("Z");
            return buff.toString();
        }
    }
}
