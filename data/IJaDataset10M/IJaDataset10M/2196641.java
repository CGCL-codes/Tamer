package net.sf.gateway.mef.configuration.batchjobs.ValidateZipFiles;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.gateway.mef.configuration.ConfigGUIJFrame;

public class ValidateZipFilesBatchJobEnabledChangeListener implements ChangeListener {

    /**
     * Serial Version ID
     */
    private static final long serialVersionUID = 1L;

    private ConfigGUIJFrame myParent;

    public ValidateZipFilesBatchJobEnabledChangeListener(ConfigGUIJFrame myParent) {
        super();
        this.myParent = myParent;
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

    public void stateChanged(ChangeEvent e) {
        AbstractButton abstractButton = (AbstractButton) e.getSource();
        ButtonModel buttonModel = abstractButton.getModel();
        boolean selected = buttonModel.isSelected();
        myParent.getConfig().getBatchJobs().getValidateZipFiles().setEnabled(selected);
    }
}
