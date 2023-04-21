package gov.nasa.worldwindx.examples.shapebuilder;

import gov.nasa.worldwind.*;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.pick.PickedObjectList;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.render.airspaces.editor.AirspaceEditorUtil;
import gov.nasa.worldwind.render.markers.*;
import gov.nasa.worldwind.terrain.Terrain;
import gov.nasa.worldwind.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * @author pabercrombie
 * @version $Id: ExtrudedPolygonEditor.java 1 2011-07-16 23:22:47Z dcollins $
 */
public class ExtrudedPolygonEditor extends AbstractShapeEditor {

    public static final String MOVE_VERTEX_ACTION = "gov.nasa.worldwind.ExtrudedPolygonEditor.MoveVertexAction";

    public static final String CHANGE_HEIGHT_ACTION = "gov.nasa.worldwind.ExtrudedPolygonEditor.ChangeHeightAction";

    public static final String MOVE_POLYGON_ACTION = "gov.nasa.worldwind.ExtrudedPolygonEditor.MovePolygonAction";

    protected ExtrudedPolygon polygon;

    protected MarkerRenderer markerRenderer;

    java.util.List<Marker> controlPoints;

    protected BasicMarkerAttributes vertexControlAttributes;

    protected BasicMarkerAttributes heightControlAttributes;

    protected ControlPointMarker activeControlPoint;

    protected int activeControlPointIndex;

    public ExtrudedPolygonEditor() {
        this.markerRenderer = new MarkerRenderer();
        this.markerRenderer.setKeepSeparated(false);
        this.markerRenderer.setOverrideMarkerElevation(false);
        this.markerRenderer.setEnablePickSizeReturn(true);
        this.assembleMarkerAttributes();
        this.initializeAnnotation();
        this.unitsFormat = new UnitsFormat();
    }

    public ExtrudedPolygon getPolygon() {
        return this.polygon;
    }

