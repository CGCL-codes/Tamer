package santa.jpaint.kernel.shapes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.ImageObserver;
import santa.nice.math.MathUtility;
import santa.jpaint.gui.MainFrame;
import santa.jpaint.kernel.tools.MoveTool;

/**
 * The abstract super class of floating shapes
 * @author Santa
 *
 */
public abstract class FloatingShape {

    /**
	 * The editor for this shape
	 */
    ShapeEditor editor;

    /**
	 * The center X position of this shape
	 */
    double centerX;

    /**
	 * The center Y position of this shape
	 */
    double centerY;

    /**
	 * The thickness of lines
	 */
    float lineThickness;

    /**
	 * The rotation angle
	 */
    double theta;

    /**
	 * The scale value on X coord
	 */
    double zoomX;

    /**
	 * The scale value on Y coord
	 */
    double zoomY;

    /**
	 * The width of the floating image
	 */
    double width;

    /**
	 * The height of the floating image
	 */
    double height;

    /**
	 * Whether the image is selected
	 */
    boolean selected = false;

    /**
	 * The action that we are resizing on the left border
	 */
    public static final int RESIZE_LEFT = 1;

    /**
	 * The action that we are resizing on the right border
	 */
    public static final int RESIZE_RIGHT = 2;

    /**
	 * The action that we are resizing on the top border
	 */
    public static final int RESIZE_TOP = 4;

    /**
	 * The action that we are resizing on the bottom border
	 */
    public static final int RESIZE_BOTTOM = 8;

    /**
	 * Default constructor, initialize the scale and rotation
	 */
    public FloatingShape() {
        zoomX = 1.0;
        zoomY = 1.0;
        theta = 0.0;
    }

    /**
	 * Paint the floating image, if selected, the editor will also be shown
	 * @param g The graphics handler
	 * @param observer The observer of this image
	 */
    public abstract void paint(Graphics2D g, ImageObserver observer);

    /**
	 * Finds out the center point of this floating shape
	 * @return The center point
	 */
    public Point getCenterPoint() {
        return new Point((int) centerX, (int) centerY);
    }

    /**
	 * Sets the center point
	 * @param x The center point X value
	 * @param y The center point Y value
	 */
    public void setCenterPoint(int x, int y) {
        centerX = x;
        centerY = y;
    }

    /**
	 * Change the size of this image, that is, scaling it
	 * @param dx Change value on x coord
	 * @param dy Change value on y coord
	 * @param resizeOption The option for scaling, whether it is extending
	 * towards top/bottom/left/right
	 */
    public void resize(double dx, double dy, int resizeOption) {
        centerX += dx / 2;
        centerY += dy / 2;
        double oldDx = dx;
        double oldDy = dy;
        dx = oldDx * Math.cos(theta) + oldDy * Math.sin(theta);
        dy = oldDy * Math.cos(theta) - oldDx * Math.sin(theta);
        if ((resizeOption & RESIZE_TOP) != 0) {
            zoomY = (height * zoomY - dy) / height;
        } else if ((resizeOption & RESIZE_BOTTOM) != 0) {
            zoomY = (height * zoomY + dy) / height;
        }
        if ((resizeOption & RESIZE_LEFT) != 0) {
            zoomX = (width * zoomX - dx) / width;
        } else if ((resizeOption & RESIZE_RIGHT) != 0) {
            zoomX = (width * zoomX + dx) / width;
        }
    }

    /**
	 * Sets the rotation angle
	 * @param theta Rotation angle
	 */
    public void setRotate(double theta) {
        this.theta = theta;
    }

    /**
	 * Gets the rotation angle
	 * @return The rotation angle
	 */
    public double getRotate() {
        return theta;
    }

    /**
	 * Let the floating shape be selected
	 * @param b Boolean value indicating whether the shape should be selected
	 */
    public void setSelected(boolean b) {
        selected = b;
    }

    /**
	 * Tests whether the shape is selected
	 * @return Whether the shape is selected
	 */
    public boolean isSelected() {
        return selected;
    }

    /**
	 * Gets the editor of the shape
	 * @return
	 */
    public ShapeEditor getEditor() {
        return editor;
    }

    /**
	 * Translate an point on image surface into rotated/shears/scaled coord
	 * @param p Point to be translated
	 * @return New Coord
	 */
    public Point translate(Point p) {
        Point pt = new Point((int) (p.x - centerX), (int) (p.y - centerY));
        double oldX = pt.x;
        double oldY = pt.y;
        pt.x = (int) (oldX * Math.cos(theta) + oldY * Math.sin(theta));
        pt.y = (int) (-oldX * Math.sin(theta) + oldY * Math.cos(theta));
        pt.x /= zoomX;
        pt.y /= zoomY;
        return pt;
    }

    /**
	 * Test if a point is in a floating shape.
	 * By default, it will test whether the point is in the rectangle shape.
	 * @param p Floating point, on swing coord (not translatedzoomed by the canvas)
	 * @return Whether the point is in the shape
	 */
    public boolean testHit(Point p) {
        Point pt = translate(MainFrame.getCurrentEnv().getCanvas().translateZoom(p));
        final double MARGIN = 5.0;
        return -width / 2 - MARGIN < pt.x && pt.x < width / 2 + MARGIN && -height / 2 - MARGIN < pt.y && pt.y < height / 2 + MARGIN;
    }

    /**
	 * Given a point, test the current action for this point.
	 * By default, returns MoveTool.Action_MOVE.
	 * @param p The point to test, on swing coord (not translatedzoomed by the canvas)
	 * @return The action type
	 */
    public int testAction(Point p) {
        Point pt = translate(MainFrame.getCurrentEnv().getCanvas().translateZoom(p));
        int actionType = MoveTool.ACTION_MOVE;
        final double POINT_RANGE = 5.0;
        if (MathUtility.distance(pt.x, pt.y, -width / 2, -height / 2) < POINT_RANGE) {
            actionType = MoveTool.ACTION_RESIZE_TOP_LEFT;
        } else if (MathUtility.distance(pt.x, pt.y, width / 2, -height / 2) < POINT_RANGE) {
            actionType = MoveTool.ACTION_RESIZE_TOP_RIGHT;
        } else if (MathUtility.distance(pt.x, pt.y, -width / 2, height / 2) < POINT_RANGE) {
            actionType = MoveTool.ACTION_RESIZE_BOTTOM_LEFT;
        } else if (MathUtility.distance(pt.x, pt.y, width / 2, height / 2) < POINT_RANGE) {
            actionType = MoveTool.ACTION_RESIZE_BOTTOM_RIGHT;
        } else if (MathUtility.distance(pt.x, pt.y, 0, -height / 2) < POINT_RANGE) {
            actionType = MoveTool.ACTION_ROTATE;
        }
        return actionType;
    }
}
