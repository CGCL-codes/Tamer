package org.vikamine.gui.subgroup.visualization.causality;

import java.awt.Color;
import java.awt.Component;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.VertexView;
import org.vikamine.kernel.subgroup.analysis.causality.CausalACausalSGNode;
import org.vikamine.kernel.subgroup.analysis.causality.CausalSGNode;
import org.vikamine.kernel.subgroup.analysis.causality.NonCausalSGNode;
import org.vikamine.kernel.subgroup.analysis.causality.TargetSGNode;

public class SGCausalNodeCellView extends VertexView {

    private static final long serialVersionUID = -246293067384069596L;

    private SGCausalNodeCell cell;

    private SGCausalNodeCellRenderer renderer = new SGCausalNodeCellRenderer();

    public SGCausalNodeCellView(SGCausalNodeCell cell) {
        super(cell);
        this.cell = cell;
    }

    @Override
    public CellViewRenderer getRenderer() {
        return renderer;
    }

    @Override
    public Point2D getPerimeterPoint(EdgeView edge, Point2D source, Point2D p) {
        if (getRenderer() instanceof SGCausalNodeCellRenderer) return ((SGCausalNodeCellRenderer) getRenderer()).getPerimeterPoint(this, p);
        return super.getPerimeterPoint(edge, source, p);
    }

    public class SGCausalNodeCellRenderer extends JPanel implements CellViewRenderer {

        private static final long serialVersionUID = 1218205964551726030L;

        public Component getRendererComponent(JGraph graph, CellView view, boolean sel, boolean focus, boolean preview) {
            Component c = cell.getComponent();
            setBorder(BorderFactory.createRaisedBevelBorder());
            CausalSGNode node = cell.getCausalNode();
            setBackground(getColorForNode(node));
            if (c instanceof JComponent) {
                ((JComponent) c).setOpaque(false);
            }
            add(c);
            return this;
        }

        private Color getColorForNode(CausalSGNode node) {
            ExtendedSGCausalGraphBuilder builder = cell.getBuilder();
            if (!builder.isUseColors()) {
                return Color.lightGray;
            } else if (node instanceof TargetSGNode) {
                return builder.getTargetNodeColor();
            } else if (node instanceof NonCausalSGNode) {
                return builder.getNonCausalNodeColor();
            } else if (node instanceof CausalACausalSGNode) {
                return builder.getACausalNodeColor();
            } else {
                return builder.getStandardNodeColor();
            }
        }

        public Point2D getPerimeterPoint(VertexView edge, Point2D p) {
            Rectangle2D bounds = edge.getBounds();
            double x = bounds.getX();
            double y = bounds.getY();
            double width = bounds.getWidth();
            double height = bounds.getHeight();
            double xCenter = x + width / 2;
            double yCenter = y + height / 2;
            double dx = p.getX() - xCenter;
            double dy = p.getY() - yCenter;
            double alpha = Math.atan2(dy, dx);
            double xout = 0, yout = 0;
            double pi = Math.PI;
            double pi2 = Math.PI / 2.0;
            double beta = pi2 - alpha;
            double t = Math.atan2(height, width);
            if (alpha < -pi + t || alpha > pi - t) {
                xout = x;
                yout = yCenter - width * Math.tan(alpha) / 2;
            } else if (alpha < -t) {
                yout = y;
                xout = xCenter - height * Math.tan(beta) / 2;
            } else if (alpha < t) {
                xout = x + width;
                yout = yCenter + width * Math.tan(alpha) / 2;
            } else {
                yout = y + height;
                xout = xCenter + height * Math.tan(beta) / 2;
            }
            return new Point2D.Double(xout, yout);
        }
    }
}
