package org.openmim.mn2.model;

import java.util.List;

public class IRCChannelParticipantImpl extends RoomParticipantImpl implements IRCChannelParticipant {

    public IRCChannel getChannel() {
        return (IRCChannel) getRoom();
    }

    public void setVoice(boolean b) {
    }

    public void setOp(boolean b) {
    }

    public void setHalfOp(boolean b) {
    }

    public void modeChange(String senderSpecification, String modeFor, List<String> vector) {
    }

    public void addIRCChannelParticipantListener(IRCChannelParticipantListener listener) {
    }

    public void removeIRCChannelParticipantListener(IRCChannelParticipantListener listener) {
    }
}
