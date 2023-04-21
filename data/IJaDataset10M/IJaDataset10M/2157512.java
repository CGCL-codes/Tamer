package net.sf.gateway.mef.configuration.batchjobs.GetAckNotification;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import net.sf.gateway.mef.configuration.ConfigGUIJFrame;

public class GetAckNotificationBatchJobAttachmentsDirectoryFocusListener implements FocusListener {

    private ConfigGUIJFrame myParent;

    public GetAckNotificationBatchJobAttachmentsDirectoryFocusListener(ConfigGUIJFrame myParent) {
        super();
        this.myParent = myParent;
    }

    public void focusGained(FocusEvent e) {
    }

    public void focusLost(FocusEvent e) {
        try {
            myParent.getConfig().getBatchJobs().getGetAckNotification().setAttachmentsDirectory(((GetAckNotificationBatchJobAttachmentsDirectoryJTextField) e.getComponent()).getText());
        } catch (NumberFormatException nfe) {
            myParent.getConfig().getBatchJobs().getGetAckNotification().setAttachmentsDirectory("");
            myParent.getTabbedPane().refreshConfig();
        }
    }
}
