package com.sun.kvem.midp.pim.formats;

import javax.microedition.pim.Event;

/**
 * Helper methods for vEvent implementations.
 *
 */
public class VEventSupport {

    /**
     * Converts JSR75 field code to vCalendar property name.
     * @param field identifier for field name
     * @return label for requested field
     */
    public static String getFieldLabel(int field) {
        switch(field) {
            case Event.ALARM:
                return "DALARM";
            case Event.CLASS:
                return "CLASS";
            case Event.END:
                return "DTEND";
            case Event.LOCATION:
                return "LOCATION";
            case Event.NOTE:
                return "DESCRIPTION";
            case Event.REVISION:
                return "LAST-MODIFIED";
            case Event.START:
                return "DTSTART";
            case Event.SUMMARY:
                return "SUMMARY";
            case Event.UID:
                return "UID";
            default:
                return null;
        }
    }

    /**
     * Converts vCalendar property name to JSR75 field code.
     * @param fieldName label of requested field
     * @return identifier for requested field
     */
    public static int getFieldCode(String fieldName) {
        if (fieldName.equals("DTSTART")) return Event.START; else if (fieldName.equals("DTEND")) return Event.END; else if (fieldName.equals("DALARM")) return Event.ALARM; else if (fieldName.equals("LOCATION")) return Event.LOCATION; else if (fieldName.equals("DESCRIPTION")) return Event.NOTE; else if (fieldName.equals("LAST-MODIFIED")) return Event.REVISION; else if (fieldName.equals("SUMMARY")) return Event.SUMMARY; else if (fieldName.equals("UID")) return Event.UID; else if (fieldName.equals("CLASS")) return Event.CLASS; else return -1;
    }

    /**
     * Gets the value of the vEvent CLASS field for the given
     * value of the Event.CLASS field.
     * This method encapsulates the following mapping:
     * Event.CLASS_PUBLIC -> "PUBLIC"
     * Event.CLASS_PRIVATE -> "PRIVATE"
     * Event.CLASS_CONFIDENTIAL -> "CONFIDENTIAL"
     *
     * @param fieldValue the value of the Event.CLASS field
     * @return a string describing the class for the field value, or null if
     *  fieldValue is out of range
     */
    public static String getClassType(int fieldValue) {
        switch(fieldValue) {
            case Event.CLASS_CONFIDENTIAL:
                return "CONFIDENTIAL";
            case Event.CLASS_PRIVATE:
                return "PRIVATE";
            case Event.CLASS_PUBLIC:
                return "PUBLIC";
        }
        return null;
    }

    /**
     * Gets the value of the Event.CLASS field for the given
     * value of the vEvent CLASS property.
     * This method encapsulates the following mapping:
     * Event.CLASS_PUBLIC <- "PUBLIC"
     * Event.CLASS_PRIVATE <- "PRIVATE"
     * Event.CLASS_CONFIDENTIAL <- "CONFIDENTIAL"
     *
     * @param s the value of the CLASS property
     * @return the corresponding field of Event, or -1 if s is not recognized
     */
    public static int getClassCode(String s) {
        switch(s.length()) {
            case 6:
                if (s.equals("PUBLIC")) {
                    return Event.CLASS_PUBLIC;
                }
                break;
            case 7:
                if (s.equals("PRIVATE")) {
                    return Event.CLASS_PRIVATE;
                }
                break;
            case 12:
                if (s.equals("CONFIDENTIAL")) {
                    return Event.CLASS_CONFIDENTIAL;
                }
                break;
        }
        return -1;
    }
}
