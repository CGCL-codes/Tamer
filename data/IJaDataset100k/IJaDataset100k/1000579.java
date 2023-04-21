package se.ytterman.jpcap.data.integer;

import java.lang.*;
import java.io.*;
import java.util.regex.*;
import se.ytterman.jpcap.data.BinaryData;

public class ShortBinaryData extends BinaryData {

    public ShortBinaryData(int short_payload) {
        this.short_payload = (short_payload & 0xFFFF);
    }

    public ShortBinaryData(String shortValue) {
        String short_pattern = "(0[X|x][0-9|A-F]{1,4})";
        Pattern p = Pattern.compile(short_pattern);
        Matcher m = p.matcher(shortValue);
        if (m.matches()) {
            StringBuffer valueBuffer = new StringBuffer(shortValue);
            String shortDigits = valueBuffer.substring(2);
            this.short_payload = Integer.parseInt(shortDigits, 16);
        } else {
        }
    }

    public ShortBinaryData(InputStream frameInputStream) throws Exception {
        this.read(frameInputStream);
    }

    public int getShortInteger() {
        return this.short_payload;
    }

    public void read(InputStream frameInputStream) throws Exception {
        DataInputStream frameDataStream = new DataInputStream(frameInputStream);
        this.short_payload = frameDataStream.readUnsignedShort();
    }

    public void write(OutputStream frameOutputStream) throws Exception {
        DataOutputStream frameDataStream = new DataOutputStream(frameOutputStream);
        frameDataStream.writeShort(this.short_payload);
    }

    public String toString() {
        String short_string = String.format("0X%04X", (this.short_payload & 0xFFFF));
        return short_string;
    }

    private int short_payload;
}
