package org.lwjgl.vector;

import java.nio.FloatBuffer;
import org.lwjgl.Math;

/**
 * $Id: Vector.java 355 2002-12-22 19:53:41Z cix_foo $
 *
 * Base class for vectors.
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @version $Revision: 355 $
 */
public abstract class Vector {

    /**
	 * Constructor for Vector.
	 */
    public Vector() {
        super();
    }

    /**
	 * @return the length of the vector
	 */
    public final float length() {
        return Math.sqrt(lengthSquared());
    }

    /**
	 * @return the length squared of the vector
	 */
    public abstract float lengthSquared();

    /**
     * Load this vector from a FloatBuffer
     * @param buf The buffer to load it from, at the current position
     * @return this
     */
    public abstract Vector load(FloatBuffer buf);

    /**
	 * Negate a vector
	 * @return this
	 */
    public abstract Vector negate();

    /**
	 * Normalise this vector
	 * @return this
	 */
    public final Vector normalize() {
        float len = length();
        if (len != 0.0f) {
            float l = 1.0f / len;
            return scale(l);
        } else {
            assert false;
            return this;
        }
    }

    /**
     * Store this vector in a FloatBuffer
     * @param buf The buffer to store it in, at the current position
     * @return this
     */
    public abstract Vector store(FloatBuffer buf);

    /**
     * Scale this vector
     * @param scale The scale factor
     * @return this
     */
    public abstract Vector scale(float scale);
}
