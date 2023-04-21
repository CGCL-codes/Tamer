package com.googlecode.sarasvati.editor.action;

import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;

public abstract class ToggledWidgetActionDecorator implements WidgetAction {

    protected WidgetAction action;

    public abstract boolean isEnabled();

    public ToggledWidgetActionDecorator(final WidgetAction action) {
        this.action = action;
    }

    public WidgetAction getAction() {
        return action;
    }

    public void setAction(final WidgetAction action) {
        this.action = action;
    }

    @Override
    public State dragEnter(final Widget widget, final WidgetDropTargetDragEvent event) {
        if (!isEnabled()) {
            return State.REJECTED;
        }
        return action.dragEnter(widget, event);
    }

    @Override
    public State dragExit(final Widget widget, final WidgetDropTargetEvent event) {
        if (!isEnabled()) {
            return State.REJECTED;
        }
        return action.dragExit(widget, event);
    }

    @Override
    public State dragOver(final Widget widget, final WidgetDropTargetDragEvent event) {
        if (!isEnabled()) {
            return State.REJECTED;
        }
        return action.dragOver(widget, event);
    }

    @Override
    public State drop(final Widget widget, final WidgetDropTargetDropEvent event) {
        if (!isEnabled()) {
            return State.REJECTED;
        }
        return action.drop(widget, event);
    }

    @Override
    public State dropActionChanged(final Widget widget, final WidgetDropTargetDragEvent event) {
        if (!isEnabled()) {
            return State.REJECTED;
        }
        return action.dropActionChanged(widget, event);
    }

    @Override
    public State focusGained(final Widget widget, final WidgetFocusEvent event) {
        if (!isEnabled()) {
            return State.REJECTED;
        }
        return action.focusGained(widget, event);
    }

    @Override
    public State focusLost(final Widget widget, final WidgetFocusEvent event) {
        if (!isEnabled()) {
            return State.REJECTED;
        }
        return action.focusLost(widget, event);
    }

    @Override
    public State keyPressed(final Widget widget, final WidgetKeyEvent event) {
        if (!isEnabled()) {
            return State.REJECTED;
        }
        return action.keyPressed(widget, event);
    }

    @Override
    public State keyReleased(final Widget widget, final WidgetKeyEvent event) {
        if (!isEnabled()) {
            return State.REJECTED;
        }
        return action.keyReleased(widget, event);
    }

    @Override
    public State keyTyped(final Widget widget, final WidgetKeyEvent event) {
        if (!isEnabled()) {
            return State.REJECTED;
        }
        return action.keyTyped(widget, event);
    }

    @Override
    public State mouseClicked(final Widget widget, final WidgetMouseEvent event) {
        if (!isEnabled()) {
            return State.REJECTED;
        }
        return action.mouseClicked(widget, event);
    }

    @Override
    public State mouseDragged(final Widget widget, final WidgetMouseEvent event) {
        if (!isEnabled()) {
            return State.REJECTED;
        }
        return action.mouseDragged(widget, event);
    }

    @Override
    public State mouseEntered(final Widget widget, final WidgetMouseEvent event) {
        if (!isEnabled()) {
            return State.REJECTED;
        }
        return action.mouseEntered(widget, event);
    }

    @Override
    public State mouseExited(final Widget widget, final WidgetMouseEvent event) {
        if (!isEnabled()) {
            return State.REJECTED;
        }
        return action.mouseExited(widget, event);
    }

    @Override
    public State mouseMoved(final Widget widget, final WidgetMouseEvent event) {
        if (!isEnabled()) {
            return State.REJECTED;
        }
        return action.mouseMoved(widget, event);
    }

    @Override
    public State mousePressed(final Widget widget, final WidgetMouseEvent event) {
        if (!isEnabled()) {
            return State.REJECTED;
        }
        return action.mousePressed(widget, event);
    }

    @Override
    public State mouseReleased(final Widget widget, final WidgetMouseEvent event) {
        if (!isEnabled()) {
            return State.REJECTED;
        }
        return action.mouseReleased(widget, event);
    }

    @Override
    public State mouseWheelMoved(final Widget widget, final WidgetMouseWheelEvent event) {
        if (!isEnabled()) {
            return State.REJECTED;
        }
        return action.mouseWheelMoved(widget, event);
    }
}
