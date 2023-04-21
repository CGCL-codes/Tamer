package net.sourceforge.jute.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.logging.Logger;
import net.sourceforge.jute.logging.LogFactory;

/**
 *
 * @author david.mcnerney
 */
public abstract class GraphicObject {

    protected GraphicObject() {
    }

    public final void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform zTransform = (AffineTransform) g2.getTransform().clone();
        Color zColor = g2.getColor();
        g2.setColor(color);
        g2.transform(getTransform());
        LOG.fine("Draw: " + this + " " + g2.getTransform().getTranslateX() + "," + g2.getTransform().getTranslateY());
        drawObject(g2);
        g2.setTransform(zTransform);
        g2.setColor(zColor);
    }

    protected abstract void drawObject(Graphics g);

    protected void layout() {
    }

    public final GraphicObject setColor(Color color) {
        this.color = color;
        return this;
    }

    public final GraphicObject move(int x, int y) {
        bounds.x = x;
        bounds.y = y;
        return this;
    }

    public final GraphicObject rotate(float deg, int rotX, int rotY) {
        rotation = deg * Math.PI / 180.0f;
        return this;
    }

    public GraphicObject pad(int top, int right, int bottom, int left) {
        pad[0] = top;
        pad[1] = right;
        pad[2] = bottom;
        pad[3] = left;
        return this;
    }

    protected void setParent(GraphicObject parent) {
        this.parent = parent;
    }

    protected AffineTransform getTransform() {
        AffineTransform result = AffineTransform.getTranslateInstance(bounds.x, bounds.y);
        result.rotate(rotation);
        return result;
    }

    protected AffineTransform getFullTransform() {
        if (parent == null) {
            return getTransform();
        }
        AffineTransform t = parent.getFullTransform();
        t.concatenate(getTransform());
        return t;
    }

    protected Point mapToGlobal(Point pt) {
        AffineTransform tx = getFullTransform();
        Point2D result = tx.transform(pt, null);
        return new Point((int) result.getX(), (int) result.getY());
    }

    protected Point mapGlobal(Point pt) {
        AffineTransform tx = getFullTransform();
        try {
            tx = getFullTransform().createInverse();
        } catch (NoninvertibleTransformException ex) {
            return null;
        }
        Point2D result = tx.transform(pt, null);
        return new Point((int) result.getX(), (int) result.getY());
    }

    protected int[] pad = new int[4];

    protected double rotation;

    private Color color = Color.BLACK;

    private GraphicObject parent;

    protected Rectangle bounds = new Rectangle();

    private static final Logger LOG = LogFactory.getLog(GraphicObject.class);
}
