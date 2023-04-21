package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO.EnhancedIOException;

public class PSoC {

    public static int elbow = 1;

    public static int wrist = 2;

    public static int finger = 3;

    public static int stickY = 4;

    public static int stickX = 5;

    public static int middle = 4;

    public static int load = 5;

    public static int bottom = 6;

    public static int range = 12;

    public static int encoderEnable = 7;

    public static int setup = 8;

    public static int driveMode = 9;

    public static int enter = 10;

    public static int deployMinibot = 11;

    public static int selectWIRC = 1;

    public static int clawClose = 2;

    public static int wristUp = 3;

    public static double kElbowMin = 0.1;

    public static double kElbowMax = 1.6;

    public static double kFingerMax = 0.8;

    public static double kFingerMin = 0.2;

    public static double kWristMax = 0.9;

    public static double kWristMin = 0;

    public static int feedback = 13;

    public static int lowerPosition = 14;

    public static int loadPosition = 15;

    public static int middlePosition = 16;

    public static double kHeightMax = 3;

    public static double kFingerDeadband = 0.3;

    public static double kWristDeadband = 0.45;

    private DriverStation driverStation;

    private RobotTemplate robot;

    private Elevator elevator;

    private Minibot minibot;

    private LogomotionDrive logoDrive;

    public PSoC(DriverStation ds, LogomotionDrive logoDrive) {
        driverStation = ds;
        this.logoDrive = logoDrive;
    }

    public void teleopPeriodic() {
        try {
            System.out.println("Minibot Button State: " + driverStation.getEnhancedIO().getDigital(1));
            if (driverStation.getEnhancedIO().getDigital(selectWIRC) == true) {
                driveWithWIRC();
            } else {
                driveWithPanel();
            }
            setElevatorLEDs();
        } catch (EnhancedIOException e) {
        }
    }

    /**
	 * @brief Gets the range of the elevator.
	 * @return Int 1 or 2, depending on the selected range.
	 */
    private int getRange() throws EnhancedIOException {
        return driverStation.getEnhancedIO().getDigital(range) == true ? Elevator.kUpperRange : Elevator.kLowerRange;
    }

    private double getFinger() {
        double rawFinger = 0;
        try {
            rawFinger = driverStation.getEnhancedIO().getAnalogIn(finger);
        } catch (EnhancedIOException e) {
            e.printStackTrace();
        }
        return rawFinger;
    }

    private double getWrist() {
        double rawWrist = 0;
        try {
            rawWrist = driverStation.getEnhancedIO().getAnalogIn(finger);
        } catch (EnhancedIOException e) {
            e.printStackTrace();
        }
        return rawWrist;
    }

    private double getHeight() {
        double calculatedHeight = 0;
        try {
            double rawHeight = kHeightMax - driverStation.getEnhancedIO().getAnalogIn(finger);
        } catch (EnhancedIOException e) {
            e.printStackTrace();
        }
        return calculatedHeight;
    }

    private void setElevatorLEDs() throws EnhancedIOException {
        final int pos = elevator.getCurrentPosition();
        driverStation.getEnhancedIO().setDigitalOutput(bottom, pos == Elevator.kBottom);
        driverStation.getEnhancedIO().setDigitalOutput(load, pos == Elevator.kFeed);
        driverStation.getEnhancedIO().setDigitalOutput(middle, pos == Elevator.kMiddle);
    }

    private void driveWithWIRC() throws EnhancedIOException {
        double wristVal = getWrist();
        double fingerVal = getFinger();
        double heightVal = getHeight();
        if (wristVal > kWristDeadband) {
            elevator.wristUp();
        } else if (wristVal < kWristDeadband) {
            elevator.wristDown();
        }
        if (fingerVal > kFingerDeadband) {
            elevator.openFinger();
        } else if (fingerVal < kFingerDeadband) {
            elevator.closeFinger();
        }
        double calcHeight = (heightVal / kHeightMax) * Configuration.kElevatorScale;
        elevator.setPosition(calcHeight);
        if (driverStation.getEnhancedIO().getDigital(setup) == false) {
            calibrateHeights();
        }
    }

    private void driveWithPanel() throws EnhancedIOException {
        if (driverStation.getEnhancedIO().getDigital(bottom)) {
            elevator.goToPosition(Elevator.kBottom, getRange());
        } else if (driverStation.getEnhancedIO().getDigital(middle)) {
            elevator.goToPosition(Elevator.kMiddle, getRange());
        } else if (driverStation.getEnhancedIO().getDigital(load) == true) {
            elevator.goToPosition(Elevator.kFeed, 0);
        }
    }

    /**
	 * @brief Used to re-calibrate the heights of the elevator using the PSoC module.
	 */
    public void calibrateHeights() {
    }
}
