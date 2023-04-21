package com.nex.context.nav;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.URL;
import edu.umd.cs.jazz.*;
import edu.umd.cs.jazz.util.*;
import edu.umd.cs.jazz.event.*;
import edu.umd.cs.jazz.component.*;

/**
 * Simple event handler for creating and selecting nodes of the graph.  Based
 * on the Squiggle event handler from HiNote.
 *
 * @author Antony Courtney
 */
public class NodeEventHandler implements ZEventHandler, ZMouseListener, ZMouseMotionListener {

    private boolean active = false;

    private ZCanvas canvas = null;

    private ZNode node = null;

    private ZGroup drawingLayer;

    private InNodeHandler inNodeHandler = null;

    private ZNode selNode = null;

    private TopicNode dragOrigin = null;

    private ZVisualLeaf dragLeaf = null;

    private static Cursor nodeAddCursor = null;

    public NodeEventHandler(ZCanvas canvas, ZGroup drawingLayer, ZNode node) {
        this.canvas = canvas;
        this.drawingLayer = drawingLayer;
        this.node = node;
        inNodeHandler = new InNodeHandler();
        if (nodeAddCursor == null) {
            try {
                URL resource;
                Toolkit tk = canvas.getToolkit();
                System.out.println("#### " + tk);
                resource = this.getClass().getClassLoader().getResource("resources rectcursor.gif");
                System.out.println("**** " + resource);
                if (resource == null) return;
                tk.getBestCursorSize(16, 16);
                Image image = canvas.getToolkit().getImage(resource);
                nodeAddCursor = canvas.getToolkit().createCustomCursor(image, new Point(7, 7), "Node Add Cursor");
            } catch (Exception e) {
                nodeAddCursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
            }
        }
    }

    /**
     * Specifies whether this event handler is active or not.
     * @param active True to make this event handler active
     */
    public void setActive(boolean active) {
        if (this.active && !active) {
            this.active = false;
            node.removeMouseListener(this);
            node.removeMouseMotionListener(this);
        } else if (!this.active && active) {
            this.active = true;
            node.addMouseListener(this);
            node.addMouseMotionListener(this);
            setNodeAddCursor();
            canvas.requestFocus();
        }
    }

    public boolean isActive() {
        return this.active;
    }

    protected void setNodeAddCursor() {
        canvas.setCursor(nodeAddCursor);
    }

    public void mousePressed(ZMouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK) {
            ZSceneGraphPath path = e.getPath();
            ZCamera camera = path.getTopCamera();
            if (selNode != null) {
                return;
            }
            ZNode pressNode = path.getNode();
            if (pressNode instanceof ZVisualLeaf) {
                ZVisualLeaf vleaf = (ZVisualLeaf) pressNode;
                if (vleaf instanceof TopicNode) {
                    selNode = pressNode;
                    dragOrigin = (TopicNode) vleaf;
                    return;
                }
            }
            Point2D pt = e.getPoint();
            path.screenToGlobal(pt);
            System.out.println("New TopicNode");
            TopicNode gnode = new TopicNode(drawingLayer, (float) pt.getX(), (float) pt.getY());
            gnode.addMouseListener(inNodeHandler);
        }
    }

    public void mouseDragged(ZMouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK) {
            if ((selNode == null) || (dragOrigin == null)) {
                return;
            }
            ZSceneGraphPath path = e.getPath();
            Point2D pt = e.getPoint();
            path.screenToGlobal(pt);
            if (dragLeaf == null) {
                ZPolygon dragLine = new ZPolygon(dragOrigin.getCenter(), pt);
                dragLeaf = new ZVisualLeaf(dragLine);
                dragLeaf.setPickable(false);
                drawingLayer.addChild(dragLeaf);
            } else {
                ZPolygon dragLine = new ZPolygon(dragOrigin.getCenter(), pt);
                dragLeaf.setVisualComponent(dragLine);
            }
        }
    }

    public void mouseReleased(ZMouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK) {
            ZSceneGraphPath path = e.getPath();
            ZCamera camera = path.getTopCamera();
            if (selNode == null) {
                return;
            }
            ZDrawingSurface surface = canvas.getDrawingSurface();
            Point2D pt = e.getPoint();
            ZSceneGraphPath pickPath = surface.pick((int) pt.getX(), (int) pt.getY());
            ZNode releaseNode = pickPath.getNode();
            if (!(releaseNode instanceof TopicNode)) {
                abortDrag();
                return;
            }
            TopicNode dragTarget = (TopicNode) releaseNode;
            if (dragOrigin.isAdjacentTo(dragTarget)) {
                abortDrag();
                return;
            }
            GraphEdge gedge = new GraphEdge(dragOrigin, dragTarget);
            if (dragLeaf != null) {
                dragLeaf.replaceWith(gedge);
            } else {
                drawingLayer.addChild(gedge);
            }
            gedge.lower();
            dragOrigin.addEdge(gedge);
            dragTarget.addEdge(gedge);
            selNode = null;
            dragOrigin = null;
            dragLeaf = null;
        }
    }

    /** abort a drag operation: */
    private void abortDrag() {
        if (dragLeaf != null) {
            dragLeaf.getParent().removeChild(dragLeaf);
        }
        selNode = null;
        dragOrigin = null;
        dragLeaf = null;
    }

    /**
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(ZMouseEvent e) {
    }

    /**
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(ZMouseEvent e) {
    }

    /**
     * Invoked when the mouse has been clicked on a component.
     */
    public void mouseClicked(ZMouseEvent e) {
    }

    /**
     * Invoked when the mouse button has been moved on a node
     * (with no buttons no down).
     */
    public void mouseMoved(ZMouseEvent e) {
    }

    /** Inner class event handler which is attached to each node of the
     * graph.
     */
    class InNodeHandler extends ZMouseAdapter {

        public void mouseEntered(ZMouseEvent e) {
            if (active) {
                canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            }
        }

        public void mouseExited(ZMouseEvent e) {
            if (active) {
                setNodeAddCursor();
            }
        }
    }
}
