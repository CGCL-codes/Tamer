package org.aiotrade.charting.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Action;
import org.aiotrade.charting.util.PathPool;

/**
 *
 * @author  Caoyuan Deng
 * @version 1.0, November 27, 2006, 7:34 AM
 * @since   1.0.4
 */
public abstract class AbstractWidget<M extends WidgetModel> implements Widget<M> {

    private static final PathPool pathPool = new PathPool(10, 10, 1000);

    protected final GeneralPath borrowPath() {
        return pathPool.borrowObject();
    }

    protected final void returnPath(GeneralPath path) {
        pathPool.returnObject(path);
    }

    protected final void returnBorrowedPaths(Collection<GeneralPath> paths) {
        for (GeneralPath path : paths) {
            pathPool.returnObject(path);
        }
    }

    private static final int HIT_TEST_SQUARE_RADIUS = 2;

    private boolean opaque;

    private Color foreground = Color.WHITE;

    private Paint background;

    private Point location;

    private Rectangle bounds;

    private M model;

    private Map<Color, GeneralPath> renderColorsWithPathBuf;

    private List<Widget<?>> children;

    private Collection<Action> actions;

    public AbstractWidget() {
    }

    public final boolean isOpaque() {
        return opaque;
    }

    public final void setOpaque(boolean opaque) {
        this.opaque = opaque;
    }

    public void setBackground(Paint paint) {
        this.background = paint;
    }

    public Paint getBackground() {
        return background;
    }

    public void setForeground(Color color) {
        this.foreground = color;
    }

    public Color getForeground() {
        return foreground;
    }

    public final void setLocation(Point point) {
        setLocation(point.x, point.y);
    }

    public void setLocation(double x, double y) {
        if (this.location == null) {
            this.location = new Point((int) x, (int) y);
        } else {
            this.location.setLocation(x, y);
        }
    }

    public final Point getLocation() {
        return location == null ? new Point(0, 0) : new Point(location);
    }

    public final void setBounds(Rectangle rect) {
        setBounds(rect.x, rect.y, rect.width, rect.height);
    }

