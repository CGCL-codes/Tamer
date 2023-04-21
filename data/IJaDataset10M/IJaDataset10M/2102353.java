package com.jme.input;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;

/**
 * <code>AbsoluteMouse</code> defines a mouse object that maintains a position
 * within the window. Each call to update adjusts the current position by the
 * change in position since the previous update. The mouse is forced to be
 * contained within the values provided during construction (typically these
 * correspond to the width and height of the window).
 *
 * @author Mark Powell
 * @author Gregg Patton
 * @version $Id: AbsoluteMouse.java 4666 2009-09-04 17:22:52Z skye.book $
 */
public class AbsoluteMouse extends Mouse {

    private static final long serialVersionUID = 1L;

    private boolean usingDelta = false;

    private int limitHeight, limitWidth;

    /**
     * @return true if mouse position delta are used to compute the absolute position, false if the absolute
     * mouse coordinates are used directly
     */
    public boolean isUsingDelta() {
        return usingDelta;
    }

    /**
     * @param usingDelta true to compute the absolute position from mouse position delta, false to use the absolute
     * mouse coordinates directly
     */
    public void setUsingDelta(boolean usingDelta) {
        this.usingDelta = usingDelta;
    }

    private InputAction xUpdateAction = new InputAction() {

        public void performAction(InputActionEvent evt) {
            if (isUsingDelta()) {
                localTranslation.x += evt.getTriggerDelta() * limitWidth * speed;
            } else {
                localTranslation.x = evt.getTriggerPosition() * limitWidth * speed - hotSpotOffset.x;
            }
            if (localTranslation.x + hotSpotOffset.x < 0) {
                localTranslation.x = -hotSpotOffset.x;
            } else if (localTranslation.x + hotSpotOffset.x > limitWidth) {
                localTranslation.x = width - hotSpotOffset.x;
            }
            worldTranslation.x = localTranslation.x;
            hotSpotLocation.x = localTranslation.x + hotSpotOffset.x;
        }
    };

    private InputAction yUpdateAction = new InputAction() {

        public void performAction(InputActionEvent evt) {
            if (isUsingDelta()) {
                localTranslation.y += evt.getTriggerDelta() * limitHeight * speed;
            } else {
                localTranslation.y = evt.getTriggerPosition() * limitHeight * speed - hotSpotOffset.y;
            }
            if (localTranslation.y + hotSpotOffset.y < 0) {
                localTranslation.y = 0 - hotSpotOffset.y;
            } else if (localTranslation.y + hotSpotOffset.y > limitHeight) {
                localTranslation.y = height - hotSpotOffset.y;
            }
            worldTranslation.y = localTranslation.y;
            hotSpotLocation.y = localTranslation.y + hotSpotOffset.y;
        }
    };

    private InputHandler registeredInputHandler;

    /**
     * Constructor instantiates a new <code>AbsoluteMouse</code> object. The
     * limits of the mouse movements are provided.
     *
     * @param name   the name of the scene element. This is required for
     *               identification and comparison purposes.
     * @param limitWidth  the width of the mouse's limit.
     * @param limitHeight the height of the mouse's limit.
     */
    public AbsoluteMouse(String name, int limitWidth, int limitHeight) {
        super(name);
        this.limitWidth = limitWidth;
        this.limitHeight = limitHeight;
        getXUpdateAction().setSpeed(1);
        getYUpdateAction().setSpeed(1);
    }

    /**
     * set the mouse's limit.
     *
     * @param limitWidth  the width of the mouse's limit.
     * @param limitHeight the height of the mouse's limit.
     */
    public void setLimit(int limitWidth, int limitHeight) {
        this.limitWidth = limitWidth;
        this.limitHeight = limitHeight;
    }

    public void setSpeed(float speed) {
        getXUpdateAction().setSpeed(speed);
        getYUpdateAction().setSpeed(speed);
    }

    /**
     * Registers the xUpdateAction and the yUpdateAction with axis 0 and 1 of the mouse.
     * Note: you can register the actions with other devices, too, instead of calling this method.
     * @param inputHandler handler to register with (null to unregister)
     * @see #getXUpdateAction()
     * @see #getYUpdateAction()
     */
    public void registerWithInputHandler(InputHandler inputHandler) {
        if (registeredInputHandler != null) {
            registeredInputHandler.removeAction(getXUpdateAction());
            registeredInputHandler.removeAction(getYUpdateAction());
        }
        registeredInputHandler = inputHandler;
        if (inputHandler != null) {
            inputHandler.addAction(getXUpdateAction(), InputHandler.DEVICE_MOUSE, InputHandler.BUTTON_NONE, 0, false);
            inputHandler.addAction(getYUpdateAction(), InputHandler.DEVICE_MOUSE, InputHandler.BUTTON_NONE, 1, false);
        }
    }

    public InputAction getXUpdateAction() {
        return xUpdateAction;
    }

    public InputAction getYUpdateAction() {
        return yUpdateAction;
    }
}
