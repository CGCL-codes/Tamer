package gov.nasa.worldwind.awt;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.view.ViewUtil;
import java.awt.*;
import java.awt.event.*;

/**
 * @author dcollins
 * @version $Id: BasicViewInputHandler.java 1 2011-07-16 23:22:47Z dcollins $
 */
public abstract class BasicViewInputHandler extends AbstractViewInputHandler {

    public class RotateActionListener extends ViewInputActionHandler {

        public boolean inputActionPerformed(AbstractViewInputHandler inputHandler, KeyEventState keys, String target, ViewInputAttributes.ActionAttributes viewAction) {
            java.util.List keyList = viewAction.getKeyActions();
            double headingInput = 0;
            double pitchInput = 0;
            for (Object k : keyList) {
                ViewInputAttributes.ActionAttributes.KeyAction keyAction = (ViewInputAttributes.ActionAttributes.KeyAction) k;
                if (keys.isKeyDown(keyAction.keyCode)) {
                    if (keyAction.direction == ViewInputAttributes.ActionAttributes.KeyAction.KA_DIR_X) {
                        headingInput += keyAction.sign;
                    } else {
                        pitchInput += keyAction.sign;
                    }
                }
            }
            if (headingInput == 0 && pitchInput == 0) {
                return false;
            }
            if (target == GENERATE_EVENTS) {
                ViewInputAttributes.DeviceAttributes deviceAttributes = inputHandler.getAttributes().getDeviceAttributes(ViewInputAttributes.DEVICE_KEYBOARD);
                onRotateView(headingInput, pitchInput, headingInput, pitchInput, deviceAttributes, viewAction);
            }
            return true;
        }
    }

    public class HorizontalTransActionListener extends ViewInputActionHandler {

        public boolean inputActionPerformed(AbstractViewInputHandler inputHandler, KeyEventState keys, String target, ViewInputAttributes.ActionAttributes viewAction) {
            double forwardInput = 0;
            double sideInput = 0;
            java.util.List keyList = viewAction.getKeyActions();
            for (Object k : keyList) {
                ViewInputAttributes.ActionAttributes.KeyAction keyAction = (ViewInputAttributes.ActionAttributes.KeyAction) k;
                if (keys.isKeyDown(keyAction.keyCode)) {
                    if (keyAction.direction == ViewInputAttributes.ActionAttributes.KeyAction.KA_DIR_X) {
                        sideInput += keyAction.sign;
                    } else {
                        forwardInput += keyAction.sign;
                    }
                }
            }
            if (forwardInput == 0 && sideInput == 0) {
                return false;
            }
            if (target == GENERATE_EVENTS) {
                inputHandler.onHorizontalTranslateRel(forwardInput, sideInput, forwardInput, sideInput, getAttributes().getDeviceAttributes(ViewInputAttributes.DEVICE_KEYBOARD), viewAction);
            }
            return true;
        }
    }

    public class VerticalTransActionListener extends ViewInputActionHandler {

        public boolean inputActionPerformed(AbstractViewInputHandler inputHandler, KeyEventState keys, String target, ViewInputAttributes.ActionAttributes viewAction) {
            double transInput = 0;
            java.util.List keyList = viewAction.getKeyActions();
            for (Object k : keyList) {
                ViewInputAttributes.ActionAttributes.KeyAction keyAction = (ViewInputAttributes.ActionAttributes.KeyAction) k;
                if (keys.isKeyDown(keyAction.keyCode)) transInput += keyAction.sign;
            }
            if (transInput == 0) {
                return false;
            }
            if (target == GENERATE_EVENTS) {
                ViewInputAttributes.DeviceAttributes deviceAttributes = getAttributes().getDeviceAttributes(ViewInputAttributes.DEVICE_KEYBOARD);
                onVerticalTranslate(transInput, transInput, deviceAttributes, viewAction);
            }
            return true;
        }
    }

    public class RotateMouseActionListener extends ViewInputActionHandler {

