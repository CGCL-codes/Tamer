package net.sf.bt747.j2se.app.trackgraph;

import gps.log.GPSRecord;

public interface CoordinateResolver {

    public GPSRecord getRecordOfTimestamp(double t);
}
