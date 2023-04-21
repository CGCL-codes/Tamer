package org.alcibiade.sculpt.mesh.annotations;

import org.alcibiade.sculpt.math.Vector;

/**
 * A segment to display a dimension. It stored as an ordered (Src->Dst) set of
 * points, even if it should only be used to represent asymetric distances.
 * 
 * @author Yannick Kirschhoffer
 * 
 */
public class DimensionSegment extends PrimitiveAnnotation {

    /**
	 * Coordinates of the segment.
	 */
    private Vector pointSrc;

    private Vector pointDst;

    /**
	 * Name of this control point.
	 */
    private String name;

    /**
	 * Create a new dimension segment.
	 * 
	 * @param source
	 *            The source of this annotation.
	 * @param pointSrc
	 *            The source point.
	 * @param pointDst
	 *            The destination point.
	 * @param name
	 *            The name of this segment.
	 */
    public DimensionSegment(AnnotationSource source, Vector pointSrc, Vector pointDst, String name) {
        super(source);
        this.pointSrc = pointSrc;
        this.pointDst = pointDst;
        this.name = name;
    }

    /**
	 * Create an unnamed segment.
	 * 
	 * @param source
	 *            The source of this annotation.
	 * @param pointSrc
	 *            The source point.
	 * @param pointDst
	 *            The destination point.
	 */
    public DimensionSegment(AnnotationSource source, Vector pointSrc, Vector pointDst) {
        this(source, pointSrc, pointDst, "");
    }

    /**
	 * Check if this annotation is named.
	 * 
	 * @return true if a name was set for this annotation.
	 */
    public boolean hasName() {
        return name.length() > 0;
    }

    /**
	 * Get the name of this segment.
	 * 
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return the source point coordinates.
	 */
    public Vector getPointSrc() {
        return pointSrc;
    }

    /**
	 * @return the destination point coordinates.
	 */
    public Vector getPointDst() {
        return pointDst;
    }

    @Override
    public String toString() {
        return "DimensionSegment " + name + ": " + pointSrc + " - " + pointDst;
    }
}
