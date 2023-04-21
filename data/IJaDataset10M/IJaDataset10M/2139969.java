package net.sf.gateway.mef.configuration.batchjobs.GetSubmissions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.sf.gateway.mef.configuration.ConfigGUIJFrame;

public class GetSubmissionsBatchJobPopupMenuJActionListener implements ActionListener {

    private final ConfigGUIJFrame myParent;

    private GetSubmissionsBatchJobJTable table;

    public GetSubmissionsBatchJobPopupMenuJActionListener(ConfigGUIJFrame parent, GetSubmissionsBatchJobJTable table) {
        this.table = table;
        this.myParent = parent;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof GetSubmissionsBatchJobAddNewSubmissionJMenuItem) {
            GetSubmissionsBatchJobTableModel model = (GetSubmissionsBatchJobTableModel) table.getModel();
            Object[] row = new Object[1];
            row[0] = new String("");
            model.addRow(row);
        } else if (e.getSource() instanceof GetSubmissionsBatchJobRemoveSelectedSubmissionJMenuItem) {
            int row = table.getSelectedRow();
            if (row != -1) {
                GetSubmissionsBatchJobTableModel model = (GetSubmissionsBatchJobTableModel) table.getModel();
                model.removeRow(row);
            }
        }
    }

    /**
     * @return the myParent
     */
    public ConfigGUIJFrame getMyParent() {
        return myParent;
    }
}
