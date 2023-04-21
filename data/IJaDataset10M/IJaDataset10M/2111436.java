package org.dcm4chee.xero.dicom;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import org.dcm4che2.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class that handles the parsing and formatting of DICOM date/time fields.
 * <p>
 * @author Andrew Cowan (andrew.cowan@agfa.com)
 */
public class DicomDateTimeHandler {

    private static Logger log = LoggerFactory.getLogger(DicomDateTimeHandler.class);

    private final DateFormat dateOnlyFormat;

    private final DateFormat dateTimeFormat;

    public DicomDateTimeHandler(DateFormat dateTimeFormat, DateFormat dateOnlyFormat) {
        if (dateTimeFormat == null || dateOnlyFormat == null) throw new IllegalArgumentException("DateFormatS must be specified");
        this.dateTimeFormat = dateTimeFormat;
        this.dateOnlyFormat = dateOnlyFormat;
    }

    public DicomDateTimeHandler() {
        this(DateFormat.getDateTimeInstance(), DateFormat.getDateInstance());
    }

    /** Formats a dateTimeString which is a concatenation of a DICOM date and time.
    * Returns null if unable to properly parse dateTimeString.
    */
    public String formatDicomDateTime(String dateTimeString) {
        if (dateTimeString == null) return null;
        String formattedDateTime;
        try {
            Date dateTime = DateUtils.parseDT(dateTimeString, false);
            formattedDateTime = formatDateOrDateTime(dateTime, dateTimeString.length() > 8);
        } catch (Exception nfe) {
            log.warn("Illegal study date or time:" + nfe);
            formattedDateTime = null;
        }
        return formattedDateTime;
    }

    /**
    * Format the indicated Date instance with either a date or date time formatter.
    * <p>
    * This method will handle synchronization of the internal DateFormat instances internally.
    */
    private String formatDateOrDateTime(Date dateTime, boolean includeTime) {
        String formattedDateTime = null;
        DateFormat format = includeTime ? dateTimeFormat : dateOnlyFormat;
        synchronized (format) {
            formattedDateTime = format.format(dateTime);
        }
        return formattedDateTime;
    }

    /** Simple concatenation of date & time parameters.
    * @return Date string if time is null, null if both arguments are null.
    */
    public String createDateTime(String date, String time) {
        String dateTime = null;
        if (date != null) {
            dateTime = date + (time == null ? "" : time);
        }
        return dateTime;
    }

    /**
    * Format the indicated Java Date instance as a DICOM date.
    * @return DICOM formatted date or "" if null
    */
    public String toDicomDate(Date date) {
        if (date == null) return "";
        return DateUtils.formatDA(date);
    }

    /**
    * Format the indicated Java Calendar instance as a DICOM date.
    * @return DICOM formatted date or "" if null.
    */
    public String toDicomDate(Calendar cal) {
        if (cal == null) return "";
        return toDicomDate(cal.getTime());
    }
}
