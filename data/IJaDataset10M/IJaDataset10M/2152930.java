package javax.microedition.location;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * A LocationProvider using a Google Earth KML file, saved in the resources
 * folder of the implementation.
 * <p>
 * Creating a file name "OpenLAPI.kml" as a resource to appear at the base of
 * the jar file should override the shipped version ("OpenLAPI-default.kml),
 * allowing for custom emulated trails. The filename is case sensitive.
 * <p>
 * Locations are saved in the KML file as Placemark elements, and many levels of
 * Folders are allowed (the metadata associated to the Folder elements is simply
 * ignored). If the Placemark has many coordinates, only the first one is used.
 * If the Placemark has a LookAt element, it is used to calculate the course.
 * The Placemark name is appended to the plain text extra info of the Location
 * object. Locations are discovered using a depth first searth of the folder
 * structure.
 * <p>
 * When there are multiple Locations in the file, the LocationProvider will
 * return them based on the time the Location is requested... this emulates a
 * moving device. The time interval of the change is set in TIME_INTERVAL.
 * <p>
 * It should be easy to include additional information in the description
 * element of each Placemark, such as a neatly formatted string that we could
 * read into AddressInfo or range, but this support has not been implemented.
 * <p>
 * The following are ideas for extra ?? features in the KML emulation mode
 * <ul>
 * <li>actually pay attention to the Criteria object
 * <li>random entry mode
 * <li>emulate a timeout
 * <li>emulate TEMPORARY_UNAVAILABLE behaviour randomly
 * </ul>
 */
class LocationProviderKML extends LocationProvider {

    /**
	 * For the purposes of time-based trails and listeners, a reference
	 * timestamp is needed. We set this one time, during construction.
	 */
    private static long startupTimestamp = -1;

    /**
	 * The time interval in milliseconds between switching the location object
	 * to the next one on the trail.
	 */
    private static final long TIME_INTERVAL = 4000;

    /**
	 * A Vector of all the Location objects found in this KML file.
	 */
    private Vector locations = new Vector();

    /**
	 * Where we store the XML parser. Only the constructor and its delegate
	 * methods should access this.
	 */
    private XmlPullParser parser = null;

    /**
	 * Location listener thread attached to this instance.
	 */
    private LocationDaemon server = null;

    /**
	 * @param criteria
	 * @param useTimeTracking
	 *            true if time tracking support is to be used if multiple
	 *            locations are found. false picks random locations from the
	 *            file.
	 */
    protected LocationProviderKML(Criteria criteria) throws IOException {
        if (startupTimestamp == -1) {
            startupTimestamp = System.currentTimeMillis();
        }
        InputStream is = getClass().getResourceAsStream("/OpenLAPI.kml");
        if (is == null) {
            is = getClass().getResourceAsStream("/OpenLAPI-default.kml");
        }
        if (is == null) throw new IOException();
        parser = new KXmlParser();
        try {
            parser.setInput(is, "UTF-8");
            parser.next();
            String namespace = parser.getAttributeValue(0);
            if (!"http://earth.google.com/kml/2.0".equals(namespace)) throw new IOException();
        } catch (XmlPullParserException e) {
            throw new IOException(e.getMessage());
        }
        try {
            findPlacemarks();
        } catch (XmlPullParserException e) {
            throw new IOException(e.getMessage());
        }
        is.close();
    }

    public Location getLocation(int timeout) throws LocationException, InterruptedException, SecurityException, IllegalArgumentException {
        OpenLAPICommon.testPermission("javax.microedition.location.Location");
        if (timeout <= 0) throw new IllegalArgumentException();
        long timestamp = System.currentTimeMillis();
        long timeSinceStartup = timestamp - startupTimestamp;
        int stepsIn = (int) (timeSinceStartup / TIME_INTERVAL);
        int total = locations.size();
        int step = stepsIn % (2 * total);
        if (step > total) {
            step = 2 * total - step;
        }
        Location storedLocation = (Location) locations.elementAt(step);
        Location location = shallowCopyLocation(storedLocation);
        location.timestamp = timestamp;
        setLastKnownLocation(location);
        return location;
    }

    public int getState() {
        if (locations.size() == 0) return OUT_OF_SERVICE;
        return AVAILABLE;
    }

    public void reset() {
    }

    public void setLocationListener(LocationListener listener, int interval, int timeout, int maxAge) {
        if ((interval == 0) || ((listener == null) && (server != null))) {
            server.shutdown();
            server = null;
            alertProximityListeners(false);
            return;
        }
        alertProximityListeners(true);
        if (server == null) {
            server = new LocationDaemon(listener, interval, timeout, maxAge, this);
            server.start();
        } else {
            server.update(listener, interval, timeout, maxAge);
        }
    }

    /**
	 * Recursively discover all folders, if a Placemark object is encountered,
	 * it is parsed into a Location abject and stored.
	 * 
	 * @param parser
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
    private void findPlacemarks() throws XmlPullParserException, IOException {
        int event = parser.next();
        for (; event != XmlPullParser.END_DOCUMENT; event = parser.next()) {
            if (event != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if ("Placemark".equals(name)) {
                Location location = parsePlacemark();
                if (location != null) {
                    locations.addElement(location);
                }
            }
        }
    }

    /**
	 * Parses a Google Earth coordinates String and returns a Coordinates
	 * object. Defines the exact coordinates of the point location in longitude,
	 * latitude, and altitude—in that precise order. Values are separated by
	 * commas. The KML format allows for multiple coordinates to be set in this
	 * field, but we will ignore all after the first.
	 * 
	 * @param content
	 * @return
	 * @throws IllegalArgumentException
	 *             if there are any parsing errors or the coordinates are out of
	 *             bounds
	 */
    private QualifiedCoordinates parseCoordinates(String content) throws IllegalArgumentException {
        int subStart = 0;
        int p = 0;
        int length = content.length();
        Double[] parts = new Double[3];
        for (int i = 0; (i < length) && (p < 3); i++) {
            char character = content.charAt(i);
            if ((character == ',') || (character == ' ') || (i == length - 1)) {
                int end = i;
                if (i == length - 1) {
                    end++;
                }
                String part = content.substring(subStart, end);
                parts[p] = Double.valueOf(part);
                p++;
                subStart = i + 1;
            }
        }
        if (p != 3) throw new IllegalArgumentException();
        QualifiedCoordinates qc = new QualifiedCoordinates(parts[1].doubleValue(), parts[0].doubleValue(), parts[2].floatValue(), Float.NaN, Float.NaN);
        return qc;
    }

    /**
	 * To be called emmediately after the parser reaches the beginning of a
	 * LookAt tag, this will extract and return the heading after proceeding to
	 * the end of the LookAt element.
	 * 
	 * @return
	 * @throws XmlPullParserException
	 *             if there are any parse errors, including if the entry for
	 *             heading is not a valid floating point number.
	 * @throws IOException
	 */
    private float parseLookAt() throws XmlPullParserException, IOException {
        Vector tags = new Vector();
        tags.addElement("LookAt");
        float heading = Float.NaN;
        int event = parser.next();
        for (; event != XmlPullParser.END_DOCUMENT; event = parser.next()) {
            if (event == XmlPullParser.START_TAG) {
                String name = parser.getName();
                tags.addElement(name);
                continue;
            } else if (event == XmlPullParser.END_TAG) {
                String name = parser.getName();
                tags.removeElement(name);
                if ("LookAt".equals(name)) return heading;
            }
            if (event == XmlPullParser.TEXT) {
                String name = (String) tags.lastElement();
                if ("heading".equals(name)) {
                    String content = parser.getText();
                    try {
                        heading = Float.parseFloat(content);
                    } catch (IllegalArgumentException e) {
                        throw new XmlPullParserException(e.getMessage());
                    }
                }
            }
        }
        throw new XmlPullParserException("expected </LookAt>");
    }

    /**
	 * Parse a Placemark element into a Location. Moves the parser to the end of
	 * the Placemark element.
	 * 
	 * @param element
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
    private Location parsePlacemark() throws XmlPullParserException, IOException {
        Location location = new Location();
        location.extraInfo_Text = "OpenLAPI KML emulator mode";
        location.valid = true;
        Vector tags = new Vector();
        tags.addElement("Placemark");
        int event = parser.next();
        for (; event != XmlPullParser.END_DOCUMENT; event = parser.next()) {
            if (event == XmlPullParser.START_TAG) {
                String name = parser.getName();
                tags.addElement(name);
                continue;
            } else if (event == XmlPullParser.END_TAG) {
                String name = parser.getName();
                tags.removeElement(name);
                if ("Placemark".equals(name)) return location;
            }
            if (event == XmlPullParser.TEXT) {
                String name = (String) tags.lastElement();
                if ("name".equals(name)) {
                    location.extraInfo_Text = location.extraInfo_Text + ": " + parser.getText();
                } else if ("description".equals(name)) {
                } else if ("LookAt".equals(name)) {
                    location.setCourse(parseLookAt());
                    tags.removeElement("LookAt");
                } else if ("Point".equals(name)) {
                    location.qualifiedCoordinates = parsePoint();
                    tags.removeElement("Point");
                }
            }
        }
        if (location.qualifiedCoordinates == null) return null;
        return location;
    }

    /**
	 * Parse a Point element, returning the QualifiedCoordinates contained
	 * within.
	 * 
	 * @param entry
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
    private QualifiedCoordinates parsePoint() throws XmlPullParserException, IOException {
        QualifiedCoordinates qc = null;
        Vector tags = new Vector();
        tags.addElement("Point");
        int event = parser.next();
        for (; event != XmlPullParser.END_DOCUMENT; event = parser.next()) {
            if (event == XmlPullParser.START_TAG) {
                String name = parser.getName();
                tags.addElement(name);
                continue;
            } else if (event == XmlPullParser.END_TAG) {
                String name = parser.getName();
                tags.removeElement(name);
                if ("Point".equals(name)) return qc;
            }
            if (event == XmlPullParser.TEXT) {
                String name = (String) tags.lastElement();
                String content = parser.getText();
                if ("coordinates".equals(name)) {
                    try {
                        qc = parseCoordinates(content);
                    } catch (IllegalArgumentException e) {
                        throw new XmlPullParserException(e.getMessage());
                    }
                }
            }
        }
        throw new XmlPullParserException("expected </Point>");
    }
}
