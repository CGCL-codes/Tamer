package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SetCPURatioPacket extends Packet {

    public class DataBlock {

        public byte Ratio = 0;

        public int getLength() {
            return 1;
        }

        public DataBlock() {
        }

        public DataBlock(ByteBuffer bytes) {
            Ratio = bytes.get();
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            bytes.put(Ratio);
        }

        public String toString() {
            String output = "-- Data --\n";
            try {
                output += "Ratio: " + Helpers.toString(Ratio) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public DataBlock createDataBlock() {
        return new DataBlock();
    }

    private Header header;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header value) {
        header = value;
    }

    public int getType() {
        return PacketType.SetCPURatio;
    }

    public DataBlock Data;

    public SetCPURatioPacket() {
        header = new LowHeader();
        header.setID((short) 396);
        header.setReliable(true);
        Data = new DataBlock();
    }

    public SetCPURatioPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        Data = new DataBlock(bytes);
    }

    public SetCPURatioPacket(Header head, ByteBuffer bytes) {
        header = head;
        Data = new DataBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += Data.getLength();
        ;
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        Data.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- SetCPURatio ---\n";
        output += Data.toString() + "\n";
        return output;
    }
}
