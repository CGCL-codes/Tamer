package objectDraw.canvas;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.*;
import javax.swing.event.*;

/**
 * Class that represents a general figure on a canvas.
 *
 */
public abstract class AbstractFigure implements Figure {

    /** Listeners for events on this figure. */
    protected EventListenerList listeners;

    /** Visible property of the figure. */
    protected boolean visible;

    /** Selectable property of the figure. */
    protected boolean selectable;

    /** Resizable property */
    protected boolean resizeable;

    /** The handle for the figure. */
    protected Handle handle;

    /** The color the figure should be rendered in. */
    protected Color color;

    public AbstractFigure() {
        setColor(Color.BLACK);
        setVisible(true);
        setSelectable(true);
        listeners = new EventListenerList();
    }

    public AbstractFigure(Color figColor) {
        setColor(figColor);
    }

    @Override
    public void setVisible(boolean v) {
        visible = v;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setSelectable(boolean s) {
        selectable = s;
    }

    @Override
    public boolean isSelectable() {
        return selectable;
    }

    @Override
    public Point2D.Double getTopLeft() {
        Rectangle2D r = getBounds();
        return new Point2D.Double(r.getX(), r.getY());
    }

    @Override
    public void setColor(Color c) {
        color = c;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override
    public void setResizeable(boolean r) {
        resizeable = r;
    }

    @Override
    public boolean isResizeable() {
        return resizeable;
    }

    @Override
    public void setHandle(Handle h) {
        handle = h;
    }

    @Override
    public Handle getHandle() {
        return handle;
    }
}
