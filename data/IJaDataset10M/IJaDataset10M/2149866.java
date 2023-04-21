package net.ekology.core.datatypes;

import java.util.Calendar;

/**
 * @author Aarón Tavío - aaron.tavio at gmail.com
 * @version 1.0.0 - 20081019-1700
 */
public class SessionData {

    public String sName;

    public String sNotes;

    public Calendar oCreationDate;

    public SessionData() {
        sName = null;
        sNotes = null;
        oCreationDate = null;
    }

    public SessionData(String sName, String sNotes) {
        this.sName = sName;
        this.sNotes = sNotes;
        this.oCreationDate = null;
    }

    public SessionData(String sName, String sNotes, Calendar oCreationDate) {
        this.sName = sName;
        this.sNotes = sNotes;
        this.oCreationDate = (Calendar) oCreationDate.clone();
    }
}
