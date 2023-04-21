package com.hyk.rpc.core.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import com.hyk.io.buffer.ChannelDataBuffer;
import com.hyk.rpc.core.address.Address;
import com.hyk.serializer.Externalizable;
import com.hyk.serializer.SerializerInput;
import com.hyk.serializer.SerializerOutput;

/**
 * @author Administrator
 *
 */
public class MessageFragment implements Externalizable {

    public Address getAddress() {
        return id.address;
    }

    public void setAddress(Address address) {
        id.address = address;
    }

    public long getSessionID() {
        return id.sessionID;
    }

    public void setSessionID(long sessionID) {
        id.sessionID = sessionID;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getTotalFragmentCount() {
        return totalFragmentCount;
    }

    public void setTotalFragmentCount(int totalFragmentCount) {
        this.totalFragmentCount = totalFragmentCount;
    }

    public ChannelDataBuffer getContent() {
        return content;
    }

    public void setContent(ChannelDataBuffer content) {
        this.content = content;
    }

    MessageID id = new MessageID();

    @Override
    public String toString() {
        return "MessageFragment [id=" + id + ", sequence=" + sequence + ", totalFragmentCount=" + totalFragmentCount + "]";
    }

    public MessageID getId() {
        return id;
    }

    int sequence;

    int totalFragmentCount;

    ChannelDataBuffer content;

    @Override
    public void readExternal(SerializerInput in) throws IOException {
        id = in.readObject(MessageID.class);
        sequence = in.readInt();
        totalFragmentCount = in.readInt();
        byte[] raw = in.readBytes();
        if (null != raw) {
            content = ChannelDataBuffer.wrap(raw);
        }
    }

    @Override
    public void writeExternal(SerializerOutput out) throws IOException {
        out.writeObject(id);
        out.writeInt(sequence);
        out.writeInt(totalFragmentCount);
        if (null != content) {
            ByteBuffer[] bufs = ChannelDataBuffer.asByteBuffers(content);
            out.writeInt(content.capacity());
            for (ByteBuffer buf : bufs) {
                out.writeRawBytes(buf.array(), buf.position(), buf.remaining());
            }
        }
    }
}
