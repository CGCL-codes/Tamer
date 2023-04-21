package org.xith3d.behaviors;

import javax.vecmath.Vector3f;

/**
 * This class may serve as a base for classes to describe the
 * transmormation speed by the three carthesian axes or a
 * user-defined axis.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class TransformationDirectives {

    /**
     * The axes of a carthesian coordinate system.
     * 
     * @author Marvin Froehlich (aka Qudus)
     */
    public enum Axes {

        X_AXIS, Y_AXIS, Z_AXIS
    }

    /**
     * The order in which to rotate around the axes.
     * 
     * @author Marvin Froehlich (aka Qudus)
     */
    public enum AxisOrder {

        XYZ, XZY, YXZ, YZX, ZXY, ZYX;

        public static AxisOrder getDefault() {
            return (XYZ);
        }
    }

    private float initValueX = 0.0f;

    private float initValueY = 0.0f;

    private float initValueZ = 0.0f;

    private float initValueU = 0.0f;

    private float speedX = 0.0f;

    private float speedY = 0.0f;

    private float speedZ = 0.0f;

    private float speedU = 0.0f;

    private Vector3f userAxis;

    private AxisOrder axisOrder;

    /**
     * @return in which order the transformation is to be applied
     */
    public AxisOrder getAxisOrder() {
        return (axisOrder);
    }

    /**
     * Sets in which order the transformation is to be applied.
     */
    public void setAxisOrder(AxisOrder axisOrder) {
        this.axisOrder = axisOrder;
    }

    /**
     * @return the user-defined axis to transform by
     */
    public Vector3f getUserAxis() {
        return (userAxis);
    }

    /**
     * Sets the user-defined axis to transform by.
     */
    public void setUserAxis(Vector3f userAxis) {
        this.userAxis = new Vector3f(userAxis);
        this.userAxis.normalize();
    }

    /**
     * @return the initial transformation value by the x-axis (1.0f is 360 degree)
     */
    public float getInitValueX() {
        return (initValueX);
    }

    /**
     * Sets the initial transformation value by the x-axis (1.0f is 360 degree).
     */
    public void setInitValueX(float value) {
        this.initValueX = value;
    }

    /**
     * @return the initial transformation value by the y-axis (1.0f is 360 degree)
     */
    public float getInitValueY() {
        return (initValueY);
    }

    /**
     * Sets the initial transformation value by the y-axis (1.0f is 360 degree).
     */
    public void setInitValueY(float value) {
        this.initValueY = value;
    }

    /**
     * @return the initial transformation value by the z-axis (1.0f is 360 degree)
     */
    public float getInitValueZ() {
        return (initValueZ);
    }

    /**
     * Sets the initial transformation value by the z-axis (1.0f is 360 degree).
     */
    public void setInitValueZ(float value) {
        this.initValueZ = value;
    }

    /**
     * @return the initial transformation value by the user-defined-axis (1.0f is 360 degree)
     */
    public float getInitValueUser() {
        return (initValueU);
    }

    /**
     * Sets the initial transformation value by the user-defined-axis (1.0f is 360 degree).
     */
    public void setInitValueUser(float value) {
        this.initValueU = value;
    }

    /**
     * @return the initial transformation value by the specified axis.
     * Pass <b>null</b> to get the user-defined value.
     */
    public float getInitValue(Axes axis) {
        if (axis == null) {
            return (getInitValueUser());
        }
        switch(axis) {
            case X_AXIS:
                return (getInitValueX());
            case Y_AXIS:
                return (getInitValueY());
            case Z_AXIS:
                return (getInitValueZ());
            default:
                return (0.0f);
        }
    }

    /**
     * @return the initial transformation value by the user-defined axis.
     */
    public float getInitValue() {
        return (getInitValue(null));
    }

    /**
     * Sets the initial transformation value by the specified axis.
     * Pass <b>null</b> to set the user-defined value.
     */
    public void setInitValue(Axes axis, float value) {
        if (axis == null) {
            setInitValueUser(value);
            return;
        }
        switch(axis) {
            case X_AXIS:
                setInitValueX(value);
                break;
            case Y_AXIS:
                setInitValueY(value);
                break;
            case Z_AXIS:
                setInitValueZ(value);
                break;
        }
    }

    /**
     * Sets the initial transformation value by the user-defined axis.
     */
    public void setInitValue(float value) {
        setInitValue(null, value);
    }

    /**
     * @return the speed by the x-axis (e.g. rotations per second)
     */
    public float getSpeedX() {
        return (speedX);
    }

    /**
     * Sets the speed by the x-axis (e.g. rotations per second).
     */
    public void setSpeedX(float speed) {
        this.speedZ = speed;
    }

    /**
     * @return the speed by the y-axis (e.g. rotations per second)
     */
    public float getSpeedY() {
        return (speedY);
    }

    /**
     * Sets the speed by the y-axis (e.g. rotations per second).
     */
    public void setSpeedY(float speed) {
        this.speedZ = speed;
    }

    /**
     * @return the speed by the z-axis (e.g. rotations per second)
     */
    public float getSpeedZ() {
        return (speedZ);
    }

    /**
     * Sets the speed by the z-axis (e.g. rotations per second).
     */
    public void setSpeedZ(float speed) {
        this.speedZ = speed;
    }

    /**
     * @return the speed by the user-defined-axis (e.g. rotations per second)
     */
    public float getSpeedUser() {
        return (speedU);
    }

    /**
     * Sets the speed by the user-defined-axis (e.g. rotations per second).
     */
    public void setSpeedUser(float speed) {
        this.speedU = speed;
    }

    /**
     * Returns the speed by the specified axis.
     * Pass <b>null</b> to get the user-defined axis speed.
     * 
     * @param axis the axis to translate by
     * 
     * @return the turns per second
     */
    public float getSpeed(Axes axis) {
        if (axis == null) {
            return (getSpeedUser());
        }
        switch(axis) {
            case X_AXIS:
                return (getSpeedX());
            case Y_AXIS:
                return (getSpeedY());
            case Z_AXIS:
                return (getSpeedZ());
            default:
                return (0.0f);
        }
    }

    /**
     * Returns the speed by the user-defined axis.
     * 
     * @return the turns per second
     */
    public float getSpeed() {
        return (getSpeed(null));
    }

    /**
     * Sets the speed by the specified axis.
     * Pass <b>null</b> to get the user-defined axis speed.
     * 
     * @param axis the axis to rotate agound
     * @param value the turns per second
     */
    public void setSpeed(Axes axis, float value) {
        if (axis == null) {
            setSpeedUser(value);
            return;
        }
        switch(axis) {
            case X_AXIS:
                setSpeedX(value);
                break;
            case Y_AXIS:
                setSpeedY(value);
                break;
            case Z_AXIS:
                setSpeedZ(value);
                break;
        }
    }

    /**
     * Sets the speed by the user-defined axis.
     * 
     * @param value the turns per second
     */
    public void setSpeed(float value) {
        setSpeed(null, value);
    }

    /**
     * Creates a new TransformationDirectives instance.
     * 
     * @param userAxis the axis by which to transform
     * @param initValue the initial transformation by the user-defined-axis (e.g. 1.0f is 360 degree)
     * @param speed the speed by the user-defined-axis (e.g. 1.0f is one turn per second)
     */
    public TransformationDirectives(Vector3f userAxis, float initValue, float speed) {
        this.initValueX = -1.0f;
        this.initValueY = -1.0f;
        this.initValueZ = -1.0f;
        this.initValueU = initValue;
        this.speedX = -1.0f;
        this.speedY = -1.0f;
        this.speedZ = -1.0f;
        this.speedU = speed;
        this.axisOrder = null;
        this.setUserAxis(userAxis);
    }

    /**
     * Creates a new TransformationDirectives instance.
     * 
     * @param userAxis the axis around which to rotate
     * @param speed the speed by the user-defined-axis (e.g. 1.0f is one turn per second)
     */
    public TransformationDirectives(Vector3f userAxis, float speed) {
        this(userAxis, 0.0f, speed);
    }

    /**
     * Creates a new TransformationDirectives instance.
     * 
     * @param userAxis the axis around which to rotate
     */
    public TransformationDirectives(Vector3f userAxis) {
        this(userAxis, 0.0f, 0.0f);
    }

    /**
     * Creates a new TransformationDirectives instance.
     * 
     * @param initValueX the initial value by the x-axis (e.g. 1.0f is 360 degree)
     * @param initValueY the initial rotation around the y-axis (e.g. 1.0f is 360 degree)
     * @param initValueZ the initial rotation around the z-axis (e.g. 1.0f is 360 degree)
     * @param speedX the rotation speed around the x-axis (e.g. 1.0f is one turn per second)
     * @param speedY the rotation speed around the y-axis (e.g. 1.0f is one turn per second)
     * @param speedZ the rotation speed around the z-axis (e.g. 1.0f is one turn per second)
     * @param axisOrder the order in which the axes are to rotate around
     */
    public TransformationDirectives(float initValueX, float initValueY, float initValueZ, float speedX, float speedY, float speedZ, AxisOrder axisOrder) {
        this.initValueX = initValueX;
        this.initValueY = initValueY;
        this.initValueZ = initValueZ;
        this.initValueU = -1.0f;
        this.speedX = speedX;
        this.speedY = speedY;
        this.speedZ = speedZ;
        this.speedU = -1.0f;
        this.axisOrder = axisOrder;
        this.userAxis = null;
    }

    /**
     * Creates a new TransformationDirectives instance.
     * 
     * @param initValueX the initial value by the x-axis (e.g. 1.0f is 360 degree)
     * @param initValueY the initial rotation around the y-axis (e.g. 1.0f is 360 degree)
     * @param initValueZ the initial rotation around the z-axis (e.g. 1.0f is 360 degree)
     * @param speedX the rotation speed around the x-axis (e.g. 1.0f is one turn per second)
     * @param speedY the rotation speed around the y-axis (e.g. 1.0f is one turn per second)
     * @param speedZ the rotation speed around the z-axis (e.g. 1.0f is one turn per second)
     */
    public TransformationDirectives(float initValueX, float initValueY, float initValueZ, float speedX, float speedY, float speedZ) {
        this(initValueX, initValueZ, initValueZ, speedX, speedY, speedZ, AxisOrder.getDefault());
    }

    /**
     * Creates a new TransformationDirectives instance.
     * 
     * @param speedX the rotation speed around the x-axis (1.0f is one turn per second)
     * @param speedY the rotation speed around the y-axis (1.0f is one turn per second)
     * @param speedZ the rotation speed around the z-axis (1.0f is one turn per second)
     * @param axisOrder the order in which the axes are to transformed by
     */
    public TransformationDirectives(float speedX, float speedY, float speedZ, AxisOrder axisOrder) {
        this(0.0f, 0.0f, 0.0f, speedX, speedY, speedZ, axisOrder);
    }

    /**
     * Creates a new TransformationDirectives instance.
     * 
     * @param speedX the transformation speed by the x-axis (1.0f is one turn per second)
     * @param speedY the transformation speed by the y-axis (1.0f is one turn per second)
     * @param speedZ the transformation speed by the z-axis (1.0f is one turn per second)
     */
    public TransformationDirectives(float speedX, float speedY, float speedZ) {
        this(0.0f, 0.0f, 0.0f, speedX, speedY, speedZ, AxisOrder.getDefault());
    }

    /**
     * Creates a new TransformationDirectives instance.
     * 
     * @param axisOrder the order in which the axes are to translated by
     */
    public TransformationDirectives(AxisOrder axisOrder) {
        this(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, axisOrder);
    }

    /**
     * Creates a new TransformationDirectives instance with all values set to zero.
     */
    public TransformationDirectives() {
        this(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, AxisOrder.getDefault());
    }
}
