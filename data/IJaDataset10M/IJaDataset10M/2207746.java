package prajna.viz.render;

import java.awt.*;

/**
 * Simple implementation of a NodeRenderer. This NodeRenderer will display the
 * nodes by drawing filled circles centered at the node's location, with a
 * radius equal to the node's size. If the node is selected, the circle will
 * have an outline. The node name is overlaid on the circle.
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 */
public class SimpleNodeRenderer implements NodeRenderer {

    private Color color = Color.RED;

    private int size = 10;

    /**
     * Instantiates a new simple node renderer with a default color of red, and
     * size of 10.
     */
    public SimpleNodeRenderer() {
    }

    /**
     * Instantiates a new simple node renderer.
     * 
     * @param nodeColor the node color
     * @param nodeSize the node size
     */
    public SimpleNodeRenderer(Color nodeColor, int nodeSize) {
        color = nodeColor;
        size = nodeSize;
    }

    /**
     * Draws the node into the Graphics Context. This renderer will draw the
     * node as a filled circle, with radius equal to the node size.
     * 
     * @param g the Graphics context to display the node
     * @param node the node to display
     * @param loc the node location
     */
    public void drawNode(Graphics g, Object node, Point loc) {
        if (loc != null) {
            Color preset = g.getColor();
            Color col = getNodeColor(node);
            g.setColor(col);
            g.fillOval(loc.x - size, loc.y - size, 2 * size, 2 * size);
            if ((col.getRed() + col.getGreen() + col.getBlue() / 2) > 250) {
                g.setColor(Color.black);
            } else {
                g.setColor(Color.white);
            }
            String name = node.toString();
            FontMetrics metrics = g.getFontMetrics();
            int width = metrics.stringWidth(name);
            g.drawString(name, loc.x - (width / 2), loc.y);
            g.setColor(preset);
        }
    }

    /**
     * Gets the color for the Renderer, if any.
     * 
     * @return the color used by the NodeRenderer
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the bounding box for the node using this renderer. Individual
     * renderers should ensure that the drawn node is created entirely within
     * the bounds returned. If the node has no position defined, Null is
     * returned.
     * 
     * @param node the node to be checked
     * @param loc The node location
     * @return the bounding box for the node
     */
    public Rectangle getNodeBounds(Object node, Point loc) {
        Rectangle bounds = null;
        if (loc != null) {
            bounds = new Rectangle(loc.x - size, loc.y - size, 2 * size, 2 * size);
        }
        return bounds;
    }

    /**
     * Get the color used for the node. Defaults to returning the node color
     * set for the renderer in general. Subclasses can override this behavior,
     * providing colors for nodes based upon attributes.
     * 
     * @param node the node to query for the color
     * @return the color of the node
     */
    protected Color getNodeColor(Object node) {
        return color;
    }

    /**
     * Gets the default size used to draw the nodes
     * 
     * @return the size in pixels
     */
    public int getSize() {
        return size;
    }

    /**
     * highlights the node into the Graphics Context. This renderer will draw
     * the node as a filled circle, with radius equal to the node size, and an
     * outline in either black or white, depending on the node color.
     * 
     * @param g the Graphics context to display the node
     * @param node the node to display
     * @param loc the node location
     */
    public void highlightNode(Graphics g, Object node, Point loc) {
        drawNode(g, node, loc);
        Color preset = g.getColor();
        Color col = getNodeColor(node);
        if ((col.getRed() + col.getGreen() + col.getBlue() / 2) > 250) {
            g.setColor(Color.black);
        } else {
            g.setColor(Color.white);
        }
        g.drawOval(loc.x - size, loc.y - size, 2 * size, 2 * size);
        g.setColor(preset);
    }

    /**
     * Checks whether the specified pickpoint is within the drawn area for the
     * node.
     * 
     * @param node the node to check for picking
     * @param loc The node location
     * @param pickPoint the coordinate of the point to check
     * @return whether the point is in the drawn area for this node
     */
    public boolean pickNode(Object node, Point loc, Point pickPoint) {
        boolean picked = false;
        if (node != null && loc != null) {
            picked = (pickPoint.distance(loc) <= size);
        }
        return picked;
    }

    /**
     * Sets the color for the NodeRenderer. If this color is set, it will
     * override any Node color.
     * 
     * @param nodeColor The color for the NodeRenderer
     */
    public void setColor(Color nodeColor) {
        color = nodeColor;
    }

    /**
     * Sets the size used to draw the node. If not set, the default size is 10
     * pixels.
     * 
     * @param nodeSize the size to draw nodes, in pixels
     */
    public void setSize(int nodeSize) {
        size = nodeSize;
    }
}
