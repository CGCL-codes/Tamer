package com.android.locationtracker.data;

import com.android.locationtracker.data.TrackerEntry.EntryType;
import android.location.Location;

/**
 * Formats tracker data as KML output
 */
class KMLFormatter implements IFormatter {

    public String getHeader() {
        LineBuilder builder = new LineBuilder();
        builder.addLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.addLine("<kml xmlns=\"http://earth.google.com/kml/2.2\">");
        builder.addLine("<Document>");
        return builder.toString();
    }

    public String getFooter() {
        LineBuilder builder = new LineBuilder();
        builder.addLine("</Document>");
        builder.addLine("</kml>");
        return builder.toString();
    }

    public String getOutput(TrackerEntry entry) {
        LineBuilder builder = new LineBuilder();
        if (entry.getType() == EntryType.LOCATION_TYPE) {
            Location loc = entry.getLocation();
            builder.addLine("<Placemark>");
            builder.addLine("<description>");
            builder.addLine("accuracy = " + loc.getAccuracy());
            builder.addLine("distance from last network location  = " + entry.getDistFromNetLocation());
            builder.addLine("</description>");
            builder.addLine("<TimeStamp>");
            builder.addLine("<when>" + entry.getTimestamp() + "</when>");
            builder.addLine("</TimeStamp>");
            builder.addLine("<Point>");
            builder.addLine("<coordinates>");
            builder.addLine(loc.getLongitude() + "," + loc.getLatitude() + "," + loc.getAltitude());
            builder.addLine("</coordinates>");
            builder.addLine("</Point>");
            builder.addLine("</Placemark>");
        }
        return builder.toString();
    }

    private static class LineBuilder {

        private StringBuilder mBuilder;

        public LineBuilder() {
            mBuilder = new StringBuilder();
        }

        public void addLine(String line) {
            mBuilder.append(line);
            mBuilder.append("\n");
        }

        @Override
        public String toString() {
            return mBuilder.toString();
        }
    }
}
