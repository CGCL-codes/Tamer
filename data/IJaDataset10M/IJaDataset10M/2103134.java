package gui.actions;

import java.awt.event.ActionEvent;
import math.geom2d.conic.*;
import dynamic.shapes.*;
import model.*;
import app.*;
import gui.*;

/**
 * Add a layer to the document
 * @author Legland
 */
public class AddEllipseArcAction extends EuclideAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public AddEllipseArcAction(EuclideGui gui, String name) {
        super(gui, name);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        EuclideSheetView view = gui.getCurrentView().getCurrentSheetView();
        EllipseArc2D arc = new EllipseArc2D(100, 100, 100, 50, 0, 0, 2 * Math.PI);
        EuclideApp appli = gui.getAppli();
        EuclideFigure element = appli.createEuclideShape(new ShapeWrapper2D(arc));
        EuclideLayer layer = view.getSheet().getCurrentLayer();
        layer.addFigure(element);
    }
}
