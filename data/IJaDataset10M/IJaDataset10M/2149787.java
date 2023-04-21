package com.peterhi.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class KickMsg extends AbstractMessage {

    public String mule;

    @Override
    protected void readData(DataInputStream in) throws IOException {
        mule = IO.readString(in);
    }

    @Override
    protected void writeData(DataOutputStream out) throws IOException {
        IO.writeString(out, mule);
    }

    @Override
    public int getID() {
        return kick;
    }
}
