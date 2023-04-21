package com.jme.input.action;

import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

/**
 * <code>KeyNodeRotateRightAction</code> rotates a node to the right. The axis
 * of rotation is dependant on the setting of the lock axis. If no lock axis is
 * set, the node rotates about it's y-axis. This will allow the node to roll.
 * However, to prevent rolling, setting the lock axis to the world's y-axis (or
 * any desired axis for that matter), will cause the node to rotate about the
 * world. The locking of the axis is particularly useful for control schemes
 * similar to first person shooters.
 * 
 * @author Mark Powell
 * @version $Id: KeyNodeRotateRightAction.java,v 1.12 2004/08/22 02:00:34 cep21
 *          Exp $
 */
public class KeyNodeRotateRightAction extends KeyInputAction {

    private Spatial node;

    private Vector3f lockAxis;

    private static final Vector3f tempVa = new Vector3f();

    private static final Matrix3f incr = new Matrix3f();

    private static final Matrix3f tempMa = new Matrix3f();

    private static final Matrix3f tempMb = new Matrix3f();

    /**
     * Constructor instantiates a new <code>KeyNodeRotateRightAction</code>
     * object using the node and speed parameters for it's attributes.
     * 
     * @param node
     *            the node that will be affected by this action.
     * @param speed
     *            the speed at which the node can move.
     */
    public KeyNodeRotateRightAction(Spatial node, float speed) {
        this.node = node;
        this.speed = speed;
    }

    /**
     * 
     * <code>setLockAxis</code> allows a certain axis to be locked, meaning
     * the camera will always be within the plane of the locked axis. For
     * example, if the node is a first person camera, the user might lock the
     * node's up vector. This will keep the node vertical with the ground.
     * 
     * @param lockAxis
     *            the axis to lock - should be unit length (normalized).
     */
    public void setLockAxis(Vector3f lockAxis) {
        this.lockAxis = lockAxis;
    }

    /**
     * <code>performAction</code> rotates the camera about it's up vector or
     * lock axis at a speed of movement speed * time. Where time is the time
     * between frames and 1 corresponds to 1 second.
     * 
     * @see com.jme.input.action.KeyInputAction#performAction(InputActionEvent)
     */
    public void performAction(InputActionEvent evt) {
        incr.loadIdentity();
        if (lockAxis == null) {
            node.getLocalRotation().getRotationColumn(1, tempVa);
            tempVa.normalizeLocal();
            incr.fromAngleNormalAxis(-speed * evt.getTime(), tempVa);
        } else {
            incr.fromAngleNormalAxis(-speed * evt.getTime(), lockAxis);
        }
        node.getLocalRotation().fromRotationMatrix(incr.mult(node.getLocalRotation().toRotationMatrix(tempMa), tempMb));
        node.getLocalRotation().normalize();
    }
}
