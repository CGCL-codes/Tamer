package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class UpdateGroupInfoPacket extends Packet {

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

    public class GroupDataBlock {

        public boolean AllowPublish = false;

        private byte[] _charter;

        public byte[] getCharter() {
            return _charter;
        }

        public void setCharter(byte[] value) throws Exception {
            if (value == null) {
                _charter = null;
            }
            if (value.length > 1024) {
                throw new OverflowException("Value exceeds 1024 characters");
            } else {
                _charter = new byte[value.length];
                Array.Copy(value, _charter, value.length);
            }
        }

        public boolean ShowInList = false;

        public LLUUID InsigniaID = null;

        public LLUUID GroupID = null;

        public int MembershipFee = 0;

        public boolean MaturePublish = false;

        public boolean OpenEnrollment = false;

        public int getLength() {
            int length = 40;
            if (getCharter() != null) {
                length += 2 + getCharter().length;
            }
            return length;
        }

        public GroupDataBlock() {
        }

        public GroupDataBlock(ByteBuffer bytes) {
            int length;
            AllowPublish = (bytes.get() != 0) ? (boolean) true : (boolean) false;
            length = (int) (bytes.get()) & 0xFFFF;
            _charter = new byte[length];
            bytes.get(_charter);
            ShowInList = (bytes.get() != 0) ? (boolean) true : (boolean) false;
            InsigniaID = new LLUUID(bytes);
            GroupID = new LLUUID(bytes);
            MembershipFee = bytes.getInt();
            MaturePublish = (bytes.get() != 0) ? (boolean) true : (boolean) false;
            OpenEnrollment = (bytes.get() != 0) ? (boolean) true : (boolean) false;
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            bytes.put((byte) ((AllowPublish) ? 1 : 0));
            bytes.putShort((short) _charter.length);
            bytes.put(_charter);
            bytes.put((byte) ((ShowInList) ? 1 : 0));
            InsigniaID.GetBytes(bytes);
            GroupID.GetBytes(bytes);
            bytes.putInt(MembershipFee);
            bytes.put((byte) ((MaturePublish) ? 1 : 0));
            bytes.put((byte) ((OpenEnrollment) ? 1 : 0));
        }

        public String toString() {
            String output = "-- GroupData --\n";
            try {
                output += "AllowPublish: " + Helpers.toString(AllowPublish) + "\n";
                output += Helpers.FieldToString(_charter, "Charter") + "\n";
                output += "ShowInList: " + Helpers.toString(ShowInList) + "\n";
                output += "InsigniaID: " + Helpers.toString(InsigniaID) + "\n";
                output += "GroupID: " + Helpers.toString(GroupID) + "\n";
                output += "MembershipFee: " + Helpers.toString(MembershipFee) + "\n";
                output += "MaturePublish: " + Helpers.toString(MaturePublish) + "\n";
                output += "OpenEnrollment: " + Helpers.toString(OpenEnrollment) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public GroupDataBlock createGroupDataBlock() {
        return new GroupDataBlock();
    }

    private Header header;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header value) {
        header = value;
    }

    public int getType() {
        return PacketType.UpdateGroupInfo;
    }

    public AgentDataBlock AgentData;

    public GroupDataBlock GroupData;

    public UpdateGroupInfoPacket() {
        header = new LowHeader();
        header.setID((short) 416);
        header.setReliable(true);
        header.setZerocoded(true);
        AgentData = new AgentDataBlock();
        GroupData = new GroupDataBlock();
    }

    public UpdateGroupInfoPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        AgentData = new AgentDataBlock(bytes);
        GroupData = new GroupDataBlock(bytes);
    }

    public UpdateGroupInfoPacket(Header head, ByteBuffer bytes) {
        header = head;
        AgentData = new AgentDataBlock(bytes);
        GroupData = new GroupDataBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += AgentData.getLength();
        length += GroupData.getLength();
        ;
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        AgentData.ToBytes(bytes);
        GroupData.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- UpdateGroupInfo ---\n";
        output += AgentData.toString() + "\n";
        output += GroupData.toString() + "\n";
        return output;
    }
}
