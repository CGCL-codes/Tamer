package com.ibm.realtime.flexotask.editor.model;

import java.util.StringTokenizer;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

/**
 * Extends ModelElement with notions of location and size, as well as property sheet editing
 *   support
 */
public abstract class ShapedModelElement extends ModelElement {

    /** Property ID to use when the size of this shape is modified. */
    public static final String SIZE_PROP = "Shape.Size";

    /** Property ID to use when the location of this shape is modified. */
    public static final String LOCATION_PROP = "Shape.Location";

    /** Location of this shape. */
    private Point location = new Point(0, 0);

    /** Size of this shape. */
    private Dimension size = new Dimension(60, 60);

    /**
   * Return the Location of this shape.
   * @return a non-null location instance
   */
    public Point getLocation() {
        return location.getCopy();
    }

    /**
   * Return the Size of this shape.
   * @return a non-null Dimension instance
   */
    public Dimension getSize() {
        return size.getCopy();
    }

    /**
   * Set the Location of this shape.
   * @param newLocation a non-null Point instance
   * @throws IllegalArgumentException if the parameter is null
   */
    public void setLocation(Point newLocation) {
        if (newLocation == null) {
            throw new IllegalArgumentException();
        }
        location.setLocation(newLocation);
        firePropertyChange(LOCATION_PROP, null, location);
    }

    /**
   * Set the Size of this shape.
   * Will not modify the size if newSize is null.
   * @param newSize a non-null Dimension instance or null
   */
    public void setSize(Dimension newSize) {
        if (newSize != null) {
            size.setSize(newSize);
            firePropertyChange(SIZE_PROP, null, size);
        }
    }

    /**
   * Set the size and location from the text of a graphics XML attribute
   *   in one of the two external XML representations
   * @param graphics the text of the graphics attribute
   */
    public void setGraphics(String graphics) {
        if (graphics == null) {
            throw new IllegalArgumentException("No graphics attribute available");
        }
        try {
            StringTokenizer toks = new StringTokenizer(graphics);
            int width = nextInt(toks);
            int height = nextInt(toks);
            setSize(new Dimension(width, height));
            int x = nextInt(toks);
            int y = nextInt(toks);
            setLocation(new Point(x, y));
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Ill-formed graphics attribute: " + graphics);
        }
    }

    /**
   * Get the size and location of this graphic task representation as a String for
   *   assignment to a graphics XML attribute
   * @return the XML graphics attribute string to use
   */
    public String getGraphics() {
        return getSize().width + " " + getSize().height + " " + getLocation().x + " " + getLocation().y;
    }

    /**
   * Get the next integer from a string
   * @param toks a StringTokenizer over the string
   * @return the next integer
   */
    private int nextInt(StringTokenizer toks) {
        return Integer.parseInt(toks.nextToken());
    }
}
