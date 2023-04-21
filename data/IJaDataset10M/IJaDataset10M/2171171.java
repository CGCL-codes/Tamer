package org.gbif.biogarage.action;

import java.util.Date;
import org.gbif.biogarage.model.User;
import org.gbif.biogarage.service.UserManager;
import org.gbif.biogarage.util.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginAction extends BaseAction {

    private String email;

    private String password;

    public String execute() {
        currentMenu = "login";
        System.out.println(String.format("%s:%s", email, password));
        User user = new User();
        user.setId(77l);
        user.setEmail(email);
        user.setPassword(password);
        user.setCreated(new Date());
        user.setRealname("Tim Robertson");
        if (user != null) {
            log.info("User " + email + "logged in successfully.");
            System.out.println("logged in");
            user.setLastLogin(new Date());
            session.put(AppConfig.SESSION_USER, user);
            return SUCCESS;
        } else {
            System.out.println("failed");
        }
        return INPUT;
    }

    public String logout() {
        session.clear();
        return SUCCESS;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
