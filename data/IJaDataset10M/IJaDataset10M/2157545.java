package org.apache.jetspeed.services.security.turbine;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletConfig;
import org.apache.jetspeed.om.profile.Profile;
import org.apache.jetspeed.om.profile.ProfileException;
import org.apache.jetspeed.om.security.Group;
import org.apache.jetspeed.om.security.JetspeedUser;
import org.apache.jetspeed.om.security.Role;
import org.apache.jetspeed.om.security.UserNamePrincipal;
import org.apache.jetspeed.om.security.turbine.TurbineGroup;
import org.apache.jetspeed.om.security.turbine.TurbineGroupPeer;
import org.apache.jetspeed.om.security.turbine.TurbineUserGroupRole;
import org.apache.jetspeed.om.security.turbine.TurbineUserGroupRolePeer;
import org.apache.jetspeed.services.JetspeedSecurity;
import org.apache.jetspeed.services.Profiler;
import org.apache.jetspeed.services.PsmlManager;
import org.apache.jetspeed.services.rundata.JetspeedRunData;
import org.apache.jetspeed.services.rundata.JetspeedRunDataService;
import org.apache.jetspeed.services.security.GroupException;
import org.apache.jetspeed.services.security.GroupManagement;
import org.apache.jetspeed.services.security.JetspeedSecurityException;
import org.apache.jetspeed.services.security.JetspeedSecurityService;
import org.apache.torque.Torque;
import org.apache.torque.om.NumberKey;
import org.apache.torque.util.Criteria;
import org.apache.turbine.services.InitializationException;
import org.apache.turbine.services.TurbineBaseService;
import org.apache.turbine.services.TurbineServices;
import org.apache.turbine.services.resources.ResourceService;
import org.apache.turbine.services.rundata.RunDataService;
import org.apache.turbine.util.Log;

/**
 * Default Jetspeed-Turbine Group Management implementation
 *
 *
 * @author <a href="mailto:david@bluesunrise.com">David Sean Taylor</a>
 * @version $Id: TurbineGroupManagement.java,v 1.11 2004/02/23 03:54:49 jford Exp $
 */
public class TurbineGroupManagement extends TurbineBaseService implements GroupManagement {

    private JetspeedRunDataService runDataService = null;

    private static final String CONFIG_DEFAULT_ROLE = "role.default";

    String defaultRole = "user";

    private static final String CASCADE_DELETE = "programmatic.cascade.delete";

    private static final boolean DEFAULT_CASCADE_DELETE = true;

    private boolean cascadeDelete;

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
    public Iterator getGroups(String username) throws JetspeedSecurityException {
        JetspeedUser user = null;
        try {
            user = JetspeedSecurity.getUser(new UserNamePrincipal(username));
        } catch (JetspeedSecurityException e) {
            throw new GroupException("Failed to Retrieve User: ", e);
        }
        Criteria criteria = new Criteria();
        criteria.add(TurbineUserGroupRolePeer.USER_ID, user.getUserId());
        List rels;
        HashMap groups;
        try {
            rels = TurbineUserGroupRolePeer.doSelect(criteria);
            if (rels.size() > 0) {
                groups = new HashMap(rels.size());
            } else groups = new HashMap();
            for (int ix = 0; ix < rels.size(); ix++) {
                TurbineUserGroupRole rel = (TurbineUserGroupRole) rels.get(ix);
                Group group = rel.getTurbineGroup();
                groups.put(group.getName(), group);
            }
        } catch (Exception e) {
            throw new GroupException("Failed to retrieve groups ", e);
        }
        return groups.values().iterator();
    }

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
    public Iterator getGroups() throws JetspeedSecurityException {
        Criteria criteria = new Criteria();
        List groups;
        try {
            groups = TurbineGroupPeer.doSelect(criteria);
        } catch (Exception e) {
            throw new GroupException("Failed to retrieve groups ", e);
        }
        return groups.iterator();
    }

    /**
     * Adds a <code>Group</code> into permanent storage.
     *
     * The security service can throw a <code>NotUniqueEntityException</code> when the public
     * credentials fail to meet the security provider-specific unique constraints.
     * The security service may optionally check the current user context
     * to determine if the requestor has permission to perform this action.
     *
     * @exception GroupException when the security provider has a general failure.
     * @exception NotUniqueEntityException when the public credentials fail to meet
     *                                   the security provider-specific unique constraints.
     * @exception InsufficientPrivilegeException when the requestor is denied due to insufficient privilege
     */
    public void addGroup(Group group) throws JetspeedSecurityException {
        if (groupExists(group.getName())) {
            throw new GroupException("The group '" + group.getName() + "' already exists");
        }
        try {
            TurbineGroup tgroup = new TurbineGroup();
            tgroup.setGroupName(group.getName());
            Criteria criteria = TurbineGroupPeer.buildCriteria(tgroup);
            NumberKey key = (NumberKey) TurbineGroupPeer.doInsert(criteria);
            group.setId(key.toString());
        } catch (Exception e) {
            throw new GroupException("Failed to create group '" + group.getName() + "'", e);
        }
        try {
            addDefaultGroupPSML(group);
        } catch (Exception e) {
            try {
                removeGroup(group.getName());
            } catch (Exception e2) {
            }
            throw new GroupException("failed to add default PSML for Group resource", e);
        }
    }

