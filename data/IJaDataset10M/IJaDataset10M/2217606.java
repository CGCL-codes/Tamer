package org.dyn4j.dynamics.joint;

import org.dyn4j.Epsilon;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.Step;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Interval;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Matrix22;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.resources.Messages;

/**
 * Represents a motor joint.
 * <p>
 * A motor joint uses motors to move two bodies relative to one another.
 * <p>
 * This joint is ideal for character movement as it allows direct control of the motion
 * using targets, but yet still allows interaction with the environment.  Make one
 * body static to achieve this effect.
 * <p>
 * NOTE: The linear and angular targets are relative to body1.
 * Nearly identical to <a href="http://www.box2d.org">Box2d</a>'s equivalent class.
 * @see <a href="http://www.box2d.org">Box2d</a>
 * @author William Bittle
 * @version 3.1.0
 * @since 3.1.0
 */
public class MotorJoint extends Joint {

    /** The linear target distance from body1's world space center */
    protected Vector2 linearTarget;

    /** The target angle between the two body's angles */
    protected double angularTarget;

    /** The correction factor in the range [0, 1] */
    protected double correctionFactor;

    /** The maximum force the constraint can apply */
    protected double maximumForce;

    /** The maximum torque the constraint can apply */
    protected double maximumTorque;

    /** The pivot mass; K = J * Minv * Jtrans */
    protected Matrix22 K;

    /** The mass for the angular constraint */
    protected double angularMass;

    /** The calculated linear error in the target distance */
    protected Vector2 linearError;

    /** The calculated angular error in the target angle */
    protected double angularError;

    /** The impulse applied to reduce linear motion */
    protected Vector2 linearImpulse;

    /** The impulse applied to reduce angular motion */
    protected double angularImpulse;

    /**
	 * Minimal constructor.
	 * @param body1 the first {@link Body}
	 * @param body2 the second {@link Body}
	 * @throws NullPointerException if body1 or body2
	 * @throws IllegalArgumentException if body1 == body2
	 */
    public MotorJoint(Body body1, Body body2) {
        super(body1, body2, false);
        if (body1 == body2) throw new IllegalArgumentException(Messages.getString("dynamics.joint.sameBody"));
        this.linearTarget = body1.getLocalPoint(body2.getWorldCenter());
        this.angularTarget = body2.getTransform().getRotation() - body1.getTransform().getRotation();
        this.correctionFactor = 0.3;
        this.K = new Matrix22();
        this.linearImpulse = new Vector2();
        this.angularImpulse = 0.0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MotorJoint[").append(super.toString()).append("|LinearTarget=").append(this.linearTarget).append("|AngularTarget=").append(this.angularTarget).append("|CorrectionFactor=").append(this.correctionFactor).append("|MaximumForce=").append(this.maximumForce).append("|MaximumTorque=").append(this.maximumTorque).append("]");
        return sb.toString();
    }

