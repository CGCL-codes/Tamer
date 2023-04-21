package org.kablink.teaming.security.function;

import java.util.Set;
import org.kablink.teaming.domain.Principal;

/**
 * 
 * 
 */
public interface WorkArea {

    public Long getWorkAreaId();

    /**
     * The type of the work area. 
     * The value must be between 1 and 16 characters long.
     * 
     * @return
     */
    public String getWorkAreaType();

    /**
     * Return parent workArea or <code>null</code> if top
     * @return
     */
    public WorkArea getParentWorkArea();

    /**
     * Return true if workArea can inherit function membership
     * @return
     */
    public boolean isFunctionMembershipInheritanceSupported();

    /**
     * Return true if workArea is currently inheritting function membership.
     * @return
     */
    public boolean isFunctionMembershipInherited();

    public void setFunctionMembershipInherited(boolean functionMembershipInherited);

    /**
     * Return the id of the owner of the workArea
     * @return
     */
    public Long getOwnerId();

    /**
     * Return the principal that is the owner of the workArea
     * @return
     */
    public Principal getOwner();

    public void setOwner(Principal owner);

    /**
     * Return true if currently inheritty team membership
     * @return
     */
    public boolean isTeamMembershipInherited();

    /**
     * Return the team members ids
     * @return
     */
    public Set<Long> getTeamMemberIds();

    public void setTeamMemberIds(Set<Long> memberIds);

    /**
     * Return the ids of child workAreas
     * @return
     */
    public Set<Long> getChildWorkAreas();
}