        public boolean inputActionPerformed(KeyEventState keys, String target, ViewInputAttributes.ActionAttributes viewAction) {
            boolean handleThisEvent = false;
            java.util.List buttonList = viewAction.getMouseActions();
            for (Object b : buttonList) {
                ViewInputAttributes.ActionAttributes.MouseAction buttonAction = (ViewInputAttributes.ActionAttributes.MouseAction) b;
                if ((keys.getMouseModifiersEx() & buttonAction.mouseButton) != 0) {
                    handleThisEvent = true;
                }
            }
            if (!handleThisEvent) {
                return false;
            }
            Point point = constrainToSourceBounds(getMousePoint(), getWorldWindow());
            Point lastPoint = constrainToSourceBounds(getLastMousePoint(), getWorldWindow());
            Point mouseDownPoint = constrainToSourceBounds(getMouseDownPoint(), getWorldWindow());
            if (point == null || lastPoint == null) {
                return false;
            }
            Point movement = ViewUtil.subtract(point, lastPoint);
            int headingInput = movement.x;
            int pitchInput = movement.y;
            Point totalMovement = ViewUtil.subtract(point, mouseDownPoint);
            int totalHeadingInput = totalMovement.x;
            int totalPitchInput = totalMovement.y;
            ViewInputAttributes.DeviceAttributes deviceAttributes = getAttributes().getDeviceAttributes(ViewInputAttributes.DEVICE_MOUSE);
            onRotateView(headingInput, pitchInput, totalHeadingInput, totalPitchInput, deviceAttributes, viewAction);
            return true;
        }

        public boolean inputActionPerformed(AbstractViewInputHandler inputHandler, java.awt.event.MouseEvent mouseEvent, ViewInputAttributes.ActionAttributes viewAction) {
            boolean handleThisEvent = false;
            java.util.List buttonList = viewAction.getMouseActions();
            for (Object b : buttonList) {
                ViewInputAttributes.ActionAttributes.MouseAction buttonAction = (ViewInputAttributes.ActionAttributes.MouseAction) b;
                if ((mouseEvent.getModifiersEx() & buttonAction.mouseButton) != 0) {
                    handleThisEvent = true;
                }
            }
            if (!handleThisEvent) {
                return false;
            }
            Point point = constrainToSourceBounds(getMousePoint(), getWorldWindow());
            Point lastPoint = constrainToSourceBounds(getLastMousePoint(), getWorldWindow());
            Point mouseDownPoint = constrainToSourceBounds(getMouseDownPoint(), getWorldWindow());
            if (point == null || lastPoint == null) {
                return false;
            }
            Point movement = ViewUtil.subtract(point, lastPoint);
            int headingInput = movement.x;
            int pitchInput = movement.y;
            if (mouseDownPoint == null) mouseDownPoint = lastPoint;
            Point totalMovement = ViewUtil.subtract(point, mouseDownPoint);
            int totalHeadingInput = totalMovement.x;
            int totalPitchInput = totalMovement.y;
            ViewInputAttributes.DeviceAttributes deviceAttributes = inputHandler.getAttributes().getDeviceAttributes(ViewInputAttributes.DEVICE_MOUSE);
            inputHandler.onRotateView(headingInput, pitchInput, totalHeadingInput, totalPitchInput, deviceAttributes, viewAction);
            return true;
        }
    }

    public class HorizTransMouseActionListener extends ViewInputActionHandler {

