package org.apache.jetspeed.services.security;

import java.util.Iterator;
import org.apache.jetspeed.om.security.Group;
import org.apache.turbine.services.Service;

/**
 * <p> The <code>GroupManagement</code> interface describes contract between
 * the portal and security provider required for Jetspeed Group Management.
 * This interface enables an application to be independent of the underlying
 * group management technology.
 *
 * @author <a href="mailto:david@bluesunrise.com">David Sean Taylor</a>
 * @version $Id: GroupManagement.java,v 1.4 2004/02/23 03:58:11 jford Exp $
 */
public interface GroupManagement extends Service {

    public String SERVICE_NAME = "GroupManagement";

    public String DEFAULT_GROUP_NAME = "Jetspeed";

    /**
     * Retrieves all <code>Group</code>s for a given username principal.
     *
     * The security service may optionally check the current user context
     * to determine if the requestor has permission to perform this action.
     *
     * @param username a user principal identity to be retrieved.
     * @return Iterator over all groups associated to the user principal.
     * @exception GroupException when the security provider has a general failure.
     * @exception InsufficientPrivilegeException when the requestor is denied due to insufficient privilege
     */
    Iterator getGroups(String username) throws JetspeedSecurityException;

    /**
     * Retrieves all <code>Group</code>s.
     *
     * The security service may optionally check the current user context
     * to determine if the requestor has permission to perform this action.
     *
     * @return Iterator over all groups.
     * @exception GroupException when the security provider has a general failure.
     * @exception InsufficientPrivilegeException when the requestor is denied due to insufficient privilege
     */
    Iterator getGroups() throws JetspeedSecurityException;

    /**
     * Adds a <code>Group</code> into permanent storage.
     *
     * The security service may optionally check the current user context
     * to determine if the requestor has permission to perform this action.
     *
     * @exception GroupException when the security provider has a general failure.
     * @exception InsufficientPrivilegeException when the requestor is denied due to insufficient privilege
     */
    void addGroup(Group group) throws JetspeedSecurityException;

    /**
     * Saves a <code>Group</code> into permanent storage.
     *
     * The security service may optionally check the current user context
     * to determine if the requestor has permission to perform this action.
     *
     * @exception GroupException when the security provider has a general failure.
     * @exception InsufficientPrivilegeException when the requestor is denied due to insufficient privilege
     */
    void saveGroup(Group group) throws JetspeedSecurityException;

    /**
     * Removes a <code>Group</code> from the permanent store.
     *
     * The security service may optionally check the current user context
     * to determine if the requestor has permission to perform this action.
     *
     * @param groupname the principal identity of the group to be retrieved.
     * @exception GroupException when the security provider has a general failure.
     * @exception InsufficientPrivilegeException when the requestor is denied due to insufficient privilege
     */
    void removeGroup(String groupname) throws JetspeedSecurityException;

    /**
     * Joins a user to a group.
     *
     * The security service may optionally check the current user context
     * to determine if the requestor has permission to perform this action.
     *
     * @exception GroupException when the security provider has a general failure retrieving groups.
     * @exception InsufficientPrivilegeException when the requestor is denied due to insufficient privilege
     */
    void joinGroup(String username, String groupname) throws JetspeedSecurityException;

    /**
     * Joins a user into a group with a specific role.
     *
     * The security service may optionally check the current user context
     * to determine if the requestor has permission to perform this action.
     *
     * @exception GroupException when the security provider has a general failure retrieving groups.
     * @exception InsufficientPrivilegeException when the requestor is denied due to insufficient privilege
     */
    void joinGroup(String username, String groupname, String rolename) throws JetspeedSecurityException;

    /**
     * Unjoins a user from a group.
     *
     * The security service may optionally check the current user context
     * to determine if the requestor has permission to perform this action.
     *
     * @exception GroupException when the security provider has a general failure retrieving groups.
     * @exception InsufficientPrivilegeException when the requestor is denied due to insufficient privilege
     */
    void unjoinGroup(String username, String groupname) throws JetspeedSecurityException;

    /**
    * Unjoins a user from a group - specific role.
    *
    * The security service may optionally check the current user context
    * to determine if the requestor has permission to perform this action.
    *
    * @exception GroupException when the security provider has a general failure retrieving groups.
    * @exception InsufficientPrivilegeException when the requestor is denied due to insufficient privilege
    */
    void unjoinGroup(String username, String groupname, String rolename) throws JetspeedSecurityException;

    /**
     * Checks for the relationship of user in a group. Returns true when the user is in the given group.
     *
     * The security service may optionally check the current user context
     * to determine if the requestor has permission to perform this action.
     *
     * @exception GroupException when the security provider has a general failure retrieving groups.
     * @exception InsufficientPrivilegeException when the requestor is denied due to insufficient privilege
     */
    boolean inGroup(String username, String groupname) throws JetspeedSecurityException;

    /**
     * Retrieves a single <code>Group</code> for a given groupname principal.
     *
     * The security service may optionally check the current user context
     * to determine if the requestor has permission to perform this action.
     *
     * @param groupname a group principal identity to be retrieved.
     * @return Group the group record retrieved.
     * @exception GroupException when the security provider has a general failure.
     * @exception InsufficientPrivilegeException when the requestor is denied due to insufficient privilege
     */
    Group getGroup(String groupname) throws JetspeedSecurityException;
}
