package com.allen_sauer.gwt.dnd.client.util;

/**
 * A position represented by a left (x) and top (y) coordinate.
 */
public class CoordinateLocation extends AbstractLocation {

    private int left;

    private int top;

    public CoordinateLocation(int left, int top) {
        this.left = left;
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
    }
}
