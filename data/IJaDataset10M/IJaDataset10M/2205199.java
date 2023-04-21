package com.substanceofcode.tracker.model;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import com.substanceofcode.gps.GpsGPGSA;
import com.substanceofcode.gps.GpsPosition;
import com.substanceofcode.tracker.view.Logger;
import com.substanceofcode.util.DateTimeUtil;
import com.substanceofcode.localization.LocaleManager;

/**
 * GpxConverter writes track/place data in GPX format.
 * 
 * @author Tommi Laukkanen
 * @author Mario Sansone
 * @author Patrick Steiner
 */
public class GpxConverter extends TrackConverter {

    /** Convert trail to GPX format.
     * @param track
     * @param places 
     * @param includePlaces
     * @param includeMarkers
     * @return 
     */
    public String convert(Track track, Vector places, boolean includePlaces, boolean includeMarkers) {
        StringBuffer gpx = new StringBuffer();
        addHeader(gpx);
        addMetaData(track, gpx);
        gpx.append(createPlaceMarks(places));
        if (includeMarkers) {
            gpx.append(createMarkers(track));
        }
        addTrailStart(gpx);
        Enumeration posEnum = track.getTrackPointsEnumeration();
        while (posEnum.hasMoreElements() == true) {
            GpsPosition pos = (GpsPosition) posEnum.nextElement();
            addPosition(pos, gpx);
        }
        addTrailEnd(gpx);
        addFooter(gpx);
        return gpx.toString();
    }

    private static void addMetaData(Track track, StringBuffer gpx) {
        gpx.append("<metadata>");
        addBounds(track, gpx);
        gpx.append("</metadata>");
    }

    /**
     * Add the bounds element which defines a bounding box containing all the points
     * @param gpx
     * <bounds minlat="37.221354973"
     *     minlon="-121.987776104"
     *     maxlat="37.330222065"
     *    maxlon="-121.892523821"/>
     */
    private static void addBounds(Track track, StringBuffer gpx) {
        gpx.append("<bounds minlat=\"" + track.getMinLatitude() + "\" maxlat=\"" + track.getMaxLatitude() + "\" minlon=\"" + track.getMinLongitude() + "\" maxlon=\"" + track.getMaxLongitude() + "\"/>\r\n");
    }

    /** Convert place to GPX format.
     * @param waypoint
     * @param includeMarkers 
     * @param includeWaypoints
     */
    public String convert(Place place, Vector places, boolean includePlaces, boolean includeMarkers) {
        StringBuffer gpx = new StringBuffer();
        addHeader(gpx);
        gpx.append(createPlaceMarks(places));
        addFooter(gpx);
        return gpx.toString();
    }

