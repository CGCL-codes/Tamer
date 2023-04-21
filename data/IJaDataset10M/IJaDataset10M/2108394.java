package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class EmailMessageRequestPacket extends Packet {

    public class DataBlockBlock {

        public LLUUID ObjectID = null;

        private byte[] _subject;

        public byte[] getSubject() {
            return _subject;
        }

        public void setSubject(byte[] value) throws Exception {
            if (value == null) {
                _subject = null;
            }
            if (value.length > 255) {
                throw new OverflowException("Value exceeds 255 characters");
            } else {
                _subject = new byte[value.length];
                Array.Copy(value, _subject, value.length);
            }
        }

        private byte[] _fromaddress;

        public byte[] getFromAddress() {
            return _fromaddress;
        }

        public void setFromAddress(byte[] value) throws Exception {
            if (value == null) {
                _fromaddress = null;
            }
            if (value.length > 255) {
                throw new OverflowException("Value exceeds 255 characters");
            } else {
                _fromaddress = new byte[value.length];
                Array.Copy(value, _fromaddress, value.length);
            }
        }

        public int getLength() {
            int length = 16;
            if (getSubject() != null) {
                length += 1 + getSubject().length;
            }
            if (getFromAddress() != null) {
                length += 1 + getFromAddress().length;
            }
            return length;
        }

        public DataBlockBlock() {
        }

        public DataBlockBlock(ByteBuffer bytes) {
            int length;
            ObjectID = new LLUUID(bytes);
            length = (int) (bytes.get()) & 0xFF;
            _subject = new byte[length];
            bytes.get(_subject);
            length = (int) (bytes.get()) & 0xFF;
            _fromaddress = new byte[length];
            bytes.get(_fromaddress);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            ObjectID.GetBytes(bytes);
            bytes.put((byte) _subject.length);
            bytes.put(_subject);
            bytes.put((byte) _fromaddress.length);
            bytes.put(_fromaddress);
        }

        public String toString() {
            String output = "-- DataBlock --\n";
            try {
                output += "ObjectID: " + Helpers.toString(ObjectID) + "\n";
                output += Helpers.FieldToString(_subject, "Subject") + "\n";
                output += Helpers.FieldToString(_fromaddress, "FromAddress") + "\n";
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
        return PacketType.EmailMessageRequest;
    }

    public DataBlockBlock DataBlock;

    public EmailMessageRequestPacket() {
        header = new LowHeader();
        header.setID((short) 410);
        header.setReliable(true);
        DataBlock = new DataBlockBlock();
    }

    public EmailMessageRequestPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        DataBlock = new DataBlockBlock(bytes);
    }

    public EmailMessageRequestPacket(Header head, ByteBuffer bytes) {
        header = head;
        DataBlock = new DataBlockBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += DataBlock.getLength();
        ;
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        DataBlock.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- EmailMessageRequest ---\n";
        output += DataBlock.toString() + "\n";
        return output;
    }
}
