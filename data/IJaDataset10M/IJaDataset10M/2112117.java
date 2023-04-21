package org.blindsideproject.asterisk.meetme;

import java.util.ArrayList;
import java.util.Collection;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.MeetMeRoom;
import org.asteriskjava.live.MeetMeUser;
import org.blindsideproject.asterisk.IConference;
import org.blindsideproject.asterisk.IParticipant;

public class MeetMeRoomAdapter implements IConference {

    MeetMeRoom room;

    private boolean muted = false;

    private boolean locked = false;

    public MeetMeRoomAdapter(MeetMeRoom room) {
        this.room = room;
    }

    public void lock() {
        try {
            room.lock();
            locked = true;
        } catch (ManagerCommunicationException e) {
            e.printStackTrace();
        }
    }

    public void unlock() {
        try {
            room.unlock();
            locked = false;
        } catch (ManagerCommunicationException e) {
            e.printStackTrace();
        }
    }

    public void mute() {
        muted = true;
    }

    public void unmute() {
        muted = false;
    }

    public Collection<IParticipant> getParticipants() {
        Collection<IParticipant> participants = new ArrayList<IParticipant>();
        for (MeetMeUser user : room.getUsers()) {
            IParticipant participant = new MeetMeUserAdapter(user);
            participants.add(participant);
        }
        return participants;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isMuted() {
        return muted;
    }
}
