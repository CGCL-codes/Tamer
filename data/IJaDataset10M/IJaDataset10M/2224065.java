package org.jowidgets.common.widgets.controller;

public interface IMouseListener {

    void mousePressed(IMouseButtonEvent event);

    void mouseReleased(IMouseButtonEvent event);

    void mouseDoubleClicked(IMouseButtonEvent event);

    void mouseEnter(IMouseEvent event);

    void mouseExit(IMouseEvent event);
}
