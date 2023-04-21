package com.team1160.breakaway.model.robotCommand;

/**
 *
 * @author CJ
 */
public class RobotCommand {

    boolean extend_shooter;

    boolean hold_shooter;

    boolean toggle_claw;

    boolean activate_winch;

    double dt_right_speed;

    double dt_left_speed;

    boolean dribbler_on;

    double dribbler_speed;

    boolean motor_stop;

    byte[] motors_stopped;

    public boolean getToggleClaw() {
        return this.toggle_claw;
    }

    public boolean getActivateWinch() {
        return this.activate_winch;
    }

    public void setShooter(boolean extend_shooter) {
        this.extend_shooter = extend_shooter;
    }

    public void setHoldShooter(boolean hold_shooter) {
        this.hold_shooter = hold_shooter;
    }

    public void setRightDrive(double dt_right_speed) {
        this.dt_right_speed = dt_right_speed;
    }

    public void setLeftDrive(double dt_left_speed) {
        this.dt_left_speed = dt_left_speed;
    }

    public boolean extendShooter() {
        return this.extend_shooter;
    }

    public void setShooterExtended(boolean val) {
        this.extend_shooter = val;
    }

    public boolean holdShooter() {
        return this.hold_shooter;
    }

    public double getDriveRight() {
        return this.dt_right_speed;
    }

    public void setDriveRight(double val) {
        this.dt_right_speed = val;
    }

    public double getDriveLeft() {
        return this.dt_left_speed;
    }

    public void setDriveLeft(double val) {
        this.dt_left_speed = val;
    }

    public boolean dribblerOn() {
        return this.dribbler_on;
    }

    public void setDribblerOn(boolean val) {
        this.dribbler_on = val;
    }

    public double dribblerSpeed() {
        return this.dribbler_speed;
    }

    public void setDribblerSpeed(double val) {
        this.dribbler_speed = val;
    }

    public boolean motorStop() {
        return this.motor_stop;
    }

    public byte[] motorsStopped() {
        return this.motors_stopped;
    }

    public String toString() {
        String ag = "";
        return ag;
    }
}
