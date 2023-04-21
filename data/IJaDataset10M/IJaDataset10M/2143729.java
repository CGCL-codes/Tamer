package org.personalsmartspace.psm.groupmgmt.api;

import java.util.Set;
import org.personalsmartspace.psm.groupmgmt.common.PssGroupId;

/**
 * Dynamic Group Membership Evaluator interface. Handles and manages the
 * repository of MERs (Membership Evaluator Rules).
 * 
 * @author Guido Spadotto
 * @version 1.0
 * @created 19-ago-2009 16.08.51
 */
public interface IDgmManager {

    /**
     * Return all the available MERs
     */
    public Set<IPssGroupMembershipEvaluator> getAllMers();

    /**
     * Links a MER to a PssGroup
     * 
     * @param merId
     *            The Id of the MER to be linked
     * @param groupId
     *            The Group to link the MER to
     */
    public void linkMerToGroup(String merId, PssGroupId groupId);

    /**
     * Unlinks a MER to a PssGroup
     * 
     * @param merId
     *            The Id of the MER to be linked
     * @param groupId
     *            The Group to link the MER to
     */
    public void unlinkMerFromGroup(String merId, PssGroupId groupId);

    /**
     * Returns the MerId of the MER associated to the group with the provided
     * GroupId, if any.
     * 
     * @param groupId
     */
    public String getMerIdForGroup(PssGroupId groupId);

    /**
     * Finds a locally available evaluator, provided its id.
     * 
     * @param merId
     *            The mer Id to search for
     * @return The locally available mer instance corresponding to the id, or
     *         null if not found
     */
    public IPssGroupMembershipEvaluator findEvaluatorById(String merId);
}
