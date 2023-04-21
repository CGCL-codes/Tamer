package com.safi.workshop.sqlexplorer.connections.actions;

import java.sql.SQLException;
import java.util.Collection;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import com.safi.server.plugin.SafiServerPlugin;
import com.safi.workshop.sqlexplorer.dbproduct.Session;
import com.safi.workshop.sqlexplorer.dbproduct.User;
import com.safi.workshop.sqlexplorer.plugin.SQLExplorerPlugin;

public class AutoCommitAction extends AbstractConnectionTreeAction {

    public AutoCommitAction() {
        super("SQLEditor.Options.AutoCommit", "SQLEditor.Options.AutoCommit.Tooltip", "Images.CommitIcon", AS_CHECK_BOX);
    }

    @Override
    public void run() {
        try {
            if (!SafiServerPlugin.getDefault().isConnected()) {
                MessageDialog.openError(Display.getCurrent().getActiveShell(), "Not Connected", "You must be connected to a SafiServer instance to access this feature");
                return;
            }
            boolean enabled = isChecked();
            for (User user : getView().getSelectedUsers(false)) {
                for (Session session : user.getSessions()) session.setAutoCommit(enabled);
                user.setAutoCommit(enabled);
            }
        } catch (SQLException e) {
            SQLExplorerPlugin.error(e);
        }
    }

    /**
   * Only show action when there is 1 alias selected
   * 
   * @see com.safi.workshop.sqlexplorer.connections.actions.AbstractConnectionTreeAction#isAvailable()
   */
    @Override
    public boolean isAvailable() {
        Collection<User> users = getView().getSelectedUsers(false);
        if (users.size() != 1) return false;
        for (User user : users) if (user.isAutoCommit()) {
            setChecked(true);
            break;
        }
        return true;
    }
}
