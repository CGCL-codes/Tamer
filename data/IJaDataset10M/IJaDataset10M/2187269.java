package de.enough.polish.android.location;

/**
 * The <code>Location</code> class represents the standard set of
 * basic location information. This
 * includes the timestamped coordinates, accuracy, speed, course, and
 * information about the positioning method used for the location,
 * plus an optional textual address.
 * <p>The location method is indicated using a bit field.
 * The individual bits are defined using constants in this class.
 * This bit field is a bitwise combination of the location
 * method technology bits (MTE_*), method type (MTY_*) and
 * method assistance information (MTA_*). All other bits in the
 * 32 bit integer than those that have defined constants in this class
 * are reserved and MUST not be set by implementations (i.e. these bits
 * must be 0).
 * </p>
 * <p>
 * A <code>Location</code> object may be either 'valid' or 'invalid'. The
 * validity can be queried using the <code>isValid</code> method. A valid
 * <code>Location</code> object represents a location with valid coordinates
 * and the <code>getQualifiedCoordinates</code> method must return
 * there coordinates.
 * An invalid <code>Location</code> object doesn't have valid coordinates,
 * but the extra info that is obtained from the <code>getExtraInfo</code> method
 * can provide information about the reason why it was not possible to provide
 * a valid <code>Location</code>.
 * For an invalid <code>Location</code> object,
 * the <code>getQualifiedCoordinates</code> method may return either
 * <code>null</code> or some coordinates where the information is
 * not necessarily fully correct.
 * The periodic location updates to the
 * <code>LocationListener</code> may return invalid Location objects if
 * it isn't possible to determine the location.
 * </p>
 * <p>
 * This class is only a container for the information.
 * When the platform implementation returns <code>Location</code> objects, it
 * MUST ensure that it only returns objects where the parameters
 * have values set as described for their semantics in this class.
 * </p>
 */
public class Location extends java.lang.Object {

    /**
	 * Location method using satellites (for example, Global
	 * Positioning System (GPS)).
	 * MTE_SATELLITE = 0x00000001
	 * <P>
	 * <DT><B>See Also:</B>
	 * 
	 */
    public static final int MTE_SATELLITE = 0x00000001;

    /**
	 * Location method Time Difference for cellular / terrestrial RF system
	 * (for example, Enhanced Observed
	 * Time Difference (E-OTD) for GSM). MTE_TIMEDIFFERENCE = 0x00000002
	 * <P>
	 * <DT><B>See Also:</B>
	 * 
	 */
    public static final int MTE_TIMEDIFFERENCE = 0x00000002;

    /**
	 * Location method Time of Arrival (TOA) for cellular / terrestrial RF
	 * system. MTE_TIMEOFARRIVAL = 0x00000004
	 * <P>
	 * <DT><B>See Also:</B>
	 * 
	 */
    public static final int MTE_TIMEOFARRIVAL = 0x00000004;

    /**
	 * Location method Cell-ID for cellular (in GSM, this is the same as CGI,
	 * Cell Global Identity). MTE_CELLID = 0x00000008
	 * <P>
	 * <DT><B>See Also:</B>
	 * 
	 */
    public static final int MTE_CELLID = 0x00000008;

    /**
	 * Location method Short-range positioning system (for example, Bluetooth
	 * LP).
	 * MTE_SHORTRANGE = 0x00000010
	 * <P>
	 * <DT><B>See Also:</B>
	 * 
	 */
    public static final int MTE_SHORTRANGE = 0x00000010;

    /**
	 * Location method Angle of Arrival for cellular / terrestrial RF system.
	 * MTE_ANGLEOFARRIVAL = 0x00000020
	 * <P>
	 * <DT><B>See Also:</B>
	 * 
	 */
    public static final int MTE_ANGLEOFARRIVAL = 0x00000020;

