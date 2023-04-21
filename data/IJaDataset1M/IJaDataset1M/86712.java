package org.alcibiade.sculpt.math.transformation;

import org.alcibiade.sculpt.math.Vector;

/**
 * This Viewpoint implementation takes a location and a target as inputs.
 * 
 * @author Yannick Kirschhoffer
 * 
 */
public class BasicViewpoint implements Viewpoint {

    /**
	 * The name of this viewpoint.
	 */
    private String name;

    /**
	 * The location coordinates.
	 */
    private Vector location;

    /**
	 * The target coordinates.
	 */
    private Vector target;

    /**
	 * The viewpoint frame vectors.
	 */
    private Vector u, v, w;

    /**
	 * Create a new Viewpoint.
	 * 
	 * @param name
	 *            The viewpoint name
	 * @param location
	 *            The viewpoint location
	 * @param target
	 *            The viewpoint target
	 */
    public BasicViewpoint(String name, Vector location, Vector target) {
        this.name = name;
        this.location = new Vector(location);
        this.target = new Vector(target);
        updateFrameVectors();
    }

    public String getName() {
        return name;
    }

    /**
	 * Update the viewpoint's name.
	 * 
	 * @param name
	 *            The viewpoint name
	 */
    public void setName(String name) {
        this.name = name;
    }

    public Vector getLocation() {
        return location;
    }

    /**
	 * Update the viewpoint's origin.
	 * 
	 * @param location
	 *            The viewpoint location
	 */
    public void setLocation(Vector location) {
        this.location = location;
        updateFrameVectors();
    }

    public Vector getTarget() {
        return target;
    }

    /**
	 * Update the viewpoint's target.
	 * 
	 * @param target
	 *            The viewpoint target
	 */
    public void setTarget(Vector target) {
        this.target = target;
        updateFrameVectors();
    }

    public Vector getFront() {
        return v;
    }

    public Vector getRight() {
        return u;
    }

    public Vector getUp() {
        return w;
    }

    @Override
    public String toString() {
        return "Viewpoint:" + location + "->" + target;
    }

    /**
	 * This method will update the u,v,w vectors every time that the viewpoint
	 * is updated.
	 */
    private void updateFrameVectors() {
        v = new Vector(location, target);
        v.normalize();
        u = new Vector(v);
        u.crossProduct(Vector.Z);
        if (u.length() == 0) {
            u = new Vector(Vector.X);
        }
        u.normalize();
        w = new Vector(u);
        w.crossProduct(v);
    }

    public boolean isColinearFrame() {
        int matches = 0;
        matches += (v.getX() == 0) ? 1 : 0;
        matches += (v.getY() == 0) ? 1 : 0;
        matches += (v.getZ() == 0) ? 1 : 0;
        return matches >= 2;
    }
}
