package org.gvt.figure;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.*;
import org.gvt.model.NodeModel;
import org.gvt.ChsXYLayout;

/**
 * This class maintains the node figure which is the UI of nodes. Each node has
 * its own color, label and shape.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class NodeFigure extends Figure {

    Label label;

    String shape;

    boolean highlight;

    Color highlightColor;

    public PointList triangle = new PointList(3);

    /**
	 * Constructors
	 */
    public NodeFigure() {
        super();
    }

    public NodeFigure(Point absLocation, Dimension size, String text, Font textFont, Color textColor, Color color, Color borderColor, String shape, Color highlightColor, boolean highlight) {
        super();
        this.label = new Label();
        add(label);
        this.highlight = highlight;
        setBackgroundColor(color);
        setForegroundColor(borderColor);
        setLayoutManager(new ChsXYLayout());
        Rectangle r = new Rectangle(absLocation.getCopy(), size.getCopy());
        setBounds(r);
        updateText(text);
        updateTextFont(textFont);
        updateTextColor(textColor);
        updateHighlightColor(highlightColor);
        updateShape(shape);
    }

    public void updateText(String str) {
        this.label.setText(str);
    }

    public void updateTextFont(Font f) {
        this.label.setFont(f);
    }

    public void updateTextColor(Color c) {
        this.label.setForegroundColor(c);
    }

    public void updateColor(Color color) {
        setBackgroundColor(color);
    }

    public void updateBorderColor(Color color) {
        setForegroundColor(color);
    }

    public void updateShape(String s) {
        this.shape = s;
        this.removeAll();
        if (shape.equals(NodeModel.shapes[0])) {
            add(new RectangleFigure(getBounds()));
        } else if (shape.equals(NodeModel.shapes[1])) {
            add(new EllipseFigure(getBounds()));
        } else if (shape.equals(NodeModel.shapes[2])) {
            add(new TriangleFigure(getBounds()));
        }
        add(label);
    }

    public void updateHighlight(Layer highlight, boolean isHighlight) {
        this.highlight = isHighlight;
        if (this.highlight) {
            ((HighlightLayer) highlight).addHighlightToNode(this);
        } else {
            ((HighlightLayer) highlight).removeHighlight(this);
        }
        repaint();
    }

    public void updateHighlightColor(Color color) {
        this.highlightColor = color;
        repaint();
    }

    protected void paintFigure(Graphics g) {
        label.setSize(getSize());
    }

    /**
	 * Triangles 3 points are calculated for drawing it.
	 */
    public PointList calculateTrianglePoints(Rectangle rect) {
        Rectangle r = rect.getCopy();
        r.height--;
        r.width--;
        this.triangle.removeAllPoints();
        this.triangle.addPoint(new Point(r.x + r.width / 2, r.y));
        this.triangle.addPoint(new Point(r.x, r.y + r.height));
        this.triangle.addPoint(new Point(r.x + r.width, r.y + r.height));
        return this.triangle;
    }

    public class RectangleFigure extends Figure {

        public RectangleFigure() {
            super();
        }

        public RectangleFigure(Rectangle rect) {
            setBounds(rect);
        }

        protected void paintFigure(Graphics g) {
            Rectangle r = getParent().getBounds().getCopy();
            setBounds(r);
            g.fillRectangle(r);
            r.x += 0.5;
            r.y += 0.5;
            r.height--;
            r.width--;
            g.drawRectangle(r);
        }
    }

    public class EllipseFigure extends Figure {

        public EllipseFigure(Rectangle rect) {
            setBounds(rect);
        }

        protected void paintFigure(Graphics g) {
            Rectangle r = getParent().getBounds().getCopy();
            setBounds(r);
            g.fillOval(r);
            r.height--;
            r.width--;
            g.drawOval(r);
        }
    }

    public class TriangleFigure extends Figure {

        public TriangleFigure(Rectangle rect) {
            setBounds(rect);
        }

        protected void paintFigure(Graphics g) {
            Rectangle r = getParent().getBounds().getCopy();
            setBounds(r);
            calculateTrianglePoints(r);
            g.fillPolygon(triangle);
            g.drawPolygon(triangle);
        }
    }
}
