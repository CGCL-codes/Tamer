package nl.javel.gisbeans.geom;

import java.io.Serializable;

/**
 * the GisObject class stores both the java2D shape as the accomplishing array
 * of attributes.
 * 
 * @author <a href="mailto:peter.jacobs@javel.nl">Peter Jacobs </a> <br>
 *         <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
 * @since JDK 1.0
 * @version 1.0
 */
public class GisObject implements Serializable {

    /** the represented shape */
    private Object shape;

    /** the attributes */
    private String[] attributes;

    /**
	 * constructs a GisObject
	 * 
	 * @param shape the shape (either a <code>java.awt.geom.Point2D</code> or
	 *        a <code>java.awt.Shape</code>
	 * @param attributes attributes
	 */
    public GisObject(final Object shape, final String[] attributes) {
        this.shape = shape;
        this.attributes = attributes;
    }

    /**
	 * returns the shape of the GisObject
	 * 
	 * @return Object the resulting shape
	 */
    public Object getShape() {
        return this.shape;
    }

    /**
	 * returns the attributes of the shape
	 * 
	 * @return String[] the array of Strings representing the attributes of the
	 *         GisObject.
	 */
    public String[] getAttributes() {
        return this.attributes;
    }
}
