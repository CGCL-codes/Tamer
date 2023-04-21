package com.yxl.test.lesson6.actions;

import com.yxl.test.lesson6.Vehicle;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;

/**
 * 车辆右转弯
 * @author yxl
 */
public class VehicleRotateRightAction extends KeyInputAction {

    private static final Matrix3f incr = new Matrix3f();

    private static final Matrix3f tempMa = new Matrix3f();

    private static final Matrix3f tempMb = new Matrix3f();

    private Vehicle vehicle;

    private Vector3f upAxis = new Vector3f(0, 1, 0);

    /**
     * 构造方法
     */
    public VehicleRotateRightAction(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * 转弯事件
     */
    public void performAction(InputActionEvent evt) {
        if (vehicle.getVelocity() < 0) {
            incr.fromAngleNormalAxis(vehicle.getTurnSpeed() * evt.getTime(), upAxis);
        } else {
            incr.fromAngleNormalAxis(-vehicle.getTurnSpeed() * evt.getTime(), upAxis);
        }
        vehicle.getLocalRotation().fromRotationMatrix(incr.mult(vehicle.getLocalRotation().toRotationMatrix(tempMa), tempMb));
        vehicle.getLocalRotation().normalize();
    }
}
