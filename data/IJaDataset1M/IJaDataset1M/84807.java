package org.dyn4j.dynamics.joint;

import org.dyn4j.Epsilon;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.Step;
import org.dyn4j.geometry.Interval;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Matrix22;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.resources.Messages;

/**
 * Represents a line joint.
 * <p>
 * A line joint constrains motion between two {@link Body}s to a line.
 * <p>
 * This differs from the {@link PrismaticJoint} since it allows the {@link Body}s
 * to rotate freely about the anchor point.
 * <p>
 * Nearly identical to <a href="http://www.box2d.org">Box2d</a>'s equivalent class.
 * @see <a href="http://www.box2d.org">Box2d</a>
 * @author William Bittle
 * @version 3.1.0
 * @since 1.0.0
 * @deprecated As of version 3.0.0 replaced with {@link WheelJoint}
 */
@Deprecated
public class LineJoint extends Joint {

    /** The local anchor point on the first {@link Body} */
    protected Vector2 localAnchor1;

    /** The local anchor point on the second {@link Body} */
    protected Vector2 localAnchor2;

    /** Whether the motor is enabled or not */
    protected boolean motorEnabled;

    /** The target velocity in meters / second */
    protected double motorSpeed;

    /** The maximum force the motor can apply in newtons */
    protected double maximumMotorForce;

    /** Whether the limit is enabled or not */
    protected boolean limitEnabled;

    /** The upper limit in meters */
    protected double upperLimit;

    /** The lower limit in meters */
    protected double lowerLimit;

    /** The constraint mass; K = J * Minv * Jtrans */
    protected Matrix22 K;

    /** The mass of the motor */
    protected double motorMass;

    /** The current state of the limit */
    protected Joint.LimitState limitState;

    /** The accumulated impulse for warm starting */
    protected Vector2 impulse;

    /** The impulse applied by the motor */
    protected double motorImpulse;

    /** The axis representing the allowed line of motion */
    protected Vector2 xAxis;

    /** The perpendicular axis of the line of motion */
    protected Vector2 yAxis;

    /** The world space yAxis  */
    protected Vector2 perp;

    /** The world space xAxis */
    protected Vector2 axis;

    /** s1 = (r1 + d).cross(perp) */
    protected double s1;

    /** s2 = r2.cross(perp) */
    protected double s2;

    /** a1 = (r1 + d).cross(axis) */
    protected double a1;

    /** a2 = r2.cross(axis) */
    protected double a2;

    /**
	 * Minimal constructor.
	 * @param body1 the first {@link Body}
	 * @param body2 the second {@link Body}
	 * @param anchor the anchor point in world coordinates
	 * @param axis the axis of allowed motion
	 * @throws NullPointerException if body1, body2, anchor, or axis is null
	 * @throws IllegalArgumentException if body1 == body2
	 */
    public LineJoint(Body body1, Body body2, Vector2 anchor, Vector2 axis) {
        super(body1, body2, false);
        if (body1 == body2) throw new IllegalArgumentException(Messages.getString("dynamics.joint.sameBody"));
        if (anchor == null) throw new NullPointerException(Messages.getString("dynamics.joint.nullAnchor"));
        if (axis == null) throw new NullPointerException(Messages.getString("dynamics.joint.nullAxis"));
        this.localAnchor1 = body1.getLocalPoint(anchor);
        this.localAnchor2 = body2.getLocalPoint(anchor);
        Vector2 n = axis.getNormalized();
        this.xAxis = body2.getLocalVector(n);
        this.yAxis = this.xAxis.cross(1.0);
        this.K = new Matrix22();
        this.impulse = new Vector2();
        this.limitEnabled = false;
        this.motorEnabled = false;
        this.limitState = Joint.LimitState.INACTIVE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LineJoint[").append(super.toString()).append("|LocalAnchor1=").append(this.localAnchor1).append("|LocalAnchor2=").append(this.localAnchor2).append("|WorldAnchor=").append(this.getAnchor1()).append("|XAxis=").append(this.xAxis).append("|YAxis=").append(this.yAxis).append("|Axis=").append(this.getAxis()).append("|IsMotorEnabled=").append(this.motorEnabled).append("|MotorSpeed=").append(this.motorSpeed).append("|MaximumMotorForce=").append(this.maximumMotorForce).append("|IsLimitEnabled=").append(this.limitEnabled).append("|LowerLimit=").append(this.lowerLimit).append("|UpperLimit=").append(this.upperLimit).append("]");
        return sb.toString();
    }

