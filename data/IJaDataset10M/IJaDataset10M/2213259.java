package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GrantedProxiesPacket extends Packet {

    public class EmpoweredBlockBlock {

        public LLUUID EmpoweredID = null;

        public int getLength() {
            return 16;
        }

        public EmpoweredBlockBlock() {
        }

        public EmpoweredBlockBlock(ByteBuffer bytes) {
            EmpoweredID = new LLUUID(bytes);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            EmpoweredID.GetBytes(bytes);
        }

        public String toString() {
            String output = "-- EmpoweredBlock --\n";
            try {
                output += "EmpoweredID: " + Helpers.toString(EmpoweredID) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public EmpoweredBlockBlock createEmpoweredBlockBlock() {
        return new EmpoweredBlockBlock();
    }

    public class GranterBlockBlock {

        public LLUUID GranterID = null;

        public int getLength() {
            return 16;
        }

        public GranterBlockBlock() {
        }

        public GranterBlockBlock(ByteBuffer bytes) {
            GranterID = new LLUUID(bytes);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            GranterID.GetBytes(bytes);
        }

        public String toString() {
            String output = "-- GranterBlock --\n";
            try {
                output += "GranterID: " + Helpers.toString(GranterID) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public GranterBlockBlock createGranterBlockBlock() {
        return new GranterBlockBlock();
    }

    public class AgentDataBlock {

        public LLUUID AgentID = null;

        public int getLength() {
            return 16;
        }

        public AgentDataBlock() {
        }

        public AgentDataBlock(ByteBuffer bytes) {
            AgentID = new LLUUID(bytes);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            AgentID.GetBytes(bytes);
        }

        public String toString() {
            String output = "-- AgentData --\n";
            try {
                output += "AgentID: " + Helpers.toString(AgentID) + "\n";
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
        return PacketType.GrantedProxies;
    }

    public EmpoweredBlockBlock[] EmpoweredBlock;

    public GranterBlockBlock[] GranterBlock;

    public AgentDataBlock AgentData;

    public GrantedProxiesPacket() {
        header = new LowHeader();
        header.setID((short) 169);
        header.setReliable(true);
        EmpoweredBlock = new EmpoweredBlockBlock[0];
        GranterBlock = new GranterBlockBlock[0];
        AgentData = new AgentDataBlock();
    }

    public GrantedProxiesPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        int count = (int) bytes.get() & 0xFF;
        EmpoweredBlock = new EmpoweredBlockBlock[count];
        for (int j = 0; j < count; j++) {
            EmpoweredBlock[j] = new EmpoweredBlockBlock(bytes);
        }
        count = (int) bytes.get() & 0xFF;
        GranterBlock = new GranterBlockBlock[count];
        for (int j = 0; j < count; j++) {
            GranterBlock[j] = new GranterBlockBlock(bytes);
        }
        AgentData = new AgentDataBlock(bytes);
    }

    public GrantedProxiesPacket(Header head, ByteBuffer bytes) {
        header = head;
        int count = (int) bytes.get() & 0xFF;
        EmpoweredBlock = new EmpoweredBlockBlock[count];
        for (int j = 0; j < count; j++) {
            EmpoweredBlock[j] = new EmpoweredBlockBlock(bytes);
        }
        count = (int) bytes.get() & 0xFF;
        GranterBlock = new GranterBlockBlock[count];
        for (int j = 0; j < count; j++) {
            GranterBlock[j] = new GranterBlockBlock(bytes);
        }
        AgentData = new AgentDataBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += AgentData.getLength();
        ;
        length++;
        for (int j = 0; j < EmpoweredBlock.length; j++) {
            length += EmpoweredBlock[j].getLength();
        }
        length++;
        for (int j = 0; j < GranterBlock.length; j++) {
            length += GranterBlock[j].getLength();
        }
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        bytes.put((byte) EmpoweredBlock.length);
        for (int j = 0; j < EmpoweredBlock.length; j++) {
            EmpoweredBlock[j].ToBytes(bytes);
        }
        bytes.put((byte) GranterBlock.length);
        for (int j = 0; j < GranterBlock.length; j++) {
            GranterBlock[j].ToBytes(bytes);
        }
        AgentData.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- GrantedProxies ---\n";
        for (int j = 0; j < EmpoweredBlock.length; j++) {
            output += EmpoweredBlock[j].toString() + "\n";
        }
        for (int j = 0; j < GranterBlock.length; j++) {
            output += GranterBlock[j].toString() + "\n";
        }
        output += AgentData.toString() + "\n";
        return output;
    }
}
