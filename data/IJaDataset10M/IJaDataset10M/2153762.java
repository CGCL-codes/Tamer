package com.mapquest.spatialbase.sid;

import com.mapquest.spatialbase.CartesianPoint;
import com.mapquest.spatialbase.FixedLatLng;
import com.mapquest.spatialbase.LatLng;

/**
 * The spatial index class assigns a sequence of spatial indices for each lat-lng.
 * Each level of indices represents concentric squares of four quadrants each.  The
 * quadrants are numbered starting from zero in the lower-left and proceeding
 * counter-clockwise to 3.  With this approach, each spatial index level uses 2 bits
 * of the spatial index.  In the C++ implementation, this assured us that 16 spatial
 * levels could be represented in one unsigned 32-bit number.  Since Java does not have
 * unsigned integers, we will use a 64-bit number to hold the spatial index.  This is
 * not as significant of a choice as it was in the C++ implementation, because most
 * JVMs that we use are now 64-bit and can natively do arithmetic on this size word.
 * <p>
 * In order to make arithmetic easier, the coordinate system is normalized such that
 * the (0,0) point is set at 0 degress longitude, -90 degrees lattitude.  This gives
 * us a coordinate system that is all positive from (0,0) -> (180,360).  In this
 * class, we are not concerned about the errors due to treating the earth as a plane
 * instead of a spheroid.  We will factor out some of this error in the query
 * phase when we determine which spatial indexes should be used to represent a
 * bounding box.  This error also fits with our construction of the spatial index as
 * an imprecise but fast estimator.
 * 
 * @author tlaurenzomq
 *
 */
public class SpatialIndexer {

    public static final int MAX_LEVELS = 28;

    /**
	 * Calculate the highest level spatial index for the given latlng, optionally calling the visitor
	 * at each level if not null.
	 * @param ll
	 * @param v
	 * @return
	 */
    public static SpatialIndex visitSpatialIndexes(LatLng ll, int maxLevel, ISpatialIndexVisitor v) {
        if (maxLevel < 0 || maxLevel > MAX_LEVELS) {
            throw new IllegalArgumentException("Illegal requested spatial level");
        }
        CartesianPoint checkPoint = ll.toCartesian();
        int[] bbCoords = new int[] { 0, 0, 360 * FixedLatLng.SCALE_FACTOR_INT, 180 * FixedLatLng.SCALE_FACTOR_INT };
        int quadrant;
        long index = 0;
        int level;
        for (level = 0; level <= maxLevel; level += 1) {
            quadrant = findQuadrant(checkPoint, bbCoords);
            index = (index << 2) | (quadrant & 0x3);
            if (v != null && !v.visit(level, index, bbCoords)) break;
        }
        return new SpatialIndex(index, level - 1);
    }

    /**
	 * Calculate a spatial index for a given lat/long.  Returns the lowest level spatial index
	 * available.
	 * @param ll
	 * @return SpatialIndex
	 */
    public static SpatialIndex calculateSpatialIndex(LatLng ll, int maxLevel) {
        return visitSpatialIndexes(ll, maxLevel, null);
    }

    /**
	 * Gets in-depth information on many spatial levels.  Primarily used when forumulating queries.
	 * @param ll
	 * @param maxLevels
	 * @return
	 */
    public static SpatialLevel[] calculateSpatialLevels(LatLng ll, final int maxLevel) {
        final SpatialLevel[] ret = new SpatialLevel[maxLevel + 1];
        visitSpatialIndexes(ll, maxLevel, new ISpatialIndexVisitor() {

            public boolean visit(int level, long index, int[] bbCoords) {
                SpatialLevel sl = new SpatialLevel(new CartesianPoint(bbCoords[0], bbCoords[1]), new CartesianPoint(bbCoords[2], bbCoords[3]), new SpatialIndex(index, level));
                ret[level] = sl;
                return true;
            }
        });
        return ret;
    }

    /**
	 * Finds the quadrant of checkXY within the given bounding box coordinates in
	 * a cartesian plane.  BB cords are specified as { llX, llY, urX, urY }.  Returned quadrant
	 * is between 0-3 as follows:
	 *   - 0: Lower Left
	 *   - 1: Upper Left
	 *   - 2: Upper Right
	 *   - 3: Lower Right
	 * <p> 
	 * When performing comparisons, less thans are promoted to <=.
	 * 
	 * <p>
	 * On return, bbCoords is updated with the coordinates of the bounding box for the quadrant returned.
	 * <p>
	 * This method does not check to make sure the requested point is within the bounding box.
	 * 
	 * @param checkPoint
	 * @param bbCoords
	 * @return Quadrant number (0-3)
	 */
    private static int findQuadrant(CartesianPoint checkPoint, int[] bbCoords) {
        int midX = (bbCoords[0] + bbCoords[2]) / 2;
        int midY = (bbCoords[1] + bbCoords[3]) / 2;
        int checkX = checkPoint.getX();
        int checkY = checkPoint.getY();
        if (checkX <= midX) {
            if (checkY <= midY) {
                bbCoords[2] = midX;
                bbCoords[3] = midY;
                return 0;
            } else {
                bbCoords[1] = midY;
                bbCoords[2] = midX;
                return 1;
            }
        } else {
            if (checkY <= midY) {
                bbCoords[0] = midX;
                bbCoords[3] = midY;
                return 3;
            } else {
                bbCoords[0] = midX;
                bbCoords[1] = midY;
                return 2;
            }
        }
    }
}
