package com.ingenico.piccolo.event;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PZoomEventHandler;

@Deprecated
public class PXZoomEventHandler extends PZoomEventHandler {

    @Override
    protected void dragActivityStep(PInputEvent aEvent) {
        System.out.println("plouf l'event: " + aEvent.getModifiers());
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
        camera.scaleViewAboutPoint(scaleDelta, 1, viewZoomPoint.getX(), viewZoomPoint.getY());
    }
}
