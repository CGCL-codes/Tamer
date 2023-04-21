package com.grt192.benchtest.controller;

import com.grt192.benchtest.mechanism.BenchDriveTrain;
import com.grt192.core.EventController;
import com.grt192.event.component.JoystickEvent;
import com.grt192.event.component.JoystickListener;
import com.grt192.mechanism.GRTDriverStation;
import com.grt192.sensor.GRTJoystick;

/**
 *
 * @author anand
 */
public class BenchDriveController extends EventController implements JoystickListener {

    public static final int TANK_DRIVE = 0;

    public static final int CAR_DRIVE = 1;

    public static final double THRESHOLD = .075;

    private GRTDriverStation ds;

    private BenchDriveTrain dt;

    private int mode;

    public BenchDriveController(GRTDriverStation ds, BenchDriveTrain dt) {
        this.ds = ds;
        this.dt = dt;
        mode = TANK_DRIVE;
        ((GRTJoystick) ds.getSensor("leftJoystick")).addJoystickListener(this);
        ((GRTJoystick) ds.getSensor("rightJoystick")).addJoystickListener(this);
    }

    public void xAxisMoved(JoystickEvent e) {
        if (mode == CAR_DRIVE) {
            if (e.getSource().getId().equals("left")) {
                dt.carDrive(e.getSource().getState("yValue"), e.getSource().getState("xValue"), Math.abs(e.getSource().getState("yValue")) < THRESHOLD);
            }
        }
    }

    public void yAxisMoved(JoystickEvent e) {
        if (mode == CAR_DRIVE) {
            if (e.getSource().getId().equals("left")) {
                dt.carDrive(e.getSource().getState("yValue"), e.getSource().getState("xValue"), Math.abs(e.getSource().getState("yValue")) < THRESHOLD);
            }
        } else if (mode == TANK_DRIVE) {
            dt.tankDrive(ds.getYLeftJoystick(), ds.getYRightJoystick());
        }
    }

    public void zAxisMoved(JoystickEvent e) {
    }

    public void throttleMoved(JoystickEvent e) {
    }
}
