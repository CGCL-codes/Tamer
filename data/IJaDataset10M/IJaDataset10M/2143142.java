package edu.umd.cs.piccolo.event;

import java.awt.event.InputEvent;
import java.awt.geom.Point2D;
import edu.umd.cs.piccolo.PCamera;

/**
 * <b>ZoomEventhandler</b> provides event handlers for basic zooming of the
 * canvas view with the right (third) button. The interaction is that the
 * initial mouse press defines the zoom anchor point, and then moving the mouse
 * to the right zooms with a speed proportional to the amount the mouse is moved
 * to the right of the anchor point. Similarly, if the mouse is moved to the
 * left, the the view is zoomed out.
 * <P>
 * On a Mac with its single mouse button one may wish to change the standard
 * right mouse button zooming behavior. This can be easily done with the
 * PInputEventFilter. For example to zoom with button one and shift you would do
 * this:
 * <P>
 * <code>
 * <pre>
 * zoomEventHandler.getEventFilter().setAndMask(InputEvent.BUTTON1_MASK | 
 *                                              InputEvent.SHIFT_MASK);
 * </pre>
 * </code>
 * <P>
 * 
 * @version 1.0
 * @author Jesse Grosjean
 */
public class PZoomEventHandler extends PDragSequenceEventHandler {

    private double minScale = 0;

    private double maxScale = Double.MAX_VALUE;

    private Point2D viewZoomPoint;

    /**
     * Creates a new zoom handler.
     */
    public PZoomEventHandler() {
        super();
        setEventFilter(new PInputEventFilter(InputEvent.BUTTON3_MASK));
    }

    /**
     * Returns the minimum view magnification factor that this event handler is
     * bound by. The default is 0.
     * 
     * @return the minimum camera view scale
     */
    public double getMinScale() {
        return minScale;
    }

    /**
     * Sets the minimum view magnification factor that this event handler is
     * bound by. The camera is left at its current scale even if
     * <code>minScale</code> is larger than the current scale.
     * 
     * @param minScale the minimum scale, must not be negative.
     */
    public void setMinScale(double minScale) {
        this.minScale = minScale;
    }

    /**
     * Returns the maximum view magnification factor that this event handler is
     * bound by. The default is Double.MAX_VALUE.
     * 
     * @return the maximum camera view scale
     */
    public double getMaxScale() {
        return maxScale;
    }

    /**
     * Sets the maximum view magnification factor that this event handler is
     * bound by. The camera is left at its current scale even if
     * <code>maxScale</code> is smaller than the current scale. Use
     * Double.MAX_VALUE to specify the largest possible scale.
     * 
     * @param maxScale the maximum scale, must not be negative.
     */
    public void setMaxScale(double maxScale) {
        this.maxScale = maxScale;
    }

    protected void dragActivityFirstStep(PInputEvent aEvent) {
        viewZoomPoint = aEvent.getPosition();
        super.dragActivityFirstStep(aEvent);
    }

    protected void dragActivityStep(PInputEvent aEvent) {
        PCamera camera = aEvent.getCamera();
        double dx = aEvent.getCanvasPosition().getX() - getMousePressedCanvasPoint().getX();
        double scaleDelta = (1.0 + (0.001 * dx));
        double currentScale = camera.getViewScale();
        double newScale = currentScale * scaleDelta;
        if (newScale < minScale) {
            scaleDelta = minScale / currentScale;
        }
        if ((maxScale > 0) && (newScale > maxScale)) {
            scaleDelta = maxScale / currentScale;
        }
        camera.scaleViewAboutPoint(scaleDelta, viewZoomPoint.getX(), viewZoomPoint.getY());
    }

    /**
     * Returns a string representing the state of this node. This method is
     * intended to be used only for debugging purposes, and the content and
     * format of the returned string may vary between implementations. The
     * returned string may be empty but may not be <code>null</code>.
     * 
     * @return a string representation of this node's state
     */
    protected String paramString() {
        StringBuffer result = new StringBuffer();
        result.append("minScale=" + minScale);
        result.append(",maxScale=" + maxScale);
        result.append(",viewZoomPoint=" + (viewZoomPoint == null ? "null" : viewZoomPoint.toString()));
        result.append(',');
        result.append(super.paramString());
        return result.toString();
    }
}
