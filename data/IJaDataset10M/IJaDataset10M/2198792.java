package gui.actions;

import gui.EuclideAction;
import gui.EuclideDocView;
import gui.EuclideGui;
import gui.EuclideSheetView;
import java.awt.event.ActionEvent;
import model.EuclideDoc;
import dynamic.DynamicShape2D;
import dynamic.shapes.CurveLocus2D;
import dynamic.shapes.PointLocus2D;

/**
 * Reset all loci in current view.
 * @author Legland
 */
public class ClearLociAction extends EuclideAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public ClearLociAction(EuclideGui gui, String name) {
        super(gui, name);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        System.out.println("clear loci");
        EuclideDocView docView = gui.getCurrentView();
        if (docView == null) return;
        EuclideDoc doc = docView.getDoc();
        for (DynamicShape2D shape : doc.getDynamicShapes()) {
            if (shape instanceof PointLocus2D) {
                ((PointLocus2D) shape).clearLocus();
            } else if (shape instanceof CurveLocus2D) {
                ((CurveLocus2D) shape).clearLocus();
            }
        }
        EuclideSheetView view = gui.getCurrentView().getCurrentSheetView();
        view.clearSelection();
        view.repaint();
    }
}
