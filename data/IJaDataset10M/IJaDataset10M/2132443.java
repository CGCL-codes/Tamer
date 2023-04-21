package at.ac.tuwien.ifs.alviz.smallworld.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import at.ac.tuwien.ifs.alviz.smallworld.layout.DOALayout;
import at.ac.tuwien.ifs.alviz.smallworld.types.Cluster;
import at.ac.tuwien.ifs.alviz.smallworld.types.VoroNode;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.render.Renderer;

/**
 * VoroNetRenderer renders VoroNodes 
 * 
 * This is currently very buggy.  There is some confusion
 * about how to correctly interpret the results of the 
 * delaunay triangulation to properly render the voronodes.
 *
 * @author Stephen
 */
public class VoroNetRenderer implements Renderer {

    public ItemRegistry m_registry = null;

    private Stroke m_node_stroke = null;

    private Stroke m_outline_stroke = null;

    private Ellipse2D m_lens = null;

    public VoroNetRenderer() {
        m_lens = new Ellipse2D.Double(0, 0, (DOALayout.DOA_RADIUS + DOALayout.ZERO_RADIUS) * 2, (DOALayout.DOA_RADIUS + DOALayout.ZERO_RADIUS) * 2);
    }

    public void render(Graphics2D g, VisualItem item) {
        VoroNode node = (VoroNode) item;
        Shape shape = node.getShape();
        Paint itemColor = item.getColor();
        Paint fillColor = item.getFillColor();
        Stroke s = g.getStroke();
        if (m_node_stroke != null) g.setStroke(m_node_stroke);
        g.setPaint(fillColor);
        g.fill(shape);
        g.setPaint(itemColor);
        g.draw(shape);
        if (node.getOutline() != null) {
            if (m_outline_stroke != null) g.setStroke(m_node_stroke);
            g.setPaint(itemColor);
            g.draw(node.getOutline());
        }
        g.setStroke(s);
    }

    public boolean locatePoint(Point2D p, VisualItem item) {
        VoroNode node = (VoroNode) item;
        return node.getShape().contains(p);
    }

    public Rectangle2D getBoundsRef(VisualItem item, Color col) {
        VoroNode node = (VoroNode) item;
        return node.getShape().getBounds2D();
    }

    public Rectangle2D getBoundsRef(VisualItem item) {
        VoroNode node = (VoroNode) item;
        return node.getShape().getBounds2D();
    }

    public static void main(String[] args) {
    }
}
