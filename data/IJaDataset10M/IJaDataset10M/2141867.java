package itGraph.UMLElements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexView;

public class StateRenderer extends JComponent implements CellViewRenderer, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5241777221314947185L;

    private JLabel header = null;

    private JTextArea internalStateArea = null;

    private boolean hasFocus, selected;

    private Insets insets = new Insets(5, 5, 5, 5);

    public StateRenderer() {
        setLayout(new BorderLayout());
        setEnabled(false);
        ImageIcon icon = null;
        header = new JLabel(icon) {

            /**
				 * 
				 */
            private static final long serialVersionUID = 5452121868761872773L;

            public Insets getInsets() {
                return insets;
            }
        };
        header.setVerticalTextPosition(JLabel.BOTTOM);
        header.setHorizontalTextPosition(JLabel.CENTER);
        header.setBorder(null);
        add(header, BorderLayout.NORTH);
        internalStateArea = new JTextArea() {

            /**
				 * 
				 */
            private static final long serialVersionUID = 8241372750817029513L;

            public Insets getInsets() {
                return insets;
            }
        };
        internalStateArea.setLineWrap(false);
        add(internalStateArea, BorderLayout.CENTER);
    }

    protected void installAttributes(CellView view) {
        Map map = view.getAllAttributes();
        StateCell cell = (StateCell) view.getCell();
        Color bordercolor = GraphConstants.getBorderColor(map);
        setOpaque(GraphConstants.isOpaque(map));
        bordercolor = GraphConstants.getBorderColor(map);
        if (getBorder() == null && bordercolor != null) {
            setBorder(BorderFactory.createLineBorder(bordercolor, 1));
        }
        Color foreground = GraphConstants.getForeground(map);
        Color background = GraphConstants.getBackground(map);
        setBackground(background);
        Font font = GraphConstants.getFont(map);
        header.setOpaque(false);
        header.setFont(font);
        header.setForeground(foreground);
        header.setBackground(background);
        Icon icon = GraphConstants.getIcon(map);
        header.setIcon(icon);
        internalStateArea.setFont(font);
        internalStateArea.setOpaque(false);
        ;
        internalStateArea.setVisible(true);
        internalStateArea.setText(cell.getInternalState());
    }

    public Point2D getPerimeterPoint(VertexView view, Point2D source, Point2D p) {
        Rectangle2D bounds = view.getBounds();
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

    public Component getRendererComponent(JGraph graph, CellView view, boolean sel, boolean focus, boolean preview) {
        if (view instanceof StateView) {
            setComponentOrientation(graph.getComponentOrientation());
            if (graph.getEditingCell() != view.getCell()) {
                Object label = graph.convertValueToString(view.getCell());
                if (label != null) {
                    header.setText("<html><font size='3'><b>" + label.toString() + "</b></font></html>");
                } else {
                    header.setText(null);
                }
            } else {
                header.setText(null);
            }
            this.hasFocus = focus;
            this.selected = sel;
            if (view.isLeaf()) {
                installAttributes(view);
            } else {
                header.setText(null);
                setBorder(null);
                setOpaque(false);
            }
            return this;
        }
        return null;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (selected && hasFocus) {
            g2d.setStroke(GraphConstants.SELECTION_STROKE);
        }
        Dimension d = getSize();
        int w = d.width - 1;
        int h = d.height - 1;
        int a = 30;
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(1, 1, w - 1, h - 1, a - 1, a - 1);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(0, 0, w, h, a, a);
        internalStateArea.setSize(d);
        Rectangle ar = internalStateArea.getBounds();
        g2d.drawLine(0, ar.y, w, ar.y);
        g2d.setColor(getForeground());
        super.paint(g2d);
    }
}
