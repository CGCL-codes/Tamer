package com.grt192.mechansim.GRTBenchTest;

import com.grt192.actuator.GRTCANJaguar;
import com.grt192.actuator.GRTSolenoid;
import com.grt192.controller.pid.AsynchronousPIDController;
import com.grt192.core.Command;
import com.grt192.core.Mechanism;
import com.grt192.event.component.GyroEvent;
import com.grt192.event.component.GyroListener;
import com.grt192.event.component.JagEncoderEvent;
import com.grt192.event.component.JagEncoderListener;
import com.grt192.event.controller.PIDEvent;
import com.grt192.event.controller.PIDListener;
import com.grt192.sensor.GRTGyro;
import com.grt192.sensor.canjaguar.GRTJagEncoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

/**
 *
 * @author student
 */
public class ShifterBoxMechanism extends Mechanism implements PIDOutput, JagEncoderListener, GyroListener, PIDListener {

    public final int LEFT = 1;

    public final int RIGHT = 2;

    public final boolean isENGAGE = true;

    public static final int WAIT_INTERVAL = 2;

    public static final int ROBOT_WIDTH = 28;

    private double turnP = .10;

    private double turnI = .10;

    private double turnD = .10;

    private double driveP = .10;

    private double driveI = .10;

    private double driveD = .10;

    private GRTCANJaguar jag1;

    private GRTCANJaguar jag2;

    private GRTJagEncoder encodDT1;

    private GRTJagEncoder encodMech1;

    private GRTSolenoid DTShifter1;

    private GRTSolenoid MechShifter1;

    private GRTCANJaguar jag3;

    private GRTCANJaguar jag4;

    private GRTJagEncoder encodDT2;

    private GRTJagEncoder encodMech2;

    private GRTSolenoid DTShifter2;

    private GRTSolenoid MechShifter2;

    private GRTGyro gyro;

    private double leftWheelX;

    private double leftWheelY;

    private double rightWheelX;

    private double rightWheelY;

    private boolean pointTurn;

    private AsynchronousPIDController turnControl;

    private AsynchronousPIDController leftDriveControl;

    private AsynchronousPIDController rightDriveControl;

    public ShifterBoxMechanism(GRTCANJaguar jag1, GRTCANJaguar jag2, GRTCANJaguar jag3, GRTCANJaguar jag4, GRTJagEncoder encodDT1, GRTJagEncoder encodMech1, GRTSolenoid DTShifter1, GRTSolenoid MechShifter1, GRTJagEncoder encodDT2, GRTJagEncoder encodMech2, GRTSolenoid DTShifter2, GRTSolenoid MechShifter2, GRTGyro gyro) {
        this.encodDT1 = encodDT1;
        this.encodMech1 = encodMech1;
        this.jag1 = jag1;
        this.jag2 = jag2;
        this.DTShifter1 = DTShifter1;
        this.MechShifter1 = MechShifter1;
        this.gyro = gyro;
        this.encodDT2 = encodDT2;
        this.encodMech2 = encodMech2;
        this.jag3 = jag3;
        this.jag4 = jag4;
        this.DTShifter2 = DTShifter2;
        this.MechShifter2 = MechShifter2;
        this.gyro = gyro;
        init();
    }

    private void init() {
        addActuator("DT1Jaguar", jag1);
        addActuator("DT1Jaguar2", jag2);
        addActuator("DT1Shifter1", DTShifter1);
        addActuator("Mech1Shifter1", MechShifter1);
        addActuator("DT1Jaguar3", jag3);
        addActuator("DT1Jaguar4", jag4);
        addActuator("DT1Shifter2", DTShifter2);
        addActuator("Mech1Shifter2", MechShifter2);
        addSensor("encodDT1", encodDT1);
        addSensor("encodMech1", encodMech1);
        addSensor("encodDT2", encodDT2);
        addSensor("encodMech2", encodMech2);
        DTShifter1.start();
        MechShifter1.start();
        jag1.start();
        jag2.start();
        encodDT1.start();
        encodMech1.start();
        DTShifter2.start();
        MechShifter2.start();
        jag3.start();
        jag4.start();
        encodDT2.start();
        encodMech2.start();
        leftWheelX = -ROBOT_WIDTH / 2;
        rightWheelX = ROBOT_WIDTH / 2;
        leftWheelY = rightWheelY = 0;
        turnControl = new AsynchronousPIDController(new PIDController(turnP, turnI, turnD, gyro, this));
        leftDriveControl = new AsynchronousPIDController(new PIDController(driveP, driveI, driveD, encodDT1, this));
        rightDriveControl = new AsynchronousPIDController(new PIDController(driveP, driveI, driveD, encodDT2, this));
        pointTurn = true;
        leftDriveControl.addPIDListener(this);
        rightDriveControl.addPIDListener(this);
        turnControl.addPIDListener(this);
        leftDriveControl.start();
        rightDriveControl.start();
        turnControl.start();
    }

    public double getDriveP() {
        return driveP;
    }

    public void setDriveP(double driveP) {
        this.driveP = driveP;
    }

    public double getDriveI() {
        return driveI;
    }

    public void setDriveI(double driveI) {
        this.driveI = driveI;
    }

    public double getDriveD() {
        return driveD;
    }

    public void setDriveD(double driveD) {
        this.driveD = driveD;
    }

    /**
     *
     * @param leftSpeed
     * @param rightSpeed
     *
     * Use this method directly in the controller to do manual stuff
     */
    public void tankDrive(double leftSpeed, double rightSpeed) {
        this.jag1.enqueueCommand(leftSpeed);
        this.jag2.enqueueCommand(leftSpeed);
        this.jag3.enqueueCommand(rightSpeed);
        this.jag4.enqueueCommand(rightSpeed);
    }

