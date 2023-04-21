package org.scotlandyard.impl.engine.boardmap;

import org.scotlandyard.engine.GameException;
import org.scotlandyard.engine.boardmap.BoardMap;
import org.scotlandyard.engine.boardmap.Coordinate;

/**
 * Implementation of position object
 * 
 * @author Hussain Al-Mutawa
 * @version 1.0
 * @since Sun Sep 18 13:28:08 NZST 2011
 */
public class CoordinateImpl implements Coordinate {

    private final transient double latitude, longtitude;

    private transient String label, color;

    private final transient BoardMap boardMap;

    /**
	 * Initialises a coordinate object and assign it to a boardMap
	 * 
	 * @param boardMap
	 * @param latitude
	 * @param longtitude
	 * @throws GameException if the boardMap is not specified
	 */
    public CoordinateImpl(final BoardMap boardMap, final double latitude, final double longtitude) throws GameException {
        super();
        if (boardMap == null) {
            throw new GameException("BoardMap must be specified first");
        }
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.boardMap = boardMap;
        this.label = boardMap.getName() + ":Coordinate[" + latitude + "," + longtitude + "]";
    }

    /**
	 * Initialises a coordinate object and assign it to a boardMap with a color
	 *
	 * @param boardMap
	 * @param latitude
	 * @param longtitude
	 * @throws GameException if the boardMap is not specified
	 */
    public CoordinateImpl(final BoardMap boardMap, final double latitude, final double longtitude, final String color) throws GameException {
        this(boardMap, latitude, longtitude);
        this.color = color;
    }

    @Override
    public int compareTo(final Coordinate other) {
        final Double c1Lng = new Double(getLongtitude());
        final Double c2Lng = new Double(other.getLongtitude());
        final int compare = c1Lng.compareTo(c2Lng);
        Double c1Lat = null;
        Double c2Lat = null;
        int temp = 0;
        if (compare == 0) {
            c1Lat = new Double(getLatitude());
            c2Lat = new Double(other.getLatitude());
            temp = c1Lat.compareTo(c2Lat);
        } else {
            temp = compare;
        }
        return temp;
    }

    @Override
    public boolean equals(final Object otherObject) {
        return toString().equals(otherObject.toString());
    }

    @Override
    public BoardMap getBoardMap() {
        return boardMap;
    }

    public String getColor() {
        return this.color;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongtitude() {
        return longtitude;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public void setLabel(final String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "{" + boardMap.toString() + " Coordinate <Latitude=" + getLatitude() + " , Longtitude=" + getLongtitude() + ">}";
    }
}
