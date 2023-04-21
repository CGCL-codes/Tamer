package com.ajah.jsvg;

/**
 * @author Eric Savage <a href="mailto:esavage@ajah.com">esavage@ajah.com</a>
 */
public class Polyline extends BasicShape {

    private String points = null;

    private String name = "polyline";

    /**
	 * Returns the points.
	 * @return String
	 */
    public String getPoints() {
        return points;
    }

    /**
	 * Sets the points.
	 * @param points The points to set
	 */
    public void setPoints(String points) {
        this.points = points;
    }

    public String serialize() {
        return "<" + getName() + " />";
    }
}
