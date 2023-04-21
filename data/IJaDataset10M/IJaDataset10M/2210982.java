package fuzzylizard.teamdocs.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import fuzzylizard.teamdocs.domain.User;
import fuzzylizard.teamdocs.services.UserService;
import fuzzylizard.teamdocs.services.UserServiceImpl;
import fuzzylizard.teamdocs.web.wicket.TeamdocsSession;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.PasswordTextField;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.PropertyModel;

public class Login extends TeamdocsPage {

    private Log log = LogFactory.getLog(Login.class);

    private String username;

    private String password;

    private FeedbackPanel feedback;

    public Login() {
        feedback = new FeedbackPanel("feedback");
        Form form = new Form("loginForm") {

            public void onSubmit() {
                authenticateUser(Login.this.getUsername(), Login.this.getPassword());
            }
        };
        form.add(new TextField("username", new PropertyModel(this, "username")));
        form.add(new PasswordTextField("password", new PropertyModel(this, "password")));
        add(feedback);
        add(form);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private void authenticateUser(String username, String password) {
        log.debug("authenticating username: " + username + ", password: " + password);
        UserService userService = new UserServiceImpl();
        User user = userService.findUser(username, password);
        if (user != null) {
            putUserInSession(user);
            redirectToNextPage();
        } else {
            feedback.error("Error logging in. Please try again.");
        }
    }

    private void redirectToNextPage() {
        if (!continueToOriginalDestination()) {
            Dashboard dashboard = new Dashboard();
            setResponsePage(dashboard);
        }
    }

    private void putUserInSession(User user) {
        TeamdocsSession teamdocsSession = getTeamdocsSession();
        teamdocsSession.setUser(user);
    }
}
