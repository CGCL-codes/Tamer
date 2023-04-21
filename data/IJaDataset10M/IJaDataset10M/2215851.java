package gui.tools;

import gui.EuclideGui;
import gui.EuclideSheetView;
import gui.EuclideTool;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import model.EuclideDoc;
import model.EuclideFigure;
import dynamic.DynamicObject2D;
import dynamic.DynamicShape2D;
import dynamic.shapes.DynamicMoveablePoint2D;
import dynamic.shapes.FreePoint2D;
import dynamic.shapes.ShapeWrapper2D;

/**
 * @author dlegland
 */
public class MoveFreePointsTool extends EuclideTool {

    private DynamicShape2D dynamic;

    double xp;

    double yp;

    public MoveFreePointsTool(EuclideGui gui, String name) {
        super(gui, name);
        setInstructions(new String[] { "Clic on the point to move and drag it" });
    }

    @Override
    public void mousePressed(MouseEvent evt) {
        EuclideSheetView view = (EuclideSheetView) evt.getSource();
        Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
        view.setMouseLabel("");
        dynamic = null;
        xp = point.getX();
        yp = point.getY();
        EuclideFigure item = view.snapShape(point);
        if (item == null) return;
        DynamicShape2D geometry = item.getGeometry();
        if (geometry instanceof DynamicMoveablePoint2D) {
            dynamic = geometry;
        } else {
            dynamic = geometry;
            return;
        }
        view.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent evt) {
        EuclideSheetView view = (EuclideSheetView) evt.getSource();
        Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
        view.setMouseLabel("");
        if (dynamic == null) return;
        double x = point.getX();
        double y = point.getY();
        double dx = x - xp;
        double dy = y - yp;
        if (dynamic instanceof DynamicMoveablePoint2D) {
            ((DynamicMoveablePoint2D) dynamic).translate(dx, dy);
        } else if (dynamic instanceof ShapeWrapper2D) {
            ShapeWrapper2D wrapper = (ShapeWrapper2D) dynamic;
            Shape2D shape = wrapper.getShape();
            if (shape instanceof Point2D) {
                Point2D pt = ((Point2D) shape).translate(dx, dy);
                ((Point2D) shape).translate(dx, dy);
                wrapper.setShape(pt);
            }
        } else {
            Collection<DynamicShape2D> points = new ArrayList<DynamicShape2D>();
            for (DynamicObject2D parent : dynamic.getParents()) {
                if (parent instanceof FreePoint2D) {
                    points.add((FreePoint2D) parent);
                } else {
                    return;
                }
            }
            for (DynamicShape2D dyn : points) {
                ((FreePoint2D) dyn).translate(dx, dy);
            }
        }
        xp = x;
        yp = y;
        EuclideDoc doc = gui.getCurrentDoc();
        doc.updateDependencies();
        doc.setModified(true);
        gui.getCurrentFrame().updateTitle();
        view.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent evt) {
        EuclideSheetView view = (EuclideSheetView) evt.getSource();
        view.setMouseLabel("");
        dynamic = null;
        Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
        EuclideFigure item = view.snapShape(point);
        if (item == null) return;
        DynamicShape2D geom = item.getGeometry();
        if (view.isSnapped(point, geom.getShape())) {
            String shapeName = gui.getAppli().getShapeString(geom);
            view.setMouseLabel("move this " + shapeName);
        }
        view.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
        EuclideSheetView view = (EuclideSheetView) evt.getSource();
        view.setMouseLabel("");
        dynamic = null;
        Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
        EuclideFigure item = view.snapShape(point);
        if (item == null) return;
        Shape2D shape = item.getGeometry().getShape();
        if (view.isSnapped(point, shape)) {
            String shapeName = gui.getAppli().getShapeString(item.getGeometry());
            view.setMouseLabel("move this " + shapeName);
        }
        view.repaint();
    }
}
