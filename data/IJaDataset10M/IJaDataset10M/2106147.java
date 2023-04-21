package org.systemsbiology.apps.gui.server.provider.access;

import java.util.ArrayList;
import org.systemsbiology.apps.gui.domain.Access;
import org.systemsbiology.apps.gui.domain.ATAQSProject;
import org.systemsbiology.apps.gui.domain.User;

/**
 * Interface for the Access Provider
 * @author Mark Christiansen
 *
 * @see Access
 * @see User
 * @see ATAQSProject
 */
public interface IAccessInfoProvider {

    /**
     * Get Access List for a given User
     * @param user User to get access list for
     * @return ArrayList of Access
     */
    public abstract ArrayList<Access> getUserAccessList(User user);

    /**
	 * Add Access to a project for a user
	 * @param user User to add access for
	 * @param permission permissions to assign
	 * @param proj project to add access to
	 * @return Access
	 */
    public abstract Access addAccess(User user, int permission, ATAQSProject proj);

    /**
	 * Save Access
	 * @param access Access to save
	 * @return boolean on success of save
	 */
    public abstract boolean saveAccess(Access access);

    /**
	 * Delete Access 
	 * @param access Access to delete
	 * @return boolean on success of delete
	 */
    public abstract boolean deleteAccess(Access access);

    /**
	 * Delete Access if there is one for a given User and ATAQSProject
	 * Delete a user's access to an ATAQSProject
	 * @param user User to delete access to
	 * @param proj Project to delete access to
	 * @return boolean on success of delete
	 */
    public abstract boolean deleteAccess(User user, ATAQSProject proj);

    /**
	 * Delete all Access for an ATAQSProject
	 * @param project ATAQSProject to delete all access to
	 * @return boolean on success of delete
	 */
    public abstract boolean deleteAccessForProject(ATAQSProject project);

    /**
	 * Retrieve access list for an ATAQSProject
	 * @param project ATAQSProject to get access to
	 * @return ArrayList of Accesses
	 */
    public abstract ArrayList<Access> getProjectAccessList(ATAQSProject project);
}
