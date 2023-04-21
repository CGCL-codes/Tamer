package simtools.diagram;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.undo.UndoableEdit;
import simtools.diagram.undo.TranslateEdit;

/**
 * This class holds an element selection and its first and future locations
 * @see DiagramComponent
 *
 * @author Claude Cazenave
 *
 * @version 1.0 1999
 */
public class ElementSelection {

    protected Element element;

    /** The element position before translation operation */
    protected Point initPoint;

    /** The element position after translation operation */
    protected Point currentPoint;

    /** The element position without taking in account the grid steps */
    protected Point referencePoint;

    /**
	 * Constructs a new selected shape
	 * @param s the shape
	 * @param p its current position
	 */
    public ElementSelection(Element s, Point initPoint) {
        this.element = s;
        this.initPoint = new Point(initPoint);
        this.currentPoint = new Point(initPoint);
        this.referencePoint = new Point(initPoint);
    }

    public Shape getShape() {
        return element;
    }

    public void translateShape(int dx, int dy) {
        element.translate(dx, dy);
        currentPoint.x += dx;
        currentPoint.y += dy;
    }

    public void translateReferencePoint(int dx, int dy) {
        referencePoint.x += dx;
        referencePoint.y += dy;
    }

    /**
     * @return the translation edit
     */
    public UndoableEdit translateShapeEnd() {
        UndoableEdit res = null;
        res = new TranslateEdit(currentPoint.x - initPoint.x, currentPoint.y - initPoint.y, element);
        initPoint = new Point(currentPoint);
        referencePoint = new Point(initPoint);
        return res;
    }

    /**
     * Draw shape bounds when the shape is selected
     * @param g2
     */
    public void drawBounds(Graphics2D g2) {
        Rectangle2D b = element.getBounds2D();
        int ox, oy, x, xb, y, w, h;
        ox = (int) b.getX();
        oy = (int) b.getY();
        w = (int) b.getWidth();
        h = (int) b.getHeight();
        g2.drawRect(ox, oy, w - 1, h - 1);
        g2.fillRect(ox, oy, 5, 5);
        x = xb = ox + w - 5;
        g2.fillRect(x, oy, 5, 5);
        y = oy + h - 5;
        g2.fillRect(x, y, 5, 5);
        g2.fillRect(ox, y, 5, 5);
        x = ox + w / 2 - 2;
        g2.fillRect(x, oy, 5, 5);
        g2.fillRect(x, y, 5, 5);
        y = oy + h / 2 - 2;
        g2.fillRect(ox, y, 5, 5);
        g2.fillRect(xb, y, 5, 5);
    }
}
