package net.onlinepresence.opos.tapestry.components;

import net.onlinepresence.opos.core.spring.SpringBean;
import net.onlinepresence.opos.domain.User;
import net.onlinepresence.opos.domain.beans.LoggedUserBean;
import net.onlinepresence.opos.domain.beans.UserBean;
import net.onlinepresence.opos.domain.service.Users;
import net.onlinepresence.opos.tapestry.pages.Connections;
import net.onlinepresence.opos.tapestry.pages.Index;
import net.onlinepresence.opos.tapestry.pages.Registration;
import net.onlinepresence.opos.util.Authentication;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.BindingConstants;

@IncludeJavaScriptLibrary({ "context:js/jquery.min.js", "context:js/login.js" })
public class Layout {

    @Inject
    @SpringBean("net.onlinepresence.opos.domain.service.Users")
    private Users persons;

    @SessionState
    private LoggedUserBean loggedUser;

    @Property
    @SuppressWarnings("unused")
    private boolean loggedUserExists;

    /** The page title, for the <title> element and the <h1> element. */
    @Property
    @SuppressWarnings("unused")
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String title;

    @Property
    @Parameter
    @SuppressWarnings("unused")
    private Block additionalJS;

    @Property
    @Persist("flash")
    @SuppressWarnings("unused")
    private User user;

    @Property
    private String usernameOrEmail;

    @Property
    private String password;

    @SetupRender
    public void createObject() {
        user = new UserBean();
    }

    Object onSubmitFromLoginTopForm() {
        Authentication auth = new Authentication(persons);
        User u = auth.authenticateUser(usernameOrEmail, password);
        if (u != null) {
            loggedUser.setUser(u);
            return Connections.class;
        } else {
            if (!auth.isUserExists()) return Registration.class; else return null;
        }
    }

    @OnEvent(component = "signout")
    public Object signout() {
        loggedUser = null;
        return Index.class;
    }
}
