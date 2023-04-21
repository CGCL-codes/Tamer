package org.nist.worldgen.t3d;

import org.nist.worldgen.*;
import java.io.*;

/**
 * Represents a collision cylinder used by path nodes and player starts.
 *
 * @author Stephen Carlson (NIST)
 * @version 4.0
 */
public class CylinderComponent extends PrimitiveComponent {

    protected double height;

    protected double radius;

    /**
	 * Creates a new cylinder component.
	 *
	 * @param name the component name
	 * @param staticName the object's inherited name
	 */
    public CylinderComponent(final String name, final String staticName) {
        super(name, staticName);
        height = 50.0;
        radius = 50.0;
    }

    public UTObject copyOf() {
        final CylinderComponent cc = new CylinderComponent(getName(), getStaticName());
        cc.setHeight(getHeight());
        cc.setRadius(getRadius());
        return copyCustom(cc);
    }

    protected void custom(final PrintWriter out, final int indent, int utCompatMode) {
        UTUtils.putAttribute(out, indent, "CollisionHeight", String.format("%f", getHeight()));
        UTUtils.putAttribute(out, indent, "CollisionRadius", String.format("%f", getRadius()));
    }

    protected boolean customPutAttribute(String key, String value) {
        boolean accept = super.customPutAttribute(key, value);
        if (!accept) {
            if (key.equalsIgnoreCase("CollisionHeight")) {
                setHeight(Double.parseDouble(value));
                accept = true;
            } else if (key.equalsIgnoreCase("CollisionRadius")) {
                setRadius(Double.parseDouble(value));
                accept = true;
            }
        }
        return accept;
    }

    /**
	 * Gets the cylinder's height.
	 *
	 * @return the cylinder's height in UU
	 */
    public double getHeight() {
        return height;
    }

    /**
	 * Gets the cylinder's radius.
	 *
	 * @return the cylinder's radius in UU
	 */
    public double getRadius() {
        return radius;
    }

    /**
	 * Changes the height of the cylinder.
	 *
	 * @param height the new cylinder height
	 */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
	 * Changes the radius of the cylinder.
	 *
	 * @param radius the new cylinder radius
	 */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Dimension3D getSize() {
        return new Dimension3D(2 * radius, 2 * radius, 2 * height);
    }

    public String toString() {
        return toString("name", getName(), "parent", getParent().getName(), "radius=%.2f", getRadius(), "height=%.2f", getHeight());
    }
}
