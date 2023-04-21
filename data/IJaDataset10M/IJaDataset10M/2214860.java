package com.bulletphysics.collision.narrowphase;

import com.bulletphysics.linearmath.IDebugDraw;
import com.bulletphysics.linearmath.Transform;
import javax.vecmath.Vector3f;

/**
 * ConvexCast is an interface for casting.
 * 
 * @author jezek2
 */
public interface ConvexCast {

    /**
	 * Cast a convex against another convex object.
	 */
    public boolean calcTimeOfImpact(Transform fromA, Transform toA, Transform fromB, Transform toB, CastResult result);

    /**
	 * RayResult stores the closest result. Alternatively, add a callback method
	 * to decide about closest/all results.
	 */
    public static class CastResult {

        public final Transform hitTransformA = new Transform();

        public final Transform hitTransformB = new Transform();

        public final Vector3f normal = new Vector3f();

        public final Vector3f hitPoint = new Vector3f();

        public float fraction = 1e30f;

        public float allowedPenetration = 0f;

        public IDebugDraw debugDrawer;

        public void debugDraw(float fraction) {
        }

        public void drawCoordSystem(Transform trans) {
        }
    }
}