    /**
     * TODO
     * 
     * @param gpx
     */
    public static void addHeader(StringBuffer gpx) {
        gpx.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
        gpx.append("<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" " + "version=\"1.1\" creator=\"Mobile Trail Explorer\" " + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " + "xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 " + "http://www.topografix.com/GPX/1/1/gpx.xsd\" >\r\n");
    }

    /**
     * TODO
     * 
     * @param gpx
     */
    public static void addTrailStart(StringBuffer gpx) {
        gpx.append("<trk>\r\n<trkseg>\r\n");
    }

    /**
     * TODO
     * 
     * @param gpx
     */
    public static void addTrailEnd(StringBuffer gpx) {
        gpx.append("</trkseg>\r\n</trk>\r\n");
    }

    /**
     * TODO
     *
     * @param gpx
     */
    public static void addTrackSegmentStart(StringBuffer gpx) {
        gpx.append("<trkseg>\r\n");
    }

    /**
     * TODO
     *
     * @param gpx
     */
    public static void addTrackSegmentEnd(StringBuffer gpx) {
        gpx.append("</trkseg>\r\n");
    }

    public static void addFooter(StringBuffer gpx) {
        gpx.append("</gpx>\r\n");
    }

    /**
     * TODO
     * 
     * @return
     */
    public static void addPosition(GpsPosition pos, StringBuffer gpx) {
        String lat = formatDegrees(pos.latitude);
        String lon = formatDegrees(pos.longitude);
        GpsGPGSA gpgsa = pos.getGpgsa();
        gpx.append("<trkpt lat=\"").append(lat).append("\" lon=\"").append(lon).append("\">\r\n");
        gpx.append("<ele>").append(String.valueOf(pos.altitude)).append("</ele>\r\n");
        Date date = pos.date;
        String universalDateStamp = DateTimeUtil.getUniversalDateStamp(date);
        gpx.append("<time>").append(universalDateStamp).append("</time>\r\n");
        if (pos.getSatcountvalid()) {
            gpx.append("<sat>").append(String.valueOf(pos.getSatcount())).append("</sat>\r\n");
        }
        if (gpgsa != null) {
            gpx.append("<fix>").append(String.valueOf(gpgsa.getFixtype().toLowerCase())).append("</fix>\r\n");
            gpx.append("<hdop>").append(String.valueOf(gpgsa.getHdop())).append("</hdop>\r\n");
            gpx.append("<vdop>").append(String.valueOf(gpgsa.getVdop())).append("</vdop>\r\n");
            gpx.append("<pdop>").append(String.valueOf(gpgsa.getPdop())).append("</pdop>\r\n");
        }
        gpx.append("</trkpt>\r\n");
    }

    private static String formatDegrees(double degrees) {
        return String.valueOf(((int) (degrees * 1000000)) / 1000000.0);
    }

    /** Export markers to GPX format */
    public static String createMarkers(Track track) {
        StringBuffer gpx = new StringBuffer();
        int markerCount = track.getMarkerCount();
        for (int i = 0; i < markerCount; i++) {
            Marker marker = track.getMarker(i);
            GpsPosition pos = marker.getPosition();
            String lat = formatDegrees(pos.latitude);
            String lon = formatDegrees(pos.longitude);
            String name = marker.getName();
            String link = marker.getReference();
            if (name.length() < 1) {
                name = "Marker-" + String.valueOf(i);
            }
            gpx.append("<wpt lat=\"").append(lat).append("\" lon=\"").append(lon).append("\">\r\n");
            gpx.append(" <name>").append(name).append("</name>\r\n");
            gpx.append(" <sym>Marker</sym>\r\n");
            if (link.length() >= 1) {
                gpx.append(" <link href=\"").append(link).append("\" />");
            }
            gpx.append("</wpt>\r\n");
        }
        return gpx.toString();
    }

    /** Export waypoints to GPX format */
    private String createPlaceMarks(Vector waypoints) {
        if (waypoints == null) {
            return "";
        }
        StringBuffer gpx = new StringBuffer();
        Enumeration waypointEnum = waypoints.elements();
        while (waypointEnum.hasMoreElements()) {
            Place wp = (Place) waypointEnum.nextElement();
            String lat = formatDegrees(wp.getLatitude());
            String lon = formatDegrees(wp.getLongitude());
            String name = wp.getName();
            gpx.append("<wpt lat=\"").append(lat).append("\" lon=\"").append(lon).append("\">\r\n");
            gpx.append(" <name>").append(name).append("</name>\r\n");
            gpx.append(" <sym>Waypoint</sym>\r\n");
            gpx.append("</wpt>\r\n");
        }
        return gpx.toString();
    }

    /**
     * Import a Gpx file to a Track object
     */
    public Track importTrack(KXmlParser parser) {
        Logger.debug("Starting to parse GPX track from file");
        Track result = null;
        try {
            int eventType = parser.getEventType();
            while (!(eventType == XmlPullParser.START_TAG && parser.getName() != null && parser.getName().toLowerCase().equals("gpx"))) {
                eventType = parser.next();
            }
            result = new Track();
            parseGPX(parser, result);
            System.out.println("Finished");
        } catch (XmlPullParserException e) {
            Logger.warn("XmlPullParserException caught Parsing Track : " + e.toString());
            e.printStackTrace();
            result = null;
        } catch (IOException e) {
            Logger.warn("IOException caught Parsing Track : " + e.toString());
            e.printStackTrace();
            result = null;
        } catch (Exception e) {
            Logger.warn("Exception caught Parsing Track : " + e.toString());
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    private void parseGPX(KXmlParser parser, Track track) throws XmlPullParserException, IOException {
        Logger.info("Starting parseGPX");
        int eventType = parser.getEventType();
        while (!(eventType == XmlPullParser.END_TAG && parser.getName() != null && parser.getName().toLowerCase().equals("gpx"))) {
            eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                final String type = parser.getName().toLowerCase();
                if (type.equals("trk")) {
                    parseTRK(parser, track);
                }
            }
        }
    }

    /**
     * TODO
     */
    private void parseTRK(KXmlParser parser, Track track) throws XmlPullParserException, IOException {
        Logger.info("Starting parseTRK");
        int eventType = parser.getEventType();
        while (!(eventType == XmlPullParser.END_TAG && parser.getName() != null && parser.getName().toLowerCase().equals("trk"))) {
            eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                final String type = parser.getName().toLowerCase();
                if (type.equals("name")) {
                    try {
                        parseName(parser, track);
                    } catch (Exception e) {
                        Logger.debug("Failed to Parse 'name'" + e.toString());
                    }
                } else if (type.equals("trkseg")) {
                    parseTRKSEG(parser, track);
                }
            }
        }
    }

    /**
     * TODO
     */
    private void parseName(KXmlParser parser, Track track) throws XmlPullParserException, IOException {
        Logger.info("Starting parseName");
        int eventType = parser.getEventType();
        if (eventType == XmlPullParser.START_TAG && parser.getName() != null && parser.getName().toLowerCase().equals("name")) {
            eventType = parser.next();
            if (eventType == XmlPullParser.TEXT) {
                track.setName(parser.getText());
                parser.next();
                return;
            } else {
                throw new XmlPullParserException(LocaleManager.getMessage("gpx_converter_parsename_error", new Object[] { Integer.toString(eventType) }));
            }
        } else {
            throw new XmlPullParserException(LocaleManager.getMessage("gpx_converter_parsename_error_start_tag", new Object[] { Integer.toString(eventType) }));
        }
    }

    /**
     * TODO
     */
    private void parseTRKSEG(KXmlParser parser, Track track) throws XmlPullParserException, IOException {
        int count = 0;
        Logger.info("Starting parseTRKSEG");
        int eventType = parser.getEventType();
        if (eventType == XmlPullParser.START_TAG && parser.getName() != null && parser.getName().toLowerCase().equals("trkseg")) {
            while (!(eventType == XmlPullParser.END_TAG && parser.getName() != null && parser.getName().toLowerCase().equals("trkseg"))) {
                if (eventType == XmlPullParser.START_TAG && parser.getName() != null && parser.getName().toLowerCase().equals("trkpt")) {
                    GpsPosition pos = null;
                    try {
                        pos = parseTRKPT(parser);
                    } catch (Exception e) {
                        Logger.warn("Exception caught trying to parseTRKPT: " + e.toString());
                    }
                    if (pos != null) {
                        count++;
                        track.addPosition(pos);
                    }
                }
                eventType = parser.next();
            }
        } else {
            throw new XmlPullParserException(LocaleManager.getMessage("gpx_converter_parsetrkseg_error", new Object[] { Integer.toString(eventType) }));
        }
        Logger.debug("Added " + count + " GpsPositions");
    }

    /**
     * TODO
     */
    private GpsPosition parseTRKPT(KXmlParser parser) throws XmlPullParserException, IOException {
        Logger.info("Starting parseTRKPT");
        short course = 0;
        double longitudeDouble = 0;
        double latitudeDouble = 0;
        double speed = 0;
        double altitude = 0;
        Date date = null;
        int eventType = parser.getEventType();
        if (eventType == XmlPullParser.START_TAG && parser.getName() != null && parser.getName().toLowerCase().equals("trkpt")) {
            int attributes = parser.getAttributeCount();
            for (int i = 0; i < attributes; i++) {
                String name = parser.getAttributeName(i).toLowerCase();
                String value = parser.getAttributeValue(i);
                if (name.equals("lat")) {
                    latitudeDouble = Double.parseDouble(value);
                } else if (name.equals("lon")) {
                    longitudeDouble = Double.parseDouble(value);
                }
            }
            while (!(eventType == XmlPullParser.END_TAG && parser.getName() != null && parser.getName().toLowerCase().equals("trkpt"))) {
                eventType = parser.next();
                if (eventType == XmlPullParser.START_TAG) {
                    final String name = parser.getName().toLowerCase();
                    if (name.equals("ele")) {
                        eventType = parser.next();
                        if (eventType == XmlPullParser.TEXT) {
                            altitude = Double.parseDouble(parser.getText());
                        }
                        parser.next();
                    } else if (name.equals("time")) {
                        eventType = parser.next();
                        if (eventType == XmlPullParser.TEXT) {
                            date = parseDate(parser.getText());
                        }
                        parser.next();
                    }
                }
            }
        } else {
            throw new XmlPullParserException(LocaleManager.getMessage("gpx_converter_parsetrkpt_error", new Object[] { Integer.toString(eventType) }));
        }
        Logger.info("Parsed GpsPosition: lat:" + latitudeDouble + " |lon:" + longitudeDouble + " |altitude:" + altitude + " |speed:" + speed);
        return new GpsPosition(course, longitudeDouble, latitudeDouble, speed, altitude, date);
    }

    /**
     * Going to assume date is always in the form:<br>
     * 2006-05-25T08:55:01Z<br>
     * 2006-05-25T08:56:35Z<br>
     * <br>
     * i.e.: yyyy-mm-ddThh-mm-ssZ
     * 
     * @param dateString
     * @return
     */
    private Date parseDate(String dateString) {
        try {
            final int year = Integer.parseInt(dateString.substring(0, 4));
            final int month = Integer.parseInt(dateString.substring(5, 7));
            final int day = Integer.parseInt(dateString.substring(8, 10));
            final int hour = Integer.parseInt(dateString.substring(11, 13));
            final int minute = Integer.parseInt(dateString.substring(14, 16));
            final int second = Integer.parseInt(dateString.substring(17, 19));
            final String reconstruct = year + "-" + (month < 10 ? "0" : "") + month + "-" + (day < 10 ? "0" : "") + day + "T" + (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute + ":" + (second < 10 ? "0" : "") + second + "Z";
            if (dateString.toLowerCase().equals(reconstruct.toLowerCase())) {
                Logger.info("Same");
            } else {
                Logger.info(dateString + " not same as " + reconstruct);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, second);
            return calendar.getTime();
        } catch (Exception e) {
            Logger.warn("Exception caught trying to parse date : " + e.toString());
        }
        return null;
    }

    public Vector importPlace(KXmlParser parser) {
        Logger.debug("Starting to parse GPX waypoint from file");
        Vector result = null;
        try {
            int eventType = parser.getEventType();
            while (!(eventType == XmlPullParser.START_TAG && parser.getName() != null && parser.getName().toLowerCase().equals("gpx"))) {
                eventType = parser.next();
            }
            result = new Vector();
            parseGPXWaypoint(parser, result);
            System.out.println("Finished");
        } catch (XmlPullParserException e) {
            Logger.warn("XmlPullParserException caught Parsing Waypoint : " + e.toString());
            e.printStackTrace();
            result = null;
        } catch (IOException e) {
            Logger.warn("IOException caught Parsing Waypoint : " + e.toString());
            e.printStackTrace();
            result = null;
        } catch (Exception e) {
            Logger.warn("Exception caught Parsing Waypoint : " + e.toString());
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    private void parseGPXWaypoint(KXmlParser parser, Vector waypoints) throws XmlPullParserException, IOException {
        Logger.info("Starting parseGPXWaypoint");
        int eventType = parser.getEventType();
        while (!(eventType == XmlPullParser.END_TAG && parser.getName() != null && parser.getName().toLowerCase().equals("gpx"))) {
            eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                final String type = parser.getName().toLowerCase();
                if (type.equals("wpt")) {
                    parseWPT(parser, waypoints);
                }
            }
        }
    }

    private void parseWPT(KXmlParser parser, Vector waypoints) throws XmlPullParserException, IOException {
        Logger.info("Starting parseWPT");
        double longitudeDouble = 0;
        double latitudeDouble = 0;
        String wptName = "WP_UNTITLED";
        int eventType = parser.getEventType();
        if (eventType == XmlPullParser.START_TAG && parser.getName() != null && parser.getName().toLowerCase().equals("wpt")) {
            int attributes = parser.getAttributeCount();
            for (int i = 0; i < attributes; i++) {
                String name = parser.getAttributeName(i).toLowerCase();
                String value = parser.getAttributeValue(i);
                if (name.equals("lat")) {
                    latitudeDouble = Double.parseDouble(value);
                } else if (name.equals("lon")) {
                    longitudeDouble = Double.parseDouble(value);
                }
            }
            while (!(eventType == XmlPullParser.END_TAG && parser.getName() != null && parser.getName().toLowerCase().equals("wpt"))) {
                eventType = parser.next();
                if (eventType == XmlPullParser.START_TAG) {
                    final String name = parser.getName().toLowerCase();
                    if (name.equals("name")) {
                        eventType = parser.next();
                        if (eventType == XmlPullParser.TEXT) {
                            wptName = parser.getText();
                        }
                        parser.next();
                    }
                }
            }
        } else {
            throw new XmlPullParserException(LocaleManager.getMessage("px_converter_parsewpt_error", new Object[] { Integer.toString(eventType) }));
        }
        Logger.info("Parsed GpsPosition: name:" + wptName + " |lat:" + latitudeDouble + " |lon:" + longitudeDouble);
        Place waypoint = new Place(wptName, latitudeDouble, longitudeDouble);
        waypoints.addElement(waypoint);
    }
}