    public void setPolygon(ExtrudedPolygon polygon) {
        if (polygon == null) {
            String message = "nullValue.Shape";
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        this.polygon = polygon;
    }

    public void setShape(AbstractShape shape) {
        setPolygon((ExtrudedPolygon) shape);
    }

    public String getEditMode() {
        return this.editMode;
    }

    public void setEditMode(String editMode) {
    }

    protected void assembleControlPoints(DrawContext dc) {
        this.controlPoints = new ArrayList<Marker>();
        this.assembleVertexControlPoints(dc);
        this.assembleHeightControlPoints();
    }

    protected void assembleVertexControlPoints(DrawContext dc) {
        Terrain terrain = dc.getTerrain();
        ExtrudedPolygon polygon = this.getPolygon();
        Position refPos = polygon.getReferencePosition();
        Vec4 refPoint = terrain.getSurfacePoint(refPos.getLatitude(), refPos.getLongitude(), 0);
        int altitudeMode = polygon.getAltitudeMode();
        double height = polygon.getHeight();
        Vec4 vaa = null;
        double vaaLength = 0;
        double vaLength = 0;
        int i = 0;
        for (LatLon location : polygon.getOuterBoundary()) {
            Vec4 vert;
            if (altitudeMode == WorldWind.CONSTANT || !(location instanceof Position)) {
                if (vaa == null) {
                    vaa = refPoint.multiply3(height / refPoint.getLength3());
                    vaaLength = vaa.getLength3();
                    vaLength = refPoint.getLength3();
                }
                vert = terrain.getSurfacePoint(location.getLatitude(), location.getLongitude(), 0);
                double delta = vaLength - vert.dot3(refPoint) / vaLength;
                vert = vert.add3(vaa.multiply3(1d + delta / vaaLength));
            } else if (altitudeMode == WorldWind.RELATIVE_TO_GROUND) {
                vert = terrain.getSurfacePoint(location.getLatitude(), location.getLongitude(), ((Position) location).getAltitude());
            } else {
                vert = terrain.getGlobe().computePointFromPosition(location.getLatitude(), location.getLongitude(), ((Position) location).getAltitude() * terrain.getVerticalExaggeration());
            }
            Position vertexPosition = this.wwd.getModel().getGlobe().computePositionFromPoint(vert);
            this.controlPoints.add(new ControlPointMarker(MOVE_VERTEX_ACTION, vertexPosition, vert, this.vertexControlAttributes, i));
            i++;
        }
    }

    protected void assembleHeightControlPoints() {
        if (this.controlPoints.size() < 2) return;
        Position firstVertex = this.controlPoints.get(0).getPosition();
        Position secondVertex = this.controlPoints.get(1).getPosition();
        Globe globe = this.wwd.getModel().getGlobe();
        Vec4 firstPoint = globe.computePointFromPosition(firstVertex);
        Vec4 secondPoint = globe.computePointFromPosition(secondVertex);
        Vec4 halfwayPoint = firstPoint.add3(secondPoint).divide3(2.0);
        Position halfwayPosition = globe.computePositionFromPoint(halfwayPoint);
        this.controlPoints.add(new ControlPointMarker(CHANGE_HEIGHT_ACTION, halfwayPosition, halfwayPoint, this.heightControlAttributes, this.controlPoints.size()));
    }

    @Override
    protected void doRender(DrawContext dc) {
        if (this.frameTimestamp != dc.getFrameTimeStamp()) {
            this.assembleControlPoints(dc);
            this.frameTimestamp = dc.getFrameTimeStamp();
        }
        this.markerRenderer.render(dc, this.controlPoints);
        if (this.annotation != null && isShowAnnotation()) {
            this.annotation.render(dc);
        }
    }

    @Override
    protected void doPick(DrawContext dc, Point point) {
        this.doRender(dc);
    }

    protected void assembleMarkerAttributes() {
        this.vertexControlAttributes = new BasicMarkerAttributes();
        this.vertexControlAttributes.setMaterial(Material.BLUE);
        this.heightControlAttributes = new BasicMarkerAttributes();
        this.heightControlAttributes.setMaterial(Material.RED);
    }

    public void mouseClicked(MouseEvent e) {
        if (this.isArmed()) {
            if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                Object topObject = null;
                PickedObjectList pickedObjects = this.wwd.getObjectsAtCurrentPosition();
                if (pickedObjects != null) topObject = pickedObjects.getTopObject();
                if (topObject instanceof ControlPointMarker) {
                    this.removeVertex((ControlPointMarker) topObject);
                    e.consume();
                } else {
                    this.addVertex(e.getPoint());
                    e.consume();
                }
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        Point lastMousePoint = this.mousePoint;
        this.mousePoint = e.getPoint();
        if (lastMousePoint == null) lastMousePoint = this.mousePoint;
        if (isShowAnnotation()) {
            if (this.activeControlPointIndex < 0) updateAnnotation(this.polygon.getReferencePosition()); else if (this.controlPoints != null) updateAnnotation(this.controlPoints.get(this.activeControlPointIndex).getPosition());
        }
        if (MOVE_VERTEX_ACTION.equals(this.activeAction)) {
            this.moveControlPoint(this.activeControlPoint, lastMousePoint, this.mousePoint);
            e.consume();
        } else if (CHANGE_HEIGHT_ACTION.equals(this.activeAction)) {
            this.setPolygonHeight(lastMousePoint, this.mousePoint);
            e.consume();
        } else if (MOVE_POLYGON_ACTION.equals(this.activeAction)) {
            this.movePolygon(lastMousePoint, this.mousePoint);
            e.consume();
        }
    }

    public void mousePressed(MouseEvent e) {
        this.mousePoint = e.getPoint();
        Object topObject = null;
        PickedObjectList pickedObjects = this.wwd.getObjectsAtCurrentPosition();
        if (pickedObjects != null) topObject = pickedObjects.getTopObject();
        if (topObject instanceof ControlPointMarker) {
            this.activeControlPoint = (ControlPointMarker) topObject;
            this.activeAction = this.activeControlPoint.getType();
            setShowAnnotation(true);
            updateAnnotation(this.activeControlPoint.getPosition());
            int i = 0;
            for (Marker controlPoint : this.controlPoints) {
                if (controlPoint.equals(topObject)) break;
                i++;
            }
            this.activeControlPointIndex = i;
            e.consume();
        } else if (topObject == this.getPolygon()) {
            this.activeAction = MOVE_POLYGON_ACTION;
            this.activeControlPointIndex = -1;
            setShowAnnotation(true);
            updateAnnotation(this.polygon.getReferencePosition());
            e.consume();
        }
    }

    public void mouseReleased(MouseEvent e) {
        this.activeControlPoint = null;
        this.activeAction = null;
        setShowAnnotation(false);
        updateAnnotation(null);
        e.consume();
    }

    protected void movePolygon(Point previousMousePoint, Point mousePoint) {
        View view = this.wwd.getView();
        Globe globe = this.wwd.getModel().getGlobe();
        Position refPos = this.polygon.getReferencePosition();
        if (refPos == null) return;
        Line ray = view.computeRayFromScreenPoint(mousePoint.getX(), mousePoint.getY());
        Line previousRay = view.computeRayFromScreenPoint(previousMousePoint.getX(), previousMousePoint.getY());
        Vec4 vec = AirspaceEditorUtil.intersectGlobeAt(this.wwd, refPos.getElevation(), ray);
        Vec4 previousVec = AirspaceEditorUtil.intersectGlobeAt(this.wwd, refPos.getElevation(), previousRay);
        if (vec == null || previousVec == null) {
            return;
        }
        Position pos = globe.computePositionFromPoint(vec);
        Position previousPos = globe.computePositionFromPoint(previousVec);
        LatLon change = pos.subtract(previousPos);
        this.polygon.move(new Position(change.getLatitude(), change.getLongitude(), 0.0));
    }

    protected void moveControlPoint(ControlPointMarker controlPoint, Point lastMousePoint, Point moveToPoint) {
        View view = this.wwd.getView();
        Globe globe = this.wwd.getModel().getGlobe();
        Position refPos = controlPoint.getPosition();
        if (refPos == null) return;
        Line ray = view.computeRayFromScreenPoint(moveToPoint.getX(), moveToPoint.getY());
        Line previousRay = view.computeRayFromScreenPoint(lastMousePoint.getX(), lastMousePoint.getY());
        Vec4 vec = AirspaceEditorUtil.intersectGlobeAt(this.wwd, refPos.getElevation(), ray);
        Vec4 previousVec = AirspaceEditorUtil.intersectGlobeAt(this.wwd, refPos.getElevation(), previousRay);
        if (vec == null || previousVec == null) {
            return;
        }
        Position pos = globe.computePositionFromPoint(vec);
        Position previousPos = globe.computePositionFromPoint(previousVec);
        LatLon change = pos.subtract(previousPos);
        java.util.List<LatLon> boundary = new ArrayList<LatLon>();
        for (LatLon ll : this.polygon.getOuterBoundary()) {
            boundary.add(ll);
        }
        boundary.set(controlPoint.getIndex(), new Position(pos.add(change), refPos.getAltitude()));
        boundary.remove(boundary.size() - 1);
        this.polygon.setOuterBoundary(boundary);
    }

    protected void setPolygonHeight(Point previousMousePoint, Point mousePoint) {
        Position referencePos = this.polygon.getReferencePosition();
        if (referencePos == null) return;
        Vec4 referencePoint = this.wwd.getModel().getGlobe().computePointFromPosition(referencePos);
        Vec4 surfaceNormal = this.wwd.getModel().getGlobe().computeSurfaceNormalAtLocation(referencePos.getLatitude(), referencePos.getLongitude());
        Line verticalRay = new Line(referencePoint, surfaceNormal);
        Line screenRay = this.wwd.getView().computeRayFromScreenPoint(mousePoint.getX(), mousePoint.getY());
        Line previousScreenRay = this.wwd.getView().computeRayFromScreenPoint(previousMousePoint.getX(), previousMousePoint.getY());
        Vec4 pointOnLine = AirspaceEditorUtil.nearestPointOnLine(verticalRay, screenRay);
        Vec4 previousPointOnLine = AirspaceEditorUtil.nearestPointOnLine(verticalRay, previousScreenRay);
        Position pos = this.wwd.getModel().getGlobe().computePositionFromPoint(pointOnLine);
        Position previousPos = this.wwd.getModel().getGlobe().computePositionFromPoint(previousPointOnLine);
        double elevationChange = pos.getElevation() - previousPos.getElevation();
        java.util.List<Position> boundary = new ArrayList<Position>();
        for (LatLon ll : this.polygon.getOuterBoundary()) {
            boundary.add(new Position(ll, ((Position) ll).getElevation() + elevationChange));
        }
        this.polygon.setOuterBoundary(boundary);
    }

    /**
     * Add a vertex to the polygon's outer boundary.
     *
     * @param mousePoint the point at which the mouse was clicked. The new vertex will be placed as near as possible to
     *                   this point, at the elevation of the polygon.
     */
    protected void addVertex(Point mousePoint) {
        Line ray = this.wwd.getView().computeRayFromScreenPoint(mousePoint.getX(), mousePoint.getY());
        Vec4 pickPoint = this.intersectPolygonAltitudeAt(ray);
        double nearestDistance = Double.MAX_VALUE;
        int newVertexIndex = 0;
        for (int i = 0; i < this.controlPoints.size(); i++) {
            ControlPointMarker thisMarker = (ControlPointMarker) this.controlPoints.get(i);
            ControlPointMarker nextMarker = (ControlPointMarker) this.controlPoints.get((i + 1) % this.controlPoints.size());
            Vec4 pointOnEdge = AirspaceEditorUtil.nearestPointOnSegment(thisMarker.point, nextMarker.point, pickPoint);
            if (!AirspaceEditorUtil.isPointBehindLineOrigin(ray, pointOnEdge)) {
                double d = pointOnEdge.distanceTo3(pickPoint);
                if (d < nearestDistance) {
                    newVertexIndex = i + 1;
                    nearestDistance = d;
                }
            }
        }
        Position newPosition = this.wwd.getModel().getGlobe().computePositionFromPoint(pickPoint);
        ArrayList<Position> positionList = new ArrayList<Position>(this.controlPoints.size());
        for (LatLon position : this.getPolygon().getOuterBoundary()) {
            positionList.add((Position) position);
        }
        positionList.add(newVertexIndex, newPosition);
        this.getPolygon().setOuterBoundary(positionList);
    }

    /**
     * Remove a vertex from the polygon.
     *
     * @param vertexToRemove the vertex to remove.
     */
    protected void removeVertex(ControlPointMarker vertexToRemove) {
        ExtrudedPolygon polygon = this.getPolygon();
        ArrayList<LatLon> locations = new ArrayList<LatLon>(this.controlPoints.size() - 1);
        for (LatLon latLon : polygon.getOuterBoundary()) {
            locations.add(latLon);
        }
        locations.remove(vertexToRemove.getIndex());
        polygon.setOuterBoundary(locations);
    }

    /**
     * Determine the point at which a ray intersects a the globe at the elevation of the polygon.
     *
     * @param ray Ray to intersect with the globe.
     *
     * @return The point at which the ray intersects the globe at the elevation of the polygon.
     */
    protected Vec4 intersectPolygonAltitudeAt(Line ray) {
        double elevation = 0.0;
        if (this.controlPoints.size() > 0) {
            elevation = this.controlPoints.get(0).getPosition().getElevation();
        }
        return AirspaceEditorUtil.intersectGlobeAt(this.wwd, elevation, ray);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    protected static class ControlPointMarker extends BasicMarker {

        protected int index;

        protected String type;

        protected Vec4 point;

        public ControlPointMarker(String type, Position position, Vec4 point, MarkerAttributes attrs, int index) {
            super(position, attrs);
            this.point = point;
            this.index = index;
            this.type = type;
        }

        public int getIndex() {
            return this.index;
        }

        public String getType() {
            return type;
        }

        public Vec4 getPoint() {
            return point;
        }
    }

    public void updateAnnotation(Position pos) {
        if (pos == null) {
            this.annotation.getAttributes().setVisible(false);
            return;
        }
        String displayString = this.getDisplayString(pos);
        if (displayString == null) {
            this.annotation.getAttributes().setVisible(false);
            return;
        }
        this.annotation.setText(displayString);
        Vec4 screenPoint = this.computeAnnotationPosition(pos);
        if (screenPoint != null) this.annotation.setScreenPoint(new Point((int) screenPoint.x, (int) screenPoint.y));
        this.annotation.getAttributes().setVisible(true);
    }

    protected Vec4 computeAnnotationPosition(Position pos) {
        Vec4 surfacePoint = this.wwd.getSceneController().getTerrain().getSurfacePoint(pos.getLatitude(), pos.getLongitude());
        if (surfacePoint == null) {
            Globe globe = this.wwd.getModel().getGlobe();
            surfacePoint = globe.computePointFromPosition(pos.getLatitude(), pos.getLongitude(), globe.getElevation(pos.getLatitude(), pos.getLongitude()));
        }
        return this.wwd.getView().project(surfacePoint);
    }

    protected String getDisplayString(Position pos) {
        String displayString = null;
        if (pos != null) {
            displayString = this.formatMeasurements(pos);
        }
        return displayString;
    }

    protected String formatMeasurements(Position pos) {
        StringBuilder sb = new StringBuilder();
        if (!this.arePositionsRedundant(pos, this.polygon.getReferencePosition())) {
            sb.append(this.unitsFormat.angleNL(this.getLabel(LATITUDE_LABEL), pos.getLatitude()));
            sb.append(this.unitsFormat.angleNL(this.getLabel(LONGITUDE_LABEL), pos.getLongitude()));
            sb.append(this.unitsFormat.lengthNL(this.getLabel(ALTITUDE_LABEL), pos.getAltitude()));
        }
        if (this.polygon.getReferencePosition() != null) {
            sb.append(this.unitsFormat.angleNL(this.getLabel(CENTER_LATITUDE_LABEL), this.polygon.getReferencePosition().getLatitude()));
            sb.append(this.unitsFormat.angleNL(this.getLabel(CENTER_LONGITUDE_LABEL), this.polygon.getReferencePosition().getLongitude()));
            sb.append(this.unitsFormat.lengthNL(this.getLabel(CENTER_ALTITUDE_LABEL), this.polygon.getReferencePosition().getAltitude()));
        }
        return sb.toString();
    }
}
