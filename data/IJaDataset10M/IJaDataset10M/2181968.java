package com.kharthick.authentication;

import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.annotations.InjectStateFlag;
import org.apache.tapestry.html.BasePage;
import com.kharthick.authentication.TapestryUser;

/**
 * AuthenticatedPage is used for Tapestry pages that require
 * user authentication.  Any Tapestry page class that needs 
 * this information should extend AuthenticatedPage instead
 * of BasePage.  The page specification can then simply use
 * "ognl:loggedIn" for any conditionals.  More complex
 * authentication, such as permissions, can be implemented
 * by the domain object stored in tapestryUser, or this
 * class can be extended to provide such application-specific
 * functionality.
 * 
 * Any login form in the application should call 
 *   setLoggedIn(user)
 * once it is satisfied that the user has provided the correct
 * credentials.  The user object provided will be
 * stored in the session, and can be accessed with
 * getTapestryUser().getUser().
 *  
 * @author Alex
 *
 */
public abstract class AuthenticatedPage extends BasePage {

    @InjectStateFlag("tapestryUser")
    public abstract boolean getTapestryUserExists();

    @InjectState("tapestryUser")
    public abstract TapestryUser getTapestryUser();

    public abstract void setTapestryUser(TapestryUser tapestryUser);

    /**
	 * Return true if the current session belongs to a logged
	 * in user.
	 * @author Alex
	 */
    public boolean getLoggedIn() {
        if (getTapestryUserExists() && getTapestryUser().isLoggedIn()) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Store user in the httpsession as logged in.  This doesn't
	 * modify user - it can be immutable.
	 * @author Alex
	 */
    public void setLoggedIn(Object user) {
        getTapestryUser().setUser(user);
        getTapestryUser().setLoggedIn(true);
    }
}