    protected void addDefaultGroupPSML(Group group) throws GroupException {
        try {
            JetspeedRunDataService runDataService = (JetspeedRunDataService) TurbineServices.getInstance().getService(RunDataService.SERVICE_NAME);
            JetspeedRunData rundata = runDataService.getCurrentRunData();
            Profile profile = Profiler.createProfile();
            profile.setGroup(group);
            profile.setMediaType("html");
            Profiler.createProfile(rundata, profile);
        } catch (ProfileException e) {
            try {
                removeGroup(group.getName());
            } catch (Exception e2) {
            }
            throw new GroupException("Failed to create Group PSML", e);
        }
    }

    /**
     * Saves a <code>Group</code> into permanent storage.
     *
     * The security service can throw a <code>NotUniqueEntityException</code> when the public
     * credentials fail to meet the security provider-specific unique constraints.
     * The security service may optionally check the current user context
     * to determine if the requestor has permission to perform this action.
     *
     * @exception GroupException when the security provider has a general failure.
     * @exception InsufficientPrivilegeException when the requestor is denied due to insufficient privilege
     */
    public void saveGroup(Group group) throws JetspeedSecurityException {
        if (!groupExists(group.getName())) {
            throw new GroupException("The group '" + group.getName() + "' doesn't exists");
        }
        try {
            if (group instanceof TurbineGroup) {
                TurbineGroupPeer.doUpdate((TurbineGroup) group);
            } else {
                throw new GroupException("TurbineGroupManagment: Group is not a Turbine group, cannot update");
            }
        } catch (Exception e) {
            throw new GroupException("Failed to create group '" + group.getName() + "'", e);
        }
    }

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
    public void removeGroup(String groupname) throws JetspeedSecurityException {
        Connection conn = null;
        try {
            conn = Torque.getConnection();
            Group group = this.getGroup(groupname);
            Criteria criteria = new Criteria();
            criteria.add(TurbineGroupPeer.GROUP_NAME, groupname);
            if (cascadeDelete) {
                Criteria criteria1 = new Criteria();
                criteria1.add(TurbineUserGroupRolePeer.GROUP_ID, group.getId());
                TurbineUserGroupRolePeer.doDelete(criteria1, conn);
            }
            TurbineGroupPeer.doDelete(criteria, conn);
            PsmlManager.removeGroupDocuments(group);
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (java.sql.SQLException sqle) {
                Log.error(sqle);
            }
            throw new GroupException("Failed to remove group '" + groupname + "'", e);
        } finally {
            try {
                Torque.closeConnection(conn);
            } catch (Exception e) {
            }
        }
    }

    /**
     * Join a user to a group.
     *
     * The security service may optionally check the current user context
     * to determine if the requestor has permission to perform this action.
     *
     * @exception GroupException when the security provider has a general failure retrieving users.
     * @exception InsufficientPrivilegeException when the requestor is denied due to insufficient privilege
     */
    public void joinGroup(String username, String groupname) throws JetspeedSecurityException {
        joinGroup(username, groupname, defaultRole);
    }

    /**
     * Join a user to a group - specific role.
     *
     * The security service may optionally check the current user context
     * to determine if the requestor has permission to perform this action.
     *
     * @exception GroupException when the security provider has a general failure retrieving groups.
     * @exception InsufficientPrivilegeException when the requestor is denied due to insufficient privilege
     */
    public void joinGroup(String username, String groupname, String rolename) throws JetspeedSecurityException {
        try {
            JetspeedUser user = JetspeedSecurity.getUser(username);
            Group group = this.getGroup(groupname);
            Role role = JetspeedSecurity.getRole(rolename);
            Criteria criteria = new Criteria();
            criteria.add(TurbineUserGroupRolePeer.USER_ID, user.getUserId());
            criteria.add(TurbineUserGroupRolePeer.GROUP_ID, group.getId());
            criteria.add(TurbineUserGroupRolePeer.ROLE_ID, role.getId());
            TurbineUserGroupRolePeer.doInsert(criteria);
        } catch (Exception e) {
            throw new GroupException("Join group '" + groupname + "' to user '" + username + "' failed: ", e);
        }
    }

    /**
     * Unjoin a user from a group.
     *
     * The security service may optionally check the current user context
     * to determine if the requestor has permission to perform this action.
     *
     * @exception GroupException when the security provider has a general failure retrieving users.
     * @exception InsufficientPrivilegeException when the requestor is denied due to insufficient privilege
     */
    public void unjoinGroup(String username, String groupname) throws JetspeedSecurityException {
        unjoinGroup(username, groupname, defaultRole);
    }

