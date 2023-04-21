package gui.actions;

import gui.EuclideAction;
import gui.EuclideDocView;
import gui.EuclideGui;
import gui.EuclideSheetView;
import gui.dialogs.EuclidePanelDialog;
import gui.panels.LineStyleEditPanel;
import java.awt.event.ActionEvent;

/**
 * adds a sheet to the current document, and creates the corresponding view.
 * @author Legland
 */
public class ChangeLineStyleAction extends EuclideAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public ChangeLineStyleAction(EuclideGui gui, String name) {
        super(gui, name);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        EuclideDocView docView = gui.getCurrentView();
        if (docView == null) return;
        EuclideSheetView view = docView.getCurrentSheetView();
        if (view == null) return;
        EuclidePanelDialog dlg = new EuclidePanelDialog(this.gui, new LineStyleEditPanel(gui), "Edit Line Style");
        gui.showDialog(dlg);
        view.repaint();
    }
}
