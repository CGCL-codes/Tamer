package com.vividsolutions.jump.demo.delineation;

import java.awt.Cursor;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.util.Collection;
import javax.swing.Icon;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.cursortool.DragTool;

/**
 *  Enables the user to move a vertex of an existing delineation. Thus, the user
 *  can take an unanchored vertex and anchor it to a point on a coastline.
 */
public class EditDelineationTool extends DragTool {

    private static final int TOLERANCE = 5;

    private Feature delineationBeingEdited;

    private String snappedAttributeName;

    private Coordinate stationaryVertex;

    private Coordinate vertexToMove;

    public EditDelineationTool(Collection snapPolicies) {
        getSnapManager().addPolicies(snapPolicies);
        setColor(DelineationUtil.TOOL_COLOR);
        setStrokeWidth(DelineationUtil.TOOL_STROKE_WIDTH);
    }

    public Icon getIcon() {
        return DelineationUtil.ICON;
    }

    public Cursor getCursor() {
        return DelineationUtil.CURSOR;
    }

    public void mousePressed(MouseEvent e) {
        try {
            delineationBeingEdited = findDelineationWithVertexAt(e.getPoint());
            if (delineationBeingEdited == null) {
                return;
            }
            super.mousePressed(e);
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }

    protected Shape getShape(Point2D source, Point2D destination) throws NoninvertibleTransformException {
        return new Line2D.Double(getPanel().getViewport().toViewPoint(new Point2D.Double(stationaryVertex.x, stationaryVertex.y)), destination);
    }

    private Layer delineationLayer() {
        return getPanel().getLayerManager().getLayer(DelineationUtil.DELINEATION_LAYER_NAME);
    }

    private Feature findDelineationWithVertexAt(Point2D viewPoint) throws Exception {
        if ((delineationLayer() == null) || !delineationLayer().isVisible()) {
            return null;
        }
        Collection delineationsWithVertex = getPanel().featuresWithVertex(viewPoint, TOLERANCE, delineationLayer().getFeatureCollectionWrapper().query(getPanel().getViewport().getEnvelopeInModelCoordinates()));
        if (delineationsWithVertex.isEmpty()) {
            return null;
        }
        Feature delineation = (Feature) delineationsWithVertex.iterator().next();
        Coordinate[] coordinates = delineation.getGeometry().getCoordinates();
        DelineationUtil.checkDelineationCoordinates(coordinates);
        Coordinate c = getPanel().getViewport().toModelCoordinate(viewPoint);
        if (coordinates[0].distance(c) < coordinates[1].distance(c)) {
            stationaryVertex = coordinates[1];
            vertexToMove = coordinates[0];
            snappedAttributeName = DelineationUtil.SOURCE_SNAPPED_ATTRIBUTE_NAME;
        } else {
            stationaryVertex = coordinates[0];
            vertexToMove = coordinates[1];
            snappedAttributeName = DelineationUtil.DESTINATION_SNAPPED_ATTRIBUTE_NAME;
        }
        return delineation;
    }

    private void moveVertex(Feature delineation, Coordinate finalLocation) throws Exception {
        vertexToMove.setCoordinate(finalLocation);
        delineation.getGeometry().geometryChanged();
        delineation.setAttribute(snappedAttributeName, DelineationUtil.toString(getSnapManager().wasSnapCoordinateFound()));
    }

    protected void gestureFinished() throws Exception {
        moveVertex(delineationBeingEdited, getModelDestination());
        delineationLayer().fireAppearanceChanged();
    }
}
