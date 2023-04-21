package edu.udo.scaffoldhunter.view.scaffoldtree;

import java.awt.geom.Point2D;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.util.PBounds;

/**
 * The <b>VZoomHandler</b> class provides event handlers for zooming via mouse wheel
 * and gives basic methods for zooming in general.
 *
 * @author <i>Projektgruppe 504, Chair of Algorithm Engineering,
 * Computer Science Department, University of Dortmund</i><br>
 * <b>Gebhard Schrader</b>
 * @version 1.0
 * @see VEventHandler
 */
public class VZoomHandler extends PBasicInputEventHandler {

    private static final double MAX_ZOOMFACTOR_PERCENT = 1000;

    private static final double MIN_ZOOMFACTOR_PERCENT = 0.03;

    private static final double MIN_ZOOMFACTOR_PICCOLO = 3;

    private static final double SCALE_DELTA = 0.095;

    private double overviewScale = 1;

    private VCanvas mycanvas;

    private boolean zoomactive = true;

    /**
     * Creates a new VZoomHandler for the <code>canvas</code>
     * @param canvas that will use this EventHandler
     */
    public VZoomHandler(VCanvas canvas) {
        super();
        PInputEventFilter pief = new PInputEventFilter();
        pief.rejectAllEventTypes();
        pief.setAcceptsMouseWheelRotated(true);
        setEventFilter(pief);
        mycanvas = canvas;
    }

    /**
     * Evaluates mouse wheel rotation and calls the correct zoom method.
     * @param event the generated mouseWheelRotation event
     */
    @Override
    public void mouseWheelRotated(PInputEvent event) {
        if (event.getWheelRotation() > 0) zoomOut(event.getCamera(), event.getPosition()); else zoomIn(event.getCamera(), event.getPosition());
    }

    /**
     * This method makes a <code>camera</code> zoom to a viewpoint with a given <code>zoomfactor</code>.
     * @param camera PCamera, the camera that will make the zoom
     * @param viewzoompoint Point2D, the new view center
     * @param zoomfactor the new scale in percent
     */
    public void setZoom(PCamera camera, Point2D viewzoompoint, int zoomfactor) {
        double newScale;
        setOverviewScale(camera);
        newScale = (100 * overviewScale) / zoomfactor;
        if (getZoom(camera) <= MAX_ZOOMFACTOR_PERCENT && getZoom(camera) >= MIN_ZOOMFACTOR_PERCENT && camera.getViewScale() < MIN_ZOOMFACTOR_PICCOLO) {
            camera.scaleViewAboutPoint(newScale, viewzoompoint.getX(), viewzoompoint.getY());
        }
    }

    /**
     * Returns the current zoom scale of a <code>camera</code> in percent.
     * @param camera a PCamera
     * @return the current zoom scale in percent
     */
    public double getZoom(PCamera camera) {
        PBounds viewBounds = camera.getViewBounds();
        PBounds centerBounds = mycanvas.getNodeLayer().getFullBoundsReference();
        double currentScale = camera.getScale();
        overviewScale = Math.min(viewBounds.getWidth() / centerBounds.getWidth(), viewBounds.getHeight() / centerBounds.getHeight());
        double percent = 100 * (overviewScale / currentScale);
        if (mycanvas.getInit()) {
            percent = 100;
        } else {
            if (percent < 5) percent = (Math.floor(percent * 100) / 100); else percent = (int) percent;
        }
        return percent;
    }

    /**
     * Makes a <code>camera</code> zoom in a default step with a fixed value at
     * a view point
     * @param camera the camera that will make the zoom
     * @param viewzoompoint Point2D, the new view center
     */
    public void zoomIn(PCamera camera, Point2D viewzoompoint) {
        if (zoomactive) {
            mycanvas.updateLayout();
            if (getZoom(camera) >= MIN_ZOOMFACTOR_PERCENT && camera.getViewScale() < MIN_ZOOMFACTOR_PICCOLO) {
                camera.scaleViewAboutPoint((1 + SCALE_DELTA), viewzoompoint.getX(), viewzoompoint.getY());
            }
        }
    }

    /**
     * Makes a <code>camera</code> zoom out a default step with a fixed value at
     * a view point
     * @param camera the camera that will make the zoom
     * @param viewzoompoint Point2D, the new view center
     */
    public void zoomOut(PCamera camera, Point2D viewzoompoint) {
        if (zoomactive) {
            mycanvas.updateLayout();
            if (getZoom(camera) <= MAX_ZOOMFACTOR_PERCENT) {
                camera.scaleViewAboutPoint((1 - SCALE_DELTA), viewzoompoint.getX(), viewzoompoint.getY());
            }
        }
    }

    /**
     * This method removes some graph painting bugs.
     * It zooms first in and then out a default zoom step.
     * @param camera the camera that will make the zoom
     */
    public void zoomStutter(PCamera camera) {
        Point2D.Double viewzoompoint = new Point2D.Double();
        double x = camera.getViewBounds().getCenterX();
        double y = camera.getViewBounds().getCenterY();
        viewzoompoint.setLocation(x, y);
        zoomIn(camera, viewzoompoint);
        zoomOut(camera, viewzoompoint);
    }

    /**
     * Calculates the piccolo scale factor for the overview of the whole graph.
     * This is allways equal to 100% in dynamic scale.
     */
    void setOverviewScale(PCamera camera) {
        PBounds viewBounds = camera.getViewBounds();
        PBounds centerBounds = mycanvas.getNodeLayer().getFullBounds();
        overviewScale = Math.min(viewBounds.getWidth() / centerBounds.getWidth(), viewBounds.getHeight() / centerBounds.getHeight());
    }

    double getOverviewScale() {
        return overviewScale;
    }

    /**
     * Enables ZoomIn and ZoomOut
     * @param active true (default) if zoom should be activated
     */
    public void setZoomActive(boolean active) {
        zoomactive = active;
    }

    /**
     * Returns the maximum view magnification factor this event handler is bound to.
     * The default is value can be found in the VISControl.
     * @return the maximum view scale value
     */
    public double getMaxScale() {
        return MAX_ZOOMFACTOR_PERCENT;
    }
}
