package org.sdf4j.core;

/**
 * Represents a decorated shape outline.
 * 
 * @author Branislav Stojkovic
 */
public class Stroke {

    /**
	 * Joins path segments by extending their outside edges until they meet.
	 */
    public static final int JOIN_MITER = 0;

    /**
	 * Joins path segments by rounding off the corner at a radius of half the
	 * line width.
	 */
    public static final int JOIN_ROUND = 1;

    /**
	 * Joins path segments by connecting the outer corners of their wide
	 * outlines with a straight segment.
	 */
    public static final int JOIN_BEVEL = 2;

    /**
	 * Ends unclosed subpaths and dash segments with no added decoration.
	 */
    public static final int CAP_BUTT = 0;

    /**
	 * Ends unclosed subpaths and dash segments with a round decoration that has
	 * a radius equal to half of the width of the pen.
	 */
    public static final int CAP_ROUND = 1;

    /**
	 * Ends unclosed subpaths and dash segments with a square projection that
	 * extends beyond the end of the segment to a distance equal to half of the
	 * line width.
	 */
    public static final int CAP_SQUARE = 2;

    private float width;

    private int join;

    private int cap;

    private float miterlimit;

    private float dash[];

    float dash_phase;

    /**
	 * Constructs a new <code>Stroke</code> with the specified attributes.
	 * 
	 * @param width
	 *            the width of this <code>Stroke</code>. The width must be
	 *            greater than or equal to 0.0f. If width is set to 0.0f, the
	 *            stroke is rendered as the thinnest possible line for the
	 *            target device and the antialias hint setting.
	 * @param cap
	 *            the decoration of the ends of a <code>Stroke</code>
	 * @param join
	 *            the decoration applied where path segments meet
	 * @param miterlimit
	 *            the limit to trim the miter join. The miterlimit must be
	 *            greater than or equal to 1.0f.
	 * @param dash
	 *            the array representing the dashing pattern
	 * @param dash_phase
	 *            the offset to start the dashing pattern
	 * @throws IllegalArgumentException
	 *             if <code>width</code> is negative
	 * @throws IllegalArgumentException
	 *             if <code>cap</code> is not either CAP_BUTT, CAP_ROUND or
	 *             CAP_SQUARE
	 * @throws IllegalArgumentException
	 *             if <code>miterlimit</code> is less than 1 and
	 *             <code>join</code> is JOIN_MITER
	 * @throws IllegalArgumentException
	 *             if <code>join</code> is not either JOIN_ROUND, JOIN_BEVEL, or
	 *             JOIN_MITER
	 * @throws IllegalArgumentException
	 *             if <code>dash_phase</code> is negative and <code>dash</code>
	 *             is not <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the length of <code>dash</code> is zero
	 * @throws IllegalArgumentException
	 *             if dash lengths are all zero.
	 */
    public Stroke(float width, int cap, int join, float miterlimit, float dash[], float dash_phase) {
        if (width < 0.0f) {
            throw new IllegalArgumentException("negative width");
        }
        if (cap != CAP_BUTT && cap != CAP_ROUND && cap != CAP_SQUARE) {
            throw new IllegalArgumentException("illegal end cap value");
        }
        if (join == JOIN_MITER) {
            if (miterlimit < 1.0f) {
                throw new IllegalArgumentException("miter limit < 1");
            }
        } else if (join != JOIN_ROUND && join != JOIN_BEVEL) {
            throw new IllegalArgumentException("illegal line join value");
        }
        if (dash != null) {
            if (dash_phase < 0.0f) {
                throw new IllegalArgumentException("negative dash phase");
            }
            boolean allzero = true;
            for (int i = 0; i < dash.length; i++) {
                float d = dash[i];
                if (d > 0.0) {
                    allzero = false;
                } else if (d < 0.0) {
                    throw new IllegalArgumentException("negative dash length");
                }
            }
            if (allzero) {
                throw new IllegalArgumentException("dash lengths all zero");
            }
        }
        this.width = width;
        this.cap = cap;
        this.join = join;
        this.miterlimit = miterlimit;
        if (dash != null) {
            this.dash = (float[]) dash.clone();
        }
        this.dash_phase = dash_phase;
    }

    /**
	 * Constructs a solid <code>Stroke</code> with the specified
	 * attributes.
	 * 
	 * @param width
	 *            the width of the <code>Stroke</code>
	 * @param cap
	 *            the decoration of the ends of a <code>Stroke</code>
	 * @param join
	 *            the decoration applied where path segments meet
	 * @param miterlimit
	 *            the limit to trim the miter join
	 * @throws IllegalArgumentException
	 *             if <code>width</code> is negative
	 * @throws IllegalArgumentException
	 *             if <code>cap</code> is not either CAP_BUTT, CAP_ROUND or
	 *             CAP_SQUARE
	 * @throws IllegalArgumentException
	 *             if <code>miterlimit</code> is less than 1 and
	 *             <code>join</code> is JOIN_MITER
	 * @throws IllegalArgumentException
	 *             if <code>join</code> is not either JOIN_ROUND, JOIN_BEVEL, or
	 *             JOIN_MITER
	 */
    public Stroke(float width, int cap, int join, float miterlimit) {
        this(width, cap, join, miterlimit, null, 0.0f);
    }

