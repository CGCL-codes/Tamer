package org.eledge.domain.participants;

import org.eledge.domain.AssignmentQuestion;
import org.eledge.domain.User;
import org.eledge.domain.auto._QuestionUserParticipant;

public class QuestionUserParticipant extends _QuestionUserParticipant implements ParticipantManager {

    /**
     * 
     */
    private static final long serialVersionUID = 3366821950094596516L;

    public static final String TYPE = "questionuser";

    private void setupType() {
        this.setType(TYPE);
    }

    @Override
    public ParticipantAssignable getParticipantAssignable() {
        return this.getAssignmentQuestion();
    }

    @Override
    public void setParticipantAssignable(ParticipantAssignable pa) {
        this.setAssignmentQuestion((AssignmentQuestion) pa);
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
