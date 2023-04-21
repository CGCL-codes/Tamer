package de.hpi.eworld.networkview.objects;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.view.mxCellState;
import de.hpi.eworld.gui.util.Point2DUtils;
import de.hpi.eworld.model.db.data.ModelElement;
import de.hpi.eworld.networkview.GraphCursor;
import de.hpi.eworld.networkview.objects.handler.Resizer;

/**
 * Abstract class for drawing EnvironmentEvents
 * 
 * @author Markus Behrens, Martin Beck
 */
public abstract class AreaView<T extends ModelElement, S extends Shape> extends GraphicsView<T> {

    private static final transient int DEFAULT_Z_VALUE = 40;

    private static Color FILL_COLOR = new Color(200, 200, 255, 120);

    private static Color STROKE_COLOR = new Color(120, 120, 255);

    protected S basicShape;

    /**
	 * holds an object of the currently used Resizer (either PointResizer,
	 * ShapeChanger or LineResizer)
	 */
    protected Resizer resizer;

    /**
	 * Constructs an EnvironmentEventItem with the given underlying event from
	 * the database
	 * 
	 * @param eventModel
	 */
    AreaView(final T eventModel, boolean withCloseButton, boolean withInfoButton) {
        super(eventModel, resourceNameFromType(eventModel), withCloseButton, withInfoButton);
    }

    @Override
    protected void initializeZLevel() {
        setZLevel(DEFAULT_Z_VALUE);
    }

    @Override
    protected Color getFillColor(mxGraphics2DCanvas canvas, mxCellState state) {
        return FILL_COLOR;
    }

    @Override
    protected Color getStrokeColor(mxGraphics2DCanvas canvas, mxCellState state) {
        return STROKE_COLOR;
    }

    @Override
    protected Rectangle2D calculateBounds() {
        return basicShape.getBounds2D();
    }

    /**
	 * calculates the angle between an fixpoint (usally the center) and the
	 * point the mouse hitted the shape
	 * 
	 * @param point
	 * @return
	 */
    protected abstract double getAngle(Point2D point);

    /**
	 * Sets the mouse cursor for resizing depending on the given angle.
	 * 
	 * @param angle
	 *            The angle on the circle in degree.
	 */
    protected Cursor getResizeCursor(Point2D point) {
        double angle = getAngle(point);
        if (angle > 22.5 && angle < 67.5) {
            return GraphCursor.RESIZE_NE;
        }
        if (angle >= 67.5 && angle <= 112.5) {
            return GraphCursor.RESIZE_N;
        }
        if (angle > 112.5 && angle < 157.5) {
            return GraphCursor.RESIZE_NW;
        }
        if (angle >= 157.5 && angle <= 202.5) {
            return GraphCursor.RESIZE_W;
        }
        if (angle > 202.5 && angle < 247.5) {
            return GraphCursor.RESIZE_SW;
        }
        if (angle >= 247.5 && angle <= 292.5) {
            return GraphCursor.RESIZE_S;
        }
        if (angle > 292.5 && angle < 337.5) {
            return GraphCursor.RESIZE_SE;
        }
        return GraphCursor.RESIZE_E;
    }

    /**
	 * converts from a coordinate of the viewport to a coordinate relative to
	 * the shape
	 * 
	 * @param localPoint
	 * @return
	 */
    protected Point2D normalizePoint(Point2D viewportPoint) {
        Point2D globalPoint = graphController.getGraphCoordinateConverter().viewportToGlobal((Point) viewportPoint);
        Point2D anchorPoint = getAffectionPosition();
        Point2D offset = Point2DUtils.convertPoint(graphController.getComponent().getViewport().getViewPosition());
        Point2D scaledOffset = Point2DUtils.divide(offset, getScale());
        Point2D normalizedPoint = Point2DUtils.subtract(globalPoint, anchorPoint);
        return Point2DUtils.subtract(normalizedPoint, scaledOffset);
    }

    /**
	 * check if mouse is near border and, if so, set an appropriate resize
	 * cursor
	 */
    protected abstract void adaptMouseCursor(Point2D point, boolean controlDown);

    /**
	 * indicates if the current object is resizing
	 * 
	 * @return
	 */
    public boolean isResizing() {
        return resizer != null;
    }

    /**
	 * When mouse is dragged, <br>
	 * if the shape is resizing, update bounds else do drag (in super class)
	 */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (isResizing()) {
            Point2D eventPoint = normalizePoint(e.getPoint());
            resizer.resizeFor(eventPoint);
        } else {
            super.mouseDragged(e);
        }
        e.consume();
    }

    /**
	 * if resize was active, stop it
	 */
    @Override
    public void mouseReleased(final MouseEvent e) {
        adaptMouseCursor(normalizePoint(e.getPoint()), false);
        if ((e.getButton() == MouseEvent.BUTTON1) && isResizing()) {
            resizer.finishResizing();
            propagatePosition();
        } else {
            super.mouseReleased(e);
        }
        e.consume();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        mouseMoved(e);
    }

    /**
	 * When mouse is moved, <br>
	 * if circle is being resized, update the size of the circle<br>
	 * else check if it was moved near the border
	 */
    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        Point2D relativePosition = normalizePoint(e.getPoint());
        adaptMouseCursor(relativePosition, e.isControlDown());
        e.consume();
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        super.mouseExited(e);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        graphController.repaint(this);
        e.consume();
    }

    protected void setBasicShape(S basicShape) {
        this.basicShape = basicShape;
    }

    /**
	 * Translates the shape which should be at 0/0 to an area based on the
	 * actual coordinates
	 */
    public Area translateBasicShapeToOutline(Shape basicShape, Rectangle2D canvasBounds) {
        Point2D offset = new Point2D.Double(canvasBounds.getX() - basicShape.getBounds2D().getMinX(), canvasBounds.getY() - basicShape.getBounds2D().getMinY());
        return translateBasicShapeToOutline(basicShape, offset);
    }

    public Area translateBasicShapeToOutline(Shape basicShape, Point2D offset) {
        Area translated = new Area(basicShape);
        translated.transform(AffineTransform.getScaleInstance(getScale(), getScale()));
        AffineTransform transformer = new AffineTransform();
        transformer.translate(offset.getX(), offset.getY());
        translated.transform(transformer);
        return translated;
    }

    @Override
    public Area getOutline() {
        if (getState() == null) return null;
        return translateBasicShapeToOutline(basicShape, canvasBounds(getState()));
    }

    @Override
    public void paintImage(Graphics2D painter, Rectangle2D canvasBounds) {
        Area translated = translateBasicShapeToOutline(basicShape, canvasBounds);
        painter.setColor(getStrokeColor(null, null));
        painter.draw(translated);
        painter.setColor(getFillColor(null, null));
        painter.fill(translated);
        painter.drawImage(getPixmap(), (int) (translated.getBounds().getCenterX() - getPixmap().getWidth(null) / 2), (int) (translated.getBounds().getCenterY() - getPixmap().getWidth(null) / 2), null);
    }

    public S getBasicShape() {
        return basicShape;
    }

    @Override
    protected boolean isVisibleAtCurrentLevelOfDetail() {
        return true;
    }
}
