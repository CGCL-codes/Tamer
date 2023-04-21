package de.shandschuh.jaolt.gui.actions;

import java.awt.event.ActionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import de.shandschuh.jaolt.core.Language;
import de.shandschuh.jaolt.core.apicall.AuctionCall;
import de.shandschuh.jaolt.gui.maintabbedpane.AuctionUploadJDialog;
import de.shandschuh.jaolt.gui.maintabbedpane.EditAuctionsListJPanel;

public class CalculateAllFeesAction extends AbstractIconAction implements TableModelListener {

    private static final long serialVersionUID = 1L;

    private EditAuctionsListJPanel editAuctionsListJPanel;

    public CalculateAllFeesAction(EditAuctionsListJPanel editAuctionsListJPanel) {
        super(Language.translateStatic("BUTTON_CALCULATEALLFEES_TEXT"), "icons/calculate_all_fees.png");
        this.editAuctionsListJPanel = editAuctionsListJPanel;
        editAuctionsListJPanel.getListJTable().getModel().addTableModelListener(this);
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        new AuctionUploadJDialog(editAuctionsListJPanel.getObjects(), AuctionCall.CALL_TYPE_VERIFY).getAuctionCallResults();
        editAuctionsListJPanel.updateRows();
    }

    public void tableChanged(TableModelEvent tableModelEvent) {
        if (tableModelEvent.getType() == TableModelEvent.INSERT) {
            setEnabled(true);
        } else if (tableModelEvent.getType() == TableModelEvent.DELETE || tableModelEvent.getType() == TableModelEvent.UPDATE) {
            setEnabled(editAuctionsListJPanel.getListJTable().getRowCount() > 0);
        }
    }
}
