package seco.gui;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolox.pswing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

/**
 * Event handler to send MousePressed, MouseReleased, MouseMoved, MouseClicked,
 * and MouseDragged events on Swing components within a PCanvas.
 * 
 * @author Ben Bederson
 * @author Lance Good
 * @author Sam Reid
 */
public class PSwingEventHandlerEx implements PInputEventListener {

    private PNode listenNode = null;

    private boolean active = false;

    private Component prevComponent = null;

    private Point2D prevPoint = null;

    private Point2D prevOff = null;

    private boolean recursing = false;

    private ButtonData leftButtonData = new ButtonData();

    private ButtonData rightButtonData = new ButtonData();

    private ButtonData middleButtonData = new ButtonData();

    private PSwingCanvas canvas;

    /**
     * Constructs a new PSwingEventHandler for the given canvas, and a node that
     * will recieve the mouse events.
     * 
     * @param canvas the canvas associated with this PSwingEventHandler.
     * @param node the node the mouse listeners will be attached to.
     */
    public PSwingEventHandlerEx(PSwingCanvas canvas, PNode node) {
        this.canvas = canvas;
        listenNode = node;
    }

    /**
     * Constructs a new PSwingEventHandler for the given canvas.
     */
    public PSwingEventHandlerEx(PSwingCanvas canvas) {
        this.canvas = canvas;
    }

    /**
     * Sets whether this event handler can fire events.
     * 
     * @param active
     */
    void setActive(boolean active) {
        if (this.active && !active) {
            if (listenNode != null) {
                this.active = false;
                listenNode.removeInputEventListener(this);
            }
        } else if (!this.active && active) {
            if (listenNode != null) {
                this.active = true;
                listenNode.addInputEventListener(this);
            }
        }
    }

    /**
     * Determines if this event handler is active.
     * 
     * @return True if active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Finds the component at the specified location (must be showing).
     * 
     * @param c
     * @param x
     * @param y
     * @return the component at the specified location.
     */
    private Component findShowingComponentAt(Component c, int x, int y) {
        if (!c.contains(x, y)) {
            return null;
        }
        if (c instanceof Container) {
            Container contain = ((Container) c);
            int ncomponents = contain.getComponentCount();
            Component component[] = contain.getComponents();
            for (int i = 0; i < ncomponents; i++) {
                Component comp = component[i];
                if (comp != null && comp.isVisible() && comp.isEnabled()) {
                    Point p = comp.getLocation();
                    if (comp instanceof Container) {
                        comp = findShowingComponentAt(comp, x - (int) p.getX(), y - (int) p.getY());
                    } else {
                        comp = comp.getComponentAt(x - (int) p.getX(), y - (int) p.getY());
                    }
                    if (comp != null && comp.isShowing()) {
                        return comp;
                    }
                }
            }
        }
        return c;
    }

    Point2D innerPt(PSwingNode swing, Point2D pt) {
        PiccoloCanvas canvas = swing.getCanvas();
        PSwingNode canv_node = GUIHelper.getPSwingNode(canvas);
        if (canv_node == null) return pt;
        cameraToLocal(canvas.getCamera(), pt, canv_node);
        return pt;
    }

