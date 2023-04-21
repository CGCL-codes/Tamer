package edu.berkeley.cs160.innovationers.shoppersidekick.model;

/**
 * The interface for the UserModel which should be implemented by the
 * Server and Client.
 * 
 * @author Nadir Muzaffar
 *
 */
public interface UserModelDefinition {

    /**
	 * 
	 * @return String: the users actuall full name or NULL if there is no name for this user.
	 */
    public String getUserName();

    /**
	 * 
	 * @return String: the email associated with the given user account.
	 */
    public String getEmail();

    /**
	 * 
	 * @return should return Email addresses of the contacts this given user has. 
	 */
    public String[] getEmailOfContacts();

    /**
	 * 
	 * @return true if successfully added, false otherwise.
	 */
    public boolean associateListToUser(ListModelDefinition list);

    /**
	 * 
	 * @return 
	 */
    public boolean unassociateListFromUser(ListModelDefinition list);
}
