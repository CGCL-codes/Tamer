package fr.gfi.gfinet.server;

import java.io.Serializable;
import java.util.List;
import fr.gfi.gfinet.server.info.UserGroup;

/**
 * Inteface for user groups service.
 * 
 * @author Jean DAT
 * @since 2 ao�t 07
 */
public interface UserGroupService extends Serializable {

    /**
	 * Saves or update a user group.
	 * 
	 * @param userGroup user group to be saved
	 * @throws UserGroupServiceException if the operation doesn't complete normally
	 */
    void saveUserGroup(UserGroup userGroup) throws UserGroupServiceException;

    /**
	 * Lists all user groups.
	 * 
	 * @return a user group list
	 * @throws UserGroupServiceException if the operation doesn't complete normally
	 */
    List<UserGroup> listUserGroups() throws UserGroupServiceException;

    /**
	 * Deletes a user group.
	 * 
	 * @param id
	 * @throws UserGroupServiceException if the operation doesn't complete normally
	 */
    void deleteUserGroup(Long id) throws UserGroupServiceException;

    /**
	 * Gets a user group.
	 * 
	 * @param id of the user group
	 * @return The user group in case it exists, null otherwise
	 * @throws UserGroupServiceException if the operation doesn't complete normally
	 */
    UserGroup getUserGroupById(Long id) throws UserGroupServiceException;

    /**
	 * Gets a user group.
	 * 
	 * @param name of the agence
	 * @return The user group in case it exists, null otherwise
	 * @throws UserGroupServiceException if the operation doesn't complete normally
	 */
    UserGroup getUserGroupByName(String name) throws UserGroupServiceException;

    /**
	 * Gets a user group.
	 * 
	 * @param portalId of the agence
	 * @return The user group in case it exists, null otherwise
	 * @throws UserGroupServiceException if the operation doesn't complete normally
	 */
    UserGroup getUserGroupByPortalId(Long portalId) throws UserGroupServiceException;
}
