package com.peterhi.net.msg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RSesMsg extends AbstractMSG {

    private int senderHashCode;

    public int getSenderHashCode() {
        return senderHashCode;
    }

    public void setSenderHashCode(int senderHashCode) {
        this.senderHashCode = senderHashCode;
    }

    @Override
    protected void readData(DataInputStream in) throws IOException {
        senderHashCode = in.readInt();
    }

    @Override
    protected void writeData(DataOutputStream out) throws IOException {
        out.writeInt(senderHashCode);
    }

    public int getID() {
        return MSG_RSES;
    }
}
