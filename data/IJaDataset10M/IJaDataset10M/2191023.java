package org.jbox2d.dynamics.joints;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 * Definition for a distance joint.  A distance joint
 * keeps two points on two bodies at a constant distance
 * from each other.
 */
public class DistanceJointDef extends JointDef {

    /** The local anchor point relative to body1's origin. */
    public Vec2 localAnchor1;

    /** The local anchor point relative to body2's origin. */
    public Vec2 localAnchor2;

    /** The equilibrium length between the anchor points. */
    public float length;

    public float frequencyHz;

    public float dampingRatio;

    public DistanceJointDef() {
        type = JointType.DISTANCE_JOINT;
        localAnchor1 = new Vec2(0.0f, 0.0f);
        localAnchor2 = new Vec2(0.0f, 0.0f);
        length = 1.0f;
        frequencyHz = 0.0f;
        dampingRatio = 0.0f;
    }

    /**
	 * Initialize the bodies, anchors, and length using the world
	 * anchors.
	 * @param b1 First body
	 * @param b2 Second body
	 * @param anchor1 World anchor on first body
	 * @param anchor2 World anchor on second body
	 */
    public void initialize(final Body b1, final Body b2, final Vec2 anchor1, final Vec2 anchor2) {
        body1 = b1;
        body2 = b2;
        localAnchor1.set(body1.getLocalPoint(anchor1));
        localAnchor2.set(body2.getLocalPoint(anchor2));
        final Vec2 d = anchor2.sub(anchor1);
        length = d.length();
    }
}
