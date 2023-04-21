package org.modss.facilitator.util.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * A panel to display a rectangle drawn over another component.
 * Pressing Escape while dragging stops the drag.
 * The rectangle will be drawn in the panel's foreground colour.
 *
 * @author John Farrell
 */
public final class RubberRectPanel extends JPanel {

    /** Minimum side of rectangle worth reporting **/
    public static final int MIN_SIDE = 10;

    /** Listeners to changes in rectangle **/
    private Vector listeners;

    /** Point dragging started at **/
    private Point origin;

    /** Point dragging is currently at **/
    private Point current;

    /** The layer underneath this panel for passing events through to. **/
    private Container cont;

    /** Thing that listens to mouse events and tells us what to do about them. */
    private MouseHandler mouseHandler;

    /** Create a new RubberRectPanel. **/
    public RubberRectPanel() {
        listeners = new Vector();
        origin = null;
        current = null;
        setOpaque(false);
        registerKeyboardAction(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                cancel();
            }
        }, "cancel", KeyStroke.getKeyStroke(27, 0), WHEN_IN_FOCUSED_WINDOW);
    }

    /** Cancel the drag in progress **/
    public void cancel() {
        origin = null;
        current = null;
        fireCancelled();
        repaint();
    }

    /** Set the container underneath this layer **/
    public void setLowerLayer(Container cont) {
        this.cont = cont;
    }

    /** This component has been added to the screen. */
    public void addNotify() {
        super.addNotify();
        mouseHandler = new MouseHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    /** This component has been removed from the screen. */
    public void removeNotify() {
        removeMouseListener(mouseHandler);
        removeMouseMotionListener(mouseHandler);
        mouseHandler = null;
        super.removeNotify();
    }

    /** Draw this panel. */
    public void paintComponent(Graphics g) {
        if ((origin != null) && (current != null)) {
            Rectangle r = pointsToRect(origin, current);
            if (tooSmall(r)) return;
            g.setColor(getForeground());
            g.drawRect(r.x, r.y, r.width, r.height);
        }
    }

    /** Add a listener for rectangle changes */
    public void addRectangleListener(RectangleListener listener) {
        listeners.addElement(listener);
    }

    /** Remove a listener. */
    public void removeRectangleListener(RectangleListener listener) {
        listeners.removeElement(listener);
    }

    /** Fire creation of a new rectangle. */
    private void fire(Rectangle rect, boolean complete) {
        synchronized (listeners) {
            for (int i = 0; i < listeners.size(); i++) {
                RectangleListener listener = (RectangleListener) listeners.elementAt(i);
                listener.rectangleChanged(rect, complete);
            }
        }
    }

    /** Fire cancellation of a rectangle */
    private void fireCancelled() {
        synchronized (listeners) {
            for (int i = 0; i < listeners.size(); i++) {
                RectangleListener listener = (RectangleListener) listeners.elementAt(i);
                listener.rectangleCancelled();
            }
        }
    }

    /** Create a rectangle from two points. */
    private Rectangle pointsToRect(Point origin, Point current) {
        int w = Math.max(Math.abs(origin.x - current.x), Math.abs(origin.y - current.y));
        int x = (origin.x < current.x) ? origin.x : (origin.x - w);
        int y = (origin.y < current.y) ? origin.y : (origin.y - w);
        return new Rectangle(x, y, w, w);
    }

    /** Is this rectangle (so far) smaller than we are allowed to create? */
    private boolean tooSmall(Rectangle r) {
        return ((r.width < MIN_SIDE) && (r.height < MIN_SIDE));
    }

    private class MouseHandler implements MouseListener, MouseMotionListener {

        /** Last component the mouse went over, refer to redispatchEvent **/
        private Component lastComponent = null;

        /** Mouse was clicked - we can't be dragging if it was clicked */
        public void mouseClicked(MouseEvent evt) {
            cancel();
            redispatchEvent(evt);
        }

        /** Mouse entered this component. */
        public void mouseEntered(MouseEvent evt) {
            redispatchEvent(evt);
        }

        /** Mouse exited this component. */
        public void mouseExited(MouseEvent evt) {
            redispatchEvent(evt);
        }

        /** Mouse was pressed - potentially starting a drag */
        public void mousePressed(MouseEvent evt) {
            requestFocus();
            origin = evt.getPoint();
            redispatchEvent(evt);
        }

        /** Mouse was released - potentially finishing a drag */
        public void mouseReleased(MouseEvent evt) {
            if (origin == null) return;
            current = evt.getPoint();
            Rectangle r = pointsToRect(origin, current);
            if (!tooSmall(r)) {
                fire(r, true);
                repaint();
            }
            redispatchEvent(evt);
        }

        /** Mouse moved with button up - not dragging */
        public void mouseMoved(MouseEvent evt) {
            redispatchEvent(evt);
        }

        /** Mouse moved with button down - definitely dragging. */
        public void mouseDragged(MouseEvent evt) {
            if (origin == null) return;
            current = evt.getPoint();
            Rectangle r = pointsToRect(origin, current);
            if (!tooSmall(r)) {
                fire(r, false);
                repaint();
            }
        }

        /**
         * Maybe the dirtiest hack ever on the planet Earth. We want to forward any
         * mouse events to the components underneath. That's easy enough for most events,
         * particularly clicks, moves and drags. However tooltips and rollover work
         * from enter and exit events, and no enter or exit events are being generated
         * for the components underneath. So, whenever a mouse move happens, we keep
         * track of which component we were last over (lastComponent) and compare it to
         * the component we are currently over. If they differ, we make up enter and
         * exit events and send them to the appropriate components.
         */
        private void redispatchEvent(MouseEvent evt) {
            if (cont == null) return;
            Point evtPoint = evt.getPoint();
            Component comp = SwingUtilities.getDeepestComponentAt(cont, evtPoint.x, evtPoint.y);
            Point compPoint = SwingUtilities.convertPoint(RubberRectPanel.this, evtPoint.x, evtPoint.y, comp);
            if (comp == null) {
                lastComponent = null;
                return;
            }
            if (comp != lastComponent) {
                if (lastComponent != null) {
                    Point lastCompPoint = SwingUtilities.convertPoint(RubberRectPanel.this, evtPoint.x, evtPoint.y, lastComponent);
                    MouseEvent evtx = new MouseEvent(lastComponent, MouseEvent.MOUSE_EXITED, evt.getWhen(), evt.getModifiers(), lastCompPoint.x, lastCompPoint.y, evt.getClickCount(), evt.isPopupTrigger());
                    lastComponent.dispatchEvent(evtx);
                }
                if (comp != null) {
                    MouseEvent evtx = new MouseEvent(comp, MouseEvent.MOUSE_ENTERED, evt.getWhen(), evt.getModifiers(), compPoint.x, compPoint.y, evt.getClickCount(), evt.isPopupTrigger());
                    comp.dispatchEvent(evtx);
                }
            }
            lastComponent = comp;
            if ((evt.getID() == MouseEvent.MOUSE_ENTERED) || (evt.getID() == MouseEvent.MOUSE_EXITED)) return;
            MouseEvent evt2 = SwingUtilities.convertMouseEvent(RubberRectPanel.this, evt, comp);
            comp.dispatchEvent(evt2);
        }
    }
}
