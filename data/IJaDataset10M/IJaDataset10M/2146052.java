package org.shapelogic.imageprocessing;

/** LineProperties contains properties that are important for a point
 * when you are dealing with curved multi line.
 * 
 * @author Sami Badawi
 *
 * Properties to keep track of
 * Direction change for 2 adjacent lines, when following the multi line
 * Sharp corners
 * 
 */
public class PointProperties {

    public double directionChange;

    public boolean sharpCorner;
}
