package com.wuala.loader2.copied.client.messages;

import java.io.IOException;
import com.wuala.loader2.copied.rmi.Loader2RemoteException;

public abstract class Ping extends MarshallableMessage {

    public short sequenceNumber;

    public Ping() {
    }

    public Ping(java.nio.ByteBuffer marshallData) {
        super(marshallData);
    }

    public abstract Pong run(Object realServer);

    public void handleConnectionProblem(IOException e) {
        throw new RuntimeException();
    }

    public void handleException(Loader2RemoteException t) {
        throw t;
    }

    public void toBytes(java.nio.ByteBuffer marshallData) {
        super.toBytes(marshallData);
    }

    public String toString() {
        return this.getClass().getName();
    }
}
