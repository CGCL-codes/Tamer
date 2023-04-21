package communication;

public class ParticipantJoinedRequest extends Request {

    private String buddyJoined;

    private String convID;

    public ParticipantJoinedRequest(String sender, String buddy, String id) {
        super(sender);
        buddyJoined = buddy;
        convID = id;
    }

    public String getBuddy() {
        return buddyJoined;
    }

    public String getConvID() {
        return convID;
    }
}
