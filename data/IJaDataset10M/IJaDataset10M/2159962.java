package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PreloadSoundPacket extends Packet {

    public class DataBlockBlock {

        public LLUUID ObjectID = null;

        public LLUUID SoundID = null;

        public LLUUID OwnerID = null;

        public int getLength() {
            return 48;
        }

        public DataBlockBlock() {
        }

        public DataBlockBlock(ByteBuffer bytes) {
            ObjectID = new LLUUID(bytes);
            SoundID = new LLUUID(bytes);
            OwnerID = new LLUUID(bytes);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            ObjectID.GetBytes(bytes);
            SoundID.GetBytes(bytes);
            OwnerID.GetBytes(bytes);
        }

        public String toString() {
            String output = "-- DataBlock --\n";
            try {
                output += "ObjectID: " + Helpers.toString(ObjectID) + "\n";
                output += "SoundID: " + Helpers.toString(SoundID) + "\n";
                output += "OwnerID: " + Helpers.toString(OwnerID) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public DataBlockBlock createDataBlockBlock() {
        return new DataBlockBlock();
    }

    private Header header;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header value) {
        header = value;
    }

    public int getType() {
        return PacketType.PreloadSound;
    }

    public DataBlockBlock[] DataBlock;

    public PreloadSoundPacket() {
        header = new LowHeader();
        header.setID((short) 18);
        header.setReliable(true);
        DataBlock = new DataBlockBlock[0];
    }

    public PreloadSoundPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        int count = (int) bytes.get() & 0xFF;
        DataBlock = new DataBlockBlock[count];
        for (int j = 0; j < count; j++) {
            DataBlock[j] = new DataBlockBlock(bytes);
        }
    }

    public PreloadSoundPacket(Header head, ByteBuffer bytes) {
        header = head;
        int count = (int) bytes.get() & 0xFF;
        DataBlock = new DataBlockBlock[count];
        for (int j = 0; j < count; j++) {
            DataBlock[j] = new DataBlockBlock(bytes);
        }
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        ;
        length++;
        for (int j = 0; j < DataBlock.length; j++) {
            length += DataBlock[j].getLength();
        }
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        bytes.put((byte) DataBlock.length);
        for (int j = 0; j < DataBlock.length; j++) {
            DataBlock[j].ToBytes(bytes);
        }
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- PreloadSound ---\n";
        for (int j = 0; j < DataBlock.length; j++) {
            output += DataBlock[j].toString() + "\n";
        }
        return output;
    }
}
