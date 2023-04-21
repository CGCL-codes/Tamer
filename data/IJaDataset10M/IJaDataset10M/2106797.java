package catalog.model.service;

import catalog.model.businessobject.User;
import catalog.model.exception.CatalogException;

/**
 * The user business service interface.
 * <p>
 * It contains all user management related business logic.
 * 
 * @author <a href="mailto:derek_shen@hotmail.com">Derek Y. Shen</a>
 */
public interface UserService {

    /**
	 * Login a user using username and password.
	 * 
	 * @param username the username to be used
	 * @param password the password to be used
	 * @return the user associated with the username and password
	 * @throws CatalogException
	 */
    public User login(String username, String password) throws CatalogException;

    /**
	 * Send email to the administrator.
	 * 
	 * @param senderAddress the email address of the sender
	 * @param senderName the name of the email sender
	 * @param sub the subject of the email
	 * @param msg the email message
	 * @throws CatalogException
	 */
    public void sendEmail(String senderAddress, String senderName, String sub, String msg) throws CatalogException;
}