    @Override
    public void initializeConstraints() {
        Step step = this.world.getStep();
        Settings settings = this.world.getSettings();
        double linearTolerance = settings.getLinearTolerance();
        Transform t1 = this.body1.getTransform();
        Transform t2 = this.body2.getTransform();
        Mass m1 = this.body1.getMass();
        Mass m2 = this.body2.getMass();
        double invM1 = m1.getInverseMass();
        double invM2 = m2.getInverseMass();
        double invI1 = m1.getInverseInertia();
        double invI2 = m2.getInverseInertia();
        Vector2 r1 = t1.getTransformedR(this.body1.getLocalCenter().to(this.localAnchor1));
        Vector2 r2 = t2.getTransformedR(this.body2.getLocalCenter().to(this.localAnchor2));
        Vector2 d = this.body1.getWorldCenter().sum(r1).subtract(this.body2.getWorldCenter().sum(r2));
        this.axis = this.body2.getWorldVector(this.xAxis);
        this.perp = this.body2.getWorldVector(this.yAxis);
        this.s1 = r1.cross(this.perp);
        this.s2 = r2.sum(d).cross(this.perp);
        this.a1 = r1.cross(this.axis);
        this.a2 = r2.sum(d).cross(this.axis);
        this.K.m00 = invM1 + invM2 + this.s1 * this.s1 * invI1 + this.s2 * this.s2 * invI2;
        this.K.m01 = this.s1 * this.a1 * invI1 + this.s2 * this.a2 * invI2;
        this.K.m10 = this.K.m01;
        this.K.m11 = invM1 + invM2 + this.a1 * this.a1 * invI1 + this.a2 * this.a2 * invI2;
        this.motorMass = this.K.m11;
        if (Math.abs(this.motorMass) > Epsilon.E) {
            this.motorMass = 1.0 / this.motorMass;
        }
        if (!this.motorEnabled) {
            this.motorImpulse = 0.0;
        }
        if (this.limitEnabled) {
            double dist = this.axis.dot(d);
            if (Math.abs(this.upperLimit - this.lowerLimit) < 2.0 * linearTolerance) {
                this.limitState = Joint.LimitState.EQUAL;
            } else if (dist <= this.lowerLimit) {
                if (this.limitState != Joint.LimitState.AT_LOWER) {
                    this.limitState = Joint.LimitState.AT_LOWER;
                    this.impulse.y = 0.0;
                }
            } else if (dist >= this.upperLimit) {
                if (this.limitState != Joint.LimitState.AT_UPPER) {
                    this.limitState = Joint.LimitState.AT_UPPER;
                    this.impulse.y = 0.0;
                }
            } else {
                this.limitState = Joint.LimitState.INACTIVE;
                this.impulse.y = 0.0;
            }
        } else {
            this.limitState = Joint.LimitState.INACTIVE;
            this.impulse.y = 0.0;
        }
        this.impulse.multiply(step.getDeltaTimeRatio());
        this.motorImpulse *= step.getDeltaTimeRatio();
        Vector2 P = new Vector2();
        P.x = this.perp.x * this.impulse.x + (this.motorImpulse + this.impulse.y) * this.axis.x;
        P.y = this.perp.y * this.impulse.x + (this.motorImpulse + this.impulse.y) * this.axis.y;
        double l1 = this.impulse.x * this.s1 + (this.motorImpulse + this.impulse.y) * this.a1;
        double l2 = this.impulse.x * this.s2 + (this.motorImpulse + this.impulse.y) * this.a2;
        this.body1.getVelocity().add(P.product(invM1));
        this.body1.setAngularVelocity(this.body1.getAngularVelocity() + invI1 * l1);
        this.body2.getVelocity().subtract(P.product(invM2));
        this.body2.setAngularVelocity(this.body2.getAngularVelocity() - invI2 * l2);
    }

