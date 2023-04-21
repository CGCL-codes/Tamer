package nl.tranquilizedquality.adm.security.business.manager;

/**
 * Interface representing the manager which is responsible for valid
 * authorization on the application.
 * 
 * @author Salomo Petrus (salomo.petrus@gmail.com)
 * @since 28 aug. 2011
 */
public interface AuthorizationManager {

    /**
	 * Determines if the loggedin user is authorized to do the stuff the
	 * specified authority allows you to do.
	 * 
	 * @param authority
	 *            The authority which will be checked on the logged in user.
	 * @return Returns true if the logged in user is authorized otherwise it
	 *         will return false.
	 */
    boolean isLoggedInUserAuthorized(String authority);
}
