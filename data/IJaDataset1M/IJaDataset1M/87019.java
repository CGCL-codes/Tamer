package gui.tools;

import java.awt.event.*;
import math.geom2d.*;
import math.geom2d.curve.*;
import dynamic.*;
import dynamic.shapes.*;
import model.*;
import app.*;
import gui.*;

/**
 * @author dlegland
 */
public class AddPointOnCurveTool extends EuclideTool {

    public AddPointOnCurveTool(EuclideGui gui, String name) {
        super(gui, name);
    }

    @Override
    public void mousePressed(MouseEvent evt) {
        EuclideSheetView view = (EuclideSheetView) evt.getSource();
        Point2D point = view.getPosition(evt.getX(), evt.getY());
        EuclideFigure elem = view.getSnappedShape(point, Curve2D.class);
        if (elem == null) return;
        DynamicShape2D curve = elem.getGeometry();
        PointOnCurve2D poc = new PointOnCurve2D(curve, new ShapeWrapper2D(point));
        EuclideApp appli = gui.getAppli();
        EuclideFigure element = appli.createEuclideShape(poc);
        EuclideDoc doc = appli.getCurrentDoc();
        EuclideLayer layer = view.getSheet().getCurrentLayer();
        doc.addFigure(element, layer);
        view.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent evt) {
        EuclideSheetView view = (EuclideSheetView) evt.getSource();
        view.setMouseLabel("");
        Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
        EuclideFigure item = view.getSnappedShape(point, Curve2D.class);
        if (item == null) return;
        DynamicShape2D dynamic = item.getGeometry();
        String shapeName = gui.getAppli().getShapeString(dynamic);
        view.setMouseLabel("new point on this " + shapeName);
    }
}