    public void setBounds(double x, double y, double width, double height) {
        if (this.bounds == null) {
            this.bounds = new Rectangle((int) x, (int) y, (int) width, (int) height);
        } else {
            this.bounds.setRect(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return bounds == null ? makePreferredBounds() : bounds;
    }

    protected Rectangle makePreferredBounds() {
        Rectangle childrenBounds = new Rectangle();
        if (children != null) {
            for (Widget child : children) {
                childrenBounds.add(child.getBounds());
            }
        }
        return childrenBounds;
    }

    public final boolean contains(Point point) {
        return contains(point.x, point.y);
    }

    public final boolean contains(double x, double y) {
        return contains(x, y, 1, 1);
    }

    public final boolean contains(Rectangle rect) {
        return contains(rect.x, rect.y, rect.width, rect.height);
    }

    public boolean contains(double x, double y, double width, double height) {
        if (isOpaque()) {
            return getBounds().intersects(x, y, width, height);
        } else {
            if (isContainerOnly()) {
                return childrenContain(x, y, width, height);
            } else {
                return widgetContains(x, y, width, height) || childrenIntersect(x, y, width, height);
            }
        }
    }

    protected boolean widgetContains(double x, double y, double width, double height) {
        return getBounds().contains(x, y, width, height);
    }

    protected boolean childrenContain(double x, double y, double width, double height) {
        if (children != null) {
            for (Widget<?> child : children) {
                if (child.contains(x, y, width, height)) {
                    return true;
                }
            }
        }
        return false;
    }

    public final boolean intersects(Rectangle rect) {
        return intersects(rect.x, rect.y, rect.width, rect.height);
    }

    public boolean intersects(double x, double y, double width, double height) {
        if (isOpaque()) {
            return getBounds().intersects(x, y, width, height);
        } else {
            if (isContainerOnly()) {
                return childrenIntersect(x, y, width, height);
            } else {
                return widgetIntersects(x, y, width, height) || childrenIntersect(x, y, width, height);
            }
        }
    }

    protected abstract boolean widgetIntersects(double x, double y, double width, double height);

    protected boolean childrenIntersect(double x, double y, double width, double height) {
        if (children != null) {
            for (Widget<?> child : children) {
                if (child.intersects(x, y, width, height)) {
                    return true;
                }
            }
        }
        return false;
    }

    public final boolean hits(Point point) {
        return hits(point.x, point.y);
    }

    public final boolean hits(double x, double y) {
        return intersects(x - HIT_TEST_SQUARE_RADIUS, y - HIT_TEST_SQUARE_RADIUS, 2 * HIT_TEST_SQUARE_RADIUS, 2 * HIT_TEST_SQUARE_RADIUS);
    }

    public M model() {
        if (model == null) {
            model = createModel();
        }
        return model;
    }

    protected abstract M createModel();

    public void plot() {
        reset();
        plotWidget();
    }

    protected abstract void plotWidget();

    public void render(Graphics g0) {
        final Graphics2D g = (Graphics2D) g0;
        final Point location = getLocation();
        final AffineTransform backupTransform = g.getTransform();
        if (!(location.x == 0 && location.y == 0)) {
            g.translate(location.x, location.y);
        }
        final Rectangle bounds = getBounds();
        final Shape backupClip = g.getClip();
        g.clip(bounds);
        if (intersects(g.getClipBounds()) || g.getClipBounds().contains(bounds)) {
            if (isOpaque()) {
                g.setPaint(getBackground());
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
            renderWidget(g0);
            renderChildren(g0);
        }
        g.setClip(backupClip);
        g.setTransform(backupTransform);
    }

    protected abstract void renderWidget(Graphics g);

    protected void renderChildren(Graphics g0) {
        if (children == null) {
            return;
        }
        final Graphics2D g = (Graphics2D) g0;
        final Rectangle clipBounds = g.getClipBounds();
        for (Widget<?> child : children) {
            if (!child.intersects(clipBounds) && !clipBounds.contains(child.getBounds())) {
                continue;
            }
            if (child instanceof PathWidget) {
                if (renderColorsWithPathBuf == null) {
                    renderColorsWithPathBuf = new HashMap<Color, GeneralPath>();
                }
                final Color color = child.getForeground();
                GeneralPath renderPathBuf = renderColorsWithPathBuf.get(color);
                if (renderPathBuf == null) {
                    renderPathBuf = borrowPath();
                    renderColorsWithPathBuf.put(color, renderPathBuf);
                }
                final GeneralPath path = ((PathWidget) child).getPath();
                Shape shape = path;
                final Point location = child.getLocation();
                if (!(location.x == 0 && location.y == 0)) {
                    final AffineTransform transform = AffineTransform.getTranslateInstance(location.x, location.y);
                    shape = path.createTransformedShape(transform);
                }
                renderPathBuf.append(shape, false);
            } else {
                child.render(g);
            }
        }
        if (renderColorsWithPathBuf != null) {
            for (Color color : renderColorsWithPathBuf.keySet()) {
                final GeneralPath path = renderColorsWithPathBuf.get(color);
                g.setColor(color);
                g.draw(path);
                returnPath(path);
            }
            renderColorsWithPathBuf.clear();
        }
    }

    /** override it if only contains children (plotWidget() do noting) */
    public boolean isContainerOnly() {
        return false;
    }

    public <T extends Widget<?>> T addChild(T child) {
        if (children == null) {
            children = new ArrayList<Widget<?>>();
        }
        children.add(child);
        return child;
    }

    public void removeChild(Widget<?> child) {
        if (children != null) {
            children.remove(child);
        }
    }

    public List<Widget<?>> getChildren() {
        if (children != null) {
            return children;
        }
        return new ArrayList<Widget<?>>(0);
    }

    public void resetChildren() {
        if (children != null) {
            for (Widget<?> child : children) {
                child.reset();
            }
        }
    }

    public void clearChildren() {
        if (children != null) {
            children.clear();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Widget<?>> List<T> lookupChildren(Class<T> widgetType, Color foreground) {
        List<T> result = new ArrayList<T>();
        if (children != null) {
            for (Widget<?> child : children) {
                if (widgetType.isInstance(child) && child.getForeground().equals(foreground)) {
                    result.add((T) child);
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T extends Widget<?>> T lookupFirstChild(Class<T> widgetType, Color foreground) {
        if (children != null) {
            for (Widget<?> child : children) {
                if (widgetType.isInstance(child) && child.getForeground().equals(foreground)) {
                    return (T) child;
                }
            }
        }
        return null;
    }

    public Action addAction(Action action) {
        if (actions == null) {
            actions = new ArrayList<Action>();
        }
        actions.add(action);
        return action;
    }

    public <T extends Action> T lookupAction(Class<T> type) {
        if (actions != null) {
            for (Action action : actions) {
                if (type.isInstance(action)) {
                    return (T) action;
                }
            }
        }
        return null;
    }

    public <T extends Action> T lookupActionAt(Class<T> type, Point point) {
        if (children != null) {
            for (Widget<?> child : children) {
                if (child.contains(point)) {
                    return child.lookupAction(type);
                }
            }
        }
        return getBounds().contains(point) ? lookupAction(type) : null;
    }

    public void reset() {
        if (children != null) {
            for (Widget<?> child : children) {
                child.reset();
            }
            children.clear();
        }
    }
}
