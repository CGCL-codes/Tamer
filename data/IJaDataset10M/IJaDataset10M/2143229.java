package org.das2.event;

/**
 *
 * @author  eew
 */
public class MouseRangeSelectionEvent extends MouseDragEvent {

    private int min, max;

    private boolean isModified;

    /** Creates a new instance of DasDevicePositionEvent */
    public MouseRangeSelectionEvent(Object source, int min, int max, boolean isModified) {
        super(source);
        if (min > max) {
            int t = min;
            min = max;
            max = t;
        }
        this.min = min;
        this.max = max;
        this.isModified = isModified;
    }

    public int getMinimum() {
        return min;
    }

    public int getMaximum() {
        return max;
    }

    public boolean isModified() {
        return isModified;
    }
}
