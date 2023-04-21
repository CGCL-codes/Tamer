package com.jme.input.action;

import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

/**
 * <code>KeyRotateRightAction</code> performs the action of rotating a camera
 * a certain angle. This angle is determined by the speed at which the camera
 * can turn and the time between frames.
 * 
 * @author Mark Powell
 * @version $Id: KeyRotateRightAction.java 4131 2009-03-19 20:15:28Z blaine.dev $
 */
public class KeyRotateRightAction extends KeyInputAction {

    private static final Matrix3f incr = new Matrix3f();

    private Camera camera;

    private Vector3f lockAxis;

    /**
     * Constructor instantiates a new <code>KeyRotateLeftAction</code> object.
     * 
     * @param camera
     *            the camera to rotate.
     * @param speed
     *            the speed at which to rotate.
     */
    public KeyRotateRightAction(Camera camera, float speed) {
        this.camera = camera;
        this.speed = speed;
    }

    /**
     * 
     * <code>setLockAxis</code> allows a certain axis to be locked, meaning
     * the camera will always be within the plane of the locked axis. For
     * example, if the camera is a first person camera, the user might lock the
     * camera's up vector. This will keep the camera vertical of the ground.
     * 
     * @param lockAxis
     *            the axis to lock - should be unit length (normalized).
     */
    public void setLockAxis(Vector3f lockAxis) {
        this.lockAxis = lockAxis;
    }

    /**
     * <code>performAction</code> rotates the camera a certain angle.
     * 
     * @see com.jme.input.action.KeyInputAction#performAction(InputActionEvent)
     */
    public void performAction(InputActionEvent evt) {
        if (lockAxis == null) {
            incr.fromAngleNormalAxis(-speed * evt.getTime(), camera.getUp());
        } else {
            incr.fromAngleNormalAxis(-speed * evt.getTime(), lockAxis);
        }
        incr.mult(camera.getUp(), camera.getUp());
        incr.mult(camera.getLeft(), camera.getLeft());
        incr.mult(camera.getDirection(), camera.getDirection());
        camera.normalize();
        camera.update();
    }
}
