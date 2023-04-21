package com.g2d.display.event;

import com.g2d.Version;

public class MouseMoveEvent extends MouseEvent {

    private static final long serialVersionUID = Version.VersionG2D;

    public final int mouseDownStartX, mouseDownStartY;

    public boolean is_click_resize = false;

    public MouseMoveEvent(java.awt.event.MouseEvent event, int startDragX, int startDragY) {
        super(event, EVENT_MOUSE_DRAGGED);
        mouseDownStartX = startDragX;
        mouseDownStartY = startDragY;
    }
}