    /**
	 * Location method is of type terminal based. This means that the
	 * final location result is calculated in the terminal.
	 * MTY_TERMINALBASED = 0x00010000
	 * <P>
	 * <DT><B>See Also:</B>
	 * 
	 */
    public static final int MTY_TERMINALBASED = 0x00010000;

    /**
	 * Location method is of type network based. This means that the
	 * final location result is calculated in the network.
	 * This bit and MTY_TERMINALBASED bit
	 * MUST NOT both be set. Only one of these bits may be set or
	 * neither to indicate that it is not known where the result
	 * is calculated.
	 * MTY_NETWORKBASED = 0x00020000
	 * <P>
	 * <DT><B>See Also:</B>
	 * 
	 */
    public static final int MTY_NETWORKBASED = 0x00020000;

    /**
	 * Location method is assisted by the other party
	 * (Terminal assisted for Network based,
	 * Network assisted for terminal based).
	 * MTA_ASSISTED = 0x00040000
	 * <P>
	 * <DT><B>See Also:</B>
	 * 
	 */
    public static final int MTA_ASSISTED = 0x00040000;

    /**
	 * Location method is unassisted. This bit and MTA_ASSISTED bit
	 * MUST NOT both be set. Only one of these bits may be set or
	 * neither to indicate that the assistance information is
	 * not known.
	 * MTA_UNASSISTED = 0x00080000
	 * <P>
	 * <DT><B>See Also:</B>
	 * 
	 * 
	 */
    public static final int MTA_UNASSISTED = 0x00080000;

    private long timestamp;

    private QualifiedCoordinates qualifiedCoordinates;

    private float speed;

    private float course;

    protected Location(android.location.Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        float altitude = (float) location.getAltitude();
        float accuracy = location.getAccuracy();
        this.speed = location.getSpeed();
        this.course = location.getBearing();
        this.timestamp = location.getTime();
        this.qualifiedCoordinates = new QualifiedCoordinates(latitude, longitude, altitude, accuracy, accuracy);
    }

    /**
	 * Returns whether this <code>Location</code> instance represents a valid
	 * location with coordinates or an invalid one where all the
	 * data, especially the latitude and longitude coordinates,
	 * may not be present.
	 * <p>A valid <code>Location</code> object contains valid coordinates
	 * whereas
	 * an invalid <code>Location</code> object may not contain valid
	 * coordinates
	 * but may contain other information via the <code>getExtraInfo()</code>
	 * method to provide information on why it was not possible to provide
	 * a valid <code>Location</code> object.</p>
	 * <P>
	 * 
	 * 
	 * @return a boolean value with true indicating that this Location instance is valid and false indicating an invalid Location instance
	 * @see #getExtraInfo(String)
	 */
    public boolean isValid() {
        return true;
    }

    /**
	 * Returns the time stamp at which the data was collected. This
	 * timestamp should represent the point in time when the measurements
	 * were made. Implementations make best effort to set the timestamp
	 * as close to this point in time as possible. The time
	 * returned is the time of the local clock in the terminal
	 * in milliseconds using the same clock and same time representation
	 * as <code>System.currentTimeMillis()</code>.
	 * <P>
	 * 
	 * 
	 * @return a timestamp representing the time
	 * @see System#currentTimeMillis()
	 */
    public long getTimestamp() {
        return this.timestamp;
    }

    /**
	 * Returns the coordinates of this location and their accuracy.
	 * <P>
	 * 
	 * 
	 * @return a QualifiedCoordinates object. If the coordinates are not known, returns null.
	 */
    public QualifiedCoordinates getQualifiedCoordinates() {
        return this.qualifiedCoordinates;
    }

    /**
	 * Returns the terminal's current ground speed in meters per second (m/s)
	 * at the time of measurement. The speed is always a non-negative value.
	 * Note that unlike the coordinates, speed does not have an associated
	 * accuracy because the methods used to determine the speed typically
	 * are not able to indicate the accuracy.
	 * <P>
	 * 
	 * 
	 * @return the current ground speed in m/s for the terminal or Float.NaN if the speed is not known
	 */
    public float getSpeed() {
        return this.speed;
    }

