package kiff.interceptor;

import kiff.entity.User;

/**
 * User Aware interface.  This interfaces injects a user object.
 * @author Adam
 * @version $Id$
 * 
 * Created on Nov 22, 2008 at 9:58:31 PM 
 */
public interface UserAware {

    /**
	 * Sets the user.
	 * @param user The user to set.
	 */
    void setUser(User user);
}
