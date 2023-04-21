package org.mars_sim.msp.ui.swing.tool.map;

import org.mars_sim.msp.core.Coordinates;
import java.awt.*;

/** 
 * The Map interface represents a map usable by the MapDisplay class.
 */
public interface Map {

    public static final int DISPLAY_HEIGHT = 300;

    public static final int DISPLAY_WIDTH = 300;

    public static final int MAP_VIS_WIDTH = 300;

    public static final int MAP_VIS_HEIGHT = 300;

    public static final double HALF_MAP_ANGLE = 0.48587D;

    public static final int MAP_HEIGHT = 1440;

    public static final int MAP_WIDTH = 2880;

    public static final double PIXEL_RHO = (double) MAP_HEIGHT / Math.PI;

    public static final double TWO_PI = Math.PI * 2D;

    public static final int SCREEN_OFFSET_X = 300;

    public static final int SCREEN_OFFSET_Y = 300;

    /** 
     * Creates a 2D map at a given center point.
     * 
     * @param newCenter the new center location
     * @throws Exception if error in drawing map.
     */
    public void drawMap(Coordinates newCenter);

    /** 
     * Checks if a requested map is complete.
     * 
     * @return true if requested map is complete
     */
    public boolean isImageDone();

    /** 
     * Gets the constructed map image.
     * 
     * @return constructed map image
     */
    public Image getMapImage();
}
