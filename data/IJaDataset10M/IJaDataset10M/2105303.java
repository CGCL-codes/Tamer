package com.isa.jump.plugin;

import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.valid.IsValidOp;
import com.vividsolutions.jump.workbench.ui.EditTransaction;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.FeatureDrawingUtil;

public class DrawConstrainedCircleTool extends ConstrainedMultiClickTool {

    private FeatureDrawingUtil featureDrawingUtil;

    private DrawConstrainedCircleTool(FeatureDrawingUtil featureDrawingUtil) {
        drawClosed = true;
        this.featureDrawingUtil = featureDrawingUtil;
    }

    public static CursorTool create(LayerNamePanelProxy layerNamePanelProxy) {
        FeatureDrawingUtil featureDrawingUtil = new FeatureDrawingUtil(layerNamePanelProxy);
        return featureDrawingUtil.prepare(new DrawConstrainedCircleTool(featureDrawingUtil), true);
    }

    public String getName() {
        return "Draw Constrained Circle";
    }

    public Icon getIcon() {
        return new ImageIcon(getClass().getResource("DrawCircleConstrained.gif"));
    }

    protected void gestureFinished() throws Exception {
        reportNothingToUndoYet();
        if (!checkCircle()) {
            return;
        }
        execute(featureDrawingUtil.createAddCommand(getCircle(), isRollingBackInvalidEdits(), getPanel(), this));
    }

    protected Polygon getCircle() throws NoninvertibleTransformException {
        ArrayList points = new ArrayList(getCoordinates());
        if (getCoordinates().size() == 2) {
            Circle circle = new Circle((Coordinate) points.get(0), ((Coordinate) points.get(0)).distance((Coordinate) points.get(1)));
            return circle.getPoly();
        } else {
            Coordinate a = (Coordinate) points.get(0);
            Coordinate b = (Coordinate) points.get(1);
            Coordinate c = (Coordinate) points.get(2);
            double A = b.x - a.x;
            double B = b.y - a.y;
            double C = c.x - a.x;
            double D = c.y - a.y;
            double E = A * (a.x + b.x) + B * (a.y + b.y);
            double F = C * (a.x + c.x) + D * (a.y + c.y);
            double G = 2.0 * (A * (c.y - b.y) - B * (c.x - b.x));
            if (G != 0.0) {
                double px = (D * E - B * F) / G;
                double py = (A * F - C * E) / G;
                Coordinate center = new Coordinate(px, py);
                double radius = Math.sqrt((a.x - px) * (a.x - px) + (a.y - py) * (a.y - py));
                Circle circle = new Circle(center, radius);
                return circle.getPoly();
            } else {
                Circle circle = new Circle((Coordinate) points.get(1), ((Coordinate) points.get(1)).distance((Coordinate) points.get(2)));
                return circle.getPoly();
            }
        }
    }

    protected boolean checkCircle() throws NoninvertibleTransformException {
        if (getCoordinates().size() < 2) {
            getPanel().getContext().warnUser("The circle must have at least 2 points");
            return false;
        }
        IsValidOp isValidOp = new IsValidOp(getCircle());
        if (!isValidOp.isValid()) {
            getPanel().getContext().warnUser(isValidOp.getValidationError().getMessage());
            if (getWorkbench().getBlackboard().get(EditTransaction.ROLLING_BACK_INVALID_EDITS_KEY, false)) {
                return false;
            }
        }
        return true;
    }

    private Polygon getCircle3points(Coordinate a, Coordinate b, Coordinate c) {
        double A = b.x - a.x;
        double B = b.y - a.y;
        double C = c.x - a.x;
        double D = c.y - a.y;
        double E = A * (a.x + b.x) + B * (a.y + b.y);
        double F = C * (a.x + c.x) + D * (a.y + c.y);
        double G = 2.0 * (A * (c.y - b.y) - B * (c.x - b.x));
        if (G != 0.0) {
            double px = (D * E - B * F) / G;
            double py = (A * F - C * E) / G;
            Coordinate center = new Coordinate(px, py);
            double radius = Math.sqrt((a.x - px) * (a.x - px) + (a.y - py) * (a.y - py));
            Circle circle = new Circle(center, radius);
            return circle.getPoly();
        } else {
            Circle circle = new Circle(b, (b).distance(c));
            return circle.getPoly();
        }
    }

    protected Shape getShape() throws NoninvertibleTransformException {
        if (coordinates.size() > 1) {
            GeneralPath shape = new GeneralPath();
            Coordinate a = (Coordinate) coordinates.get(0);
            Coordinate b = (Coordinate) coordinates.get(1);
            Coordinate c = tentativeCoordinate;
            Polygon polygon = getCircle3points(a, b, c);
            Coordinate[] polygonCoordinates = polygon.getCoordinates();
            Coordinate firstCoordinate = (Coordinate) polygonCoordinates[0];
            Point2D firstPoint = getPanel().getViewport().toViewPoint(firstCoordinate);
            shape.moveTo((float) firstPoint.getX(), (float) firstPoint.getY());
            for (int i = 1, n = polygonCoordinates.length; i < n; i++) {
                Point2D nextPoint = getPanel().getViewport().toViewPoint(polygonCoordinates[i]);
                shape.lineTo((int) nextPoint.getX(), (int) nextPoint.getY());
            }
            return shape;
        }
        return super.getShape();
    }
}