    /**
	 * Constructs a solid <code>Stroke</code> with the specified
	 * attributes. The <code>miterlimit</code> parameter is unnecessary in cases
	 * where the default is allowable or the line joins are not specified as
	 * JOIN_MITER.
	 * 
	 * @param width
	 *            the width of the <code>Stroke</code>
	 * @param cap
	 *            the decoration of the ends of a <code>Stroke</code>
	 * @param join
	 *            the decoration applied where path segments meet
	 * @throws IllegalArgumentException
	 *             if <code>width</code> is negative
	 * @throws IllegalArgumentException
	 *             if <code>cap</code> is not either CAP_BUTT, CAP_ROUND or
	 *             CAP_SQUARE
	 * @throws IllegalArgumentException
	 *             if <code>join</code> is not either JOIN_ROUND, JOIN_BEVEL, or
	 *             JOIN_MITER
	 */
    public Stroke(float width, int cap, int join) {
        this(width, cap, join, 10.0f, null, 0.0f);
    }

    /**
	 * Constructs a solid <code>Stroke</code> with the specified line width
	 * and with default values for the cap and join styles.
	 * 
	 * @param width
	 *            the width of the <code>Stroke</code>
	 * @throws IllegalArgumentException
	 *             if <code>width</code> is negative
	 */
    public Stroke(float width) {
        this(width, CAP_SQUARE, JOIN_MITER, 10.0f, null, 0.0f);
    }

    /**
	 * Constructs a new <code>BasicStroke</code> with defaults for all
	 * attributes. The default attributes are a solid line of width 1.0,
	 * CAP_SQUARE, JOIN_MITER, a miter limit of 10.0.
	 */
    public Stroke() {
        this(1.0f, CAP_SQUARE, JOIN_MITER, 10.0f, null, 0.0f);
    }

    /**
	 * Returns the line width. Line width is represented in user space, which is
	 * the default-coordinate space used by Java 2D. See the
	 * <code>Graphics2D</code> class comments for more information on the user
	 * space coordinate system.
	 * 
	 * @return the line width of this <code>BasicStroke</code>.
	 * @see Graphics2D
	 */
    public float getLineWidth() {
        return width;
    }

    /**
	 * Returns the end cap style.
	 * 
	 * @return the end cap style of this <code>BasicStroke</code> as one of the
	 *         static <code>int</code> values that define possible end cap
	 *         styles.
	 */
    public int getEndCap() {
        return cap;
    }

    /**
	 * Returns the line join style.
	 * 
	 * @return the line join style of the <code>BasicStroke</code> as one of the
	 *         static <code>int</code> values that define possible line join
	 *         styles.
	 */
    public int getLineJoin() {
        return join;
    }

    /**
	 * Returns the limit of miter joins.
	 * 
	 * @return the limit of miter joins of the <code>BasicStroke</code>.
	 */
    public float getMiterLimit() {
        return miterlimit;
    }

    /**
	 * Returns the array representing the lengths of the dash segments.
	 * Alternate entries in the array represent the user space lengths of the
	 * opaque and transparent segments of the dashes. As the pen moves along the
	 * outline of the <code>Shape</code> to be stroked, the user space distance
	 * that the pen travels is accumulated. The distance value is used to index
	 * into the dash array. The pen is opaque when its current cumulative
	 * distance maps to an even element of the dash array and transparent
	 * otherwise.
	 * 
	 * @return the dash array.
	 */
    public float[] getDashArray() {
        if (dash == null) {
            return null;
        }
        return (float[]) dash.clone();
    }

    /**
	 * Returns the current dash phase. The dash phase is a distance specified in
	 * user coordinates that represents an offset into the dashing pattern. In
	 * other words, the dash phase defines the point in the dashing pattern that
	 * will correspond to the beginning of the stroke.
	 * 
	 * @return the dash phase as a <code>float</code> value.
	 */
    public float getDashPhase() {
        return dash_phase;
    }

    /**
	 * Returns the hashcode for this stroke.
	 * 
	 * @return a hash code for this stroke.
	 */
    public int hashCode() {
        int hash = Float.floatToIntBits(width);
        hash = hash * 31 + join;
        hash = hash * 31 + cap;
        hash = hash * 31 + Float.floatToIntBits(miterlimit);
        if (dash != null) {
            hash = hash * 31 + Float.floatToIntBits(dash_phase);
            for (int i = 0; i < dash.length; i++) {
                hash = hash * 31 + Float.floatToIntBits(dash[i]);
            }
        }
        return hash;
    }

    /**
	 * Tests if a specified object is equal to this <code>BasicStroke</code> by
	 * first testing if it is a <code>BasicStroke</code> and then comparing its
	 * width, join, cap, miter limit, dash, and dash phase attributes with those
	 * of this <code>BasicStroke</code>.
	 * 
	 * @param obj
	 *            the specified object to compare to this
	 *            <code>BasicStroke</code>
	 * @return <code>true</code> if the width, join, cap, miter limit, dash, and
	 *         dash phase are the same for both objects; <code>false</code>
	 *         otherwise.
	 */
    public boolean equals(Object obj) {
        if (!(obj instanceof Stroke)) {
            return false;
        }
        Stroke bs = (Stroke) obj;
        if (width != bs.width) {
            return false;
        }
        if (join != bs.join) {
            return false;
        }
        if (cap != bs.cap) {
            return false;
        }
        if (miterlimit != bs.miterlimit) {
            return false;
        }
        if (dash != null) {
            if (dash_phase != bs.dash_phase) {
                return false;
            }
            if (!java.util.Arrays.equals(dash, bs.dash)) {
                return false;
            }
        } else if (bs.dash != null) {
            return false;
        }
        return true;
    }
}
