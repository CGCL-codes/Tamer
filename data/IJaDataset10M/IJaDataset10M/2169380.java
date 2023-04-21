package org.eledge.domain.participants;

import org.eledge.domain.CalendarEvent;
import org.eledge.domain.CourseStudentUserGroup;
import org.eledge.domain.User;
import org.eledge.domain.auto._EventGroupParticipant;

public class EventGroupParticipant extends _EventGroupParticipant implements ParticipantManager {

    /**
     * 
     */
    private static final long serialVersionUID = -3029949727872043088L;

    public static final String TYPE = "eventgroup";

    private void setupType() {
        this.setType(TYPE);
    }

    @Override
    public ParticipantAssignable getParticipantAssignable() {
        return this.getEvent();
    }

    @Override
    public void setParticipantAssignable(ParticipantAssignable pa) {
        this.setEvent((CalendarEvent) pa);
    }

    @Override
    public Participant getParticipant() {
        return this.getGroup();
    }

    @Override
    public void setParticipant(Participant p) {
        this.setGroup((CourseStudentUserGroup) p);
    }

    @Override
    public boolean isParticipating(User u) {
        return GroupParticicpantDeterminer.isParticipant(u, this);
    }
}
