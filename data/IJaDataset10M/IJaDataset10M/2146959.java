package org.blueoxygen.cimande.security.login;

import org.blueoxygen.cimande.security.User;
import org.blueoxygen.cimande.security.UserAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class Signup extends ActionSupport implements ModelDriven {

    private User user = new User();

    private String verifyPassword;

    @Autowired
    private UserAccessor ua;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    public Object getModel() {
        return user;
    }

    public String execute() {
        if (ua.getByUsername(user.getUsername()) == null) {
            ua.signup(user);
            return SUCCESS;
        } else {
            addFieldError("username", "Username is already taken, please choose another");
            return ERROR;
        }
    }
}