    /**
     * Determines if any Swing components in Piccolo should receive the given
     * MouseEvent and forwards the event to that component. However,
     * mouseEntered and mouseExited are independent of the buttons. Also, notice
     * the notes on mouseEntered and mouseExited.
     * 
     * @param pSwingMouseEvent
     * @param aEvent
     */
    void dispatchEvent(PSwingEvent pSwingMouseEvent, PInputEvent aEvent) {
        MouseEvent mEvent = pSwingMouseEvent.asMouseEvent();
        Component comp = null;
        Point2D pt = null;
        PNode pickedNode = pSwingMouseEvent.getPath().getPickedNode();
        int offX = 0;
        int offY = 0;
        PNode currentNode = pSwingMouseEvent.getCurrentNode();
        if (currentNode instanceof PSwing) {
            PSwingNode swing = (PSwingNode) currentNode;
            PNode grabNode = pickedNode;
            if (true) {
                boolean inner = !grabNode.isDescendentOf(canvas.getRoot());
                pt = new Point2D.Double(mEvent.getX(), mEvent.getY());
                cameraToLocal(pSwingMouseEvent.getPath().getTopCamera(), pt, grabNode);
                if (inner) pt = innerPt(swing, pt);
                prevPoint = new Point2D.Double(pt.getX(), pt.getY());
                comp = findShowingComponentAt(swing.getComponent(), (int) pt.getX(), (int) pt.getY());
                if (comp != null && comp != swing.getComponent()) {
                    for (Component c = comp; c != swing.getComponent(); c = c.getParent()) {
                        offX += c.getLocation().getX();
                        offY += c.getLocation().getY();
                    }
                }
                if (comp != null && pSwingMouseEvent.getID() == MouseEvent.MOUSE_PRESSED) {
                    if (SwingUtilities.isLeftMouseButton(mEvent)) {
                        leftButtonData.setState(swing, pickedNode, comp, offX, offY);
                    } else if (SwingUtilities.isMiddleMouseButton(mEvent)) {
                        middleButtonData.setState(swing, pickedNode, comp, offX, offY);
                    } else if (SwingUtilities.isRightMouseButton(mEvent)) {
                        rightButtonData.setState(swing, pickedNode, comp, offX, offY);
                    }
                }
            }
        }
        if (pSwingMouseEvent.getID() == MouseEvent.MOUSE_DRAGGED || pSwingMouseEvent.getID() == MouseEvent.MOUSE_RELEASED) {
            if (SwingUtilities.isLeftMouseButton(mEvent) && leftButtonData.getFocusedComponent() != null) {
                handleButton(pSwingMouseEvent, aEvent, leftButtonData);
            }
            if (SwingUtilities.isMiddleMouseButton(mEvent) && middleButtonData.getFocusedComponent() != null) {
                handleButton(pSwingMouseEvent, aEvent, middleButtonData);
            }
            if (SwingUtilities.isRightMouseButton(mEvent) && rightButtonData.getFocusedComponent() != null) {
                handleButton(pSwingMouseEvent, aEvent, rightButtonData);
            }
        } else if ((pSwingMouseEvent.getID() == MouseEvent.MOUSE_PRESSED || pSwingMouseEvent.getID() == MouseEvent.MOUSE_CLICKED || pSwingMouseEvent.getID() == MouseEvent.MOUSE_MOVED) && (comp != null)) {
            MouseEvent e_temp = new MouseEvent(comp, pSwingMouseEvent.getID(), mEvent.getWhen(), mEvent.getModifiers(), (int) pt.getX() - offX, (int) pt.getY() - offY, mEvent.getClickCount(), mEvent.isPopupTrigger());
            PSwingEvent e2 = PSwingMouseEvent.createMouseEvent(e_temp.getID(), e_temp, aEvent);
            dispatchEvent(comp, e2);
        } else if (pSwingMouseEvent.getID() == MouseEvent.MOUSE_WHEEL && (comp != null)) {
            MouseWheelEvent mWEvent = (MouseWheelEvent) mEvent;
            MouseWheelEvent e_temp = new MouseWheelEvent(comp, pSwingMouseEvent.getID(), mEvent.getWhen(), mEvent.getModifiers(), (int) pt.getX() - offX, (int) pt.getY() - offY, mEvent.getClickCount(), mEvent.isPopupTrigger(), mWEvent.getScrollType(), mWEvent.getScrollAmount(), mWEvent.getWheelRotation());
            PSwingMouseWheelEvent e2 = new PSwingMouseWheelEvent(e_temp.getID(), e_temp, aEvent);
            dispatchEvent(comp, e2);
        }
        if (prevComponent != null) {
            if (comp == null || pSwingMouseEvent.getID() == MouseEvent.MOUSE_EXITED) {
                MouseEvent e_temp = createExitEvent(mEvent);
                PSwingEvent e2 = PSwingMouseEvent.createMouseEvent(e_temp.getID(), e_temp, aEvent);
                dispatchEvent(prevComponent, e2);
                prevComponent = null;
            } else if (prevComponent != comp) {
                MouseEvent e_temp = createExitEvent(mEvent);
                PSwingEvent e2 = PSwingMouseEvent.createMouseEvent(e_temp.getID(), e_temp, aEvent);
                dispatchEvent(prevComponent, e2);
                e_temp = createEnterEvent(comp, mEvent, offX, offY);
                e2 = PSwingMouseEvent.createMouseEvent(e_temp.getID(), e_temp, aEvent);
                comp.dispatchEvent(e2.asMouseEvent());
            }
        } else {
            if (comp != null) {
                MouseEvent e_temp = createEnterEvent(comp, mEvent, offX, offY);
                PSwingEvent e2 = PSwingMouseEvent.createMouseEvent(e_temp.getID(), e_temp, aEvent);
                dispatchEvent(comp, e2);
            }
        }
        prevComponent = comp;
        if (comp != null) {
            prevOff = new Point2D.Double(offX, offY);
        }
    }

