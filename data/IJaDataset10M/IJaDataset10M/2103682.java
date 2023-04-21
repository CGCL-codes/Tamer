package gui.tools;

import gui.EuclideGui;
import gui.EuclideSheetView;
import gui.EuclideTool;
import java.awt.event.MouseEvent;
import math.geom2d.Point2D;
import model.EuclideDoc;
import model.EuclideLayer;
import model.EuclideFigure;
import app.EuclideApp;
import dynamic.DynamicShape2D;
import dynamic.shapes.FreePoint2D;
import dynamic.shapes.PointRelativePoint2D;
import dynamic.shapes.ShapeWrapper2D;

/**
 * @author dlegland
 */
public class AddPointRelativeToPointTool extends EuclideTool {

    /** The point used as reference */
    DynamicShape2D refPoint = null;

    public AddPointRelativeToPointTool(EuclideGui gui, String name) {
        super(gui, name);
        setInstructions(new String[] { "Clic on the reference point", "Clic on the initial position" });
    }

    @Override
    public void mousePressed(MouseEvent evt) {
        EuclideFigure elem;
        EuclideSheetView view = (EuclideSheetView) evt.getSource();
        Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
        EuclideApp appli = this.gui.getAppli();
        if (step == 1) {
            elem = view.getSnappedShape(point, Point2D.class);
            if (elem == null) {
                DynamicShape2D refPoint = new FreePoint2D(point.getX(), point.getY());
                elem = appli.createEuclideShape(refPoint);
                EuclideDoc doc = appli.getCurrentDoc();
                EuclideLayer layer = view.getSheet().getCurrentLayer();
                doc.addFigure(elem, layer);
            }
            refPoint = elem.getGeometry();
            step = 2;
        } else {
            DynamicShape2D target = new ShapeWrapper2D(point);
            PointRelativePoint2D prp = new PointRelativePoint2D(refPoint, target);
            elem = appli.createEuclideShape(prp);
            EuclideDoc doc = appli.getCurrentDoc();
            EuclideLayer layer = view.getSheet().getCurrentLayer();
            doc.addFigure(elem, layer);
            step = 1;
        }
        view.repaint();
        view.setInstruction(this.getInstruction());
    }

    @Override
    public void mouseMoved(MouseEvent evt) {
        EuclideSheetView view = (EuclideSheetView) evt.getSource();
        view.setMouseLabel("");
        Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
        EuclideFigure item = view.getSnappedShape(point, Point2D.class);
        if (item == null) return;
        DynamicShape2D dynamic = item.getGeometry();
        String shapeName = gui.getAppli().getShapeString(dynamic);
        view.setMouseLabel("this " + shapeName);
    }
}
