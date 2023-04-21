package org.das2.event;

/**
 *
 * @author  jbf
 */
public class MouseDragEvent extends DasMouseEvent {

    Gesture gesture;

    public MouseDragEvent(Object source) {
        super(source);
        gesture = Gesture.NONE;
    }

    public MouseDragEvent(Object source, Gesture gesture) {
        super(source);
        this.gesture = gesture;
    }

    public boolean isGesture() {
        return gesture != Gesture.NONE;
    }

    public Gesture getGesture() {
        return gesture;
    }

    public String toString() {
        return isGesture() ? gesture.toString() : "MouseDragEvent source: " + source;
    }
}
