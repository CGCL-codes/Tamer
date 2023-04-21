package org.designerator.common.widgets;

public class ImageClickedEvent extends java.util.EventObject {

    public int x, y;

    public ImageClickedEvent(Object source, int x, int y) {
        super(source);
        this.x = x;
        this.y = y;
    }
}
