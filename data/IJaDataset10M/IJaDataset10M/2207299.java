package pl.otros.logview.gui.actions;

import org.jdesktop.swingx.JXTable;
import pl.otros.logview.gui.LogDataTableModel;
import pl.otros.logview.gui.OtrosApplication;
import pl.otros.logview.gui.StatusObserver;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

public class MarkRowAction extends OtrosAction {

    private static final Logger LOGGER = Logger.getLogger(MarkRowAction.class.getName());

    public MarkRowAction(OtrosApplication otrosApplication) {
        super(otrosApplication);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LogDataTableModel model = getOtrosApplication().getSelectedPaneLogDataTableModel();
        StatusObserver observer = getOtrosApplication().getStatusObserver();
        JXTable table = getOtrosApplication().getSelectPaneJXTable();
        if (model == null || table == null) {
            return;
        }
        int[] selected = table.getSelectedRows();
        for (int i = 0; i < selected.length; i++) {
            selected[i] = table.convertRowIndexToModel(selected[i]);
        }
        model.markRows(getOtrosApplication().getSelectedMarkColors(), selected);
        if (observer != null) {
            observer.updateStatus(selected.length + " rows marked");
        }
    }
}
