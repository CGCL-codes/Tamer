package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RequestRegionInfoPacket extends Packet {

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
        return PacketType.RequestRegionInfo;
    }

    public AgentDataBlock AgentData;

    public RequestRegionInfoPacket() {
        header = new LowHeader();
        header.setID((short) 182);
        header.setReliable(true);
        AgentData = new AgentDataBlock();
    }

    public RequestRegionInfoPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        AgentData = new AgentDataBlock(bytes);
    }

    public RequestRegionInfoPacket(Header head, ByteBuffer bytes) {
        header = head;
        AgentData = new AgentDataBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += AgentData.getLength();
        ;
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        AgentData.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- RequestRegionInfo ---\n";
        output += AgentData.toString() + "\n";
        return output;
    }
}
