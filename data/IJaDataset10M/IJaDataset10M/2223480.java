package net.onlinepresence.opos.tapestry.pages;

import net.onlinepresence.opos.core.spring.SpringBean;
import net.onlinepresence.opos.domain.Membership;
import net.onlinepresence.opos.domain.beans.LoggedUserBean;
import net.onlinepresence.opos.domain.service.Applications;
import net.onlinepresence.opos.domain.service.Users;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

@IncludeStylesheet({ "context:css/jquery.fancybox-1.3.0.css" })
@IncludeJavaScriptLibrary({ "context:js/jquery.min.js", "context:js/login.js", "context:js/jquery.fancybox-1.3.0.js", "context:js/jquery.fancybox-1.3.0.activation.js" })
public class Account {

    @Property
    @SessionState
    private LoggedUserBean loggedUser;

    private boolean loggedUserExists;

    @Inject
    @SpringBean("net.onlinepresence.opos.domain.service.Users")
    private Users persons;

    @SuppressWarnings("unused")
    @Inject
    @SpringBean("net.onlinepresence.opos.domain.service.Applications")
    private Applications apps;

    @SuppressWarnings("unused")
    @Property
    private Membership currentMembership;

    @Property
    private String passwordConfirmation;

    @Property
    private String password;

    Object onActivate() {
        if (!loggedUserExists) return Login.class;
        return null;
    }

    Object onSubmitFromUserDetailsForm() {
        if (!password.equals(passwordConfirmation)) return null;
        loggedUser.getUser().setPassword(password);
        persons.update(loggedUser.getUser());
        return null;
    }

    Object onSubmitFromDeleteAccount() {
        persons.removeUser(loggedUser.getUser());
        loggedUser = null;
        return Index.class;
    }
}