        public boolean inputActionPerformed(KeyEventState keys, String target, ViewInputAttributes.ActionAttributes viewAction) {
            boolean handleThisEvent = false;
            java.util.List buttonList = viewAction.getMouseActions();
            for (Object b : buttonList) {
                ViewInputAttributes.ActionAttributes.MouseAction buttonAction = (ViewInputAttributes.ActionAttributes.MouseAction) b;
                if ((keys.getMouseModifiersEx() & buttonAction.mouseButton) != 0) {
                    handleThisEvent = true;
                }
            }
            if (!handleThisEvent) {
                return false;
            }
            if (target == GENERATE_EVENTS) {
                Point point = constrainToSourceBounds(getMousePoint(), getWorldWindow());
                Point lastPoint = constrainToSourceBounds(getLastMousePoint(), getWorldWindow());
                Point mouseDownPoint = constrainToSourceBounds(getMouseDownPoint(), getWorldWindow());
                Point movement = ViewUtil.subtract(point, lastPoint);
                if (point == null || lastPoint == null) return false;
                int forwardInput = movement.y;
                int sideInput = -movement.x;
                Point totalMovement = ViewUtil.subtract(point, mouseDownPoint);
                int totalForward = totalMovement.y;
                int totalSide = -totalMovement.x;
                ViewInputAttributes.DeviceAttributes deviceAttributes = getAttributes().getDeviceAttributes(ViewInputAttributes.DEVICE_MOUSE);
                onHorizontalTranslateRel(forwardInput, sideInput, totalForward, totalSide, deviceAttributes, viewAction);
            }
            return (true);
        }

        public boolean inputActionPerformed(AbstractViewInputHandler inputHandler, java.awt.event.MouseEvent mouseEvent, ViewInputAttributes.ActionAttributes viewAction) {
            boolean handleThisEvent = false;
            java.util.List buttonList = viewAction.getMouseActions();
            for (Object b : buttonList) {
                ViewInputAttributes.ActionAttributes.MouseAction buttonAction = (ViewInputAttributes.ActionAttributes.MouseAction) b;
                if ((mouseEvent.getModifiersEx() & buttonAction.mouseButton) != 0) {
                    handleThisEvent = true;
                }
            }
            if (!handleThisEvent) {
                return false;
            }
            Point point = constrainToSourceBounds(getMousePoint(), getWorldWindow());
            Point lastPoint = constrainToSourceBounds(getLastMousePoint(), getWorldWindow());
            Point mouseDownPoint = constrainToSourceBounds(getMouseDownPoint(), getWorldWindow());
            if (point == null || lastPoint == null || mouseDownPoint == null) {
                return (false);
            }
            Point movement = ViewUtil.subtract(point, lastPoint);
            int forwardInput = movement.y;
            int sideInput = -movement.x;
            Point totalMovement = ViewUtil.subtract(point, mouseDownPoint);
            int totalForward = totalMovement.y;
            int totalSide = -totalMovement.x;
            ViewInputAttributes.DeviceAttributes deviceAttributes = inputHandler.getAttributes().getDeviceAttributes(ViewInputAttributes.DEVICE_MOUSE);
            onHorizontalTranslateRel(forwardInput, sideInput, totalForward, totalSide, deviceAttributes, viewAction);
            return (true);
        }
    }

    public class VertTransMouseActionListener extends ViewInputActionHandler {

        public boolean inputActionPerformed(AbstractViewInputHandler inputHandler, java.awt.event.MouseEvent mouseEvent, ViewInputAttributes.ActionAttributes viewAction) {
            boolean handleThisEvent = false;
            java.util.List buttonList = viewAction.getMouseActions();
            for (java.lang.Object b : buttonList) {
                ViewInputAttributes.ActionAttributes.MouseAction buttonAction = (ViewInputAttributes.ActionAttributes.MouseAction) b;
                if ((mouseEvent.getModifiersEx() & buttonAction.mouseButton) != 0) {
                    handleThisEvent = true;
                }
            }
            if (!handleThisEvent) {
                return false;
            }
            Point point = constrainToSourceBounds(inputHandler.getMousePoint(), inputHandler.getWorldWindow());
            Point lastPoint = constrainToSourceBounds(inputHandler.getLastMousePoint(), inputHandler.getWorldWindow());
            Point mouseDownPoint = constrainToSourceBounds(getMouseDownPoint(), getWorldWindow());
            if (point == null || lastPoint == null) {
                return false;
            }
            Point movement = ViewUtil.subtract(point, lastPoint);
            int translationInput = movement.y;
            Point totalMovement = ViewUtil.subtract(point, mouseDownPoint);
            int totalTranslationInput = totalMovement.y;
            ViewInputAttributes.DeviceAttributes deviceAttributes = inputHandler.getAttributes().getDeviceAttributes(ViewInputAttributes.DEVICE_MOUSE);
            onVerticalTranslate((double) translationInput, totalTranslationInput, deviceAttributes, viewAction);
            return true;
        }

