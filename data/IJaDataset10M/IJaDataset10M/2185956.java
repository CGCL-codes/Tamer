package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TransferAbortPacket extends Packet {

    public class TransferInfoBlock {

        public LLUUID TransferID = null;

        public int ChannelType = 0;

        public int getLength() {
            return 20;
        }

        public TransferInfoBlock() {
        }

        public TransferInfoBlock(ByteBuffer bytes) {
            TransferID = new LLUUID(bytes);
            ChannelType = bytes.getInt();
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            TransferID.GetBytes(bytes);
            bytes.putInt(ChannelType);
        }

        public String toString() {
            String output = "-- TransferInfo --\n";
            try {
                output += "TransferID: " + Helpers.toString(TransferID) + "\n";
                output += "ChannelType: " + Helpers.toString(ChannelType) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public TransferInfoBlock createTransferInfoBlock() {
        return new TransferInfoBlock();
    }

    private Header header;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header value) {
        header = value;
    }

    public int getType() {
        return PacketType.TransferAbort;
    }

    public TransferInfoBlock TransferInfo;

    public TransferAbortPacket() {
        header = new LowHeader();
        header.setID((short) 196);
        header.setReliable(true);
        header.setZerocoded(true);
        TransferInfo = new TransferInfoBlock();
    }

    public TransferAbortPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        TransferInfo = new TransferInfoBlock(bytes);
    }

    public TransferAbortPacket(Header head, ByteBuffer bytes) {
        header = head;
        TransferInfo = new TransferInfoBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += TransferInfo.getLength();
        ;
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        TransferInfo.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- TransferAbort ---\n";
        output += TransferInfo.toString() + "\n";
        return output;
    }
}
