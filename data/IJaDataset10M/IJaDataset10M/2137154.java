package org.bpaul.rtalk.protocol;

import java.util.List;

public class CSJoinChatRoomResponse extends Response {

    private List<String> participants;

    public CSJoinChatRoomResponse() {
        super(RESPONSE_ID_JOINCHATROOM);
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}