        public boolean inputActionPerformed(KeyEventState keys, String target, ViewInputAttributes.ActionAttributes viewAction) {
            boolean handleThisEvent = false;
            java.util.List buttonList = viewAction.getMouseActions();
            for (java.lang.Object b : buttonList) {
                ViewInputAttributes.ActionAttributes.MouseAction buttonAction = (ViewInputAttributes.ActionAttributes.MouseAction) b;
                if ((keys.getMouseModifiersEx() & buttonAction.mouseButton) != 0) {
                    handleThisEvent = true;
                }
            }
            if (!handleThisEvent) {
                return false;
            }
            Point point = constrainToSourceBounds(getMousePoint(), getWorldWindow());
            Point lastPoint = constrainToSourceBounds(getLastMousePoint(), getWorldWindow());
            Point mouseDownPoint = constrainToSourceBounds(getMouseDownPoint(), getWorldWindow());
            if (point == null || lastPoint == null) {
                return false;
            }
            Point movement = ViewUtil.subtract(point, lastPoint);
            int translationInput = movement.y;
            Point totalMovement = ViewUtil.subtract(point, mouseDownPoint);
            int totalTranslationInput = totalMovement.y;
            ViewInputAttributes.DeviceAttributes deviceAttributes = getAttributes().getDeviceAttributes(ViewInputAttributes.DEVICE_MOUSE);
            onVerticalTranslate((double) translationInput, totalTranslationInput, deviceAttributes, viewAction);
            return true;
        }
    }

    public class MoveToMouseActionListener extends ViewInputActionHandler {

        public boolean inputActionPerformed(AbstractViewInputHandler inputHandler, java.awt.event.MouseEvent mouseEvent, ViewInputAttributes.ActionAttributes viewAction) {
            boolean handleThisEvent = false;
            java.util.List buttonList = viewAction.getMouseActions();
            for (Object b : buttonList) {
                ViewInputAttributes.ActionAttributes.MouseAction buttonAction = (ViewInputAttributes.ActionAttributes.MouseAction) b;
                if ((mouseEvent.getButton() == buttonAction.mouseButton)) {
                    handleThisEvent = true;
                }
            }
            if (!handleThisEvent) {
                return false;
            }
            Position pos = inputHandler.computeSelectedPosition();
            if (pos == null) {
                return false;
            }
            onMoveTo(pos, getAttributes().getDeviceAttributes(ViewInputAttributes.DEVICE_MOUSE), viewAction);
            return true;
        }
    }

    public class ResetHeadingActionListener extends ViewInputActionHandler {

        public boolean inputActionPerformed(AbstractViewInputHandler inputHandler, java.awt.event.KeyEvent event, ViewInputAttributes.ActionAttributes viewAction) {
            java.util.List keyList = viewAction.getKeyActions();
            for (Object k : keyList) {
                ViewInputAttributes.ActionAttributes.KeyAction keyAction = (ViewInputAttributes.ActionAttributes.KeyAction) k;
                if (event.getKeyCode() == keyAction.keyCode) {
                    onResetHeading(viewAction);
                    return true;
                }
            }
            return false;
        }
    }

    public class ResetHeadingPitchActionListener extends ViewInputActionHandler {

        public boolean inputActionPerformed(AbstractViewInputHandler inputHandler, java.awt.event.KeyEvent event, ViewInputAttributes.ActionAttributes viewAction) {
            java.util.List keyList = viewAction.getKeyActions();
            for (Object k : keyList) {
                ViewInputAttributes.ActionAttributes.KeyAction keyAction = (ViewInputAttributes.ActionAttributes.KeyAction) k;
                if (event.getKeyCode() == keyAction.keyCode) {
                    onResetHeadingPitchRoll(viewAction);
                    return true;
                }
            }
            return false;
        }
    }

