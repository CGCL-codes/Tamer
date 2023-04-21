package ghm.follow.gui;

import ghm.follow.FollowApp;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Action which deletes the contents of all followed files.
 * 
 * @author <a href="mailto:greghmerrill@yahoo.com">Greg Merrill</a>
 */
public class DeleteAll extends FollowAppAction {

    public static final String NAME = "deleteAll";

    private Logger log = Logger.getLogger(DeleteAll.class.getName());

    public DeleteAll(FollowApp app) throws IOException {
        super(app, FollowApp.getResourceString("action.DeleteAll.name"), FollowApp.getResourceString("action.DeleteAll.mnemonic"), FollowApp.getResourceString("action.DeleteAll.accelerator"), FollowApp.getIcon(DeleteAll.class, "action.DeleteAll.icon"), ActionContext.MULTI_FILE);
    }

    public void actionPerformed(ActionEvent e) {
        if (getApp().getAttributes().confirmDeleteAll()) {
            DisableableConfirm confirm = new DisableableConfirm(getApp().getFrame(), FollowApp.getResourceString("dialog.confirmDeleteAll.title"), FollowApp.getResourceString("dialog.confirmDeleteAll.message"), FollowApp.getResourceString("dialog.confirmDeleteAll.confirmButtonText"), FollowApp.getResourceString("dialog.confirmDeleteAll.doNotConfirmButtonText"), FollowApp.getResourceString("dialog.confirmDeleteAll.disableText"));
            confirm.pack();
            confirm.setVisible(true);
            if (confirm.markedDisabled()) {
                getApp().getAttributes().setConfirmDeleteAll(false);
            }
            if (confirm.markedConfirmed()) {
                performDelete();
            }
        } else {
            performDelete();
        }
    }

    private void performDelete() {
        getApp().setCursor(Cursor.WAIT_CURSOR);
        List<FileFollowingPane> allFileFollowingPanes = getApp().getAllFileFollowingPanes();
        try {
            for (FileFollowingPane fileFollowingPane : allFileFollowingPanes) {
                fileFollowingPane.clear();
            }
            getApp().setCursor(Cursor.DEFAULT_CURSOR);
        } catch (IOException ioe) {
            log.log(Level.SEVERE, "IOException error in DeleteAll", ioe);
            getApp().setCursor(Cursor.DEFAULT_CURSOR);
            JOptionPane.showMessageDialog(getApp().getFrame(), FollowApp.getResourceString("message.unableToDeleteAll.text"), FollowApp.getResourceString("message.unableToDeleteAll.title"), JOptionPane.WARNING_MESSAGE);
        }
    }
}
