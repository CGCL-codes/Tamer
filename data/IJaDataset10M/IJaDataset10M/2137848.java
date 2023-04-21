package org.gello.client.actions.manageAccountsDialog;

import java.util.ArrayList;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.gello.client.Application;
import org.gello.client.IImageKeys;
import org.gello.client.composites.ManageAccountsComposite;
import org.gello.client.dialogs.EditUserDialog;
import org.gello.client.manager.ServerException_Exception;
import org.gello.client.manager.User;

public class EditUserAction extends Action implements ActionFactory.IWorkbenchAction {

    private final IWorkbenchWindow window;

    public static final String ID = "org.gello.client.actions.manageAccountsDialog.EditAccount";

    private User user;

    public EditUserAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("&Edit");
        setToolTipText("Edit Account");
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, IImageKeys.EDIT_ACCOUNT));
    }

    /**
	 * Opens a user for editing in a dialog.  When pressing
	 * OK on the dialog window, the user is updated on the server.
	 * If successful, that updated user replaces the old one on
	 * the client.
	 * 
	 */
    @SuppressWarnings("unchecked")
    public void run() {
        EditUserDialog d = new EditUserDialog(window.getShell());
        d.create();
        d.setEditableUsername(false);
        d.setUser(user);
        int code = d.open();
        if (code == EditUserDialog.OK) {
            User updatedUser = null;
            try {
                updatedUser = Application.getManager().updateUser(user);
            } catch (ServerException_Exception e) {
                e.printStackTrace();
            }
            if (updatedUser != null) {
                ArrayList<User> userList = (ArrayList<User>) ManageAccountsComposite.usersViewer.getInput();
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getUserName().equals(updatedUser.getUserName())) {
                        userList.remove(i);
                        userList.add(updatedUser);
                    }
                }
                ManageAccountsComposite.usersViewer.setSelection(null);
                ManageAccountsComposite.usersViewer.refresh();
            }
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void dispose() {
    }
}
