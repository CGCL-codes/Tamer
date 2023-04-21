package br.furb.inf.tcc.tankcoders.scene.tank.m1abrams;

import br.furb.inf.tcc.tankcoders.scene.tank.suspension.Wheel;
import com.jme.math.Vector3f;
import com.jmex.physics.DynamicPhysicsNode;

/**
 * Wheels of the M1-Abrams tank
 * @author Germano Fronza
 */
public class M1AbramsWheel extends Wheel {

    private static final long serialVersionUID = 1L;

    private static final float WHEEL_MASS = 3f;

    private static final float WHEEL_SCALE = 0.8f;

    private static final float TRACTION_SPEED = 20;

    private static final float TURN_SPEED = 75;

    private static final float TRACTION_ACCELERATION = 230;

    private static final float TURN_ACCELERATION = 500;

    private static final float MAX_TURN = 0.2f;

    public M1AbramsWheel(DynamicPhysicsNode suspensionBase, Vector3f relativePosition, boolean canTurn) {
        super(suspensionBase, relativePosition, canTurn);
    }

    protected float getWheelMass() {
        return WHEEL_MASS;
    }

    protected float getWheelScale() {
        return WHEEL_SCALE;
    }

    protected float getTractionSpeed() {
        return TRACTION_SPEED;
    }

    protected float getTurnSpeed() {
        return TURN_SPEED;
    }

    protected float getTractionAcceleration() {
        return TRACTION_ACCELERATION;
    }

    public float getTurnAcceleration() {
        return TURN_ACCELERATION;
    }

    public float getMaxTurn() {
        return MAX_TURN;
    }
}