    @Override
    public void solveVelocityConstraints() {
        Step step = this.world.getStep();
        Mass m1 = this.body1.getMass();
        Mass m2 = this.body2.getMass();
        double invM1 = m1.getInverseMass();
        double invM2 = m2.getInverseMass();
        double invI1 = m1.getInverseInertia();
        double invI2 = m2.getInverseInertia();
        Vector2 v1 = this.body1.getVelocity();
        Vector2 v2 = this.body2.getVelocity();
        double w1 = this.body1.getAngularVelocity();
        double w2 = this.body2.getAngularVelocity();
        if (this.motorEnabled && this.limitState != Joint.LimitState.EQUAL) {
            double Cdt = this.axis.dot(v1.difference(v2)) + this.a1 * w1 - this.a2 * w2;
            double impulse = this.motorMass * (this.motorSpeed - Cdt);
            double oldImpulse = this.motorImpulse;
            double maxImpulse = this.maximumMotorForce * step.getDeltaTime();
            this.motorImpulse = Interval.clamp(this.motorImpulse + impulse, -maxImpulse, maxImpulse);
            impulse = this.motorImpulse - oldImpulse;
            Vector2 P = this.axis.product(impulse);
            double l1 = impulse * this.a1;
            double l2 = impulse * this.a2;
            v1.add(P.product(invM1));
            w1 += l1 * invI1;
            v2.subtract(P.product(invM2));
            w2 -= l2 * invI2;
        }
        double Cdt = this.perp.dot(v1.difference(v2)) + this.s1 * w1 - this.s2 * w2;
        if (this.limitEnabled && this.limitState != Joint.LimitState.INACTIVE) {
            double Cdtl = this.axis.dot(v1.difference(v2)) + this.a1 * w1 - this.a2 * w2;
            Vector2 b = new Vector2(Cdt, Cdtl);
            Vector2 impulse = this.K.solve(b.negate());
            Vector2 f1 = this.impulse.copy();
            this.impulse.add(impulse);
            if (this.limitState == Joint.LimitState.AT_LOWER) {
                this.impulse.y = Math.max(this.impulse.y, 0.0);
            } else if (this.limitState == Joint.LimitState.AT_UPPER) {
                this.impulse.y = Math.min(this.impulse.y, 0.0);
            }
            double f2_1 = -Cdt - (this.impulse.y - f1.y) * this.K.m01;
            double f2r;
            if (Math.abs(this.K.m00) > Epsilon.E) {
                f2r = f2_1 / this.K.m00 + f1.x;
            } else {
                f2r = f1.x;
            }
            this.impulse.x = f2r;
            impulse = this.impulse.difference(f1);
            Vector2 P = new Vector2();
            P.x = this.perp.x * impulse.x + impulse.y * this.axis.x;
            P.y = this.perp.y * impulse.x + impulse.y * this.axis.y;
            double l1 = impulse.x * this.s1 + impulse.y * this.a1;
            double l2 = impulse.x * this.s2 + impulse.y * this.a2;
            v1.add(P.product(invM1));
            w1 += l1 * invI1;
            v2.subtract(P.product(invM2));
            w2 -= l2 * invI2;
        } else {
            double f2r;
            if (Math.abs(this.K.m00) > Epsilon.E) {
                f2r = -Cdt / this.K.m00;
            } else {
                f2r = 0.0;
            }
            this.impulse.x += f2r;
            Vector2 P = this.perp.product(f2r);
            double l1 = f2r * this.s1;
            double l2 = f2r * this.s2;
            v1.add(P.product(invM1));
            w1 += l1 * invI1;
            v2.subtract(P.product(invM2));
            w2 -= l2 * invI2;
        }
        this.body1.setAngularVelocity(w1);
        this.body2.setAngularVelocity(w2);
    }

