package com.jmex.physics.impl.ode.joints;

import com.jme.math.Vector3f;
import com.jmex.physics.JointAxis;
import com.jmex.physics.RotationalJointAxis;

/**
 * @author Irrisor
 */
public class RotationalOdeJointAxis extends RotationalJointAxis implements OdeJointAxis {

    private JointAxis delegate;

    public void setDelegate(JointAxis delegate) {
        this.delegate = delegate;
    }

    @Override
    public float getPosition() {
        if (delegate != null) {
            return delegate.getPosition();
        } else {
            return Float.NaN;
        }
    }

    @Override
    public float getVelocity() {
        if (delegate != null) {
            return delegate.getVelocity();
        } else {
            return Float.NaN;
        }
    }

    @Override
    public void setDirection(Vector3f direction) {
        super.setDirection(direction);
        if (delegate != null) {
            delegate.setDirection(direction);
        }
    }

    private float availableAcceleration = 0;

    private float desiredVelocity = 0;

    @Override
    public void setAvailableAcceleration(float value) {
        availableAcceleration = value;
        if (delegate != null) {
            delegate.setAvailableAcceleration(value);
        }
    }

    @Override
    public float getAvailableAcceleration() {
        return availableAcceleration;
    }

    @Override
    public void setDesiredVelocity(float value) {
        desiredVelocity = value;
        if (delegate != null) {
            delegate.setDesiredVelocity(value);
        }
    }

    @Override
    public float getDesiredVelocity() {
        return desiredVelocity;
    }

    private float min = Float.NEGATIVE_INFINITY;

    private float max = Float.POSITIVE_INFINITY;

    @Override
    public float getPositionMaximum() {
        return max;
    }

    @Override
    public float getPositionMinimum() {
        return min;
    }

    @Override
    public void setPositionMaximum(float value) {
        max = value;
        if (delegate != null) {
            delegate.setPositionMaximum(value);
        }
    }

    @Override
    public void setPositionMinimum(float value) {
        min = value;
        if (delegate != null) {
            delegate.setPositionMinimum(value);
        }
    }
}
