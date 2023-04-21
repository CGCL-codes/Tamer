package dsa.distance.ui.renderer;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import dsa.distance.data.RouteManager;

/**
 * Layer, der den Pfad zeichnet. 
 * @Deprecated Ben�tigt unter Windows extra DLLs, die nicht standard sind. Keine gute L�sung. 
 */
public class OldPathLayer implements ILayer {

    public void dispose() {
    }

    public boolean render(GC gc, SharedLayerData layerData, Image map, int viewX, int viewY, int scaledViewWidth, int scaledViewHeight, double zoomFactor, int projectionX, int projectionY, int projectionWidth, int projectionHeight) {
        ArrayList<Point> wayPoints = RouteManager.getInstance().getWaypoints();
        int[] shapeCoords = new int[wayPoints.size() * 2];
        for (int i = 0; i < wayPoints.size(); i += 1) {
            shapeCoords[i * 2] = (int) ((wayPoints.get(i).x - viewX) * zoomFactor);
            shapeCoords[i * 2 + 1] = (int) ((wayPoints.get(i).y - viewY) * zoomFactor);
        }
        gc.setLineWidth(layerData.CONNECTOR_LINE_STROKE_WIDTH);
        gc.setForeground(layerData.waypointBorderColor);
        gc.setAlpha(255);
        gc.drawPolyline(shapeCoords);
        gc.setLineWidth(layerData.CONNECTOR_LINE_STROKE_WIDTH);
        gc.setForeground(layerData.shadowColor);
        gc.setAlpha(layerData.SHADOW_ALPHA);
        gc.drawPolyline(shapeCoords);
        gc.setAlpha(255);
        gc.setForeground(layerData.lineColor);
        gc.setLineWidth(layerData.CONNECTOR_LINE_WIDTH);
        gc.drawPolyline(shapeCoords);
        gc.setLineWidth(2);
        Point firstPoint = null;
        if (wayPoints.size() > 0) firstPoint = wayPoints.get(0);
        Point lastPoint = null;
        double distanceBuffer = 0;
        for (Point currentPoint : wayPoints) {
            if (lastPoint != null) {
                double currentDistance = Point2D.distance(lastPoint.x, lastPoint.y, currentPoint.x, currentPoint.y) * RouteManager.REFERENCE_RATIO;
                if (lastPoint == firstPoint) {
                    gc.setLineWidth(layerData.STROKE_LINE_WIDTH);
                    gc.setAlpha(layerData.SHADOW_ALPHA);
                    gc.setBackground(layerData.shadowColor);
                    gc.fillOval((int) ((lastPoint.x - viewX) * zoomFactor) - layerData.OVAL_OFFSET + 3, (int) ((lastPoint.y - viewY) * zoomFactor) - layerData.OVAL_OFFSET + 3, layerData.OVAL_WIDTH, layerData.OVAL_WIDTH);
                    gc.setAlpha(255);
                    gc.setForeground(layerData.waypointBorderColor);
                    gc.setBackground(layerData.waypointColor);
                    gc.fillOval((int) ((lastPoint.x - viewX) * zoomFactor) - layerData.OVAL_OFFSET, (int) ((lastPoint.y - viewY) * zoomFactor) - layerData.OVAL_OFFSET, layerData.OVAL_WIDTH, layerData.OVAL_WIDTH);
                    gc.drawOval((int) ((lastPoint.x - viewX) * zoomFactor) - layerData.OVAL_OFFSET, (int) ((lastPoint.y - viewY) * zoomFactor) - layerData.OVAL_OFFSET, layerData.OVAL_WIDTH, layerData.OVAL_WIDTH);
                    gc.setAlpha(layerData.LENGTH_TEXT_ALPHA);
                    gc.setForeground(layerData.textColor);
                    gc.setBackground(layerData.textBackgroundColor);
                    gc.setFont(layerData.lengthDescriptionFont);
                    gc.drawText("Start", (int) ((lastPoint.x - viewX) * zoomFactor), (int) ((lastPoint.y - viewY) * zoomFactor) + layerData.OVAL_WIDTH);
                } else {
                    gc.setLineWidth(layerData.SMALL_LINE_WIDTH);
                    gc.setAlpha(layerData.SHADOW_ALPHA);
                    gc.setBackground(layerData.shadowColor);
                    gc.fillRectangle((int) ((lastPoint.x - viewX) * zoomFactor) - layerData.SQUARE_OFFSET + 2, (int) ((lastPoint.y - viewY) * zoomFactor) - layerData.SQUARE_OFFSET + 2, layerData.SQUARE_WIDTH, layerData.SQUARE_WIDTH);
                    gc.setAlpha(255);
                    gc.setForeground(layerData.waypointBorderColor);
                    gc.setBackground(layerData.waypointColor);
                    gc.fillRectangle((int) ((lastPoint.x - viewX) * zoomFactor) - layerData.SQUARE_OFFSET, (int) ((lastPoint.y - viewY) * zoomFactor) - layerData.SQUARE_OFFSET, layerData.SQUARE_WIDTH, layerData.SQUARE_WIDTH);
                    gc.drawRectangle((int) ((lastPoint.x - viewX) * zoomFactor) - layerData.SQUARE_OFFSET, (int) ((lastPoint.y - viewY) * zoomFactor) - layerData.SQUARE_OFFSET, layerData.SQUARE_WIDTH, layerData.SQUARE_WIDTH);
                }
                distanceBuffer += currentDistance;
                if (distanceBuffer > layerData.MIN_SHOWING_DISTANCE) {
                    distanceBuffer = Math.round(distanceBuffer * 100d) / 100d;
                    int textX = (int) (((Math.max(lastPoint.x, currentPoint.x) - Math.min(lastPoint.x, currentPoint.x)) / 2 + Math.min(lastPoint.x, currentPoint.x) - viewX) * zoomFactor);
                    int textY = (int) (((Math.max(lastPoint.y, currentPoint.y) - Math.min(lastPoint.y, currentPoint.y)) / 2 + Math.min(lastPoint.y, currentPoint.y) - viewY) * zoomFactor);
                    gc.setAlpha(layerData.LENGTH_TEXT_ALPHA);
                    gc.setForeground(layerData.textColor);
                    gc.setBackground(layerData.textBackgroundColor);
                    gc.setFont(layerData.lengthDescriptionFont);
                    gc.drawText(distanceBuffer + RouteManager.DISTANCE_UNIT, textX, textY);
                    distanceBuffer = 0;
                }
            }
            lastPoint = currentPoint;
        }
        if (lastPoint != null) {
            gc.setLineWidth(layerData.STROKE_LINE_WIDTH);
            gc.setAlpha(layerData.SHADOW_ALPHA);
            gc.setBackground(layerData.shadowColor);
            gc.fillOval((int) ((lastPoint.x - viewX) * zoomFactor) - layerData.OVAL_OFFSET + 3, (int) ((lastPoint.y - viewY) * zoomFactor) - layerData.OVAL_OFFSET + 3, layerData.OVAL_WIDTH, layerData.OVAL_WIDTH);
            gc.setAlpha(255);
            gc.setForeground(layerData.waypointBorderColor);
            gc.setBackground(layerData.waypointColor);
            gc.fillOval((int) ((lastPoint.x - viewX) * zoomFactor) - layerData.OVAL_OFFSET, (int) ((lastPoint.y - viewY) * zoomFactor) - layerData.OVAL_OFFSET, layerData.OVAL_WIDTH, layerData.OVAL_WIDTH);
            gc.drawOval((int) ((lastPoint.x - viewX) * zoomFactor) - layerData.OVAL_OFFSET, (int) ((lastPoint.y - viewY) * zoomFactor) - layerData.OVAL_OFFSET, layerData.OVAL_WIDTH, layerData.OVAL_WIDTH);
            gc.setAlpha(layerData.LENGTH_TEXT_ALPHA);
            gc.setForeground(layerData.textColor);
            gc.setBackground(layerData.textBackgroundColor);
            gc.setFont(layerData.lengthDescriptionFont);
            double totalLen = Math.round(RouteManager.getInstance().getLength() * 1000d) / 1000d;
            gc.drawText("Ziel: " + totalLen + " " + RouteManager.DISTANCE_UNIT, (int) ((lastPoint.x - viewX) * zoomFactor), (int) ((lastPoint.y - viewY) * zoomFactor) + layerData.OVAL_WIDTH);
        }
        return true;
    }

    public void init(SharedLayerData layerData) {
    }
}
