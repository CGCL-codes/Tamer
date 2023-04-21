package net.sf.gateway.mef.configuration.batchjobs.GetSubmissionReconciliationList;

import javax.swing.JTextField;
import net.sf.gateway.mef.configuration.ConfigGUIJFrame;

public class GetSubmissionReconciliationListBatchJobMaxResultsJTextField extends JTextField {

    /**
     * Serial Version ID
     */
    private static final long serialVersionUID = 1L;

    private ConfigGUIJFrame myParent;

    public GetSubmissionReconciliationListBatchJobMaxResultsJTextField(ConfigGUIJFrame myParent) {
        super();
        this.setMyParent(myParent);
        this.setColumns(8);
        this.addFocusListener(new GetSubmissionReconciliationListBatchJobMaxResultsFocusListener(myParent));
    }

    /**
     * @param myParent
     *        the myParent to set
     */
    public void setMyParent(ConfigGUIJFrame myParent) {
        this.myParent = myParent;
    }

    /**
     * @return the myParent
     */
    public ConfigGUIJFrame getMyParent() {
        return myParent;
    }
}
