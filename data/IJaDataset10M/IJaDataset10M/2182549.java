package com.jme.input.thirdperson;

import com.jme.input.ThirdPersonHandler;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;
import com.jme.math.Vector3f;

/**
 * 
 * <code>ThirdPersonRightAction</code>
 * 
 * @author Joshua Slack
 * @version $Revision: 4131 $
 */
public class ThirdPersonStrafeRightAction extends KeyInputAction {

    private Vector3f rot;

    private ThirdPersonHandler handler;

    /**
     * Constructor creates a new <code>ThirdPersonRightAction</code> object.
     * During construction, the character to direct and the speed at which to
     * move the character is set.
     * 
     * @param character
     *            the character that will be affected by this action.
     * @param speed
     *            the speed at which the camera can move.
     */
    public ThirdPersonStrafeRightAction(ThirdPersonHandler handler, float speed) {
        this.handler = handler;
        this.speed = speed;
        rot = new Vector3f();
    }

    /**
     * <code>performAction</code> moves the node towards the right direction
     * vector at a speed of movement speed * time. Where time is the time
     * between frames and 1 corresponds to 1 second.
     * 
     * @see com.jme.input.action.InputActionInterface#performAction(InputActionEvent)
     */
    public void performAction(InputActionEvent event) {
        if (handler.getPermitter() != null && !handler.getPermitter().canBeMoved()) return;
        handler.setStrafing(true);
        Vector3f loc = handler.getTarget().getLocalTranslation();
        if (!handler.isStrafeAlignTarget() && handler.isCameraAlignedMovement()) {
            rot.set(handler.getCamera().getLeft());
            rot.y = 0;
        } else {
            handler.getTarget().getLocalRotation().getRotationColumn(0, rot);
            rot.negateLocal();
        }
        rot.normalizeLocal();
        loc.subtractLocal(rot.multLocal((speed * event.getTime())));
    }
}