    public class StopViewActionListener extends ViewInputActionHandler {

        public boolean inputActionPerformed(AbstractViewInputHandler inputHandler, java.awt.event.KeyEvent event, ViewInputAttributes.ActionAttributes viewAction) {
            java.util.List keyList = viewAction.getKeyActions();
            for (Object k : keyList) {
                ViewInputAttributes.ActionAttributes.KeyAction keyAction = (ViewInputAttributes.ActionAttributes.KeyAction) k;
                if (event.getKeyCode() == keyAction.keyCode) {
                    onStopView();
                    return true;
                }
            }
            return false;
        }
    }

    public class VertTransMouseWheelActionListener extends ViewInputActionHandler {

        public boolean inputActionPerformed(AbstractViewInputHandler inputHandler, java.awt.event.MouseWheelEvent mouseWheelEvent, ViewInputAttributes.ActionAttributes viewAction) {
            double zoomInput = mouseWheelEvent.getWheelRotation();
            ViewInputAttributes.DeviceAttributes deviceAttributes = inputHandler.getAttributes().getDeviceAttributes(ViewInputAttributes.DEVICE_MOUSE_WHEEL);
            onVerticalTranslate(zoomInput, zoomInput, deviceAttributes, viewAction);
            return true;
        }
    }

    public BasicViewInputHandler() {
        ViewInputAttributes.ActionAttributes actionAttrs;
        RotateActionListener rotateActionListener = new RotateActionListener();
        this.getAttributes().setActionListener(ViewInputAttributes.DEVICE_KEYBOARD, ViewInputAttributes.VIEW_ROTATE_KEYS, rotateActionListener);
        this.getAttributes().setActionListener(ViewInputAttributes.DEVICE_KEYBOARD, ViewInputAttributes.VIEW_ROTATE_SLOW, rotateActionListener);
        this.getAttributes().setActionListener(ViewInputAttributes.DEVICE_KEYBOARD, ViewInputAttributes.VIEW_ROTATE_KEYS_SHIFT, rotateActionListener);
        this.getAttributes().setActionListener(ViewInputAttributes.DEVICE_KEYBOARD, ViewInputAttributes.VIEW_ROTATE_KEYS_SHIFT_SLOW, rotateActionListener);
        VerticalTransActionListener vertActionsListener = new VerticalTransActionListener();
        actionAttrs = this.getAttributes().getActionMap(ViewInputAttributes.DEVICE_KEYBOARD).getActionAttributes(ViewInputAttributes.VIEW_VERTICAL_TRANS_KEYS_CTRL);
        actionAttrs.setActionListener(vertActionsListener);
        actionAttrs = this.getAttributes().getActionMap(ViewInputAttributes.DEVICE_KEYBOARD).getActionAttributes(ViewInputAttributes.VIEW_VERTICAL_TRANS_KEYS_SLOW_CTRL);
        actionAttrs.setActionListener(vertActionsListener);
        actionAttrs = this.getAttributes().getActionMap(ViewInputAttributes.DEVICE_KEYBOARD).getActionAttributes(ViewInputAttributes.VIEW_VERTICAL_TRANS_KEYS);
        actionAttrs.setActionListener(vertActionsListener);
        actionAttrs = this.getAttributes().getActionMap(ViewInputAttributes.DEVICE_KEYBOARD).getActionAttributes(ViewInputAttributes.VIEW_VERTICAL_TRANS_KEYS_SLOW);
        actionAttrs.setActionListener(vertActionsListener);
        HorizontalTransActionListener horizTransActionListener = new HorizontalTransActionListener();
        actionAttrs = this.getAttributes().getActionMap(ViewInputAttributes.DEVICE_KEYBOARD).getActionAttributes(ViewInputAttributes.VIEW_HORIZONTAL_TRANS_KEYS);
        actionAttrs.setActionListener(horizTransActionListener);
        actionAttrs = this.getAttributes().getActionMap(ViewInputAttributes.DEVICE_KEYBOARD).getActionAttributes(ViewInputAttributes.VIEW_HORIZONTAL_TRANSLATE_SLOW);
        actionAttrs.setActionListener(horizTransActionListener);
        RotateMouseActionListener rotateMouseListener = new RotateMouseActionListener();
        actionAttrs = this.getAttributes().getActionMap(ViewInputAttributes.DEVICE_MOUSE).getActionAttributes(ViewInputAttributes.VIEW_ROTATE);
        actionAttrs.setMouseActionListener(rotateMouseListener);
        actionAttrs = this.getAttributes().getActionMap(ViewInputAttributes.DEVICE_MOUSE).getActionAttributes(ViewInputAttributes.VIEW_ROTATE_SHIFT);
        actionAttrs.setMouseActionListener(rotateMouseListener);
        actionAttrs = this.getAttributes().getActionMap(ViewInputAttributes.DEVICE_MOUSE).getActionAttributes(ViewInputAttributes.VIEW_HORIZONTAL_TRANSLATE);
        actionAttrs.setMouseActionListener(new HorizTransMouseActionListener());
        VertTransMouseActionListener vertTransListener = new VertTransMouseActionListener();
        actionAttrs = this.getAttributes().getActionMap(ViewInputAttributes.DEVICE_MOUSE).getActionAttributes(ViewInputAttributes.VIEW_VERTICAL_TRANSLATE);
        actionAttrs.setMouseActionListener(vertTransListener);
        actionAttrs = this.getAttributes().getActionMap(ViewInputAttributes.DEVICE_MOUSE).getActionAttributes(ViewInputAttributes.VIEW_VERTICAL_TRANSLATE_CTRL);
        actionAttrs.setMouseActionListener(vertTransListener);
        actionAttrs = this.getAttributes().getActionMap(ViewInputAttributes.DEVICE_MOUSE).getActionAttributes(ViewInputAttributes.VIEW_MOVE_TO);
        actionAttrs.setMouseActionListener(new MoveToMouseActionListener());
        actionAttrs = this.getAttributes().getActionMap(ViewInputAttributes.DEVICE_KEYBOARD).getActionAttributes(ViewInputAttributes.VIEW_RESET_HEADING);
        actionAttrs.setActionListener(new ResetHeadingActionListener());
        actionAttrs = this.getAttributes().getActionMap(ViewInputAttributes.DEVICE_KEYBOARD).getActionAttributes(ViewInputAttributes.VIEW_RESET_HEADING_PITCH_ROLL);
        actionAttrs.setActionListener(new ResetHeadingPitchActionListener());
        actionAttrs = this.getAttributes().getActionMap(ViewInputAttributes.DEVICE_KEYBOARD).getActionAttributes(ViewInputAttributes.VIEW_STOP_VIEW);
        actionAttrs.setActionListener(new StopViewActionListener());
        actionAttrs = this.getAttributes().getActionMap(ViewInputAttributes.DEVICE_MOUSE_WHEEL).getActionAttributes(ViewInputAttributes.VIEW_VERTICAL_TRANSLATE);
        actionAttrs.setMouseActionListener(new VertTransMouseWheelActionListener());
    }

