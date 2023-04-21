package org.usfirst.frc172;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Stephen Nutt
 */
public class iPhoneJoystick extends LogomotionJoystick {

    private static final int kFollowLine = 8;

    private static final int kMaintainDirection = 9;

    private static final int kLeftY = 1;

    private static final int kLeftX = 2;

    private static final int kRightX = 3;

    private static final int kRightY = 4;

    private static final int kTriggerZ = 5;

    public iPhoneJoystick(final int port) {
        super(port);
    }

    /**
     * Get the X value of the joystick.
     * This depends on the mapping of the joystick connected to the current port.
     *
     * @return The X value of the joystick.
     */
    public double getX(Hand hand) {
        return super.getRawAxis(hand.value == Hand.kLeft.value ? kLeftX : kRightX);
    }

    /**
     * Get the Y value of the joystick.
     * This depends on the mapping of the joystick connected to the current port.
     *
     * @return The Y value of the joystick.
     */
    public double getY(Hand hand) {
        return super.getRawAxis(hand.value == Hand.kLeft.value ? kLeftY : kRightY);
    }

    public void setControlMode(int mode) {
        controlMode = mode;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public double getDirection() {
        return ((180.0 / java.lang.Math.PI) * (Atan2.atan2(getY(Joystick.Hand.kLeft), getX(Joystick.Hand.kLeft))));
    }

    public double getStrafe() {
        return (super.getRawAxis(kTriggerZ));
    }

    public double getRotation() {
        return (getX(Joystick.Hand.kRight));
    }

    public double getSpeed() {
        double x = getX(Joystick.Hand.kLeft);
        double y = getY(Joystick.Hand.kRight);
        return java.lang.Math.sqrt(x * x + y * y);
    }

    /** @brief Returns true if the follow line button is pressed
	 *
	 */
    public boolean getFollowLine() {
        return getRawButton(kFollowLine);
    }

    /** @brief Returns true if the maintain direction button is pressed
	 *
	 */
    public boolean getMaintainDirection() {
        return getRawButton(kMaintainDirection);
    }
}
