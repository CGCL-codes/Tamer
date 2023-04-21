package org.torbs.engine.protocol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 */
public class RaceEndInfo implements MessageInterface {

    public long mSimTime;

    public RaceEndInfo(long simTime) {
        mSimTime = simTime;
    }

    public RaceEndInfo(ObjectInputStream pOis) throws IOException, ClassNotFoundException {
        receive(pOis);
    }

    @Override
    public int getID() {
        return Protocol.RACE_END_INFO;
    }

    @Override
    public void receive(ObjectInputStream pOis) throws IOException, ClassNotFoundException {
        mSimTime = pOis.readLong();
    }

    @Override
    public void send(ObjectOutputStream pOos) throws IOException {
        pOos.writeLong(mSimTime);
    }
}
