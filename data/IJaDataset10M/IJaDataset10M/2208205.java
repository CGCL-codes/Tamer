package org.mtmi.gestures;

import org.eclipse.swt.widgets.Display;
import org.mtmi.tuio.Trace;
import org.mtmi.tuio.CursorPoint.States;
import org.mtmi.ui.controls.ITouchableControl;
import org.mtmi.ui.controls.listeners.DoubleTapListener;

public class DblClkGesture implements Gesture {

    public ITouchableControl target;

    @Override
    public GestureType getGestureType() {
        return GestureType.DOUBLE_TOUCH_GESTURE;
    }

    @Override
    public String getName() {
        return "DblClkGesture";
    }

    public static boolean detectFromTrace(Trace trace) {
        if (trace.getLast().getM_state() == States.UPDATED) {
            return false;
        }
        if (trace.count() == 3) {
            if ((trace.getPath().get(0).getM_state() == States.ADDED) && (trace.getPath().get(1).getM_state() == States.REMOVED) && (trace.getPath().get(2).getM_state() == States.ADDED)) {
                return true;
            }
        }
        return false;
    }

    public DblClkGesture(Trace trace) {
        this.target = trace.getInitialTarget();
    }

    @Override
    public void notifyTarget() {
        if (this.target instanceof DoubleTapListener) {
            final DoubleTapListener finalTarget = (DoubleTapListener) target;
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    finalTarget.doubleTap();
                }
            });
        }
    }
}
