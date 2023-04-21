package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PlacesQueryPacket extends Packet {

    public class QueryDataBlock {

        private byte[] _simname;

        public byte[] getSimName() {
            return _simname;
        }

        public void setSimName(byte[] value) throws Exception {
            if (value == null) {
                _simname = null;
            }
            if (value.length > 255) {
                throw new OverflowException("Value exceeds 255 characters");
            } else {
                _simname = new byte[value.length];
                Array.Copy(value, _simname, value.length);
            }
        }

        public byte Category = 0;

        public int QueryFlags = 0;

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

        public int getLength() {
            int length = 5;
            if (getSimName() != null) {
                length += 1 + getSimName().length;
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
            length = (int) (bytes.get()) & 0xFF;
            _simname = new byte[length];
            bytes.get(_simname);
            Category = bytes.get();
            QueryFlags = bytes.getInt();
            length = (int) (bytes.get()) & 0xFF;
            _querytext = new byte[length];
            bytes.get(_querytext);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            bytes.put((byte) _simname.length);
            bytes.put(_simname);
            bytes.put(Category);
            bytes.putInt(QueryFlags);
            bytes.put((byte) _querytext.length);
            bytes.put(_querytext);
        }

        public String toString() {
            String output = "-- QueryData --\n";
            try {
                output += Helpers.FieldToString(_simname, "SimName") + "\n";
                output += "Category: " + Helpers.toString(Category) + "\n";
                output += "QueryFlags: " + Helpers.toString(QueryFlags) + "\n";
                output += Helpers.FieldToString(_querytext, "QueryText") + "\n";
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

        public LLUUID QueryID = null;

        public int getLength() {
            return 48;
        }

        public AgentDataBlock() {
        }

        public AgentDataBlock(ByteBuffer bytes) {
            AgentID = new LLUUID(bytes);
            SessionID = new LLUUID(bytes);
            QueryID = new LLUUID(bytes);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            AgentID.GetBytes(bytes);
            SessionID.GetBytes(bytes);
            QueryID.GetBytes(bytes);
        }

        public String toString() {
            String output = "-- AgentData --\n";
            try {
                output += "AgentID: " + Helpers.toString(AgentID) + "\n";
                output += "SessionID: " + Helpers.toString(SessionID) + "\n";
                output += "QueryID: " + Helpers.toString(QueryID) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public AgentDataBlock createAgentDataBlock() {
        return new AgentDataBlock();
    }

    public class TransactionDataBlock {

        public LLUUID TransactionID = null;

        public int getLength() {
            return 16;
        }

        public TransactionDataBlock() {
        }

        public TransactionDataBlock(ByteBuffer bytes) {
            TransactionID = new LLUUID(bytes);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            TransactionID.GetBytes(bytes);
        }

        public String toString() {
            String output = "-- TransactionData --\n";
            try {
                output += "TransactionID: " + Helpers.toString(TransactionID) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public TransactionDataBlock createTransactionDataBlock() {
        return new TransactionDataBlock();
    }

    private Header header;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header value) {
        header = value;
    }

    public int getType() {
        return PacketType.PlacesQuery;
    }

    public QueryDataBlock QueryData;

    public AgentDataBlock AgentData;

    public TransactionDataBlock TransactionData;

    public PlacesQueryPacket() {
        header = new LowHeader();
        header.setID((short) 41);
        header.setReliable(true);
        header.setZerocoded(true);
        QueryData = new QueryDataBlock();
        AgentData = new AgentDataBlock();
        TransactionData = new TransactionDataBlock();
    }

    public PlacesQueryPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        QueryData = new QueryDataBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
        TransactionData = new TransactionDataBlock(bytes);
    }

    public PlacesQueryPacket(Header head, ByteBuffer bytes) {
        header = head;
        QueryData = new QueryDataBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
        TransactionData = new TransactionDataBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += QueryData.getLength();
        length += AgentData.getLength();
        length += TransactionData.getLength();
        ;
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        QueryData.ToBytes(bytes);
        AgentData.ToBytes(bytes);
        TransactionData.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- PlacesQuery ---\n";
        output += QueryData.toString() + "\n";
        output += AgentData.toString() + "\n";
        output += TransactionData.toString() + "\n";
        return output;
    }
}
