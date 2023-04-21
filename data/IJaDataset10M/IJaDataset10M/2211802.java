package beans.administration;

import core.UsersEB;
import javax.ejb.EJB;
import sb.UserSessionRemote;
import beans.administration.AdministrationBackingBean;

/**
 * AdministrationControllerBean contains methods for creating new, updating and deleting users.
 * Contacts database through an EJB.
 *
 * @author pehrsona
 */
public class AdministrationControllerBean {

    @EJB
    UserSessionRemote userSessionBean;

    AdministrationBackingBean administrationBackingBean;

    public AdministrationBackingBean getAdministrationBackingBean() {
        return administrationBackingBean;
    }

    public void setAdministrationBackingBean(AdministrationBackingBean administrationBackingBean) {
        this.administrationBackingBean = administrationBackingBean;
    }

    public Object deleteUser() {
        if (userSessionBean.deleteUser(getAdministrationBackingBean().getUser().getUser()) != 0) {
            getAdministrationBackingBean().closeDelete();
            return "admin-delete";
        }
        getAdministrationBackingBean().clearSelected();
        return "admin-delete-failed";
    }

    public Object createUser() {
        userSessionBean.addUser(getAdministrationBackingBean().getUser().getUser());
        getAdministrationBackingBean().closeNew();
        return "createSuccess";
    }

    public Object updateUser() {
        UsersEB user = getAdministrationBackingBean().getUser().getUser();
        int n = userSessionBean.updateUser(user);
        System.out.println(n);
        if (n == 1) {
            getAdministrationBackingBean().closeEdit();
        }
        getAdministrationBackingBean().clearSelected();
        return "updateSuccess";
    }
}
