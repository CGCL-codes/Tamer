package org.jbox2d.dynamics.joints;

import org.jbox2d.common.Vec2;

/**
 * This joint is used to add force to an object to accelerate to a position,
 * normally used with the mouse.
 */
public class MouseJointDef extends JointDef {

    /**
	 * The initial world target point. This is assumed
	 * to coincide with the body anchor initially.
	 */
    public Vec2 target;

    /**
	 * The maximum constraint force that can be exerted
	 * to move the candidate body. Usually you will express
	 * as some multiple of the weight (multiplier * mass * gravity).
	 */
    public float maxForce;

    /** The response speed. */
    public float frequencyHz;

    /** The damping ratio. 0 = no damping, 1 = critical damping. */
    public float dampingRatio;

    /** The time step used in the simulation. */
    public float timeStep;

    public MouseJointDef() {
        type = JointType.MOUSE_JOINT;
        target = new Vec2(0.0f, 0.0f);
        maxForce = 0.0f;
        frequencyHz = 5.0f;
        dampingRatio = 0.7f;
        timeStep = 1.0f / 60.0f;
    }
}
