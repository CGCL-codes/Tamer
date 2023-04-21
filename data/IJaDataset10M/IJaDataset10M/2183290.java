package mobilestock.server.service.api.hibernate;

import mobilestock.server.metamodel.User;

public interface ILogIn {

    /**Verifies is user exists
	 * <p>
	 * @param username
	 * @param password
	 * @return User if true, null if false
	 */
    User verifyUser(String username, String password);
}
