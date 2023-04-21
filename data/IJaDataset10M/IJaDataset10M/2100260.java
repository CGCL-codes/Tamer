package uk.org.ogsadai.resource.property;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.w3c.dom.Node;
import uk.org.ogsadai.exception.ResourcePropertyValueDateParseException;
import uk.org.ogsadai.exception.ResourcePropertyValueParseException;

/**
 * Resource property convertor for <code>Date</code> objects.  <code>Date</code>
 * objects are serialized as a single <code>Text</code> node.
 *
 * @author The OGSA-DAI Project Team.
 */
public class DatePropertyConvertor {

    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /**
     * Private constructor.  The class has only static methods.
     *
     */
    private DatePropertyConvertor() {
    }

    /**
     * Deserializes a <code>Node</code> array representing a resource property
     * value containing a single date.
     * 
     * @param resourcePropertyValue 
     *           nodes that store the resource property value.
     *           
     * @return the date stored in the resource property value.
     * 
     * @throws ResourcePropertyValueParseException 
     *           if the resource property value cannot be successfully parsed.
     */
    public static Date deserialize(final Node[] resourcePropertyValue) throws ResourcePropertyValueParseException {
        String dateString = StringPropertyConvertor.deserialize(resourcePropertyValue);
        if (dateString == null) return null;
        try {
            return parseXSDDate(dateString);
        } catch (ParseException e) {
            throw new ResourcePropertyValueDateParseException(e);
        }
    }

    /**
     * Serializes a <code>Date</code> into a resource property value.
     * 
     * @param date date to be serialized.
     * 
     * @return <code>Node</code> array that represents the resource property
     *         value.  This array will contain a single <code>Text</code> node 
     *         that contains the date value.
     */
    public static Node[] serialize(final Date date) {
        String formattedDate = null;
        if (date != null) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            formattedDate = formatter.format(date);
        }
        return StringPropertyConvertor.serialize(formattedDate);
    }

    /**
     * Serializes the date/time stored in a <code>Calendar</code> into a 
     * resource property value.
     * 
     * @param calendar calendar to be serialized.
     * 
     * @return <code>Node</code> array that represents the resource property
     *         value.  This array will contain a single <code>Text</code> node 
     *         that contains the date value.
     */
    public static Node[] serialize(final Calendar calendar) {
        Date date = null;
        if (calendar != null) {
            date = calendar.getTime();
        }
        return serialize(date);
    }

    /**
     * Maps date strings in xsd:datetime format into a format that is parsable
     * using <code>java.text.SimpleDateFormat</code>.  The output string are
     * in format <code>yyyy-MM-dd'T'HH:mm:ss.SSSZ</code>.
     * 
     * @param xsdDateTimeString data string is xsd:datatime format.
     * 
     * @return the date time in <code>yyyy-MM-dd'T'HH:mm:ss.SSSZ</code> format.
     */
    public static String mapXSDDateTimeStringToStandardDateTimeString(final String xsdDateTimeString) {
        final int END_OF_BASIC_DATETIME = 19;
        String dateToProcess = xsdDateTimeString;
        if (dateToProcess.startsWith("-")) {
            dateToProcess = dateToProcess.substring(1);
        }
        if (dateToProcess.endsWith("Z")) {
            dateToProcess = dateToProcess.replaceAll("Z", "+0000");
        }
        String basicDateTime;
        String milliSeconds = ".000";
        String timeZoneAdjustment = "+0000";
        if (dateToProcess.length() <= END_OF_BASIC_DATETIME) {
            basicDateTime = dateToProcess;
        } else {
            basicDateTime = dateToProcess.substring(0, END_OF_BASIC_DATETIME);
            String milliSecondsAndTimeZone = dateToProcess.substring(END_OF_BASIC_DATETIME);
            int startOfTimeZone = Math.max(milliSecondsAndTimeZone.indexOf("+"), milliSecondsAndTimeZone.indexOf("-"));
            if (startOfTimeZone == -1) {
                milliSeconds = milliSecondsAndTimeZone;
            } else {
                timeZoneAdjustment = milliSecondsAndTimeZone.substring(startOfTimeZone);
                milliSeconds = milliSecondsAndTimeZone.substring(0, startOfTimeZone);
            }
            timeZoneAdjustment = timeZoneAdjustment.replaceAll(":", "");
            if (milliSeconds.length() > 4) {
                milliSeconds = milliSeconds.substring(0, milliSeconds.length() > 4 ? 4 : milliSeconds.length());
            }
            if (milliSeconds.equals("")) {
                milliSeconds = ".000";
            }
        }
        return basicDateTime + milliSeconds + timeZoneAdjustment;
    }

    /**
     * Parses an XSD date into a <code>Date</code> object.
     * 
     * @param xsdDate date in xsd:Date format.
     * 
     * @return date object containing the date in the string
     * 
     * @throws ParseException if the date string cannot be successfully parsed.
     */
    public static Date parseXSDDate(final String xsdDate) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String dateString = mapXSDDateTimeStringToStandardDateTimeString(xsdDate);
        Date result = formatter.parse(dateString);
        return result;
    }
}
