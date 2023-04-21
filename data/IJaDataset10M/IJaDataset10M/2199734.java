package com.ryanm.util.geom;

import org.lwjgl.util.vector.Vector3f;
import com.ryanm.util.math.Range;

/**
 * Axis aligned cuboid
 * 
 * @author ryanm
 */
public class BoundingCuboid extends BoundingRectangle {

    /**
	 * Extent on the z-axis
	 */
    public final Range z = new Range(0, 0);

    /**
	 * @param x
	 * @param y
	 * @param z
	 * @param width
	 * @param height
	 * @param depth
	 */
    public BoundingCuboid(float x, float y, float z, float width, float height, float depth) {
        super(x, y, width, height);
        this.z.set(z, z + depth);
    }

    /**
	 * Copy constructor
	 * 
	 * @param c
	 */
    public BoundingCuboid(BoundingCuboid c) {
        super(c);
        z.set(c.z);
    }

    /**
	 * Alters this {@link BoundingRectangle} as necessary to contain
	 * the specified point
	 * 
	 * @param px
	 * @param py
	 * @param pz
	 */
    public void encompass(float px, float py, float pz) {
        super.encompass(px, py);
        z.encompass(pz);
    }

    /**
	 * Alters this {@link BoundingRectangle} to entirely encompass
	 * another
	 * 
	 * @param c
	 */
    public void encompass(BoundingCuboid c) {
        super.encompass(c);
        z.encompass(c.z);
    }

    /**
	 * Determines if this {@link BoundingRectangle} contains the
	 * supplied point
	 * 
	 * @param px
	 * @param py
	 * @param pz
	 * @return <code>true</code> if the point lies within this
	 *         {@link BoundingRectangle}'s volume, <code>false</code>
	 *         otherwise
	 */
    public boolean contains(float px, float py, float pz) {
        return super.contains(px, py) && z.contains(pz);
    }

    /**
	 * @param b
	 * @return <code>true</code> if this and b intersect
	 */
    public boolean intersects(BoundingCuboid b) {
        return super.intersects(b) && z.intersects(b.z);
    }

    /**
	 * Shifts this cuboid
	 * 
	 * @param dx
	 * @param dy
	 * @param dz
	 */
    public void translate(float dx, float dy, float dz) {
        super.translate(dx, dy);
        z.translate(dz);
    }

    /**
	 * Scales this cuboid around the origin
	 * 
	 * @param sx
	 * @param sy
	 * @param sz
	 */
    public void scale(float sx, float sy, float sz) {
        super.scale(sx, sy);
        z.scale(sz);
    }

    /**
	 * @return The center of the cuboid
	 */
    public Vector3f getCenter() {
        return new Vector3f(x.toValue(0.5f), y.toValue(0.5f), z.toValue(0.5f));
    }

    /**
	 * Computes the time period for which two cuboids intersect, given
	 * their velocities
	 * 
	 * @param a
	 * @param avx
	 * @param avy
	 * @param avz
	 * @param b
	 * @param bvx
	 * @param bvy
	 * @param bvz
	 * @return the intersection period, or <code>null</code> if there
	 *         is no intersection
	 */
    public static Range intersectionTime(BoundingCuboid a, float avx, float avy, float avz, BoundingCuboid b, float bvx, float bvy, float bvz) {
        Range twoDOverlap = BoundingRectangle.intersectionTime(a, avx, avy, b, bvx, bvy);
        bvz -= avz;
        Range zOverlap = Range.intersectionTime(a.z, b.z, bvz);
        if (twoDOverlap != null && zOverlap != null) {
            return twoDOverlap.intersection(zOverlap);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder("( ");
        buff.append(x.getMin());
        buff.append(", ");
        buff.append(y.getMin());
        buff.append(", ");
        buff.append(z.getMin());
        buff.append(" ) [ ");
        buff.append(x.getSpan());
        buff.append(" x ");
        buff.append(y.getSpan());
        buff.append(" x ");
        buff.append(z.getSpan());
        buff.append(" ]");
        return buff.toString();
    }
}
