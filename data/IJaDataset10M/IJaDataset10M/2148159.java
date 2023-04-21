package ge.telasi.tasks.ui.user;

import ge.telasi.tasks.ejb.TaskFacadeRemote;
import ge.telasi.tasks.model.User;
import ge.telasi.tasks.ui.Platform;
import ge.telasi.tasks.ui.UIUtils;
import java.util.List;
import static ge.telasi.tasks.ui.log.LoggerUtils.clearLogger;
import static ge.telasi.tasks.ui.log.LoggerUtils.manageException;
import static ge.telasi.tasks.ui.comp.TableUtils.setSelectionAt;

/**
 * @author dimitri
 */
public class UserTable extends UserTableBase {

    private static final long serialVersionUID = 2436222992954906460L;

    private boolean onlyActives;

    public void setOnlyActives(boolean onlyActives) {
        this.onlyActives = onlyActives;
    }

    public boolean isOnlyActives() {
        return onlyActives;
    }

    public boolean refresh() {
        if (Platform.getDefault().validateConnection()) {
            clearLogger(getLogger(), null);
            try {
                Platform pl = Platform.getDefault();
                TaskFacadeRemote facade = pl.getFacade();
                List<User> users = onlyActives ? facade.getAllActiveUsers(pl.getCredentials()) : facade.getAllUsers(pl.getCredentials());
                display(users);
                return true;
            } catch (Exception ex) {
                manageException(this, getLogger(), ex);
                return false;
            }
        }
        return false;
    }

    public void edit() {
        User selected = getSelectedUser();
        if (selected != null && Platform.getDefault().validateConnection()) {
            UserDialog dialog = new UserDialog(this);
            dialog.setUser(selected);
            UIUtils.openDialog(dialog);
            if (dialog.isApproved()) {
                UserTableModel model = (UserTableModel) getModel();
                int index = model.indexOf(selected);
                model.replaceItemAt(dialog.getUser(), index);
                setSelectionAt(this, index);
            }
        }
    }
}
