package org.grailrtls.gui.network;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Eiman Elnahrawy
 *
 */
public class SnapshotInfo {

    static final long DEFAULT_TIME = Long.MAX_VALUE;

    long timeInstance;

    ArrayList<PositionInfo> positions;

    ArrayList<TransmitterInfo> transmitters;

    public SnapshotInfo(long time) {
        this.timeInstance = time;
        positions = new ArrayList<PositionInfo>();
        transmitters = new ArrayList<TransmitterInfo>();
    }

    public long getTime() {
        return this.timeInstance;
    }

    public ArrayList getPositions() {
        return this.positions;
    }

    public ArrayList getTransmitters() {
        return this.transmitters;
    }

    public void addPosition(PositionInfo position) {
        this.positions.add(position);
        this.transmitters.add(position.transmitterInfo);
    }

    public void addPosition(TransmitterInfo transmitter, RegionInfo region, float x, float y, float xPrime, float yPrime) {
        PositionInfo posInfo = new PositionInfo(transmitter, region, PositionInfo.LOCATION_TYPE_ELLIPSE, this.timeInstance, x, xPrime, y, yPrime, Float.NaN, Float.NaN);
        this.positions.add(posInfo);
        this.transmitters.add(transmitter);
    }

    public void clear() {
        this.timeInstance = this.DEFAULT_TIME;
        this.positions = null;
        this.transmitters = null;
    }

    public ArrayList<String> computeIntersections() {
        if (this.positions.size() == 0) {
            System.out.println("No locations in this snapshot... ");
            return null;
        }
        ArrayList<String> intersectionList = new ArrayList();
        for (PositionInfo location1 : this.positions) {
            for (PositionInfo location2 : this.positions) {
                String intersectionString = "";
                intersectionString += location1.transmitterInfo.address + " ";
                intersectionString += String.format("%.2f", location1.area()) + " ";
                intersectionString += location2.transmitterInfo.address + " ";
                intersectionString += String.format("%.2f", location2.area()) + " ";
                intersectionString += String.format("%.2f", location1.intersectWith(location2));
                System.out.println(intersectionString);
                intersectionList.add(intersectionString);
            }
        }
        return intersectionList;
    }

    public ArrayList<String> computeIntersections(Rectangle2D rec) {
        if (this.positions.size() == 0) {
            System.out.println("No locations in this snapshot... ");
            return null;
        }
        ArrayList<String> intersectionList = new ArrayList();
        for (PositionInfo location : this.positions) {
            String intersectionString = "";
            intersectionString += String.format("%.2f", rec.getWidth() * rec.getHeight()) + " ";
            intersectionString += location.transmitterInfo.address + " ";
            intersectionString += String.format("%.2f", location.area()) + " ";
            intersectionString += String.format("%.2f", location.intersectWith(rec));
            System.out.println(intersectionString);
            intersectionList.add(intersectionString);
        }
        return intersectionList;
    }
}
