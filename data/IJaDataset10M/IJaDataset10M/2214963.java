package org.blueoxygen.cimande.security.usermanager.actions;

import org.blueoxygen.cimande.commons.CimandeAction;
import org.blueoxygen.cimande.security.User;

/**
 * @author Abdul Rizal
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ViewUser extends CimandeAction {

    protected User user;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public String execute() {
        user = (User) manager.getById(User.class, getId());
        modelMap.put("user", user);
        if (user == null) {
            addActionError("Cannot find such descriptor");
            return ERROR;
        } else {
            return SUCCESS;
        }
    }
}
