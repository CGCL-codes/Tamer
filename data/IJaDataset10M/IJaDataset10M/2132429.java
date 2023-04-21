package org.isi.monet.applications.manager.control.actions;

import org.isi.monet.applications.manager.core.constants.ErrorCode;
import org.isi.monet.core.exceptions.SessionException;
import org.isi.monet.core.model.UserList;

public class ActionDoLoadUsers extends Action {

    public ActionDoLoadUsers() {
        super();
    }

    public String execute() {
        UserList oUserList = new UserList();
        if (!this.oKernel.isLogged()) {
            throw new SessionException(ErrorCode.USER_NOT_LOGGED, this.idSession);
        }
        oUserList = this.oKernel.loadUsers();
        return oUserList.serializeToJSON();
    }
}