    public void apply() {
        super.apply();
    }

    protected void handleKeyPressed(KeyEvent e) {
        boolean eventHandled = false;
        Integer modifier = e.getModifiersEx();
        for (int i = 0; i < NUM_MODIFIERS; i++) {
            if ((((modifier & this.modifierList[i]) == this.modifierList[i]))) {
                ViewInputAttributes.ActionAttributesList actionList = (ViewInputAttributes.ActionAttributesList) keyModsActionMap.get(this.modifierList[i]);
                eventHandled = callActionListListeners(e, ViewInputAttributes.ActionAttributes.ActionTrigger.ON_PRESS, actionList);
            }
        }
        if (!eventHandled) {
            super.handleKeyPressed(e);
        }
    }

    protected void handleMouseClicked(MouseEvent e) {
        boolean eventHandled = false;
        Integer modifier = e.getModifiersEx();
        for (int i = 0; i < NUM_MODIFIERS; i++) {
            if ((((modifier & this.modifierList[i]) == this.modifierList[i]))) {
                ViewInputAttributes.ActionAttributesList actionList = (ViewInputAttributes.ActionAttributesList) mouseModsActionMap.get(this.modifierList[i]);
                eventHandled = callMouseActionListListeners(e, ViewInputAttributes.ActionAttributes.ActionTrigger.ON_PRESS, actionList);
            }
        }
        if (!eventHandled) {
            super.handleMouseClicked(e);
        }
    }

