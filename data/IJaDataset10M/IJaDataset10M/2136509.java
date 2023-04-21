package libsecondlife;

import java.nio.ByteBuffer;

public class LowHeader extends Header {

    public int getID() {
        byte byte_6 = Data[6];
        byte byte_7 = Data[7];
        int id = (((int) byte_6 & 0xFF) * 256) + ((int) byte_7 & 0xFF);
        return id;
    }

    public void setID(int value) {
        Data[6] = (byte) (value >> 8);
        Data[7] = (byte) (value % 256);
    }

    public int getFrequency() {
        return PacketFrequency.Low;
    }

    public LowHeader() {
        Data = new byte[8];
        Data[4] = Data[5] = (byte) 0xFF;
        AckList = new int[0];
    }

    public LowHeader(ByteBuffer bytes, int[] a_packetEnd) throws Exception {
        if (bytes.limit() < 8) {
            throw new Exception("Not enough bytes for LowHeader");
        }
        Data = new byte[8];
        bytes.get(Data);
        if ((bytes.get(0) & Helpers.MSG_ZEROCODED) != 0 && bytes.get(6) == 0) {
            if (bytes.get(7) == 1) Data[7] = bytes.get(8); else throw new Exception();
        }
        CreateAckList(bytes, a_packetEnd);
    }

    public void ToBytes(ByteBuffer bytes) {
        bytes.put(Data);
    }
}
