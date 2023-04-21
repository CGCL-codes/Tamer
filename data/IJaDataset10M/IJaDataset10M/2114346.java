package com.yxl.test.lesson7.actions;

import com.yxl.test.lesson7.Vehicle;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;

/**
 * 车辆左右转向控制
 * @author yxl
 */
public class VehicleRotateAction extends KeyInputAction {

    public static final int RIGHT = 0;

    public static final int LEFT = 1;

    private static final Matrix3f incr = new Matrix3f();

    private static final Matrix3f tempMa = new Matrix3f();

    private static final Matrix3f tempMb = new Matrix3f();

    /**沿Y轴转动*/
    private Vector3f upAxis = new Vector3f(0, 1, 0);

    /**车辆*/
    private Vehicle vehicle;

    /**方向*/
    private int direction;

    /**1-向左,-1向右*/
    private int modifier = 1;

    /**
     * 转向
     * @param vehicle 车辆
     * @param direction 方向
     */
    public VehicleRotateAction(Vehicle vehicle, int direction) {
        this.vehicle = vehicle;
        this.direction = direction;
    }

    /**
     * 处理事件
     */
    public void performAction(InputActionEvent evt) {
        if (direction == LEFT) {
            modifier = 1;
        } else if (direction == RIGHT) {
            modifier = -1;
        }
        if (vehicle.getVelocity() < 0) {
            incr.fromAngleNormalAxis(-modifier * vehicle.getTurnSpeed() * evt.getTime(), upAxis);
        } else {
            incr.fromAngleNormalAxis(modifier * vehicle.getTurnSpeed() * evt.getTime(), upAxis);
        }
        vehicle.getLocalRotation().fromRotationMatrix(incr.mult(vehicle.getLocalRotation().toRotationMatrix(tempMa), tempMb));
        vehicle.getLocalRotation().normalize();
    }
}