    protected void handleMouseWheelMoved(MouseWheelEvent e) {
        boolean eventHandled = false;
        Integer modifier = e.getModifiersEx();
        for (int i = 0; i < NUM_MODIFIERS; i++) {
            if ((((modifier & this.modifierList[i]) == this.modifierList[i]))) {
                ViewInputAttributes.ActionAttributesList actionList = (ViewInputAttributes.ActionAttributesList) mouseWheelModsActionMap.get(this.modifierList[i]);
                eventHandled = callMouseWheelActionListListeners(e, ViewInputAttributes.ActionAttributes.ActionTrigger.ON_DRAG, actionList);
            }
        }
        if (!eventHandled) {
            super.handleMouseWheelMoved(e);
        }
    }

    protected void handleMouseDragged(MouseEvent e) {
        int modifier = e.getModifiersEx();
        for (int i = 0; i < NUM_MODIFIERS; i++) {
            if ((((modifier & this.modifierList[i]) == this.modifierList[i]))) {
                ViewInputAttributes.ActionAttributesList actionList = (ViewInputAttributes.ActionAttributesList) mouseModsActionMap.get(this.modifierList[i]);
                if (callMouseActionListListeners(e, ViewInputAttributes.ActionAttributes.ActionTrigger.ON_DRAG, actionList)) {
                    return;
                }
            }
        }
    }

    protected boolean handlePerFrameKeyState(KeyEventState keys, String target) {
        if (keys.getNumKeysDown() == 0) {
            return false;
        }
        boolean isKeyEventTrigger = false;
        Integer modifier = keys.getModifiersEx();
        for (int i = 0; i < NUM_MODIFIERS; i++) {
            if (((modifier & this.modifierList[i]) == this.modifierList[i])) {
                ViewInputAttributes.ActionAttributesList actionList = (ViewInputAttributes.ActionAttributesList) keyModsActionMap.get(this.modifierList[i]);
                isKeyEventTrigger = callActionListListeners(keys, target, ViewInputAttributes.ActionAttributes.ActionTrigger.ON_KEY_DOWN, actionList);
                break;
            }
        }
        return isKeyEventTrigger;
    }

    protected boolean handlePerFrameMouseState(KeyEventState keys, String target) {
        boolean eventHandled = false;
        if (keys.getNumButtonsDown() == 0) {
            return false;
        }
        Integer modifier = keys.getModifiersEx();
        for (int i = 0; i < NUM_MODIFIERS; i++) {
            if (((modifier & this.modifierList[i]) == this.modifierList[i])) {
                ViewInputAttributes.ActionAttributesList actionList = (ViewInputAttributes.ActionAttributesList) mouseModsActionMap.get(this.modifierList[i]);
                if (callActionListListeners(keys, target, ViewInputAttributes.ActionAttributes.ActionTrigger.ON_KEY_DOWN, actionList)) {
                    return true;
                }
            }
        }
        return (eventHandled);
    }

    protected boolean callMouseActionListListeners(MouseEvent e, ViewInputAttributes.ActionAttributes.ActionTrigger trigger, ViewInputAttributes.ActionAttributesList actionList) {
        boolean eventHandled = false;
        if (actionList != null) {
            for (ViewInputAttributes.ActionAttributes actionAttr : actionList) {
                if (actionAttr.getMouseActionListener() == null || actionAttr.getActionTrigger() != trigger) {
                    continue;
                }
                if (actionAttr.getMouseActionListener().inputActionPerformed(this, e, actionAttr)) {
                    eventHandled = true;
                }
            }
        }
        return eventHandled;
    }

