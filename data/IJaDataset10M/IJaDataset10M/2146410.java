package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DirPeopleQueryPacket extends Packet {

    public class QueryDataBlock {

        public int SkillFlags = 0;

        private byte[] _name;

        public byte[] getName() {
            return _name;
        }

        public void setName(byte[] value) throws Exception {
            if (value == null) {
                _name = null;
            }
            if (value.length > 255) {
                throw new OverflowException("Value exceeds 255 characters");
            } else {
                _name = new byte[value.length];
                Array.Copy(value, _name, value.length);
            }
        }

        public LLUUID QueryID = null;

        public byte Online = 0;

        private byte[] _reputation;

        public byte[] getReputation() {
            return _reputation;
        }

        public void setReputation(byte[] value) throws Exception {
            if (value == null) {
                _reputation = null;
            }
            if (value.length > 255) {
                throw new OverflowException("Value exceeds 255 characters");
            } else {
                _reputation = new byte[value.length];
                Array.Copy(value, _reputation, value.length);
            }
        }

        private byte[] _distance;

        public byte[] getDistance() {
            return _distance;
        }

        public void setDistance(byte[] value) throws Exception {
            if (value == null) {
                _distance = null;
            }
            if (value.length > 255) {
                throw new OverflowException("Value exceeds 255 characters");
            } else {
                _distance = new byte[value.length];
                Array.Copy(value, _distance, value.length);
            }
        }

        private byte[] _group;

        public byte[] getGroup() {
            return _group;
        }

        public void setGroup(byte[] value) throws Exception {
            if (value == null) {
                _group = null;
            }
            if (value.length > 255) {
                throw new OverflowException("Value exceeds 255 characters");
            } else {
                _group = new byte[value.length];
                Array.Copy(value, _group, value.length);
            }
        }

        private byte[] _querytext;

        public byte[] getQueryText() {
            return _querytext;
        }

        public void setQueryText(byte[] value) throws Exception {
            if (value == null) {
                _querytext = null;
            }
            if (value.length > 255) {
                throw new OverflowException("Value exceeds 255 characters");
            } else {
                _querytext = new byte[value.length];
                Array.Copy(value, _querytext, value.length);
            }
        }

        public int WantToFlags = 0;

        public int getLength() {
            int length = 25;
            if (getName() != null) {
                length += 1 + getName().length;
            }
            if (getReputation() != null) {
                length += 1 + getReputation().length;
            }
            if (getDistance() != null) {
                length += 1 + getDistance().length;
            }
            if (getGroup() != null) {
                length += 1 + getGroup().length;
            }
            if (getQueryText() != null) {
                length += 1 + getQueryText().length;
            }
            return length;
        }

        public QueryDataBlock() {
        }

        public QueryDataBlock(ByteBuffer bytes) {
            int length;
            SkillFlags = bytes.getInt();
            length = (int) (bytes.get()) & 0xFF;
            _name = new byte[length];
            bytes.get(_name);
            QueryID = new LLUUID(bytes);
            Online = bytes.get();
            length = (int) (bytes.get()) & 0xFF;
            _reputation = new byte[length];
            bytes.get(_reputation);
            length = (int) (bytes.get()) & 0xFF;
            _distance = new byte[length];
            bytes.get(_distance);
            length = (int) (bytes.get()) & 0xFF;
            _group = new byte[length];
            bytes.get(_group);
            length = (int) (bytes.get()) & 0xFF;
            _querytext = new byte[length];
            bytes.get(_querytext);
            WantToFlags = bytes.getInt();
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            bytes.putInt(SkillFlags);
            bytes.put((byte) _name.length);
            bytes.put(_name);
            QueryID.GetBytes(bytes);
            bytes.put(Online);
            bytes.put((byte) _reputation.length);
            bytes.put(_reputation);
            bytes.put((byte) _distance.length);
            bytes.put(_distance);
            bytes.put((byte) _group.length);
            bytes.put(_group);
            bytes.put((byte) _querytext.length);
            bytes.put(_querytext);
            bytes.putInt(WantToFlags);
        }

        public String toString() {
            String output = "-- QueryData --\n";
            try {
                output += "SkillFlags: " + Helpers.toString(SkillFlags) + "\n";
                output += Helpers.FieldToString(_name, "Name") + "\n";
                output += "QueryID: " + Helpers.toString(QueryID) + "\n";
                output += "Online: " + Helpers.toString(Online) + "\n";
                output += Helpers.FieldToString(_reputation, "Reputation") + "\n";
                output += Helpers.FieldToString(_distance, "Distance") + "\n";
                output += Helpers.FieldToString(_group, "Group") + "\n";
                output += Helpers.FieldToString(_querytext, "QueryText") + "\n";
                output += "WantToFlags: " + Helpers.toString(WantToFlags) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public QueryDataBlock createQueryDataBlock() {
        return new QueryDataBlock();
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
        return PacketType.DirPeopleQuery;
    }

    public QueryDataBlock QueryData;

    public AgentDataBlock AgentData;

    public DirPeopleQueryPacket() {
        header = new LowHeader();
        header.setID((short) 48);
        header.setReliable(true);
        header.setZerocoded(true);
        QueryData = new QueryDataBlock();
        AgentData = new AgentDataBlock();
    }

    public DirPeopleQueryPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        QueryData = new QueryDataBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
    }

    public DirPeopleQueryPacket(Header head, ByteBuffer bytes) {
        header = head;
        QueryData = new QueryDataBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += QueryData.getLength();
        length += AgentData.getLength();
        ;
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        QueryData.ToBytes(bytes);
        AgentData.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- DirPeopleQuery ---\n";
        output += QueryData.toString() + "\n";
        output += AgentData.toString() + "\n";
        return output;
    }
}
