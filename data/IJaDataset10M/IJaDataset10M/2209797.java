package com.grt192.mechanism;

import com.grt192.core.Mechanism;
import com.grt192.core.Sensor;
import com.grt192.sensor.GRTJoystick;

/**
 * A standard Driverstation with two joysticks connected
 * Contains basic button states and throttle access
 * @author Student
 */
public class GRTDriverStation extends Mechanism {

    public GRTDriverStation(int left, int right, int secondary) {
        this(new GRTJoystick(left, 50, "left"), new GRTJoystick(right, 50, "right"), new GRTJoystick(secondary, 50, "secondary"));
    }

    public GRTDriverStation(GRTJoystick left, GRTJoystick right, GRTJoystick secondary) {
        left.start();
        right.start();
        secondary.start();
        addSensor("leftJoystick", left);
        addSensor("rightJoystick", right);
        addSensor("secondaryJoystick", secondary);
    }

    public double getLeftJoyStickAngle() {
        return getSensor("leftJoystick").getState("JoystickAngle");
    }

    public double getRightJoyStickAngle() {
        return getSensor("rightJoystick").getState("JoystickAngle");
    }

    public boolean getLeftButton(int button) {
        return getSensor("leftJoystick").getState("Button" + button) == Sensor.TRUE;
    }

    public boolean getRightButton(int button) {
        return getSensor("rightJoystick").getState("Button" + button) == Sensor.TRUE;
    }

    public boolean getSecondaryButton(int button) {
        return getSensor("secondaryJoystick").getState("Button" + button) == Sensor.TRUE;
    }

    public double getXLeftJoystick() {
        return getSensor("leftJoystick").getState("xValue");
    }

    public double getYLeftJoystick() {
        return getSensor("leftJoystick").getState("yValue");
    }

    public double getZLeftJoystick() {
        return getSensor("leftJoystick").getState("zValue");
    }

    public double getXRightJoystick() {
        return getSensor("rightJoystick").getState("xValue");
    }

    public double getYRightJoystick() {
        return getSensor("rightJoystick").getState("yValue");
    }

    public double getZRightJoystick() {
        return getSensor("rightJoystick").getState("zValue");
    }

    public double getLeftThrottle() {
        return getSensor("rightJoystick").getState("Throttle");
    }

    public double getRightThrottle() {
        return getSensor("rightJoystick").getState("Throttle");
    }

    public double getSecondaryThrottle() {
        return getSensor("secondaryJoystick").getState("Throttle");
    }

    public double getXSecondaryJoystick() {
        return getSensor("secondaryJoystick").getState("xValue");
    }

    public double getYSecondaryJoystick() {
        return getSensor("secondaryJoystick").getState("yValue");
    }

    public double getZSecondaryJoystick() {
        return getSensor("secondaryJoystick").getState("zValue");
    }

    public String toString() {
        return "Driver Station with two joysticks";
    }
}
