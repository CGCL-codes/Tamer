package org.eledge.domain.permissions;

import org.eledge.domain.AdvisorWard;
import org.eledge.domain.DiscussionBoardEntry;
import org.eledge.domain.User;
import org.eledge.domain.auto._DiscussionEntryPermission;
import org.eledge.domain.participants.ParticipantDeterminer;

public class DiscussionEntryPermission extends _DiscussionEntryPermission {

    /**
     * 
     */
    private static final long serialVersionUID = -7270590781537191615L;

    public static final String NAME = "discussionentry";

    private void setupName() {
        this.setName(NAME);
    }

    @Override
    public boolean meetsReadPermissionConditions(User u, PermissionRequired obj) {
        DiscussionBoardEntry e = (DiscussionBoardEntry) obj;
        return (AdvisorWard.isAdvising(u, e.getAuthor())) || (ParticipantDeterminer.isParticipating(u, e.getForum()) && e.getApproved().booleanValue()) || (u.getUserId().equals(e.getAuthor().getUserId()));
    }

    @Override
    public boolean meetsEditPermissionConditions(User u, PermissionRequired obj) {
        DiscussionBoardEntry e = (DiscussionBoardEntry) obj;
        if (e.getAuthor().getUserId().equals(u.getUserId()) || AdvisorWard.isAdvising(u, e.getAuthor())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isConditionalBitUsed() {
        return true;
    }
}
