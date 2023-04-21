package org.eledge.domain.participants;

import org.eledge.domain.DiscussionBoardForum;
import org.eledge.domain.User;
import org.eledge.domain.auto._ForumUserParticipant;

public class ForumUserParticipant extends _ForumUserParticipant implements ParticipantManager {

    /**
     * 
     */
    private static final long serialVersionUID = -436575923366301942L;

    public static final String TYPE = "forumuser";

    private void setupType() {
        this.setType(TYPE);
    }

    @Override
    public ParticipantAssignable getParticipantAssignable() {
        return this.getForum();
    }

    @Override
    public void setParticipantAssignable(ParticipantAssignable pa) {
        this.setForum((DiscussionBoardForum) pa);
    }

    @Override
    public Participant getParticipant() {
        return this.getUser();
    }

    @Override
    public void setParticipant(Participant p) {
        this.setUser((User) p);
    }

    @Override
    public boolean isParticipating(User u) {
        return UserParticipantDeterminer.isParticipant(u, this);
    }
}
