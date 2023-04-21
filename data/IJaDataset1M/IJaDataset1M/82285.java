package uk.azdev.openfire.net.attrvalues;

import java.nio.ByteBuffer;
import uk.azdev.openfire.common.SessionId;

public class SessionIdAttributeValue implements AttributeValue<SessionId> {

    public static final int TYPE_ID = 3;

    private SessionId sessionId;

    public SessionIdAttributeValue() {
        sessionId = new SessionId();
    }

    public SessionIdAttributeValue(SessionId sessionId) {
        this.sessionId = sessionId;
    }

    public SessionId getValue() {
        return sessionId;
    }

    public void setSessionId(SessionId sessionId) {
        this.sessionId = sessionId;
    }

    public int getSize() {
        return SessionId.SESSION_ID_SIZE;
    }

    public int getTypeId() {
        return TYPE_ID;
    }

    public SessionIdAttributeValue newInstance() {
        return new SessionIdAttributeValue();
    }

    public void readValue(ByteBuffer buffer) {
        byte[] sessionIdBytes = new byte[SessionId.SESSION_ID_SIZE];
        buffer.get(sessionIdBytes);
        sessionId = new SessionId(sessionIdBytes);
    }

    public void writeValue(ByteBuffer buffer) {
        buffer.put(sessionId.getBytes());
    }

    @Override
    public String toString() {
        return sessionId.toString();
    }
}