    @Override
    public void initializeConstraints() {
        Step step = this.world.getStep();
        Transform t1 = this.body1.getTransform();
        Transform t2 = this.body2.getTransform();
        Mass m1 = this.body1.getMass();
        Mass m2 = this.body2.getMass();
        double invM1 = m1.getInverseMass();
        double invM2 = m2.getInverseMass();
        double invI1 = m1.getInverseInertia();
        double invI2 = m2.getInverseInertia();
        Vector2 r1 = t1.getTransformedR(this.body1.getLocalCenter().getNegative());
        Vector2 r2 = t2.getTransformedR(this.body2.getLocalCenter().getNegative());
        this.K.m00 = invM1 + invM2 + r1.y * r1.y * invI1 + r2.y * r2.y * invI2;
        this.K.m01 = -invI1 * r1.x * r1.y - invI2 * r2.x * r2.y;
        this.K.m10 = this.K.m01;
        this.K.m11 = invM1 + invM2 + r1.x * r1.x * invI1 + r2.x * r2.x * invI2;
        this.K.invert();
        this.angularMass = invI1 + invI2;
        if (this.angularMass > Epsilon.E) {
            this.angularMass = 1.0 / this.angularMass;
        }
        Vector2 d1 = r1.sum(this.body1.getWorldCenter());
        Vector2 d2 = r2.sum(this.body2.getWorldCenter());
        Vector2 d0 = t1.getTransformedR(this.linearTarget);
        this.linearError = d2.subtract(d1).subtract(d0);
        this.angularError = this.getAngularError();
        this.linearImpulse.multiply(step.getDeltaTimeRatio());
        this.angularImpulse *= step.getDeltaTimeRatio();
        this.body1.getVelocity().subtract(this.linearImpulse.product(invM1));
        this.body1.setAngularVelocity(this.body1.getAngularVelocity() - invI1 * (r1.cross(this.linearImpulse) + this.angularImpulse));
        this.body2.getVelocity().add(this.linearImpulse.product(invM2));
        this.body2.setAngularVelocity(this.body2.getAngularVelocity() + invI2 * (r2.cross(this.linearImpulse) + this.angularImpulse));
    }

    @Override
    public void solveVelocityConstraints() {
        Step step = this.world.getStep();
        double dt = step.getDeltaTime();
        double invdt = step.getInverseDeltaTime();
        Transform t1 = this.body1.getTransform();
        Transform t2 = this.body2.getTransform();
        Mass m1 = this.body1.getMass();
        Mass m2 = this.body2.getMass();
        double invM1 = m1.getInverseMass();
        double invM2 = m2.getInverseMass();
        double invI1 = m1.getInverseInertia();
        double invI2 = m2.getInverseInertia();
        {
            double C = this.body2.getAngularVelocity() - this.body1.getAngularVelocity() + invdt * this.correctionFactor * this.angularError;
            double impulse = this.angularMass * -C;
            double oldImpulse = this.angularImpulse;
            double maxImpulse = this.maximumTorque * dt;
            this.angularImpulse = Interval.clamp(this.angularImpulse + impulse, -maxImpulse, maxImpulse);
            impulse = this.angularImpulse - oldImpulse;
            this.body1.setAngularVelocity(this.body1.getAngularVelocity() - invI1 * impulse);
            this.body2.setAngularVelocity(this.body2.getAngularVelocity() + invI2 * impulse);
        }
        Vector2 r1 = t1.getTransformedR(this.body1.getLocalCenter().getNegative());
        Vector2 r2 = t2.getTransformedR(this.body2.getLocalCenter().getNegative());
        Vector2 v1 = this.body1.getVelocity().sum(r1.cross(this.body1.getAngularVelocity()));
        Vector2 v2 = this.body2.getVelocity().sum(r2.cross(this.body2.getAngularVelocity()));
        Vector2 pivotV = v2.subtract(v1);
        pivotV.add(this.linearError.product(this.correctionFactor * invdt));
        Vector2 impulse = this.K.multiply(pivotV);
        impulse.negate();
        Vector2 oldImpulse = this.linearImpulse.copy();
        this.linearImpulse.add(impulse);
        double maxImpulse = this.maximumForce * dt;
        if (this.linearImpulse.getMagnitudeSquared() > maxImpulse * maxImpulse) {
            this.linearImpulse.normalize();
            this.linearImpulse.multiply(maxImpulse);
        }
        impulse = this.linearImpulse.difference(oldImpulse);
        this.body1.getVelocity().subtract(impulse.product(invM1));
        this.body1.setAngularVelocity(this.body1.getAngularVelocity() - invI1 * r1.cross(impulse));
        this.body2.getVelocity().add(impulse.product(invM2));
        this.body2.setAngularVelocity(this.body2.getAngularVelocity() + invI2 * r2.cross(impulse));
    }

    @Override
    public boolean solvePositionConstraints() {
        return true;
    }