    @Override
    public boolean solvePositionConstraints() {
        Settings settings = this.world.getSettings();
        double maxLinearCorrection = settings.getMaximumLinearCorrection();
        double linearTolerance = settings.getLinearTolerance();
        Transform t1 = this.body1.getTransform();
        Transform t2 = this.body2.getTransform();
        Mass m1 = this.body1.getMass();
        Mass m2 = this.body2.getMass();
        double invM1 = m1.getInverseMass();
        double invM2 = m2.getInverseMass();
        double invI1 = m1.getInverseInertia();
        double invI2 = m2.getInverseInertia();
        Vector2 c1 = this.body1.getWorldCenter();
        Vector2 c2 = this.body2.getWorldCenter();
        Vector2 r1 = t1.getTransformedR(this.body1.getLocalCenter().to(this.localAnchor1));
        Vector2 r2 = t2.getTransformedR(this.body2.getLocalCenter().to(this.localAnchor2));
        Vector2 d = c1.sum(r1).subtract(c2.sum(r2));
        this.axis = this.body2.getWorldVector(this.xAxis);
        this.perp = this.body2.getWorldVector(this.yAxis);
        double Cx = this.perp.dot(d);
        double Cy = 0.0;
        double linearError = 0.0;
        boolean limitActive = false;
        if (this.limitEnabled) {
            this.a1 = r1.cross(axis);
            this.a2 = r2.sum(d).cross(axis);
            double dist = axis.dot(d);
            if (Math.abs(this.upperLimit - this.lowerLimit) < 2.0 * linearTolerance) {
                Cy = Interval.clamp(dist, -maxLinearCorrection, maxLinearCorrection);
                linearError = Math.abs(dist);
                limitActive = true;
            } else if (dist <= this.lowerLimit) {
                Cy = Interval.clamp(dist - this.lowerLimit + linearTolerance, -maxLinearCorrection, 0.0);
                linearError = this.lowerLimit - dist;
                limitActive = true;
            } else if (dist >= this.upperLimit) {
                Cy = Interval.clamp(dist - this.upperLimit - linearTolerance, 0.0, maxLinearCorrection);
                linearError = dist - this.upperLimit;
                limitActive = true;
            }
        }
        this.s1 = r1.cross(this.perp);
        this.s2 = r2.sum(d).cross(this.perp);
        linearError = Math.max(linearError, Math.abs(Cx));
        Vector2 impulse;
        if (limitActive) {
            this.K.m00 = invM1 + invM2 + this.s1 * this.s1 * invI1 + this.s2 * this.s2 * invI2;
            this.K.m01 = this.s1 * this.a1 * invI1 + this.s2 * this.a2 * invI2;
            this.K.m10 = this.K.m01;
            this.K.m11 = invM1 + invM2 + this.a1 * this.a1 * invI1 + this.a2 * this.a2 * invI2;
            Vector2 C = new Vector2(Cx, Cy);
            impulse = this.K.solve(C.negate());
        } else {
            double mass = invM1 + invM2 + this.s1 * this.s1 * invI1 + this.s2 * this.s2 * invI2;
            if (Math.abs(mass) > Epsilon.E) {
                mass = 1.0 / mass;
            }
            double impulsex = -mass * Cx;
            impulse = new Vector2(impulsex, 0.0);
        }
        Vector2 P = new Vector2();
        P.x = this.perp.x * impulse.x + impulse.y * this.axis.x;
        P.y = this.perp.y * impulse.x + impulse.y * this.axis.y;
        double l1 = impulse.x * this.s1 + impulse.y * this.a1;
        double l2 = impulse.x * this.s2 + impulse.y * this.a2;
        this.body1.translate(P.product(invM1));
        this.body1.rotateAboutCenter(l1 * invI1);
        this.body2.translate(P.product(-invM2));
        this.body2.rotateAboutCenter(-l2 * invI2);
        return linearError <= linearTolerance;
    }

