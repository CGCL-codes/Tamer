package org.redemptionhost.web.pages;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class LoginPage extends BasePage {

    private static final long serialVersionUID = 1L;

    public LoginPage() {
        super();
        add(new Label("username", new UsernameModel()));
        add(new LoginLink("loginLink"));
    }

    /** Provides the username and email. */
    private final class UsernameModel extends AbstractReadOnlyModel<String> {

        private static final long serialVersionUID = 1L;

        @Override
        public String getObject() {
            final UserService userService = getUserService();
            if (userService.isUserLoggedIn()) {
                final User currentUser = userService.getCurrentUser();
                return String.format("%s [%s]", currentUser.getNickname(), currentUser.getEmail());
            } else {
                return null;
            }
        }

        private UserService getUserService() {
            return UserServiceFactory.getUserService();
        }
    }
}
