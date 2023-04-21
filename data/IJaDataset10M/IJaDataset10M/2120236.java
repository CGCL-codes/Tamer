package net.infonode.gui.draggable;

import java.awt.event.*;

public class DraggableComponentEvent {

    public static final int TYPE_UNDEFINED = -1;

    public static final int TYPE_MOVED = 0;

    public static final int TYPE_PRESSED = 1;

    public static final int TYPE_RELEASED = 2;

    public static final int TYPE_ENABLED = 3;

    public static final int TYPE_DISABLED = 4;

    private DraggableComponent source;

    private int type = TYPE_UNDEFINED;

    private MouseEvent mouseEvent;

    public DraggableComponentEvent(DraggableComponent source) {
        this(source, null);
    }

    public DraggableComponentEvent(DraggableComponent source, MouseEvent mouseEvent) {
        this(source, TYPE_UNDEFINED, mouseEvent);
    }

    public DraggableComponentEvent(DraggableComponent source, int type) {
        this(source, type, null);
    }

    public DraggableComponentEvent(DraggableComponent source, int type, MouseEvent mouseEvent) {
        this.source = source;
        this.type = type;
        this.mouseEvent = mouseEvent;
    }

    public DraggableComponent getSource() {
        return source;
    }

    public int getType() {
        return type;
    }

    public MouseEvent getMouseEvent() {
        return mouseEvent;
    }
}
