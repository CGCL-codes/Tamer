package net.sf.brightside.stockswatcher.www.tapestry.pages;

import net.sf.brightside.stockswatcher.server.metamodel.User;
import net.sf.brightside.stockswatcher.server.service.api.hibernate.ILogIn;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Login {

    private String username;

    private String password;

    @InjectPage
    private Home homePage;

    @Inject
    private ILogIn login;

    @Validate("required")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Validate("required")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OnEvent(value = "submit", component = "userLoginForm")
    Object onFormSubmit() {
        System.out.println("Handling from submission!");
        System.out.println("Pokusaj logoavnja usera: " + this.username + " sa sifrom: " + this.password);
        User user = login.verifyUser(this.username, this.password);
        if (user != null) {
            homePage.setUser(user);
            return homePage;
        } else {
            System.out.println("Username or password incorect! Try again.");
            return null;
        }
    }
}
