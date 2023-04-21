package com.peterhi.net.message;

import com.peterhi.io.IO;
import com.peterhi.PeterHi;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author YUN TAO
 */
public class CommandMessage extends Message {

    public UUID id;

    public String command;

    @Override
    public byte getType() {
        return PeterHi.COMMAND_MESSAGE;
    }

    @Override
    protected void writeData(DataOutput out) throws IOException {
        IO.writeString(out, id.toString());
        IO.writeString(out, command);
    }

    @Override
    protected void readData(DataInput in) throws IOException {
        id = UUID.fromString(IO.readString(in));
        command = IO.readString(in);
    }
}
