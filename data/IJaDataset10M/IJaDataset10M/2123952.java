package library.admin;

import javax.validation.Valid;
import library.common.AbstractAction;
import library.common.Delegate;
import library.enums.Role;
import library.interceptor.DataTransfer;
import library.utils.bean.IdBean;
import library.utils.bean.RoleBean;

@SuppressWarnings("serial")
public class RemovePersonAction extends AbstractAction.Admin {

    @Valid
    @DataTransfer
    protected IdBean id;

    @Valid
    @DataTransfer
    protected RoleBean role;

    {
        actionName = "removePerson";
        processButtonName = "processRemovePersonButton";
    }

    public IdBean getId() {
        return id;
    }

    public RoleBean getRole() {
        return role;
    }

    @Override
    protected String processButtonPressed() {
        int personID = Integer.parseInt(id.getId());
        Role r = Role.valueOf(role.getRole());
        boolean removed = server.removePerson(personID, r);
        String personRemoved = getText("personRemoved");
        String personNotRemoved = getText("personNotRemoved");
        String _personID = getText("personID");
        message = Delegate.generateMessage(removed, personRemoved, personID, personNotRemoved, _personID);
        return "removed";
    }

    public void setId(IdBean id) {
        this.id = id;
    }

    public void setRole(RoleBean role) {
        this.role = role;
    }
}
