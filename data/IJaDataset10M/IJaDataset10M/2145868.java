package com.peterhi.net.message;

import com.peterhi.PeterHi;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 *
 * @author YUN TAO
 */
public class AcceptSessionMessage extends ReliableMessage {

    public int sender;

    public int receiver;

    @Override
    public byte getType() {
        return PeterHi.ACCEPT_SESSION_MESSAGE;
    }

    @Override
    protected void writeData(DataOutput out) throws IOException {
        super.writeData(out);
        out.writeInt(sender);
        out.writeInt(receiver);
    }

    @Override
    protected void readData(DataInput in) throws IOException {
        super.readData(in);
        sender = in.readInt();
        receiver = in.readInt();
    }
}
