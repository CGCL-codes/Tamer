package org.skycastle.opengl.navigationgestures;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

/**
 * Turn camera using right mouse button drag (change yaw and pitch, pitch locked to avoid rolling to back,
 * there could be a maximum angle to avoid getting lost looking at the sky) (cursor disappears while mouse pressed,
 * relative movement is measured. Similar to first person view games). (A right mouse button click without drag will
 * typically show a context menu for the feature under the mouse).
 *
 * @author Hans H�ggstr�m
 */
public final class RotateGesture extends AbstractDragGesture {

    private Quaternion myRotation = new Quaternion(0, 0, 0, 1);

    private Quaternion myDirection = new Quaternion(0, 0, 0, 1);

    private static final Vector3f Z_AXIS = new Vector3f(0, 0, 1);

    private static final float DEFAULT_ROTATION_SENSITIVITY = 0.01f;

    public RotateGesture() {
        super(DEFAULT_ROTATION_SENSITIVITY, MouseEvent.BUTTON1, InputEvent.BUTTON1_DOWN_MASK);
    }

    protected void applyDragGesture(final Camera camera, final float deltaX, final float deltaY) {
        final Vector3f left = camera.getLeft();
        final Vector3f up = camera.getUp();
        final Vector3f forward = camera.getDirection();
        myDirection.fromAxes(left, up, forward);
        myRotation.fromAngleNormalAxis(deltaY, left);
        myRotation.mult(myDirection, myDirection);
        myRotation.fromAngleNormalAxis(-deltaX, Z_AXIS);
        myRotation.mult(myDirection, myDirection);
        camera.setAxes(myDirection);
    }
}
