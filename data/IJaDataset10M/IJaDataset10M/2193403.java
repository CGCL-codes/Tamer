package com.peterhi.net.msg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class InitSessionMessage extends AbstractMessage {

    private int targetClientHashCode;

    public int getTargetClientHashCode() {
        return targetClientHashCode;
    }

    public void setTargetClientHashCode(int targetClientHashCode) {
        this.targetClientHashCode = targetClientHashCode;
    }

    @Override
    protected void readData(DataInputStream in) throws IOException {
        targetClientHashCode = in.readInt();
    }

    @Override
    protected void writeData(DataOutputStream out) throws IOException {
        out.writeInt(targetClientHashCode);
    }

    public int getID() {
        return initSessionMessage;
    }
}