    /**
	 * Returns the terminal's course made good
	 * in degrees relative to true north. The value is always
	 * in the range [0.0,360.0) degrees.
	 * <P>
	 * 
	 * 
	 * @return the terminal's course made good in degrees relative to true north or Float.NaN if the course is not known
	 */
    public float getCourse() {
        return this.course;
    }

    /**
	 * Returns information about the location method used. The returned
	 * value is a bitwise combination (OR) of the method technology,
	 * method type and assistance information.
	 * The method technology values are defined as constant values named
	 * MTE_* in this class, the method type values are named MTY_*
	 * and assistance information values are named MTA_*.
	 * 
	 * <p>For example, if the location method used is terminal based,
	 * network assisted E-OTD, the
	 * value 0x00050002 ( = <code>MTY_TERMINALBASED | MTA_ASSISTED |
	 * MTE_TIMEDIFFERENCE</code>)
	 * would be returned.
	 * </p>
	 * <p>
	 * If the location is determined by combining several location
	 * technologies, the returned value may have several MTE_* bits set.
	 * </p>
	 * <p>
	 * If the used location method is unknown, the returned value
	 * must have all the bits set to zero.
	 * </p>
	 * <p>Only bits that have defined constants within this class
	 * are allowed to be used. Other bits are reserved and must
	 * be set to 0.
	 * </p>
	 * <P>
	 * 
	 * 
	 * @return a bitfield identifying the used location method
	 */
    public int getLocationMethod() {
        return 0;
    }

    /**
	 * Returns the <code>AddressInfo</code> associated with this
	 * <code>Location</code> object.
	 * If no address is available, <code>null</code> is returned.
	 * <P>
	 * 
	 * 
	 * @return an AddressInfo associated with this Location object
	 */
    public AddressInfo getAddressInfo() {
        return null;
    }

    /**
	 * Returns extra information about the location. This method
	 * is intended to provide location method specific extra
	 * information that applications that are aware of the
	 * used location method and information format are able to
	 * use.
	 * <p>A MIME type is used to identify the type of the
	 * extra information when requesting it. If the implementation
	 * supports this type, it returns the extra information as a String
	 * encoded according to format identified by the MIME type.
	 * If the implementation does not support this type, the method
	 * returns <code>null</code>.
	 * </p>
	 * <p>This specification does not require implementations to
	 * support any extra information type.
	 * </p>
	 * <p>The following MIME types are defined here together with
	 * their definitions in order to ensure interoperability of
	 * implementations wishing to use these types. The definition
	 * of these types here is not an indication that these formats
	 * are preferred over any other format not defined here.
	 * </p>
	 * <p>When the MIME type is "application/X-jsr179-location-nmea",
	 * the returned string shall be a valid sequence of NMEA
	 * sentences formatted according to the syntax specified
	 * in the NMEA 0183 v3.1 specification. These sentences shall
	 * represent the set of NMEA sentences that are related to
	 * this location at the time this location was created.
	 * </p>
	 * <p>When the MIME type is "application/X-jsr179-location-lif",
	 * the returned string shall contain an XML formatted document
	 * containing the "pd" element defined in the LIF Mobile Location
	 * Protocol TS 101 v3.0.0 as the root element of the document.
	 * </p>
	 * <p>When the MIME type is "text/plain", the returned string
	 * shall contain textual extra information that can be displayed
	 * to the end user.
	 * </p>
	 * <P>
	 * 
	 * @param mimetype - the MIME type of the requested extra information
	 * @return string encoded according to the format identified by the MIME type defined in the parameter. null if the information for the requested MIME type is not available or not supported by this implementation.
	 */
    public java.lang.String getExtraInfo(java.lang.String mimetype) {
        return null;
    }

    public String toString() {
        return this.qualifiedCoordinates.toString() + ".Speed:" + this.speed + ".Time:" + this.timestamp + ".Course:" + this.course + ".";
    }
}
