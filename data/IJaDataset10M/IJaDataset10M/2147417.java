package piccolo;

import java.awt.geom.Point2D;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

/** Alternative Zoom event handler to the standard Piccolo PZoomEventHandler<br>
 * 	Use mouse wheel to zoom in/out 
 * 
 * @author sal
 */
public class MouseWheelZoomEventHandler extends PBasicInputEventHandler {

    private Point2D viewZoomPoint;

    private double minScale = 0.1;

    private double maxScale = 2;

    @Override
    public void mouseMoved(PInputEvent event) {
        viewZoomPoint = event.getPosition();
    }

    public void mouseWheelRotated(PInputEvent event) {
        PCamera camera = event.getCamera();
        double scaleDelta;
        if (event.getWheelRotation() == 1) {
            scaleDelta = 0.8;
        } else {
            scaleDelta = 1.2;
        }
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

    public double getMinScale() {
        return minScale;
    }

    public void setMinScale(double minScale) {
        this.minScale = minScale;
    }

    public double getMaxScale() {
        return maxScale;
    }

    public void setMaxScale(double maxScale) {
        this.maxScale = maxScale;
    }
}