    protected boolean callMouseWheelActionListListeners(MouseWheelEvent e, ViewInputAttributes.ActionAttributes.ActionTrigger trigger, ViewInputAttributes.ActionAttributesList actionList) {
        boolean eventHandled = false;
        if (actionList != null) {
            for (ViewInputAttributes.ActionAttributes actionAttr : actionList) {
                if (actionAttr.getMouseActionListener() == null || actionAttr.getActionTrigger() != trigger) {
                    continue;
                }
                if (actionAttr.getMouseActionListener().inputActionPerformed(this, e, actionAttr)) {
                    eventHandled = true;
                }
            }
        }
        return eventHandled;
    }

    protected boolean callActionListListeners(KeyEvent e, ViewInputAttributes.ActionAttributes.ActionTrigger trigger, ViewInputAttributes.ActionAttributesList actionList) {
        boolean eventHandled = false;
        if (actionList != null) {
            for (ViewInputAttributes.ActionAttributes actionAttr : actionList) {
                if (actionAttr.getActionListener() == null || actionAttr.getActionTrigger() != trigger) {
                    continue;
                }
                if (actionAttr.getActionListener().inputActionPerformed(this, e, actionAttr)) {
                    eventHandled = true;
                }
            }
        }
        return eventHandled;
    }

    protected boolean callActionListListeners(KeyEventState keys, String target, ViewInputAttributes.ActionAttributes.ActionTrigger trigger, ViewInputAttributes.ActionAttributesList actionList) {
        boolean eventHandled = false;
        if (actionList != null) {
            for (ViewInputAttributes.ActionAttributes actionAttr : actionList) {
                if (actionAttr.getActionTrigger() != trigger) {
                    continue;
                }
                if (callActionListener(keys, target, actionAttr)) {
                    eventHandled = true;
                }
            }
        }
        return eventHandled;
    }

    protected void handlePropertyChange(java.beans.PropertyChangeEvent e) {
        super.handlePropertyChange(e);
        if (e.getPropertyName() == View.VIEW_STOPPED) {
            this.handleViewStopped();
        }
    }

    protected void handleViewStopped() {
    }

    protected void onHorizontalTranslateAbs(Angle latitudeChange, Angle longitudeChange, ViewInputAttributes.ActionAttributes actionAttribs) {
    }

    protected void onMoveTo(Position focalPosition, ViewInputAttributes.ActionAttributes actionAttribs) {
    }

    protected void onResetHeading(ViewInputAttributes.ActionAttributes actionAttribs) {
    }

    protected void onResetHeadingPitchRoll(ViewInputAttributes.ActionAttributes actionAttribs) {
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    protected void onRotateView(Angle headingChange, Angle pitchChange, double totalHeadingInput, double totalPitchInput, ViewInputAttributes.ActionAttributes actionAttribs) {
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    protected void onRotateView(double headingChange, double pitchChange, ViewInputAttributes.DeviceAttributes deviceAttributes, ViewInputAttributes.ActionAttributes actionAttributes) {
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    protected void onVerticalTranslate(double translateChange, ViewInputAttributes.ActionAttributes actionAttribs) {
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    protected void onVerticalTranslate(double translateChange, ViewInputAttributes.DeviceAttributes deviceAttributes, ViewInputAttributes.ActionAttributes actionAttributes) {
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    protected void onHorizontalTranslateRel(double forwardInput, double sideInput, double totalForwardInput, double totalSideInput, ViewInputAttributes.DeviceAttributes deviceAttributes, ViewInputAttributes.ActionAttributes actionAttributes) {
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    protected void onHorizontalTranslateRel(double forwardChange, double sideChange, ViewInputAttributes.ActionAttributes actionAttribs) {
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    protected void onMoveTo(Position focalPosition, ViewInputAttributes.DeviceAttributes deviceAttributes, ViewInputAttributes.ActionAttributes actionAttribs) {
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public void goTo(Position lookAtPos, double elevation) {
    }
}
