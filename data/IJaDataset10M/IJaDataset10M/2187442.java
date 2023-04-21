package com.mapquest.spatialbase.sid;

import com.mapquest.spatialbase.CartesianPoint;
import com.mapquest.spatialbase.LatLng;
import com.mapquest.spatialbase.geometry.DistanceApproximation;
import com.mapquest.spatialbase.geometry.LLRect;

/**
 * Info object for data regarding a specific spatial level
 * @author tlaurenzomq
 *
 */
public class SpatialLevel {

    private static final double milesPerLatDegree = 69.0467669;

    private SpatialIndex spatialIndex;

    private LLRect boundingBox;

    private CartesianPoint urPoint;

    private CartesianPoint llPoint;

    public SpatialLevel(CartesianPoint llPoint, CartesianPoint urPoint, SpatialIndex spatialIndex) {
        this.llPoint = llPoint;
        this.urPoint = urPoint;
        this.spatialIndex = spatialIndex;
        LatLng ll = LatLng.fromCartesian(llPoint);
        LatLng ur = LatLng.fromCartesian(urPoint);
        this.boundingBox = new LLRect(ll, ur);
    }

    public String toString() {
        return "Level[" + getLevel() + "]:" + spatialIndex.getStringValue() + ":" + boundingBox;
    }

    /**
	 * Returns an approximate width of the bounding box in miles.  This does a very simple
	 * calculations to arrive at the width, since the spatial index is just an estimate anyway.
	 * @return
	 */
    public double getWidthInMiles() {
        int latMid = (int) Math.abs(((boundingBox.getUpperRight().getLat() + boundingBox.getLowerLeft().getLat()) / 2));
        DistanceApproximation approx = new DistanceApproximation();
        approx.setTestPoint(latMid, boundingBox.getLowerLeft().getLng());
        double sq = approx.getDistanceSq(latMid, boundingBox.getUpperRight().getLng());
        return Math.sqrt(sq);
    }

    /**
	 * Returns an approximate height of the bounding box in miles.  This does a very simple
	 * calculation to arrive at the height, since the spatial index is just an estimate anyway.
	 * @return
	 */
    public double getHeightInMiles() {
        double latDelta = Math.abs(boundingBox.getUpperRight().getLat() - boundingBox.getLowerLeft().getLat());
        return latDelta * milesPerLatDegree;
    }

    public int getLevel() {
        return spatialIndex.getLevel();
    }

    public SpatialIndex getSpatialIndex() {
        return spatialIndex;
    }

    public LLRect getBoundingBox() {
        return boundingBox;
    }

    public SpatialLevel translateUp() {
        SpatialIndex idx = spatialIndex.moveUp();
        return translateY(idx, 1);
    }

    public SpatialLevel translateDown() {
        SpatialIndex idx = spatialIndex.moveDown();
        return translateY(idx, -1);
    }

    /**
	 * Constructs a new level with the given index and the bounding box translated
	 * in the y direction by count*the height of this box.
	 * @param idx
	 * @param i
	 * @return
	 */
    private SpatialLevel translateY(SpatialIndex idx, int count) {
        int translation = Math.abs(urPoint.getY() - llPoint.getY()) * count;
        return new SpatialLevel(llPoint.translate(0, translation), urPoint.translate(0, translation), idx);
    }

    public SpatialLevel translateLeft() {
        SpatialIndex idx = spatialIndex.moveLeft();
        return translateX(idx, -1);
    }

    public SpatialLevel translateRight() {
        SpatialIndex idx = spatialIndex.moveRight();
        return translateX(idx, 1);
    }

    public SpatialLevel translate(TranslateDirection dir) {
        switch(dir) {
            case DOWN:
                return translateDown();
            case UP:
                return translateUp();
            case LEFT:
                return translateLeft();
            case RIGHT:
                return translateRight();
        }
        throw new Error("Unexpected enum constant");
    }

    /**
	 * Constructs a new level with the given index and the bounding box translated
	 * in the y direction by count*the height of this box.
	 * @param idx
	 * @param i
	 * @return
	 */
    private SpatialLevel translateX(SpatialIndex idx, int count) {
        int translation = Math.abs(urPoint.getX() - llPoint.getX()) * count;
        return new SpatialLevel(llPoint.translate(translation, 0), urPoint.translate(translation, 0), idx);
    }
}
