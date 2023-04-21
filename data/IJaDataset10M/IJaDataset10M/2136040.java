package org.alcibiade.sculpt.mesh.annotations;

import org.alcibiade.sculpt.math.Vector;

/**
 * A single control point annotation.
 * 
 * @author Yannick Kirschhoffer
 * 
 */
public class ControlPoint extends PrimitiveAnnotation {

    /**
	 * Actual coordinates of this control point.
	 */
    private Vector coordinates;

    /**
	 * Name of this control point.
	 */
    private String name;

    /**
	 * Create a new control point annotation.
	 * 
	 * @param source
	 *            The source of this annotation.
	 * @param coordinates
	 *            Coordinates of the point.
	 * @param name
	 *            Name of this control point
	 */
    public ControlPoint(AnnotationSource source, Vector coordinates, String name) {
        super(source);
        this.coordinates = coordinates;
        this.name = name;
    }

    /**
	 * Create an unnamed control point
	 * 
	 * @param source
	 *            The source of this annotation.
	 * @param coordinates
	 *            Coordinates of the point.
	 */
    public ControlPoint(AnnotationSource source, Vector coordinates) {
        this(source, coordinates, "");
    }

    /**
	 * Get the name of this control point.
	 * 
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * Get the coordinates of this control point.
	 * 
	 * @return the coordinates as a vector.
	 */
    public Vector getCoordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        return "ControlPoint " + name + ": " + coordinates;
    }
}
