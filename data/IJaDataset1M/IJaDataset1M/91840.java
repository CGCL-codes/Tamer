package com.jme3.network.message;

import com.jme3.network.serializing.Serializable;

@Serializable()
public class StreamMessage extends Message {

    private short streamID;

    public StreamMessage(short id) {
        streamID = id;
    }

    public StreamMessage() {
    }

    public short getStreamID() {
        return streamID;
    }

    public void setStreamID(short streamID) {
        this.streamID = streamID;
    }
}