    private MouseEvent createEnterEvent(Component comp, MouseEvent e1, int offX, int offY) {
        return new MouseEvent(comp, MouseEvent.MOUSE_ENTERED, e1.getWhen(), 0, (int) prevPoint.getX() - offX, (int) prevPoint.getY() - offY, e1.getClickCount(), e1.isPopupTrigger());
    }

    private MouseEvent createExitEvent(MouseEvent e1) {
        return new MouseEvent(prevComponent, MouseEvent.MOUSE_EXITED, e1.getWhen(), 0, (int) prevPoint.getX() - (int) prevOff.getX(), (int) prevPoint.getY() - (int) prevOff.getY(), e1.getClickCount(), e1.isPopupTrigger());
    }

    private void handleButton(PSwingEvent e1, PInputEvent aEvent, ButtonData buttonData) {
        Point2D pt;
        MouseEvent m1 = e1.asMouseEvent();
        if (true) {
            pt = new Point2D.Double(m1.getX(), m1.getY());
            boolean inner = !buttonData.getPNode().isDescendentOf(canvas.getRoot());
            cameraToLocal(e1.getPath().getTopCamera(), pt, buttonData.getPNode());
            if (inner) pt = innerPt((PSwingNode) buttonData.getPNode(), pt);
            MouseEvent e_temp = new MouseEvent(buttonData.getFocusedComponent(), e1.getID(), m1.getWhen(), m1.getModifiers(), (int) pt.getX() - buttonData.getOffsetX(), (int) pt.getY() - buttonData.getOffsetY(), m1.getClickCount(), m1.isPopupTrigger());
            PSwingEvent e2 = PSwingMouseEvent.createMouseEvent(e_temp.getID(), e_temp, aEvent);
            dispatchEvent(buttonData.getFocusedComponent(), e2);
        } else {
            dispatchEvent(buttonData.getFocusedComponent(), e1);
        }
        m1.consume();
        if (e1.getID() == MouseEvent.MOUSE_RELEASED) {
            buttonData.mouseReleased();
        }
    }

    private void dispatchEvent(final Component target, final PSwingEvent event) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                target.dispatchEvent(event.asMouseEvent());
            }
        });
    }

    private void cameraToLocal(PCamera topCamera, Point2D pt, PNode node) {
        AffineTransform inverse = null;
        try {
            inverse = topCamera.getViewTransform().createInverse();
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        PNode searchNode = node;
        do {
            searchNode = searchNode.getParent();
            if (searchNode instanceof PLayer) {
                inverse.transform(pt, pt);
                break;
            }
        } while (searchNode != null);
        if (node != null) node.globalToLocal(pt);
    }

    /**
     * Process a piccolo event and (if active) dispatch the corresponding Swing
     * event.
     * 
     * @param aEvent
     * @param type
     */
    public void processEvent(PInputEvent aEvent, int type) {
        if (aEvent.isMouseEvent()) {
            InputEvent sourceSwingEvent = aEvent.getSourceSwingEvent();
            if (sourceSwingEvent instanceof MouseEvent) {
                if (sourceSwingEvent instanceof MouseWheelEvent) {
                }
                MouseEvent swingMouseEvent = (MouseEvent) sourceSwingEvent;
                PSwingEvent pSwingMouseEvent = PSwingMouseEvent.createMouseEvent(swingMouseEvent.getID(), swingMouseEvent, aEvent);
                if (!recursing) {
                    recursing = true;
                    dispatchEvent(pSwingMouseEvent, aEvent);
                    if (pSwingMouseEvent.asMouseEvent().isConsumed()) {
                        aEvent.setHandled(true);
                    }
                    recursing = false;
                }
            } else {
                new Exception("PInputEvent.getSourceSwingEvent was not a MouseEvent.  Actual event: " + sourceSwingEvent + ", class=" + sourceSwingEvent.getClass().getName()).printStackTrace();
            }
        }
    }

    /**
     * Internal Utility class for handling button interactivity.
     */
    private static class ButtonData {

        private PSwing focusPSwing = null;

        private PNode focusNode = null;

        private Component focusComponent = null;

        private int focusOffX = 0;

        private int focusOffY = 0;

        public void setState(PSwing swing, PNode visualNode, Component comp, int offX, int offY) {
            focusPSwing = swing;
            focusComponent = comp;
            focusNode = visualNode;
            focusOffX = offX;
            focusOffY = offY;
        }

        public Component getFocusedComponent() {
            return focusComponent;
        }

        public PNode getPNode() {
            return focusNode;
        }

        public int getOffsetX() {
            return focusOffX;
        }

        public int getOffsetY() {
            return focusOffY;
        }

        public PSwing getPSwing() {
            return focusPSwing;
        }

        public void mouseReleased() {
            focusComponent = null;
            focusNode = null;
        }
    }
}