    /**
     * This is mainly for autonomous like stuff
     * @param position
     */
    public void driveToPosition(double position) {
        driveToPosition(position, position);
    }

    /**
     * Drive dts to a specific position, provided where the l&r wheels should be
     *
     * @param leftPosition
     * @param rightPosition
     */
    public void driveToPosition(double leftPosition, double rightPosition) {
        stopPIDControl();
        leftDriveControl.getPidThread().setSetpoint(leftPosition);
        rightDriveControl.getPidThread().setSetpoint(rightPosition);
        leftDriveControl.enable();
        rightDriveControl.enable();
    }

    public void driveDistance(double distance) {
        driveDistance(distance, distance);
    }

    /**
     * Drive l&r wheels a certain distance
     *
     * @param leftDistance
     * @param rightDistance
     */
    public void driveDistance(double leftDistance, double rightDistance) {
        driveToPosition(encodDT1.getState("Distance") + leftDistance, encodDT2.getState("Distance") + rightDistance);
    }

    public void turnTo(double angle) {
        turnTo(angle, true);
    }

    /**
     * Turn the robot to a specific heading
     *
     * @param angle
     * @param point
     */
    public void turnTo(double angle, boolean point) {
        stopPIDControl();
        pointTurn = point;
        turnControl.getPidThread().setSetpoint(angle);
        turnControl.enable();
    }

    public void pidWrite(double output) {
        if (pointTurn) {
            tankDrive(output, -output);
        } else {
            tankDrive((1 + output) / 2, 1 - (1 + output) / 2);
        }
    }

    public void stop() {
        stopPIDControl();
        tankDrive(0, 0);
    }

    public double getTurnP() {
        return turnP;
    }

    public void setTurnP(double turnP) {
        this.turnP = turnP;
    }

    public double getTurnI() {
        return turnI;
    }

    public void setTurnI(double turnI) {
        this.turnI = turnI;
    }

    public double getTurnD() {
        return turnD;
    }

    public void setTurnD(double turnD) {
        this.turnD = turnD;
    }

    public boolean isPointTurn() {
        return pointTurn;
    }

    public void setPointTurn(boolean pointTurn) {
        this.pointTurn = pointTurn;
    }

    private void block() {
        try {
            Thread.sleep(WAIT_INTERVAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public double getLeftPosition() {
        return encodDT1.getState("Distance");
    }

    public double getRightPosition() {
        return encodDT2.getState("Distance");
    }

    public double getHeading() {
        return gyro.getState("Angle");
    }

    public void countDidChange(JagEncoderEvent e) {
        double deltaR = e.getDistance() - e.getSource().getState("Previous");
        if (e.getSource().getId().equals("ljagencoder")) {
            leftWheelX = deltaR * Math.cos(Math.toRadians(getHeading()));
            leftWheelY = deltaR * Math.sin(Math.toRadians(getHeading()));
        } else if (e.getSource().getId().equals("rjagencoder")) {
            rightWheelX = deltaR * Math.cos(Math.toRadians(getHeading()));
            rightWheelY = deltaR * Math.sin(Math.toRadians(getHeading()));
        }
    }

    public void rotationDidStart(JagEncoderEvent e) {
    }

    public void rotationDidStop(JagEncoderEvent e) {
    }

    public void changedDirection(JagEncoderEvent e) {
    }

    public void didReceiveAngle(GyroEvent e) {
    }

    public void didAngleChange(GyroEvent e) {
    }

    public void didAngleSpike(GyroEvent e) {
    }

    public void onSetpointReached(PIDEvent e) {
        e.getSource().reset();
    }

    public double getLeftWheelX() {
        return leftWheelX;
    }

    public double getLeftWheelY() {
        return leftWheelY;
    }

    public double getRightWheelX() {
        return rightWheelX;
    }

    public double getRightWheelY() {
        return rightWheelY;
    }

    public double getX() {
        return (getLeftWheelX() + getRightWheelX()) / 2;
    }

    public double getY() {
        return (getRightWheelY() + getRightWheelY()) / 2;
    }

    public void stopPIDControl() {
        leftDriveControl.reset();
        rightDriveControl.reset();
        turnControl.reset();
    }

    public void suspendPIDControl() {
        leftDriveControl.disable();
        rightDriveControl.disable();
        turnControl.disable();
    }

    public void changeMechPos(boolean state, int side) {
        if (state) {
            if (side == LEFT) {
                this.MechShifter1.enqueueCommand(new Command(MechShifter1.ON));
            } else {
                this.MechShifter2.enqueueCommand(new Command(MechShifter2.ON));
            }
        } else {
            if (side == LEFT) {
                this.MechShifter1.enqueueCommand(new Command(MechShifter1.OFF));
            } else {
                this.MechShifter2.enqueueCommand(new Command(MechShifter2.OFF));
            }
        }
    }

    public void changeDTPos(boolean state, int side) {
        if (state) {
            if (side == LEFT) {
                this.DTShifter1.enqueueCommand(new Command(DTShifter1.ON));
            } else {
                this.DTShifter2.enqueueCommand(new Command(DTShifter2.ON));
            }
        } else {
            if (side == LEFT) {
                this.DTShifter1.enqueueCommand(new Command(DTShifter1.OFF));
            } else {
                this.DTShifter2.enqueueCommand(new Command(DTShifter2.OFF));
            }
        }
    }
}
