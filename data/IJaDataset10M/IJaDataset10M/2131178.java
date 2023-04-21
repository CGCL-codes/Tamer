package com.google.gdata.data.calendar;

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * GData namespace definitions related to Google Calendar.
 *
 * 
 */
public class Namespaces {

    private Namespaces() {
    }

    /** Google Calendar (GCAL) namespace */
    public static final String gCal = "http://schemas.google.com/gCal/2005";

    /** Google Calendar (GCAL) namespace prefix */
    public static final String gCalPrefix = gCal + "#";

    /** Google Calendar (GCAL) namespace alias */
    public static final String gCalAlias = "gCal";

    /** XML writer namespace for Google Calendar (GCAL) */
    public static final XmlNamespace gCalNs = new XmlNamespace(gCalAlias, gCal);
}
