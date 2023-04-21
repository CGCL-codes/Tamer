package com.jujunie.integration.calendar;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Formats some ouputs for a given ICS entry
 * @author Julien BETI <a href="mailto:beti@fimasys.fr">beti@fimasys.fr</a>
 * @version $Revision $
 */
public class ICSFormatter {

    /** Date Format */
    public static final SimpleDateFormat DATE_FORMAT_SHORT = new SimpleDateFormat("yyyyMMdd");

    /** Date Format */
    public static final SimpleDateFormat DATE_FORMAT_LONG = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    /** Entry to format */
    private ICSEntry entry = null;

    /**
     * @param e the ICS entry to format
     */
    public ICSFormatter(ICSEntry e) {
        this.entry = e;
    }

    /**
     * Add one day if dates are equals
     * @param d1 date where the day will be added if == to ref
     * @param ref reference date
     * @return updated date 
     */
    private static Date addOneDayIfEqual(Date d1, Date ref) {
        if (d1.equals(ref)) {
            return new Date(d1.getTime() + 86400000);
        } else {
            return d1;
        }
    }

    /**
     * @return the formatted start date
     */
    public String getStartDate() {
        if (this.entry.isFullDay()) {
            return DATE_FORMAT_SHORT.format(this.entry.getStart());
        } else {
            return DATE_FORMAT_LONG.format(this.entry.getStart());
        }
    }

    /**
     * @return the formatted end date
     */
    public String getEndDate() {
        Date toProcess = addOneDayIfEqual(this.entry.getEnd(), this.entry.getStart());
        if (this.entry.isFullDay()) {
            return DATE_FORMAT_SHORT.format(toProcess);
        } else {
            return DATE_FORMAT_LONG.format(toProcess);
        }
    }

    /**
     * @return true if it is an event
     */
    public boolean isEvent() {
        return ICSEntry.Type.EVENT.equals(this.entry.getType());
    }

    /**
     * @return the ICS entry
     */
    public ICSEntry getEntry() {
        return this.entry;
    }
}
