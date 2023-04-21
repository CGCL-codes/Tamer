package org.skycastle.util.region;

/**
 * A region of interest that has setter methods.
 *
 * @author Hans Haggstrom
 */
public interface MutableRegionOfInterest extends RegionOfInterest, MutableRectangularRegion {

    void set(RegionOfInterest sourceRegion);

    void set(double centerX, double centerZ, double width_m, double depth_m, double resolution);

    /**
     * @param resolution_m the diameter of the smallest objects to include in this region of interest.
     */
    void setResolution_m(double resolution_m);
}
