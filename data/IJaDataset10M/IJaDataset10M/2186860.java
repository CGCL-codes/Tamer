package org.ourgrid.peer.ui.async.gui.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import org.ourgrid.peer.ui.async.gui.AddUserDialog;

/**
 * It represents an Action to add a new peer user.
 */
public class AddPeerUserAction extends AbstractAction {

    private Frame frame;

    private JDialog addUserDialog;

    /** Creates new form AddPeerUserAction */
    public AddPeerUserAction(Frame frame) {
        super("Add peer user");
        this.frame = frame;
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent arg0) {
        addUserDialog = new AddUserDialog(frame, true);
        addUserDialog.setVisible(true);
    }
}
