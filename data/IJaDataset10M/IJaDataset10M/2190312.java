package com.jchapman.jempire.map;

import java.awt.Point;

/**
 *
 * @author Jeff Chapman
 * @version 1.0
 */
public interface MapLocator {

    /**
     * Returns the row and column values for a map location.
     * The row is the x value and the column is the y value.
     *
     * @param location int
     * @return Point
     */
    public Point getRowColumnForLocation(int location);
}
