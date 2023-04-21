package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FreezeUserPacket extends Packet {

    public class DataBlock {

        public LLUUID TargetID = null;

        public int Flags = 0;

        public int getLength() {
            return 20;
        }

        public DataBlock() {
        }

        public DataBlock(ByteBuffer bytes) {
            TargetID = new LLUUID(bytes);
            Flags = bytes.getInt();
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            TargetID.GetBytes(bytes);
            bytes.putInt(Flags);
        }

        public String toString() {
            String output = "-- Data --\n";
            try {
                output += "TargetID: " + Helpers.toString(TargetID) + "\n";
                output += "Flags: " + Helpers.toString(Flags) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public DataBlock createDataBlock() {
        return new DataBlock();
    }

    public class AgentDataBlock {

        public LLUUID AgentID = null;

        public LLUUID SessionID = null;

        public int getLength() {
            return 32;
        }

        public AgentDataBlock() {
        }

        public AgentDataBlock(ByteBuffer bytes) {
            AgentID = new LLUUID(bytes);
            SessionID = new LLUUID(bytes);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            AgentID.GetBytes(bytes);
            SessionID.GetBytes(bytes);
        }

        public String toString() {
            String output = "-- AgentData --\n";
            try {
                output += "AgentID: " + Helpers.toString(AgentID) + "\n";
                output += "SessionID: " + Helpers.toString(SessionID) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public AgentDataBlock createAgentDataBlock() {
        return new AgentDataBlock();
    }

    private Header header;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header value) {
        header = value;
    }

    public int getType() {
        return PacketType.FreezeUser;
    }

    public DataBlock Data;

    public AgentDataBlock AgentData;

    public FreezeUserPacket() {
        header = new LowHeader();
        header.setID((short) 211);
        header.setReliable(true);
        Data = new DataBlock();
        AgentData = new AgentDataBlock();
    }

    public FreezeUserPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        Data = new DataBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
    }

    public FreezeUserPacket(Header head, ByteBuffer bytes) {
        header = head;
        Data = new DataBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += Data.getLength();
        length += AgentData.getLength();
        ;
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        Data.ToBytes(bytes);
        AgentData.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- FreezeUser ---\n";
        output += Data.toString() + "\n";
        output += AgentData.toString() + "\n";
        return output;
    }
}
