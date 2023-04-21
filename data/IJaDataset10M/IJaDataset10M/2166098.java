package rez;

import rez.World;
import rez.Coord;

/**
 * <dl>
 * <p><dt><b>Summary :</b></dt></p>
 * <dd>
 * This class represents a bounds with a topLeft (NorthWest) coord and a bottomRight
 * (southEast) coord.
 * </dd>
 * <p><dt><b>Description :</b></dt></p>
 * <dd>
 * fields:
 
 * </OL>
 * </dd>
 * </dl>
 * <p>
 * @author Chris Thorne
 * @version 1.0 feb 2004
 * <p>
 */
public class Bounds extends GeoGraph {

    /** botttom right corner x coord */
    private double bottomRightx = 1d;

    /** botttom right corner z coord */
    private double bottomRightz = 1d;

    private Coord bottomRightCoord = new Coord();

    private Coord topLeftCoord = new Coord();

    /** bounds of in the x direction */
    private double xbboxSize = 1d;

    /** bounds of in the z direction */
    private double zbboxSize = 1d;

    public double getxbboxSize() {
        return xbboxSize;
    }

    public void setxbboxSize(double xbboxSize) {
        this.xbboxSize = xbboxSize;
    }

    public double getzbboxSize() {
        return zbboxSize;
    }

    public void setzbboxSize(double zbboxSize) {
        this.zbboxSize = zbboxSize;
    }

    /** constructor */
    public Bounds() {
    }

    public double getBottomRightx() {
        return bottomRightx;
    }

    public void setBottomRightx(double bottomRightx) {
        this.bottomRightx = bottomRightx;
        bottomRightCoord.setLon(bottomRightx);
    }

    public void setBottomRightLon(double bottomRightx) {
        this.bottomRightx = bottomRightx;
        bottomRightCoord.setLon(bottomRightx);
    }

    public double getBottomRightz() {
        return bottomRightz;
    }

    public void setBottomRightz(double bottomRightz) {
        this.bottomRightz = bottomRightz;
        bottomRightCoord.setLat(bottomRightz);
    }

    public void setBottomRightLat(double bottomRightz) {
        this.bottomRightz = bottomRightz;
        bottomRightCoord.setLat(bottomRightz);
    }

    /**
     * Set the North West lat
     */
    public void setTopLeftLat(double lat) {
        topLeftCoord.setLat(lat);
    }

    /**
     * Set the North West lon
     */
    public void setTopLeftLon(double lon) {
        topLeftCoord.setLon(lon);
    }

    /**
     * Get the North West lon
     */
    public double getTopLeftLon() {
        return topLeftCoord.getLon();
    }

    /**
     * Get the North West lat
     */
    public double getTopLeftLat() {
        return topLeftCoord.getLat();
    }

    /**
     * Get the South East lon
     */
    public double getBottomRightLon() {
        return bottomRightCoord.getLon();
    }

    /**
     * Get the South East lat
     */
    public double getBottomRightLat() {
        return bottomRightCoord.getLat();
    }

    /**
     * Get the south east lon in meters
     */
    public double getBottomRightLonInMeters() {
        return bottomRightCoord.getLonInMeters();
    }

    /**
     * Get the south east lat in meters
     */
    public double getBottomRightLatInMeters() {
        return bottomRightCoord.getLatInMeters();
    }
}