    @Override
    public Vector2 getAnchor1() {
        return this.body1.getWorldPoint(this.localAnchor1);
    }

    @Override
    public Vector2 getAnchor2() {
        return this.body2.getWorldPoint(this.localAnchor2);
    }

    @Override
    public Vector2 getReactionForce(double invdt) {
        Vector2 force = new Vector2();
        force.x = this.impulse.x * this.perp.x + (this.motorImpulse + this.impulse.y) * this.axis.x;
        force.y = this.impulse.x * this.perp.y + (this.motorImpulse + this.impulse.y) * this.axis.y;
        force.multiply(invdt);
        return force;
    }

    @Override
    public double getReactionTorque(double invdt) {
        return 0;
    }

    @Override
    protected void shiftCoordinates(Vector2 shift) {
    }

    /**
	 * Returns the axis of the line joint.
	 * @return {@link Vector2}
	 * @since 3.0.2
	 */
    public Vector2 getAxis() {
        return this.body2.getWorldVector(this.xAxis);
    }

    /**
	 * Returns the current joint speed.
	 * @return double
	 */
    public double getJointSpeed() {
        Transform t1 = this.body1.getTransform();
        Transform t2 = this.body2.getTransform();
        Vector2 c1 = this.body1.getWorldCenter();
        Vector2 c2 = this.body2.getWorldCenter();
        Vector2 r1 = t1.getTransformedR(this.body1.getLocalCenter().to(this.localAnchor1));
        Vector2 r2 = t2.getTransformedR(this.body2.getLocalCenter().to(this.localAnchor2));
        Vector2 d = c1.sum(r1).subtract(c2.sum(r2));
        Vector2 axis = this.body2.getWorldVector(this.xAxis);
        Vector2 v1 = this.body1.getVelocity();
        Vector2 v2 = this.body2.getVelocity();
        double w1 = this.body1.getAngularVelocity();
        double w2 = this.body2.getAngularVelocity();
        double speed = d.dot(axis.cross(w2)) + axis.dot(v1.sum(r1.cross(w1)).subtract(v2.sum(r2.cross(w2))));
        return speed;
    }

    /**
	 * Returns the current joint translation.
	 * @return double
	 */
    public double getJointTranslation() {
        Vector2 p1 = this.body1.getWorldPoint(this.localAnchor1);
        Vector2 p2 = this.body2.getWorldPoint(this.localAnchor2);
        Vector2 d = p2.difference(p1);
        Vector2 axis = this.body2.getWorldVector(this.xAxis);
        return d.dot(axis);
    }

    /**
	 * Returns true if the motor is enabled.
	 * @return boolean
	 */
    public boolean isMotorEnabled() {
        return motorEnabled;
    }

    /**
	 * Enables or disables the motor.
	 * @param motorEnabled true if the motor should be enabled
	 */
    public void setMotorEnabled(boolean motorEnabled) {
        this.body1.setAsleep(false);
        this.body2.setAsleep(false);
        this.motorEnabled = motorEnabled;
    }

    /**
	 * Returns the target motor speed in meters / second.
	 * @return double
	 */
    public double getMotorSpeed() {
        return motorSpeed;
    }

    /**
	 * Sets the target motor speed.
	 * @param motorSpeed the target motor speed in meters / second
	 */
    public void setMotorSpeed(double motorSpeed) {
        this.body1.setAsleep(false);
        this.body2.setAsleep(false);
        this.motorSpeed = motorSpeed;
    }

