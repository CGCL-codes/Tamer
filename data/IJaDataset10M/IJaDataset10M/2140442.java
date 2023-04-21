package net.sf.brightside.stockswatcher.www.tapestry.pages;

import net.sf.brightside.stockswatcher.server.metamodel.User;
import net.sf.brightside.stockswatcher.server.service.api.hibernate.ICreateUser;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Start {

    @Inject
    private ICreateUser createUser;

    private String testUsername, testPassword;

    private String name = "This is just a test";

    @Persist
    private String userStatus;

    public String getUserStatus() {
        return userStatus;
    }

    public String getUsername() {
        return testUsername;
    }

    @Validate("required")
    public void setUsername(String testUsername) {
        this.testUsername = testUsername;
    }

    public String getPassword() {
        return testPassword;
    }

    @Validate("required")
    public void setPassword(String testPassword) {
        this.testPassword = testPassword;
    }

    @OnEvent(value = "submit", component = "createUserForm")
    void onFormSubmit() {
        System.out.println("Handling from submission!");
        this.userStatus = "Wait...";
        User user = createUser.creteUser(name, this.testUsername, this.testPassword);
        if (user != null) {
            System.out.println("Successfully created test user with username: " + user.getUsername());
            this.userStatus = "User created successfully!";
        } else {
            System.out.println("User with username : " + this.testUsername + " already exists!");
            this.userStatus = "User with username : " + this.testUsername + " already exists!\nUser not created!";
        }
    }
}
