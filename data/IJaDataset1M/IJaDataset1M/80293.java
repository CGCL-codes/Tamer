package com.peterhi.net.message;

import com.peterhi.io.IO;
import com.peterhi.PeterHi;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 *
 * @author YUN TAO
 */
public class PublicTextMessage extends SenderMessage {

    public String text;

    @Override
    public byte getType() {
        return PeterHi.PUBLIC_TEXT_MESSAGE;
    }

    @Override
    protected void writeData(DataOutput out) throws IOException {
        super.writeData(out);
        IO.writeString(out, text);
    }

    @Override
    protected void readData(DataInput in) throws IOException {
        super.readData(in);
        text = IO.readString(in);
    }
}
