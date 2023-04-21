package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.buttons.Button;

/**
 *
 * @author matt
 */
public class LogitechControllerButton extends Button {

    LogitechController m_controller;

    int m_buttonNumber;

    /**
     * Create a joystick button for triggering commands
     * @param joystick The joystick object that has the button
     * @param buttonNumber The button number (see {@link Joystick#getRawButton(int) }
     */
    public LogitechControllerButton(LogitechController joystick, int buttonNumber) {
        m_controller = joystick;
        m_buttonNumber = buttonNumber;
    }

    /**
     * Gets the value of the joystick button
     * @return The value of the joystick button
     */
    public boolean get() {
        return m_controller.getRawButton(m_buttonNumber);
    }
}
