package client.game.input;

import java.util.ArrayList;
import client.game.hud.HudManager;
import client.game.state.PlayerStatesMachine;
import client.game.state.U3dState;
import client.game.state.WorldStatesMachine;
import client.manager.CollisionManager;
import com.jme.input.ChaseCamera;
import com.jme.input.KeyBindingManager;
import com.jme.input.MouseInput;
import com.jme.input.RelativeMouse;
import com.jme.input.action.InputActionEvent;
import com.jme.input.thirdperson.ThirdPersonMouseLook;
import com.jme.intersection.PickData;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

/**
 * En esta clase se setean las posiciones de la c�mara que sigue al personaje
 * que recorre el juego. Es utilizada en U3DChaseCamera
 * 
 * @author kike
 * 
 */
public class U3DThirdPersonMouseLook extends ThirdPersonMouseLook {

    private Vector3f u3dInitialSphereCoords;

    private boolean resetPosition;

    public U3DThirdPersonMouseLook(RelativeMouse mouse, ChaseCamera camera, Spatial target) {
        super(mouse, camera, target);
        u3dInitialSphereCoords = null;
        setSpeed(0.3f);
        resetPosition = false;
    }

    private boolean cursor = false;

    public void performAction(InputActionEvent event) {
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("cursor", false)) {
            MouseInput.get().setCursorVisible(!MouseInput.get().isCursorVisible());
            cursor = MouseInput.get().isCursorVisible();
        } else if (!cursor) MouseInput.get().setCursorVisible(HudManager.getInstance().isPopUpActive());
        if (!MouseInput.get().isCursorVisible()) {
            rollInForced();
            mouseYSpeed = 1;
            rollInSpeed = 2;
            if (!isCollision()) super.performAction(event);
        }
    }

    public void setU3dInitialSphereCoords(Vector3f u3dInitialSphereCoords) {
        this.u3dInitialSphereCoords = u3dInitialSphereCoords;
    }

    private boolean left = false;

    public void rollInForced() {
        if (isCollision()) {
            resetPosition = true;
            if (camera.getIdealSphereCoords().getX() > minRollOut) camera.getIdealSphereCoords().setX(camera.getIdealSphereCoords().getX() - 2); else if (camera.getIdealSphereCoords().getZ() < maxAscent) camera.getIdealSphereCoords().setZ(camera.getIdealSphereCoords().getZ() + 0.2f); else if (left) camera.getIdealSphereCoords().setY(camera.getIdealSphereCoords().getY() - 0.2f); else camera.getIdealSphereCoords().setY(camera.getIdealSphereCoords().getY() + 0.2f);
            camera.getCamera().onFrameChange();
        } else {
            left = MouseInput.get().getXDelta() < 0;
            if (resetPosition && u3dInitialSphereCoords != null) {
                if (PlayerStatesMachine.getInstace().getPlayer().getState().getState().equals("MOVING")) {
                    if (camera.getIdealSphereCoords().getZ() > u3dInitialSphereCoords.getZ()) camera.getIdealSphereCoords().setZ(camera.getIdealSphereCoords().getZ() - 0.01f); else if (camera.getIdealSphereCoords().getX() < u3dInitialSphereCoords.getX()) camera.getIdealSphereCoords().setX(camera.getIdealSphereCoords().getX() + 0.1f); else resetPosition = false;
                    camera.getCamera().onFrameChange();
                }
            }
        }
    }

    private boolean isCollision() {
        U3dState state = WorldStatesMachine.getInstace().getActiveState();
        Vector3f positionCamera = getChaseCamera().getCamera().getLocation().clone();
        Vector3f positionTipitoHead = target.getLocalTranslation().add(new Vector3f(0, 15, 0));
        ArrayList<PickData> picks = CollisionManager.getInstace().getIntersectionsBetween(positionTipitoHead, positionCamera, state.getNodeForCollision());
        if (picks.size() > 0) return true;
        return false;
    }
}
