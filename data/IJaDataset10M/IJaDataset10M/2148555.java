package org.skycastle.util.componentgraph.nodeview;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * An UI component that shows a setOrientation of ViewNodes.
 * <p/>
 * Also keeps track of the current camera vector.
 * <p/>
 * IDEA: Show scroll bars if any node is outside the visible area
 * <p/>
 * IDEA: Add mouse wheel zooming
 *
 * @author Hans Haggstrom
 */
public class ViewNodePanel extends JPanel {

    private final CompositeViewNode myCompositeViewNode = new CompositeViewNode();

    /**
     * @param cameraPosition the view transform to use for this panel. A copy is made, so the original transformation
     *                       can be changed outside this class.
     */
    public void setCameraPosition(final AffineTransform cameraPosition) {
        myCompositeViewNode.setTransform(cameraPosition);
    }

    /**
     * Sets the panning vector of the node view panel.
     *
     * @param x x offset
     * @param y y offset
     */
    public void setPan(float x, float y) {
        myCompositeViewNode.setPosition(x, y);
    }

    /**
     * Adds the specified view node to this view.
     *
     * @param viewNode node to be added.  Should not be null or already added.
     */
    public void addNode(final ViewNode viewNode) {
        myCompositeViewNode.addNode(viewNode);
    }

    /**
     * Removes the specified view node from this view.
     *
     * @param viewNode node to be removed.  Should not be null.
     */
    public void removeNode(final ViewNode viewNode) {
        myCompositeViewNode.removeNode(viewNode);
    }

    /**
     * @param x swing coordinate, with 0,0 corresponding to upper left corner of the ViewNode viewing area.
     * @param y swing coordinate, with 0,0 corresponding to upper left corner of the ViewNode viewing area.
     *
     * @return the ViewNode at the specified JComponent coordinates, or null if none found.
     */
    public ViewNode getViewNodeAt(int x, int y) {
        return myCompositeViewNode.getViewNodeAt(x, y);
    }

    /**
     * Removes all nodes from this panel.
     */
    public void removeAllNodes() {
        myCompositeViewNode.removeAllNodes();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        myCompositeViewNode.render(g2);
    }
}
