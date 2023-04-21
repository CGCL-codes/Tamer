package uk.co.weft.fisherman.fieldkit;

import java.io.*;
import java.util.*;
import java.util.Calendar;
import uk.co.weft.fisherman.entities.*;

/**
 * A loggable is something the Field Kit creates when it sees a bag, and
 * deletes when it has received confirmation from base that the loggable has
 * been logged
 */
public class Loggable {

    /**
	 * the inspector's badge scanned most recently at the time of the creation
	 * of this loggable
	 */
    protected String fisherman = null;

    /**
	 * the inspector's badge scanned most recently at the time of the creation
	 * of this loggable
	 */
    protected String inspector = null;

    /**
	 * the tag number scanned; should be a parsable integer, but we'll leave
	 * it to the server to enforce this
	 */
    protected String tag = null;

    /** the location at which the tag was scanned: latitude */
    protected double latitude = 0.0;

    /** the location at which the tag was scanned: longitude */
    protected double longitude = 0.0;

    /** the time this loggable was created */
    Calendar timestamp = null;

    /** Whether or not I'm locked by a sending process. */
    private boolean locked = false;

    /**
	 * Creates a new Loggable object.
	 *
	 * @param inspector the inspector logging this observation
	 * @param fisher the fisher whose tag is being logged
	 * @param tag the tag being logged
	 */
    public Loggable(String inspector, String fisher, String tag) {
        super();
        this.inspector = inspector;
        this.fisherman = fisher;
        this.tag = tag;
        this.timestamp = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    }

    /**
	 * Creates a new Loggable object, with position information
	 *
	 * @param inspector the inspector logging this observation
	 * @param fisher the fisher whose tag is being logged
	 * @param tag the tag being logged
	 * @param latitude latitude in degrees of arc
	 * @param longitude longitude in degrees of arc
	 */
    public Loggable(String inspector, String fisher, String tag, double latitude, double longitude) {
        this(inspector, fisher, tag);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
	 * Creates a new Loggable object.
	 *
	 * @param record a loggable record in the form saved to persistent store
	 */
    public Loggable(String record) {
        int fieldStart = 0;
        Stack fields = new Stack();
        for (int fieldEnd = record.indexOf(','); fieldEnd > -1; fieldEnd = record.indexOf(fieldStart, ',')) {
            fields.push(record.substring(fieldStart, fieldEnd));
            fieldStart = fieldEnd + 1;
        }
        timestamp = Calendar.getInstance();
        timestamp.setTime(new Date(Long.parseLong(fields.pop().toString())));
        longitude = Double.parseDouble(fields.pop().toString());
        latitude = Double.parseDouble(fields.pop().toString());
        tag = fields.pop().toString();
        inspector = fields.pop().toString();
        fisherman = fields.pop().toString();
        locked = false;
    }

    /**
	 * @param locked The locked to set.
	 *
	 * @return true if it could be set, else false
	 */
    public synchronized boolean setLocked(boolean locked) {
        boolean result = false;
        if (this.locked != locked) {
            this.locked = locked;
            result = true;
        }
        return result;
    }

    /**
	 * @return Returns whether or not I'm locked.
	 */
    public synchronized boolean isLocked() {
        return locked;
    }

    /**
	 * Format me into an http argument string, to make it easier to send me to
	 * the server. Serialization would be another solution to this.
	 *
	 * @return a string which, when passed to the server, will populate the
	 * 		   right fields to save to the observation table, q.v.
	 */
    public String toArgumentString() throws IOException {
        StringBuffer buffy = new StringBuffer("?");
        buffy.append(Observation.OFFICERFN).append("=").append(inspector).append("&");
        buffy.append(Badge.KEYFN).append("=").append(fisherman).append("&");
        buffy.append("string_list").append("=").append(tag).append("&");
        buffy.append(Observation.LATITUDEFN).append("=").append(latitude).append("&");
        buffy.append(Observation.LONGITUDEFN).append("=").append(longitude).append("&");
        buffy.append("timestamp").append("=");
        buffy.append(timestamp.getTime().getTime());
        return buffy.toString().toString();
    }

    /**
	 * produce this loggable in a format in which it can conveniently be saved
	 * to local persistent storage, when the application is terminated or the
	 * device is powered down
	 */
    public String toRecord() {
        StringBuffer buffy = new StringBuffer();
        buffy.append(fisherman).append(',');
        buffy.append(inspector).append(',');
        buffy.append(tag).append(',');
        buffy.append(latitude).append(',');
        buffy.append(longitude).append(',');
        buffy.append(timestamp.getTime().getTime());
        return buffy.toString();
    }

    /**
	 * print this field of this calendar onto this StringBuffer as a double
	 * digit (e.g. '05' instead of '5')
	 *
	 * @param buffy the buffer
	 * @param cal the calendar
	 * @param field the field - one of the recognised fields of a calendar
	 */
    private void doubleDigit(StringBuffer buffy, Calendar cal, int field) {
        int i = cal.get(field);
        if (field == Calendar.MONTH) {
            i++;
        }
        if (i < 10) {
            buffy.append('0');
        }
        buffy.append(i);
    }
}
