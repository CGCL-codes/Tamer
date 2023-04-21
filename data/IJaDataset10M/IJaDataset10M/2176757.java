package be.vds.jtbdive.core.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.model.DiveLocationManagerFacade;
import be.vds.jtbdive.core.view.panel.DiveLocationDetailPanel;
import be.vds.jtbdive.core.view.panel.DiverDetailPanel;

public class NewDiveLocationDialogAction extends AbstractAction {

    private DiveLocationManagerFacade diveLocationManagerFacade;

    private Window parentWindow;

    public NewDiveLocationDialogAction(Window parentWindow, DiveLocationManagerFacade diveLocationManagerFacade) {
        this.parentWindow = parentWindow;
        this.diveLocationManagerFacade = diveLocationManagerFacade;
    }

    public void actionPerformed(ActionEvent e) {
        DiveLocationDetailPanel detailPanel = new DiveLocationDetailPanel(DiveLocationDetailPanel.TYPE_NEW);
        int i = detailPanel.showDialog(parentWindow, "New Dive Location");
        if (i == DiverDetailPanel.OPTION_NEW) {
            try {
                diveLocationManagerFacade.saveDiveLocation(detailPanel.getNewDiveLocation());
            } catch (DataStoreException e2) {
                e2.printStackTrace();
            }
        }
    }
}
