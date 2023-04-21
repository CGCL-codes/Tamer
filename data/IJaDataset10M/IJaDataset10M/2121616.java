package edu.arsc.fullmetal.server;

import edu.arsc.fullmetal.commons.RobotCapabilities;

/**
 * Provides access to a robot through a driver class.
 * 
 * @todo Add methods for sending commands to the robot. 
 */
public interface Robot {

    /**
     * 
     * @return A friendly name for the robot.
     */
    String getName();

    /**
     * Connects to the robot represented by the object.
     * 
     * @post The robot is connected to the caller.
     * @throws RobotDriverException whenever the method fails for any reason.
     */
    void connect() throws RobotDriverException;

    /**
     * Disconnects from the robot represented by the object
     * 
     * @post The robot is disconnected from the caller.
     * @throws RobotDriverException whenever the method fails for any reason.
     */
    void disconnect() throws RobotDriverException;

    /**
     * Checks whether the robot is currently connected to the caller.
     * 
     * @return True if and only if the caller is currently connected to the
     * robot.
     */
    boolean isConnected();

    /**
     * Checks if the robot is available for use.
     * 
     * @return True if and only if a call to @code{connect()} should succeed (if
     * the robot is not already connected to anyone, is still present, etc.).
     */
    boolean mayConnect();

    /**
     * Describes the capabilities of the robot.
     * 
     * @return A {@code RobotCapabilities} object representing the features of
     * the robot.
     */
    RobotCapabilities getCapabilities();
}
