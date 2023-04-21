package com.zerocool.scene;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;

@SuppressWarnings("serial")
public abstract class UpdatableNode extends Node {

    abstract void Update(int elapsed);

    /**
	 * <code>getX</code> returns the x coordinate.
	 * @return x
	 */
    public float getX() {
        return getLocalTranslation().x;
    }

    /**
	 * <code>getY</code> returns the y coordinate.
	 * @return y
	 */
    public float getY() {
        return getLocalTranslation().y;
    }

    /**
	 * <code>getZ</code> returns the z coordinate.
	 * @return z
	 */
    public float getZ() {
        return getLocalTranslation().z;
    }

    /**
	 * <code>setX</code> sets the x coordinate
	 * @param x
	 */
    public void setX(float x) {
        localTranslation.x = x;
    }

    /**
	 * <code>setY</code> sets the y coordinate
	 * @param y
	 */
    public void setY(float y) {
        localTranslation.y = y;
    }

    /**
	 * <code>setZ</code> sets the z coordinate
	 * @param z
	 */
    public void setZ(float z) {
        localTranslation.z = z;
    }
}
