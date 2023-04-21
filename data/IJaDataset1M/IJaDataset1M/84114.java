package org.mediavirus.graphl.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import org.mediavirus.graphl.GraphlPane;
import org.mediavirus.graphl.graph.Edge;
import org.mediavirus.graphl.graph.Node;
import org.mediavirus.graphl.vocabulary.NS;
import org.mediavirus.util.ParseUtils;

/**
 * @author Flo Ledermann <ledermann@ims.tuwien.ac.at>
 * created: 24.09.2004 22:07:13
 */
public class ShapeNodePainter extends AbstractNodePainter {

    public static final int SQUARE = 0;

    public static final int CIRCLE = 1;

    public static final int CROSS = 2;

    public static final int X = 3;

    float size = 10.0f;

    Shape shape = null;

    Stroke stroke = new BasicStroke(2.0f);

    Color borderColor = Color.BLACK;

    Color fillColor = new Color(1.0f, 1.0f, 1.0f, 0.4f);

    public ShapeNodePainter() {
        super();
        setShape(SQUARE);
    }

    public void setShape(Shape s) {
        this.shape = s;
    }

    public void setShape(int preset) {
        switch(preset) {
            case SQUARE:
                shape = new Rectangle2D.Float(-5, -5, 10, 10);
                break;
            case CIRCLE:
                shape = new Ellipse2D.Float(-5, -5, 10, 10);
                break;
            case CROSS:
                GeneralPath p = new GeneralPath();
                p.moveTo(-5, 0);
                p.lineTo(5, 0);
                p.moveTo(0, -5);
                p.lineTo(0, 5);
                shape = p;
                break;
            case X:
                p = new GeneralPath();
                p.moveTo(-5, -5);
                p.lineTo(5, 5);
                p.moveTo(5, -5);
                p.lineTo(-5, 5);
                shape = p;
                break;
            default:
                break;
        }
    }

    public Object clone() {
        ShapeNodePainter s = new ShapeNodePainter();
        s.setShape(shape);
        s.setBorderColor(borderColor);
        s.setFillColor(fillColor);
        s.setSize(size);
        return s;
    }

    public void paintNode(GraphlPane graphPane, Graphics2D g, Node node, boolean selected, boolean highlighted) {
        AffineTransform xf = g.getTransform();
        g.translate(node.getCenterX(), node.getCenterY());
        g.scale(size / 10, size / 10);
        g.setColor(fillColor);
        g.fill(shape);
        g.setColor(borderColor);
        g.setStroke(stroke);
        g.draw(shape);
        g.setTransform(xf);
    }

    public void getNodeScreenBounds(GraphlPane graphPane, Node node, Rectangle nodeScreenRectangle) {
        nodeScreenRectangle.setBounds((int) (node.getCenterX() - size / 2), (int) (node.getCenterY() - size / 2), (int) size, (int) size);
    }

    public String getToolTipText(GraphlPane graphPane, Node node, Point point) {
        return null;
    }

    public boolean isEdgeDragPoint(GraphlPane graphPane, Node node, Point p) {
        return false;
    }

    public Point getEdgePin(Node node, Edge edge) {
        return new Point((int) node.getCenterX(), (int) node.getCenterY());
    }

    public boolean hasVisualController() {
        return true;
    }

    public JComponent getVisualController() {
        return new ShapeNodePainterController(this);
    }

    public String getName() {
        return "Shape";
    }

    /**
     * @return Returns the stroke.
     */
    public Stroke getStroke() {
        return stroke;
    }

    /**
     * @param stroke The stroke to set.
     */
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    /**
     * @return Returns the borderColor.
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * @param borderColor The borderColor to set.
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * @return Returns the fillColor.
     */
    public Color getFillColor() {
        return fillColor;
    }

    /**
     * @param fillColor The fillColor to set.
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * @return Returns the size.
     */
    public float getSize() {
        return size;
    }

    /**
     * @param size The size to set.
     */
    public void setSize(float size) {
        this.size = size;
    }

    /**
     * @return Returns the shape.
     */
    public Shape getShape() {
        return shape;
    }

    public boolean isSameClass(Object o) {
        return (o instanceof ShapeNodePainter);
    }

    public void setConfigurationNode(Node node) {
        String str = node.getProperty(NS.graphl + "shape");
        if (str != null) {
            if (str.equalsIgnoreCase("X")) {
                setShape(X);
            } else if (str.equalsIgnoreCase("cross")) {
                setShape(CROSS);
            } else if (str.equalsIgnoreCase("circle")) {
                setShape(CIRCLE);
            } else if (str.equalsIgnoreCase("square")) {
                setShape(SQUARE);
            }
        }
        Stroke stroke = ParseUtils.parseStroke(node.getProperty(NS.graphl + "stroke"));
        if (stroke != null) setStroke(stroke);
        Color col = ParseUtils.parseColor(node.getProperty(NS.graphl + "color"));
        if (col != null) setBorderColor(col);
        col = ParseUtils.parseColor(node.getProperty(NS.graphl + "fillColor"));
        if (col != null) setFillColor(col);
    }

    public String getLabel(Node node) {
        return null;
    }
}
