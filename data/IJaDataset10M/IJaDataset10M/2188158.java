package JavaOrc.diagram;

import diagram.Figure;
import diagram.DefaultLinkRenderer;
import diagram.shape.ArrowHead;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.UIManager;

/**
 * @class AssociationLinkRenderer
 *
 * @date 08-20-2001
 * @author Eric Crahen
 * @version 1.0
 *
 * Renderer for AssociationLink Figures.
 */
public class AssociationLinkRenderer extends DefaultLinkRenderer {

    protected static final CustomUI associationUI = new CustomUI("association");

    static {
        UIManager.put("association.foreground", Color.black);
        UIManager.put("association.background", Color.white);
    }

    /**
   * Create a new renderer
   */
    public AssociationLinkRenderer() {
        setUI(associationUI);
    }

    /**
   * Paint the normal line with a dashed Stroke.
   */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    /**
   * No decoration on the source endpoint
   */
    protected GeneralPath getSourceEndpoint(double x, double y, GeneralPath path) {
        return null;
    }

    /**
   * Draw an open ArrowHead shape at the sink
   */
    protected GeneralPath getSinkEndpoint(double x, double y, GeneralPath path) {
        return ArrowHead.createArrowHead(13.0, ArrowHead.OPEN, x, y, path);
    }

    /**
   * Paint that open ArrowHead shape with a solid Stroke
   */
    protected void paintSinkEndpoint(Graphics2D g2, AffineTransform at, GeneralPath path) {
        super.paintSinkEndpoint(g2, at, path);
        g2.draw(path);
    }
}
