package com.jme.input;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;

/**
 * <code>RelativeMouse</code> defines a mouse controller that only maintains
 * the relative change from one poll to the next. This does not maintain the
 * position of a mouse in a rendering window. This type of controller is
 * typically useful for a first person mouse look or similar.
 * 
 * @author Mark Powell
 * @version $Id: RelativeMouse.java 4666 2009-09-04 17:22:52Z skye.book $
 */
public class RelativeMouse extends Mouse {

    private static final long serialVersionUID = 1L;

    private InputAction updateAction = new InputAction() {

        public void performAction(InputActionEvent evt) {
            localTranslation.x = MouseInput.get().getXDelta() * _speed;
            localTranslation.y = MouseInput.get().getYDelta() * _speed;
            worldTranslation.set(localTranslation);
            hotSpotLocation.set(localTranslation).addLocal(hotSpotOffset);
        }
    };

    private InputHandler registeredInputHandler;

    protected float _speed = 1.0f;

    /**
     * Constructor creates a new <code>RelativeMouse</code> object.
     * 
     * @param name
     *            the name of the scene element. This is required for
     *            identification and comparison purposes.
     */
    public RelativeMouse(String name) {
        super(name);
    }

    public void registerWithInputHandler(InputHandler inputHandler) {
        if (registeredInputHandler != null) {
            registeredInputHandler.removeAction(updateAction);
        }
        registeredInputHandler = inputHandler;
        if (inputHandler != null) {
            inputHandler.addAction(updateAction, InputHandler.DEVICE_MOUSE, InputHandler.BUTTON_NONE, 0, true);
        }
    }

    /**
     * Sets the speed multiplier for updating the cursor position
     *
     * @param speed
     */
    public void setSpeed(float speed) {
        _speed = speed;
    }
}
