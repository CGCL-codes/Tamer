package org.jcrpg.threed.input.action;

import org.jcrpg.apps.Jcrpg;
import org.jcrpg.threed.J3DCore;
import org.jcrpg.threed.input.ClassicKeyboardLookHandler;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

public class CKeyRotateRightAction extends CKeyAction {

    public CKeyRotateRightAction(ClassicKeyboardLookHandler handler, Camera camera, float speed) {
        super(handler, camera);
        this.speed = speed;
    }

    public void setLockAxis(Vector3f lockAxis) {
    }

    /**
     * <code>performAction</code> rotates the camera a certain angle.
     * 
     * @see com.jme.input.action.KeyInputAction#performAction(InputActionEvent)
     */
    public void performAction(InputActionEvent evt) {
        if (!performActionCheck(evt)) {
            if (J3DCore.LOGGING()) Jcrpg.LOGGER.finest("locked...");
            return;
        }
        handler.lockHandling();
        if (handler.lookLeftRightPercent < 0) {
            if (handler.sEngine.optimizeAngle) handler.sEngine.renderToViewPort(J3DCore.ROTATE_VIEW_ANGLE + 0.6f);
        } else {
            if (handler.sEngine.optimizeAngle) handler.sEngine.renderToViewPort(J3DCore.ROTATE_VIEW_ANGLE);
        }
        Vector3f from = J3DCore.turningDirectionsUnit[handler.core.gameState.getCurrentRenderPositions().viewDirection];
        Vector3f fromPos = J3DCore.getInstance().getCurrentLocation();
        handler.core.turnRight();
        Vector3f toReach = J3DCore.turningDirectionsUnit[handler.core.gameState.getCurrentRenderPositions().viewDirection];
        Vector3f toPos = J3DCore.getInstance().getCurrentLocation();
        float steps = J3DCore.MOVE_STEPS;
        turnDirectionAndMove(steps, from, toReach, fromPos, toPos, false);
        handler.lookLeftRightPercent = 0;
        handler.lookUpDownPercent = 0;
        setLookVertical();
        camera.update();
        handler.unlockHandling(true);
    }
}
