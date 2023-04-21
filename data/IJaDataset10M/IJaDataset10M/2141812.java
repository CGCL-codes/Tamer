package com.tensegrity.palowebviewer.modules.widgets.client.dnd;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Timer;
import com.tensegrity.palowebviewer.modules.util.client.Mouse;
import com.tensegrity.palowebviewer.modules.widgets.client.factories.IWidgetFactory;

public class DnDEngine implements IDnDEngine {

    private static final int DRAG_START_TIME = 500;

    private static IDnDEngine instance;

    private IDnDContrloller controller = new DnDController();

    private Timer task = new Timer() {

        public void run() {
            state.startDrag();
        }
    };

    private final DragState state = new DragState();

    private final TargetCollection targets = new TargetCollection(state);

    private final DnDView view = new DnDView();

    private final EventPreview eventPreview = new DnDMouseListener(state);

    private final IDragStateListener stateListener = new IDragStateListener() {

        public void onCanDrop() {
        }

        public void onCanNotDrop() {
        }

        public void onPositionChanged() {
        }

        public void onStartDrag() {
            task.cancel();
            Mouse mouse = Mouse.getInstance();
            DOM.addEventPreview(eventPreview);
            state.setPosition(mouse.getX(), mouse.getY());
        }

        public void onStopDrag() {
            IDnDTarget target = state.getCurrentTarget();
            if (target != null) {
                target.onDrop(state);
            } else {
                target = state.getSourceTarget();
                target.cancelDrag(state);
            }
            DOM.removeEventPreview(eventPreview);
        }

        public void onTargetChanged(IDnDTarget oldTarget) {
        }
    };

    public static IDnDEngine getInstance() {
        return instance == null ? instance = new DnDEngine() : instance;
    }

    private DnDEngine() {
        view.setDnDState(state);
        state.addListener(stateListener);
    }

    public void setWidgetFactory(IWidgetFactory factory) {
    }

    public void startDrag(IDnDTarget source, Object o) {
        cancelDrag();
        if (source.canDrag(o)) {
            task.schedule(DRAG_START_TIME);
            state.setModelObject(o);
            state.setSource(source);
        }
    }

    public void cancelDrag(IDnDTarget source, Object o) {
        cancelDrag();
    }

    private void cancelDrag() {
        task.cancel();
        state.cancel();
    }

    public void registerTarget(IDnDTarget target) {
        targets.addTarget(target);
    }

    public IDnDContrloller getController() {
        return controller;
    }
}
