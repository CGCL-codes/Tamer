package com.rlsoftwares.visual;

/**
 *
 * @author rodrigo
 */
public interface Dragable {

    public void mouseDraggedEvent(java.awt.event.MouseEvent evt, EventType type);

    public void mousePressedEvent(java.awt.event.MouseEvent evt, EventType type);

    public void mouseReleasedEvent(java.awt.event.MouseEvent evt, EventType type);

    public boolean dragging = false;
}