    /**
     * Unjoin a user from a group in which the user has a specific role instead of <Code>JetspeedSecurity.getRole(defaultRole)</Code>
     *
     * The security service may optionally check the current user context
     * to determine if the requestor has permission to perform this action.
     *
     * @exception GroupException when the security provider has a general failure retrieving users.
     * @exception InsufficientPrivilegeException when the requestor is denied due to insufficient privilege
     */
    public void unjoinGroup(String username, String groupname, String rolename) throws JetspeedSecurityException {
        try {
            JetspeedUser user = JetspeedSecurity.getUser(username);
            Group group = this.getGroup(groupname);
            Role role = JetspeedSecurity.getRole(rolename);
            Criteria criteria = new Criteria();
            criteria.add(TurbineUserGroupRolePeer.USER_ID, user.getUserId());
            criteria.add(TurbineUserGroupRolePeer.GROUP_ID, group.getId());
            criteria.add(TurbineUserGroupRolePeer.ROLE_ID, role.getId());
            TurbineUserGroupRolePeer.doDelete(criteria);
        } catch (Exception e) {
            throw new GroupException("Unjoin group '" + groupname + "' to user '" + username + "' failed: ", e);
        }
    }

    /**
     * Checks for the relationship of user in a group. Returns true when the user is in the given group.
     *
     * The security service may optionally check the current user context
     * to determine if the requestor has permission to perform this action.
     *
     * @exception GroupException when the security provider has a general failure retrieving users.
     * @exception InsufficientPrivilegeException when the requestor is denied due to insufficient privilege
     */
    public boolean inGroup(String username, String groupname) throws JetspeedSecurityException {
        List groups;
        try {
            JetspeedUser user = JetspeedSecurity.getUser(username);
            Group group = this.getGroup(groupname);
            Criteria criteria = new Criteria();
            criteria.add(TurbineUserGroupRolePeer.USER_ID, user.getUserId());
            criteria.add(TurbineUserGroupRolePeer.GROUP_ID, group.getId());
            groups = TurbineUserGroupRolePeer.doSelect(criteria);
        } catch (Exception e) {
            throw new GroupException("Failed to check group '" + groupname + "'", e);
        }
        return (groups.size() > 0);
    }

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
    public Group getGroup(String groupname) throws JetspeedSecurityException {
        List groups;
        try {
            Criteria criteria = new Criteria();
            criteria.add(TurbineGroupPeer.GROUP_NAME, groupname);
            groups = TurbineGroupPeer.doSelect(criteria);
        } catch (Exception e) {
            throw new GroupException("Failed to retrieve group '" + groupname + "'", e);
        }
        if (groups.size() > 1) {
            throw new GroupException("Multiple Groups with same groupname '" + groupname + "'");
        }
        if (groups.size() == 1) {
            TurbineGroup group = (TurbineGroup) groups.get(0);
            return group;
        }
        throw new GroupException("Unknown group '" + groupname + "'");
    }

    protected JetspeedRunData getRunData() {
        JetspeedRunData rundata = null;
        if (this.runDataService != null) {
            rundata = this.runDataService.getCurrentRunData();
        }
        return rundata;
    }

    /**
     * Check whether a specified group exists.
     *
     * The login name is used for looking up the account.
     *
     * @param groupName the name of the group to check for existence.
     * @return true if the specified account exists
     * @throws GroupException if there was a general db access error
     *
     */
    protected boolean groupExists(String groupName) throws GroupException {
        Criteria criteria = new Criteria();
        criteria.add(TurbineGroupPeer.GROUP_NAME, groupName);
        List groups;
        try {
            groups = TurbineGroupPeer.doSelect(criteria);
        } catch (Exception e) {
            throw new GroupException("Failed to check account's presence", e);
        }
        if (groups.size() < 1) {
            return false;
        }
        return true;
    }

    /**
     * This is the early initialization method called by the
     * Turbine <code>Service</code> framework
     * @param conf The <code>ServletConfig</code>
     * @exception throws a <code>InitializationException</code> if the service
     * fails to initialize
     */
    public synchronized void init(ServletConfig conf) throws InitializationException {
        if (getInit()) return;
        super.init(conf);
        ResourceService serviceConf = ((TurbineServices) TurbineServices.getInstance()).getResources(JetspeedSecurityService.SERVICE_NAME);
        this.runDataService = (JetspeedRunDataService) TurbineServices.getInstance().getService(RunDataService.SERVICE_NAME);
        defaultRole = serviceConf.getString(CONFIG_DEFAULT_ROLE, defaultRole);
        cascadeDelete = serviceConf.getBoolean(CASCADE_DELETE, DEFAULT_CASCADE_DELETE);
        setInit(true);
    }
}
