package jard.webshop.jsfbeans;

import jard.webshop.nbp.User;
import jard.webshop.util.Constants;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author CJP
 */
@ManagedBean(name = "userModelBean")
@SessionScoped
public class UserModelBean implements Serializable {

    private User user = null;

    /** Creates a new instance of UserModelBean */
    public UserModelBean() {
        System.out.println("Created new userModelBean");
    }

    public User getUser() {
        return user;
    }

    public void setUser(User userObj) {
        System.out.println("Setting user in userModelBean to " + userObj);
        this.user = userObj;
    }

    public String getName() {
        return user.getName();
    }

    public Boolean isAdmin() {
        if (user != null) {
            return user.getAuthLevel() >= Constants.getAdminLevel();
        } else {
            return false;
        }
    }

    public Boolean isEmployee() {
        if (user != null) {
            return user.getAuthLevel() >= Constants.getEmployeeLevel();
        } else {
            return false;
        }
    }

    public Boolean isLoggedIn() {
        System.out.println("Is logged in? " + (user != null));
        return user != null;
    }
}
