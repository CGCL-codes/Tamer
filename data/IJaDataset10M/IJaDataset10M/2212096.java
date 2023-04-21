package org.peercast.core.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * MSID. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 050811 nsano initial version <br>
 */
class MSID {

    /** */
    MSID() {
    }

    /** */
    public MSID(int data1, int data2, int data3, int d41, int d42, int d43, int d44, int d45, int d46, int d47, int d48) {
        this.data1 = data1;
        this.data2 = (short) data2;
        this.data3 = (short) data3;
        this.data4 = new byte[] { (byte) d41, (byte) d42, (byte) d43, (byte) d44, (byte) d45, (byte) d46, (byte) d47, (byte) d48 };
    }

    /** */
    public void read(DataInputStream in) throws IOException {
        data1 = in.readInt();
        data2 = in.readShort();
        data3 = in.readShort();
        in.read(data4, 0, 8);
    }

    /** */
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(data1);
        out.writeShort(data2);
        out.writeShort(data3);
        out.write(data4, 0, 8);
    }

    /** */
    public String toString() {
        return String.format("%X-%X-%X-%02X%02X%02X%02X%02X%02X%02X%02X", data1, data2, data3, data4[0], data4[1], data4[2], data4[3], data4[4], data4[5], data4[6], data4[7]);
    }

    /** */
    public boolean equals(final MSID msid) {
        return data1 == msid.data1 && data2 == msid.data2 && data3 == msid.data3 && Arrays.equals(data4, msid.data4);
    }

    /** */
    int data1;

    /** */
    short data2, data3;

    /** */
    byte[] data4 = new byte[8];

    /** */
    static final MSID headObjID = new MSID(0x75B22630, 0x668E, 0x11CF, 0xA6, 0xD9, 0x00, 0xAA, 0x00, 0x62, 0xCE, 0x6C);

    /** */
    static final MSID dataObjID = new MSID(0x75B22636, 0x668E, 0x11CF, 0xA6, 0xD9, 0x00, 0xAA, 0x00, 0x62, 0xCE, 0x6C);

    /** */
    static final MSID filePropObjID = new MSID(0x8CABDCA1, 0xA947, 0x11CF, 0x8E, 0xE4, 0x00, 0xC0, 0x0C, 0x20, 0x53, 0x65);

    /** */
    static final MSID streamPropObjID = new MSID(0xB7DC0791, 0xA9B7, 0x11CF, 0x8E, 0xE6, 0x00, 0xC0, 0x0C, 0x20, 0x53, 0x65);

    /** */
    static final MSID audioStreamObjID = new MSID(0xF8699E40, 0x5B4D, 0x11CF, 0xA8, 0xFD, 0x00, 0x80, 0x5F, 0x5C, 0x44, 0x2B);

    /** */
    static final MSID videoStreamObjID = new MSID(0xBC19EFC0, 0x5B4D, 0x11CF, 0xA8, 0xFD, 0x00, 0x80, 0x5F, 0x5C, 0x44, 0x2B);

    /** */
    static final MSID streamBitrateObjID = new MSID(0x7BF875CE, 0x468D, 0x11D1, 0x8D, 0x82, 0x00, 0x60, 0x97, 0xC9, 0xA2, 0xB2);
}
