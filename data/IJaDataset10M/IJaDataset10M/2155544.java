package lokahi.core.common.authentication;

import lokahi.core.common.exception.AuthenticationException;

/**
 * @author Stephen Toback
 * @version $Id: Authentication.java,v 1.1 2006/03/07 20:18:50 drtobes Exp $
 */
public interface Authentication {

    boolean authenticate(String userID, String password) throws AuthenticationException;
}
