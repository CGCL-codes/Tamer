package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class StartExpungeProcessPacket extends Packet {

    public class ExpungeDataBlock {

        public LLUUID AgentID = null;

        public int getLength() {
            return 16;
        }

        public ExpungeDataBlock() {
        }

        public ExpungeDataBlock(ByteBuffer bytes) {
            AgentID = new LLUUID(bytes);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            AgentID.GetBytes(bytes);
        }

        public String toString() {
            String output = "-- ExpungeData --\n";
            try {
                output += "AgentID: " + Helpers.toString(AgentID) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public ExpungeDataBlock createExpungeDataBlock() {
        return new ExpungeDataBlock();
    }

    private Header header;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header value) {
        header = value;
    }

    public int getType() {
        return PacketType.StartExpungeProcess;
    }

    public ExpungeDataBlock[] ExpungeData;

    public StartExpungeProcessPacket() {
        header = new LowHeader();
        header.setID((short) 477);
        header.setReliable(true);
        header.setZerocoded(true);
        ExpungeData = new ExpungeDataBlock[0];
    }

    public StartExpungeProcessPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        int count = (int) bytes.get() & 0xFF;
        ExpungeData = new ExpungeDataBlock[count];
        for (int j = 0; j < count; j++) {
            ExpungeData[j] = new ExpungeDataBlock(bytes);
        }
    }

    public StartExpungeProcessPacket(Header head, ByteBuffer bytes) {
        header = head;
        int count = (int) bytes.get() & 0xFF;
        ExpungeData = new ExpungeDataBlock[count];
        for (int j = 0; j < count; j++) {
            ExpungeData[j] = new ExpungeDataBlock(bytes);
        }
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        ;
        length++;
        for (int j = 0; j < ExpungeData.length; j++) {
            length += ExpungeData[j].getLength();
        }
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        bytes.put((byte) ExpungeData.length);
        for (int j = 0; j < ExpungeData.length; j++) {
            ExpungeData[j].ToBytes(bytes);
        }
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- StartExpungeProcess ---\n";
        for (int j = 0; j < ExpungeData.length; j++) {
            output += ExpungeData[j].toString() + "\n";
        }
        return output;
    }
}