    /**
	 * Returns error in the angle between the joined bodies given the target angle.
	 * @return double
	 */
    private double getAngularError() {
        double rr = this.body2.getTransform().getRotation() - this.body1.getTransform().getRotation() - this.angularTarget;
        if (rr < -Math.PI) rr += Geometry.TWO_PI;
        if (rr > Math.PI) rr -= Geometry.TWO_PI;
        return rr;
    }

    @Override
    public Vector2 getAnchor1() {
        return this.body1.getWorldCenter();
    }

    @Override
    public Vector2 getAnchor2() {
        return this.body2.getWorldCenter();
    }

    @Override
    public Vector2 getReactionForce(double invdt) {
        return this.linearImpulse.product(invdt);
    }

    @Override
    public double getReactionTorque(double invdt) {
        return this.angularImpulse * invdt;
    }

    @Override
    protected void shiftCoordinates(Vector2 shift) {
    }

    /**
	 * Returns the desired linear distance along the x and y coordinates from body1's world center.
	 * @return {@link Vector2}
	 */
    public Vector2 getLinearTarget() {
        return this.linearTarget;
    }

    /**
	 * Sets the desired linear distance along the x and y coordinates from body1's world center.
	 * @param target the desired distance along the x and y coordinates
	 */
    public void setLinearTarget(Vector2 target) {
        if (!target.equals(this.linearTarget)) {
            this.body1.setAsleep(false);
            this.body2.setAsleep(false);
            this.linearTarget = target;
        }
    }

    /**
	 * Returns the desired angle between the bodies.
	 * @return double
	 */
    public double getAngularTarget() {
        return this.angularTarget;
    }

    /**
	 * Sets the desired angle between the bodies.
	 * @param target the desired angle between the bodies
	 */
    public void setAngularTarget(double target) {
        if (target != this.angularTarget) {
            this.body1.setAsleep(false);
            this.body2.setAsleep(false);
            this.angularTarget = target;
        }
    }

    /**
	 * Returns the correction factor.
	 * @return double
	 */
    public double getCorrectionFactor() {
        return this.correctionFactor;
    }

    /**
	 * Sets the correction factor.
	 * <p>
	 * The correction factor controls the rate at which the bodies perform the desired actions.
	 * <p>
	 * A value of zero means that the bodies do not perform any action.
	 * @param correctionFactor the correction factor in the range [0, 1]
	 */
    public void setCorrectionFactor(double correctionFactor) {
        if (correctionFactor < 0.0 || correctionFactor > 1.0) throw new IllegalArgumentException(Messages.getString("dynamics.joint.motor.invalidCorrectionFactor"));
        this.correctionFactor = correctionFactor;
    }

    /**
	 * Returns the maximum torque this constraint will apply in newton-meters.
	 * @return double
	 */
    public double getMaximumTorque() {
        return this.maximumTorque;
    }

    /**
	 * Sets the maximum torque this constraint will apply in newton-meters.
	 * @param maximumTorque the maximum torque in newton-meters; in the range [0, &infin;]
	 * @throws IllegalArgumentException if maxTorque is less than zero
	 */
    public void setMaximumTorque(double maximumTorque) {
        if (maximumTorque < 0.0) throw new IllegalArgumentException(Messages.getString("dynamics.joint.friction.invalidMaximumTorque"));
        this.maximumTorque = maximumTorque;
    }

    /**
	 * Returns the maximum force this constraint will apply in newtons.
	 * @return double
	 */
    public double getMaximumForce() {
        return this.maximumForce;
    }

    /**
	 * Sets the maximum force this constraint will apply in newtons.
	 * @param maximumForce the maximum force in newtons; in the range [0, &infin;]
	 * @throws IllegalArgumentException if maxForce is less than zero
	 */
    public void setMaximumForce(double maximumForce) {
        if (maximumForce < 0.0) throw new IllegalArgumentException(Messages.getString("dynamics.joint.friction.invalidMaximumForce"));
        this.maximumForce = maximumForce;
    }
}
