package com.ingenico.insider.nodes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolo.util.PUtil;

public class PCurvePath extends PNode {

    /**
	 * Generated Serial Version UID
	 */
    private static final long serialVersionUID = -6183525037462033068L;

    /** 
	 * The property name that identifies a change of this node's stroke paint
	 * (see {@link #getStrokePaint getStrokePaint}). Both old and new value will
	 * be set correctly to Paint objects in any property change event.
	 */
    public static final String PROPERTY_STROKE_PAINT = "strokePaint";

    public static final int PROPERTY_CODE_STROKE_PAINT = 1 << 16;

    /** 
	 * The property name that identifies a change of this node's stroke (see
	 * {@link #getStroke getStroke}). Both old and new value will be set
	 * correctly to Stroke objects in any property change event.
	 */
    public static final String PROPERTY_STROKE = "stroke";

    public static final int PROPERTY_CODE_STROKE = 1 << 17;

    /** 
	 * The property name that identifies a change of this node's path (see
	 * {@link #getPathReference getPathReference}).  In any property change
	 * event the new value will be a reference to this node's path,  but old
	 * value will always be null.
	 */
    public static final String PROPERTY_PATH = "path";

    public static final int PROPERTY_CODE_PATH = 1 << 18;

    private static final BasicStroke DEFAULT_STROKE = new BasicStroke(1.0f);

    private static final Color DEFAULT_STROKE_PAINT = Color.black;

    private transient Path2D path;

    private transient Stroke stroke;

    private Paint strokePaint;

    public PCurvePath() {
        strokePaint = DEFAULT_STROKE_PAINT;
        stroke = DEFAULT_STROKE;
        path = new Path2D.Float();
    }

    public PCurvePath(Path2D aPath) {
        this(aPath, DEFAULT_STROKE);
    }

    /**
	 * Construct this path with the given shape and stroke.
	 * This method may be used to optimize the creation of a large number of
	 * PPaths. Normally PPaths have a default stroke of width one, but when a
	 * path has a non null stroke it takes significantly longer to compute its
	 * bounds. This method allows you to override that default stroke before the
	 * bounds are ever calculated, so if you pass in a null stroke here you
	 * won't ever have to pay that bounds calculation price if you don't need
	 * to.
	 */
    public PCurvePath(Path2D aPath, Stroke aStroke) {
        this();
        stroke = aStroke;
        path = aPath;
    }

    public Paint getStrokePaint() {
        return strokePaint;
    }

    public void setStrokePaint(Paint aPaint) {
        strokePaint = aPaint;
        invalidatePaint();
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke aStroke) {
        Stroke old = stroke;
        stroke = aStroke;
        updateBoundsFromPath();
        invalidatePaint();
        firePropertyChange(PROPERTY_CODE_STROKE, PROPERTY_STROKE, old, stroke);
    }

    public boolean intersects(Rectangle2D aBounds) {
        if (super.intersects(aBounds)) {
            if (getPaint() != null && path.intersects(aBounds)) {
                return true;
            } else if (stroke != null && strokePaint != null) {
                return stroke.createStrokedShape(path).intersects(aBounds);
            }
        }
        return false;
    }

    public Rectangle2D getPathBoundsWithStroke() {
        if (stroke != null) {
            return stroke.createStrokedShape(path).getBounds2D();
        } else {
            return path.getBounds2D();
        }
    }

    public void updateBoundsFromPath() {
        if (path == null) {
            resetBounds();
        } else {
            Rectangle2D b = getPathBoundsWithStroke();
            setBounds(b.getX(), b.getY(), b.getWidth(), b.getHeight());
        }
    }

    protected void paint(PPaintContext paintContext) {
        Paint p = getPaint();
        Graphics2D g2 = paintContext.getGraphics();
        if (p != null) {
            g2.setPaint(p);
            g2.fill(path);
        }
        if (stroke != null && strokePaint != null) {
            g2.setPaint(strokePaint);
            g2.setStroke(stroke);
            g2.draw(path);
        }
    }

    public Path2D getPathReference() {
        return path;
    }

    public void setPathReference(Path2D aPath) {
        path = aPath;
        updateBoundsFromPath();
        invalidatePaint();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        PUtil.writeStroke(stroke, out);
        PUtil.writePath(path, out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        stroke = PUtil.readStroke(in);
        path = PUtil.readPath(in);
    }

    /**
	 * Returns a string representing the state of this node. This method is
	 * intended to be used only for debugging purposes, and the content and
	 * format of the returned string may vary between implementations. The
	 * returned string may be empty but may not be <code>null</code>.
	 *
	 * @return  a string representation of this node's state
	 */
    protected String paramString() {
        StringBuffer result = new StringBuffer();
        result.append("path=" + (path == null ? "null" : path.toString()));
        result.append(",stroke=" + (stroke == null ? "null" : stroke.toString()));
        result.append(",strokePaint=" + (strokePaint == null ? "null" : strokePaint.toString()));
        result.append(',');
        result.append(super.paramString());
        return result.toString();
    }
}
