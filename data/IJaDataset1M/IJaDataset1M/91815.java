package za.um.td.input.mouse.action;

import com.jme.input.Mouse;
import com.jme.input.MouseInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyBackwardAction;
import com.jme.input.action.KeyForwardAction;
import com.jme.input.action.KeyStrafeDownAction;
import com.jme.input.action.KeyStrafeLeftAction;
import com.jme.input.action.KeyStrafeRightAction;
import com.jme.input.action.KeyStrafeUpAction;
import com.jme.input.action.MouseInputAction;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.system.DisplaySystem;

public class MouseRtsAction extends MouseInputAction {

    KeyStrafeLeftAction strafeLeft;

    KeyStrafeRightAction strafeRight;

    KeyStrafeUpAction strafeUp;

    KeyStrafeDownAction strafeDown;

    KeyForwardAction keyForwardAndBack;

    private InputActionEvent event;

    public MouseRtsAction(Mouse mouse, Camera camera, float speed) {
        this.mouse = mouse;
        this.speed = speed;
        strafeDown = new KeyStrafeDownAction(camera, speed);
        strafeDown.setUpVector(new Vector3f(0, 0, -1));
        strafeUp = new KeyStrafeUpAction(camera, speed);
        strafeUp.setUpVector(new Vector3f(0, 0, -1));
        strafeLeft = new KeyStrafeLeftAction(camera, speed);
        strafeRight = new KeyStrafeRightAction(camera, speed);
        keyForwardAndBack = new KeyForwardAction(camera, speed);
        event = new InputActionEvent();
    }

    @Override
    public void performAction(InputActionEvent evt) {
        float time = evt.getTime() * speed;
        if (mouse.getHotSpotPosition().x < 1.0f) {
            event.setTime(time);
            strafeLeft.performAction(event);
        }
        if (mouse.getHotSpotPosition().y < 1.0f) {
            event.setTime(time);
            strafeDown.performAction(event);
        }
        if (mouse.getHotSpotPosition().x > DisplaySystem.getDisplaySystem().getWidth() - 2.0f) {
            event.setTime(time);
            strafeRight.performAction(event);
        }
        if (mouse.getHotSpotPosition().y > DisplaySystem.getDisplaySystem().getHeight() - 2.0f) {
            event.setTime(time);
            strafeUp.performAction(event);
        }
        if (MouseInput.get().getWheelDelta() != 0) {
            event.setTime(time * MouseInput.get().getWheelDelta() * 0.05f);
            keyForwardAndBack.performAction(event);
        }
    }
}
