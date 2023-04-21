package com.bulletphysics.collision.narrowphase;

import javax.vecmath.Vector3f;

/**
 *
 * @author jezek2
 */
public class PointCollector implements DiscreteCollisionDetectorInterface.Result {

    public final Vector3f normalOnBInWorld = new Vector3f();

    public final Vector3f pointInWorld = new Vector3f();

    public float distance = 1e30f;

    public boolean hasResult = false;

    public void setShapeIdentifiers(int partId0, int index0, int partId1, int index1) {
    }

    public void addContactPoint(Vector3f normalOnBInWorld, Vector3f pointInWorld, float depth) {
        if (depth < distance) {
            hasResult = true;
            this.normalOnBInWorld.set(normalOnBInWorld);
            this.pointInWorld.set(pointInWorld);
            distance = depth;
        }
    }
}