    /**
	 * Returns the maximum force the motor can apply to the joint
	 * to achieve the target speed.
	 * @return double
	 */
    public double getMaximumMotorForce() {
        return maximumMotorForce;
    }

    /**
	 * Sets the maximum force the motor can apply to the joint
	 * to achieve the target speed.
	 * @param maxMotorForce the maximum force in newtons; in the range [0, &infin;]
	 * @throws IllegalArgumentException if maxMotorForce is less than zero
	 */
    public void setMaximumMotorForce(double maxMotorForce) {
        if (maxMotorForce < 0.0) throw new IllegalArgumentException(Messages.getString("dynamics.joint.invalidMaximumMotorForce"));
        this.body1.setAsleep(false);
        this.body2.setAsleep(false);
        this.maximumMotorForce = maxMotorForce;
    }

    /**
	 * Returns the applied motor force.
	 * @return double
	 */
    public double getMotorForce() {
        return this.motorImpulse;
    }

    /**
	 * Returns true if the limit is enabled.
	 * @return boolean
	 */
    public boolean isLimitEnabled() {
        return this.limitEnabled;
    }

    /**
	 * Enables or disables the limits.
	 * @param limitEnabled true if the limit should be enabled.
	 */
    public void setLimitEnabled(boolean limitEnabled) {
        this.body1.setAsleep(false);
        this.body2.setAsleep(false);
        this.limitEnabled = limitEnabled;
    }

    /**
	 * Returns the lower limit in meters.
	 * @return double
	 */
    public double getLowerLimit() {
        return lowerLimit;
    }

    /**
	 * Sets the lower limit.
	 * @param lowerLimit the lower limit in meters
	 * @throws IllegalArgumentException if lowerLimit is greater than the current upper limit
	 */
    public void setLowerLimit(double lowerLimit) {
        if (lowerLimit > this.upperLimit) throw new IllegalArgumentException(Messages.getString("dynamics.joint.invalidLowerLimit"));
        this.body1.setAsleep(false);
        this.body2.setAsleep(false);
        this.lowerLimit = lowerLimit;
    }

    /**
	 * Returns the upper limit in meters.
	 * @return double
	 */
    public double getUpperLimit() {
        return upperLimit;
    }

    /**
	 * Sets the upper limit.
	 * @param upperLimit the upper limit in meters
	 * @throws IllegalArgumentException if upperLimit is less than the current lower limit
	 */
    public void setUpperLimit(double upperLimit) {
        if (upperLimit < this.lowerLimit) throw new IllegalArgumentException(Messages.getString("dynamics.joint.invalidUpperLimit"));
        this.body1.setAsleep(false);
        this.body2.setAsleep(false);
        this.upperLimit = upperLimit;
    }

    /**
	 * Sets the upper and lower limits.
	 * <p>
	 * The lower limit must be less than or equal to the upper limit.
	 * @param lowerLimit the lower limit in meters
	 * @param upperLimit the upper limit in meters
	 * @throws IllegalArgumentException if lowerLimit is greater than upperLimit
	 */
    public void setLimits(double lowerLimit, double upperLimit) {
        if (lowerLimit > upperLimit) throw new IllegalArgumentException(Messages.getString("dynamics.joint.invalidLimits"));
        this.body1.setAsleep(false);
        this.body2.setAsleep(false);
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
    }

    /**
	 * Sets the upper and lower limits and enables the limit.
	 * <p>
	 * The lower limit must be less than or equal to the upper limit.
	 * @param lowerLimit the lower limit in meters
	 * @param upperLimit the upper limit in meters
	 * @throws IllegalArgumentException if lowerLimit is greater than upperLimit
	 * @since 3.0.0
	 */
    public void setLimitsEnabled(double lowerLimit, double upperLimit) {
        this.setLimits(lowerLimit, upperLimit);
        this.limitEnabled = true;
    }
}
