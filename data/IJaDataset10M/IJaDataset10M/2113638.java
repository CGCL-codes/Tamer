package com.ngranek.unsolved.server.messages;

import java.io.IOException;

public abstract class AbstractMessage {

    private int sessionId = -1;

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public abstract byte[] getBytes() throws IOException;

    public abstract void buildMessage(byte[] bytes) throws IOException;
}
