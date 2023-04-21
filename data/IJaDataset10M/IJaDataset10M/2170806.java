package com.tensegrity.palowebviewer.modules.widgets.client.dnd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.util.client.Assertions;
import com.tensegrity.palowebviewer.modules.widgets.client.util.GuiHelper;
import com.tensegrity.palowebviewer.modules.widgets.client.util.IRectangle;

public class TargetCollection {

    private final List targets = new ArrayList();

    private final DragState state;

    private final IDragStateListener stateListener = new IDragStateListener() {

        public void onCanDrop() {
        }

        public void onCanNotDrop() {
        }

        public void onPositionChanged() {
            IDnDTarget result = findTarget(state);
            state.setCurrentTarget(result);
        }

        public void onStartDrag() {
            IDnDTarget source = state.getSourceTarget();
            source.dragStart(state);
        }

        public void onStopDrag() {
        }

        public void onTargetChanged(IDnDTarget oldTarget) {
            if (oldTarget != null) {
                oldTarget.onDragExit(state);
            }
            IDnDTarget currentTarget = state.getCurrentTarget();
            if (currentTarget != null) {
                currentTarget.onDragEnter(state);
            }
        }
    };

    public TargetCollection(DragState state) {
        this.state = state;
        state.addListener(stateListener);
    }

    public void addTarget(IDnDTarget target) {
        Assertions.assertNotNull(target, "Target");
        targets.add(target);
    }

    private IDnDTarget findTarget(DragState state) {
        Widget widget = state.getWidget();
        IDnDTarget result = null;
        if (widget != null) {
            IRectangle rect = GuiHelper.getRectangle(widget);
            double feet = 0;
            for (Iterator it = targets.iterator(); it.hasNext() && (feet < 1); ) {
                IDnDTarget target = (IDnDTarget) it.next();
                if (!target.isVisible()) {
                    continue;
                }
                double intersection = GuiHelper.getIntersection(rect, target);
                if (intersection > feet) {
                    feet = intersection;
                    result = target;
                }
            }
        }
        return result;
    }
}
