package edu.harvard.fas.rregan.requel.project;

import java.util.Set;
import edu.harvard.fas.rregan.requel.user.User;

/**
 * A stakeholder that is a system user with project specific permission to
 * modify project entities.
 * 
 * @author ron
 */
public interface UserStakeholder extends Stakeholder {

    /**
	 * @return if the stakeholder is associated with a user, this will return
	 *         that user, otherwise it will return null.
	 */
    public User getUser();

    /**
	 * @return the team that this stakeholder is assigned for the project.
	 */
    public ProjectTeam getTeam();

    /**
	 * @return The set of permissions that the stakeholder has for a project.
	 */
    public Set<StakeholderPermission> getStakeholderPermissions();

    /**
	 * @param entityType
	 * @param permissionType
	 * @return true if the user has the specified permission type on the
	 *         specified entity type.
	 */
    public boolean hasPermission(Class<?> entityType, StakeholderPermissionType permissionType);

    /**
	 * @param stakeholderPermission
	 * @return true if the user has the specified permission.
	 */
    public boolean hasPermission(StakeholderPermission stakeholderPermission);

    /**
	 * Grant the stakeholder the specified permission.
	 * 
	 * @param stakeholderPermission
	 */
    public void grantStakeholderPermission(StakeholderPermission stakeholderPermission);

    /**
	 * Revoke the specified permission from the stakeholder.
	 * 
	 * @param stakeholderPermission
	 */
    public void revokeStakeholderPermission(StakeholderPermission stakeholderPermission);
}
