package net.sourceforge.code2uml.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * This class adds support for a simple dragging of any graphics component that
 * can have MouseListener and MouseMotionListener added to it. It allows to drag
 * that component only inside its parent. To use it for Component c call: <br/>
 * <code>c.addMouseListener(new MouseDragListener(c));</code><br/>
 * Do not call: <br/>
 * <code>c.addMouseMotionListener(new MouseDragListener(c));</code><br/>
 * It will happen automatically when needed and calling it explicitly can 
 * cause MouseDragListener to work improperly.
 *
 * @author Mateusz Wenus
 */
public class MouseDragListener implements MouseListener, MouseMotionListener {

    private Component c;

    private int clickX, clickY;

    /** 
     * Creates a new instance of MouseDragListener.
     *
     * @param c Component to which dragging support will be added
     */
    public MouseDragListener(Component c) {
        this.c = c;
    }

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     *
     * @param e event object
     */
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Invoked when a mouse button is pressed on a component and then 
     * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be 
     * delivered to the component where the drag originated until the 
     * mouse button is released (regardless of whether the mouse position 
     * is within the bounds of the component).
     * <p> 
     * Due to platform-dependent Drag&Drop implementations, 
     * <code>MOUSE_DRAGGED</code> events may not be delivered during a native 
     * Drag&Drop operation.
     *
     * @param e event object
     */
    public void mouseDragged(MouseEvent e) {
        if (!c.isEnabled()) return;
        int oldX = c.getX();
        int oldY = c.getY();
        c.setLocation(Math.max(0, Math.min(c.getParent().getWidth() - c.getWidth(), c.getX() + e.getX() - clickX)), Math.max(0, Math.min(c.getParent().getHeight() - c.getHeight(), c.getY() + e.getY() - clickY)));
        c.getParent().setComponentZOrder(c, 0);
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e event object
     */
    public void mouseReleased(MouseEvent e) {
        c.removeMouseMotionListener(this);
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e event object
     */
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == e.BUTTON1 && c.isEnabled()) {
            clickX = e.getX();
            clickY = e.getY();
            c.addMouseMotionListener(this);
        }
    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e event object
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e event object
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e event object
     */
    public void mouseClicked(MouseEvent e) {
    }
}
