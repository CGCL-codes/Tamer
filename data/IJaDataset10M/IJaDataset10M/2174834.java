package presentation.controler.actions;

import core.data_tier.entities.User;
import java.awt.event.ActionEvent;
import presentation.Globals;
import presentation.dialogs.EditUserDialog;
import presentation.dialogs.UserDialog;
import sun.awt.GlobalCursorManager;

/**
 *
 * @author Lahvi
 */
public class EditUserAction extends AbstractObserverAction {

    private static EditUserAction instance;

    public static EditUserAction getInstance() {
        if (instance == null) instance = new EditUserAction();
        return instance;
    }

    public EditUserAction() {
        super("Upravit uživatele");
    }

    @Override
    public boolean isEnabledInState() {
        return (Globals.getInstance().getSelectedUser() != null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        User selectedUser = Globals.getInstance().getSelectedUser();
        new UserDialog(selectedUser).setVisible(true);
        Globals.getInstance().refreshData();
    }
}
