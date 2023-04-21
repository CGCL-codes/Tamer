package org.jboss.netty.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Almost same as {@link CompositeChannelBuffer} except that empty buffers will not be copied,
 * it checks also for buffers that are already AggregateChannelBuffer.
 *
 * The idea is to limit the number of stored empty ChannelBuffer when constructor is called.
 * Except that, everything is similar to CompositeChannelBuffer.
 *
 * @author frederic bregier
 *
 */
public class AggregateChannelBuffer extends AbstractChannelBuffer {

    private ChannelBuffer[] slices;

    private ByteOrder order;

    private int[] indices;

    private int lastSliceId;

    /**
     * Creates a new aggregate buffer which wraps the specified buffers without
     * copying them.  A modification on the specified buffers' content will be
     * visible to the returned buffer.
     *
     * @throws IllegalArgumentException
     *         if the specified buffers' endianness are different from each
     *         other
     */
    public static ChannelBuffer wrappedCheckedBuffer(ChannelBuffer... buffers) {
        switch(buffers.length) {
            case 0:
                break;
            case 1:
                if (buffers[0].readable()) {
                    return wrappedCheckedBuffer(buffers[0]);
                }
                break;
            default:
                for (ChannelBuffer b : buffers) {
                    if (b.readable()) {
                        return new AggregateChannelBuffer(buffers);
                    }
                }
        }
        return ChannelBuffers.EMPTY_BUFFER;
    }

    /**
     * Creates a new buffer which wraps the specified buffer's readable bytes.
     * A modification on the specified buffer's content will be visible to the
     * returned buffer.
     */
    public static ChannelBuffer wrappedCheckedBuffer(ChannelBuffer buffer) {
        if (buffer.readable()) {
            return buffer.slice();
        } else {
            return ChannelBuffers.EMPTY_BUFFER;
        }
    }

    /**
     * Creates a new aggregate buffer which wraps the specified buffers without
     * copying them.  A modification on the specified buffers' content will be
     * visible to the returned buffer.
     *
     * @param buffers
     */
    public AggregateChannelBuffer(ChannelBuffer... buffers) {
        if (buffers.length == 0) {
            throw new IllegalArgumentException("buffers should not be empty.");
        }
        ByteOrder expectedEndianness = null;
        ArrayList<ChannelBuffer> bufferList = new ArrayList<ChannelBuffer>(buffers.length);
        for (ChannelBuffer buffer : buffers) {
            if (buffer.readableBytes() > 0) {
                expectedEndianness = buffer.order();
                if (buffer instanceof AggregateChannelBuffer) {
                    AggregateChannelBuffer aggregate = (AggregateChannelBuffer) buffer;
                    ArrayList<ChannelBuffer> subList = aggregate.getBufferList(aggregate.readerIndex(), aggregate.readableBytes());
                    bufferList.addAll(subList);
                } else {
                    ChannelBuffer other = buffer.slice(buffer.readerIndex(), buffer.readableBytes());
                    bufferList.add(other);
                }
            } else if (buffer.capacity() != 0) {
                expectedEndianness = buffer.order();
            }
        }
        if (expectedEndianness == null) {
            throw new IllegalArgumentException("buffers have only empty buffers.");
        }
        setFromList(bufferList, expectedEndianness);
    }

    /**
     *
     * @param index
     * @param length
     * @return the list of valid buffers from index and length, slicing contents
     */
    private ArrayList<ChannelBuffer> getBufferList(int index, int length) {
        int localReaderIndex = index;
        int localWriterIndex = this.writerIndex();
        int sliceId = sliceId(localReaderIndex);
        int maxlength = localWriterIndex - localReaderIndex;
        if (maxlength < length) {
            maxlength = capacity() - localReaderIndex;
            if (maxlength < length) {
                throw new IllegalArgumentException("Length is bigger than available.");
            }
        }
        ArrayList<ChannelBuffer> bufferList = new ArrayList<ChannelBuffer>(slices.length);
        ChannelBuffer buf = slices[sliceId].duplicate();
        buf.readerIndex(localReaderIndex - indices[sliceId]);
        buf.writerIndex(slices[sliceId].writerIndex());
        while (length > 0) {
            int leftInBuffer = buf.capacity() - buf.readerIndex();
            if (length <= leftInBuffer) {
                buf.writerIndex(buf.readerIndex() + length);
                bufferList.add(buf);
                length = 0;
                break;
            } else {
                bufferList.add(buf);
                length -= leftInBuffer;
                sliceId++;
                buf = slices[sliceId].duplicate();
                buf.readerIndex(0);
                buf.writerIndex(slices[sliceId].writerIndex());
            }
        }
        return bufferList;
    }

    /**
     * Setup this ChannelBuffer from the list and the Endianness
     * @param listBuf
     * @param expectedEndianness
     */
    private void setFromList(ArrayList<ChannelBuffer> listBuf, ByteOrder expectedEndianness) {
        int number = listBuf.size();
        if (number == 0) {
            order = expectedEndianness;
            slices = new ChannelBuffer[1];
            slices[0] = ChannelBuffers.EMPTY_BUFFER.slice();
            indices = new int[2];
            indices[1] = indices[0] + slices[0].capacity();
            readerIndex(0);
            writerIndex(capacity());
            return;
        }
        order = expectedEndianness;
        slices = new ChannelBuffer[number];
        int i = 0;
        for (ChannelBuffer buffer : listBuf) {
            if (buffer.order() != expectedEndianness) {
                throw new IllegalArgumentException("All buffers must have the same endianness.");
            }
            slices[i] = buffer;
            i++;
        }
        indices = new int[number + 1];
        indices[0] = 0;
        for (i = 1; i <= number; i++) {
            indices[i] = indices[i - 1] + slices[i - 1].capacity();
        }
        writerIndex(capacity());
        readerIndex(listBuf.get(0).readerIndex());
    }

    /**
     * Use by duplicate method
     * @param buffer the buffer to duplicate
     */
    private AggregateChannelBuffer(AggregateChannelBuffer buffer) {
        order = buffer.order;
        slices = buffer.slices.clone();
        indices = buffer.indices.clone();
        setIndex(buffer.readerIndex(), buffer.writerIndex());
    }

    @Override
    public void discardReadBytes() {
        int localReaderIndex = this.readerIndex();
        if (localReaderIndex == 0) {
            return;
        }
        int localWriterIndex = this.writerIndex();
        int localLength = capacity() - localReaderIndex;
        ArrayList<ChannelBuffer> list = getBufferList(localReaderIndex, localLength);
        ChannelBuffer first = list.get(0);
        int firstCapacity = first.writerIndex() - first.readerIndex();
        ChannelBuffer firstbuf = ChannelBuffers.buffer(order, firstCapacity);
        firstbuf.writeBytes(first);
        list.set(0, firstbuf);
        ChannelBuffer buffer = ChannelBuffers.buffer(localReaderIndex);
        list.add(buffer);
        int localMarkedReaderIndex = localReaderIndex;
        try {
            resetReaderIndex();
            localMarkedReaderIndex = this.readerIndex();
        } catch (IndexOutOfBoundsException e) {
        }
        int localMarkedWriterIndex = localWriterIndex;
        try {
            resetWriterIndex();
            localMarkedWriterIndex = this.writerIndex();
        } catch (IndexOutOfBoundsException e) {
        }
        setFromList(list, order);
        localMarkedReaderIndex = Math.max(localMarkedReaderIndex - localReaderIndex, 0);
        localMarkedWriterIndex = Math.max(localMarkedWriterIndex - localReaderIndex, 0);
        this.readerIndex(localMarkedReaderIndex);
        this.writerIndex(localMarkedWriterIndex);
        markReaderIndex();
        markWriterIndex();
        localWriterIndex = Math.max(localWriterIndex - localReaderIndex, 0);
        localReaderIndex = 0;
        this.readerIndex(localReaderIndex);
        this.writerIndex(localWriterIndex);
    }

    public ChannelBuffer slice(int index, int length) {
        if (index == 0) {
            if (length == 0) {
                return ChannelBuffers.EMPTY_BUFFER;
            } else {
                ArrayList<ChannelBuffer> listBuffer = this.getBufferList(0, length);
                ChannelBuffer[] buffers = new ChannelBuffer[listBuffer.size()];
                listBuffer.toArray(buffers);
                AggregateChannelBuffer newbuf = new AggregateChannelBuffer(buffers);
                newbuf.readerIndex(0);
                newbuf.writerIndex(length);
                return newbuf;
            }
        } else if (index < 0 || index > capacity() - length) {
            throw new IndexOutOfBoundsException();
        } else if (length == 0) {
            return ChannelBuffers.EMPTY_BUFFER;
        } else {
            ArrayList<ChannelBuffer> listBuffer = this.getBufferList(index, length);
            ChannelBuffer[] buffers = new ChannelBuffer[listBuffer.size()];
            listBuffer.toArray(buffers);
            AggregateChannelBuffer newbuf = new AggregateChannelBuffer(buffers);
            newbuf.writerIndex(length);
            return newbuf;
        }
    }

    public void writeBytes(ChannelBuffer src, int srcIndex, int length) {
        if (src.order() != order) {
            throw new IllegalArgumentException("All buffers must have the same endianness.");
        }
        if (src != null) {
            if (capacity() > 0 && order != src.order()) {
                throw new IllegalArgumentException("All buffers must have the same endianness.");
            }
            ArrayList<ChannelBuffer> bufferAddedList = new ArrayList<ChannelBuffer>(1);
            if (src instanceof AggregateChannelBuffer) {
                AggregateChannelBuffer subbuf = (AggregateChannelBuffer) src;
                ArrayList<ChannelBuffer> subList = subbuf.getBufferList(srcIndex, length);
                bufferAddedList.addAll(subList);
            } else {
                ChannelBuffer other = src.slice(srcIndex, length);
                bufferAddedList.add(other);
            }
            int localWriterIndex = this.writerIndex();
            for (ChannelBuffer buffer : bufferAddedList) {
                int sublength = buffer.readableBytes();
                setBytes(localWriterIndex, buffer, buffer.readerIndex(), sublength);
                localWriterIndex += sublength;
            }
            this.writerIndex(localWriterIndex);
        }
    }

    public ChannelBufferFactory factory() {
        return HeapChannelBufferFactory.getInstance(order());
    }

    public ByteOrder order() {
        return order;
    }

    public int capacity() {
        return indices[slices.length];
    }

    public byte getByte(int index) {
        int sliceId = sliceId(index);
        return slices[sliceId].getByte(index - indices[sliceId]);
    }

    public short getShort(int index) {
        int sliceId = sliceId(index);
        if (index + 2 <= indices[sliceId + 1]) {
            return slices[sliceId].getShort(index - indices[sliceId]);
        } else if (order() == ByteOrder.BIG_ENDIAN) {
            return (short) ((getByte(index) & 0xff) << 8 | getByte(index + 1) & 0xff);
        } else {
            return (short) (getByte(index) & 0xff | (getByte(index + 1) & 0xff) << 8);
        }
    }

    public int getUnsignedMedium(int index) {
        int sliceId = sliceId(index);
        if (index + 3 <= indices[sliceId + 1]) {
            return slices[sliceId].getUnsignedMedium(index - indices[sliceId]);
        } else if (order() == ByteOrder.BIG_ENDIAN) {
            return (getShort(index) & 0xffff) << 8 | getByte(index + 2) & 0xff;
        } else {
            return getShort(index) & 0xFFFF | (getByte(index + 2) & 0xFF) << 16;
        }
    }

    public int getInt(int index) {
        int sliceId = sliceId(index);
        if (index + 4 <= indices[sliceId + 1]) {
            return slices[sliceId].getInt(index - indices[sliceId]);
        } else if (order() == ByteOrder.BIG_ENDIAN) {
            return (getShort(index) & 0xffff) << 16 | getShort(index + 2) & 0xffff;
        } else {
            return getShort(index) & 0xFFFF | (getShort(index + 2) & 0xFFFF) << 16;
        }
    }

    public long getLong(int index) {
        int sliceId = sliceId(index);
        if (index + 8 <= indices[sliceId + 1]) {
            return slices[sliceId].getLong(index - indices[sliceId]);
        } else if (order() == ByteOrder.BIG_ENDIAN) {
            return (getInt(index) & 0xffffffffL) << 32 | getInt(index + 4) & 0xffffffffL;
        } else {
            return getInt(index) & 0xFFFFFFFFL | (getInt(index + 4) & 0xFFFFFFFFL) << 32;
        }
    }

    public void getBytes(int index, byte[] dst, int dstIndex, int length) {
        int sliceId = sliceId(index);
        if (index > capacity() - length || dstIndex > dst.length - length) {
            throw new IndexOutOfBoundsException();
        }
        int i = sliceId;
        while (length > 0) {
            ChannelBuffer s = slices[i];
            int adjustment = indices[i];
            int localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.getBytes(index - adjustment, dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
            length -= localLength;
            i++;
        }
    }

    public void getBytes(int index, ByteBuffer dst) {
        int sliceId = sliceId(index);
        int limit = dst.limit();
        int length = dst.remaining();
        if (index > capacity() - length) {
            throw new IndexOutOfBoundsException();
        }
        int i = sliceId;
        try {
            while (length > 0) {
                ChannelBuffer s = slices[i];
                int adjustment = indices[i];
                int localLength = Math.min(length, s.capacity() - (index - adjustment));
                dst.limit(dst.position() + localLength);
                s.getBytes(index - adjustment, dst);
                index += localLength;
                length -= localLength;
                i++;
            }
        } finally {
            dst.limit(limit);
        }
    }

    public void getBytes(int index, ChannelBuffer dst, int dstIndex, int length) {
        int sliceId = sliceId(index);
        if (index > capacity() - length || dstIndex > dst.capacity() - length) {
            throw new IndexOutOfBoundsException();
        }
        int i = sliceId;
        while (length > 0) {
            ChannelBuffer s = slices[i];
            int adjustment = indices[i];
            int localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.getBytes(index - adjustment, dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
            length -= localLength;
            i++;
        }
    }

    public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
        return out.write(toByteBuffer(index, length));
    }

    public void getBytes(int index, OutputStream out, int length) throws IOException {
        int sliceId = sliceId(index);
        if (index > capacity() - length) {
            throw new IndexOutOfBoundsException();
        }
        int i = sliceId;
        while (length > 0) {
            ChannelBuffer s = slices[i];
            int adjustment = indices[i];
            int localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.getBytes(index - adjustment, out, localLength);
            index += localLength;
            length -= localLength;
            i++;
        }
    }

    public void setByte(int index, byte value) {
        int sliceId = sliceId(index);
        slices[sliceId].setByte(index - indices[sliceId], value);
    }

    public void setShort(int index, short value) {
        int sliceId = sliceId(index);
        if (index + 2 <= indices[sliceId + 1]) {
            slices[sliceId].setShort(index - indices[sliceId], value);
        } else if (order() == ByteOrder.BIG_ENDIAN) {
            setByte(index, (byte) (value >>> 8));
            setByte(index + 1, (byte) value);
        } else {
            setByte(index, (byte) value);
            setByte(index + 1, (byte) (value >>> 8));
        }
    }

    public void setMedium(int index, int value) {
        int sliceId = sliceId(index);
        if (index + 3 <= indices[sliceId + 1]) {
            slices[sliceId].setMedium(index - indices[sliceId], value);
        } else if (order() == ByteOrder.BIG_ENDIAN) {
            setShort(index, (short) (value >> 8));
            setByte(index + 2, (byte) value);
        } else {
            setShort(index, (short) value);
            setByte(index + 2, (byte) (value >>> 16));
        }
    }

    public void setInt(int index, int value) {
        int sliceId = sliceId(index);
        if (index + 4 <= indices[sliceId + 1]) {
            slices[sliceId].setInt(index - indices[sliceId], value);
        } else if (order() == ByteOrder.BIG_ENDIAN) {
            setShort(index, (short) (value >>> 16));
            setShort(index + 2, (short) value);
        } else {
            setShort(index, (short) value);
            setShort(index + 2, (short) (value >>> 16));
        }
    }

    public void setLong(int index, long value) {
        int sliceId = sliceId(index);
        if (index + 8 <= indices[sliceId + 1]) {
            slices[sliceId].setLong(index - indices[sliceId], value);
        } else if (order() == ByteOrder.BIG_ENDIAN) {
            setInt(index, (int) (value >>> 32));
            setInt(index + 4, (int) value);
        } else {
            setInt(index, (int) value);
            setInt(index + 4, (int) (value >>> 32));
        }
    }

    public void setBytes(int index, byte[] src, int srcIndex, int length) {
        int sliceId = sliceId(index);
        if (index > capacity() - length || srcIndex > src.length - length) {
            throw new IndexOutOfBoundsException();
        }
        int i = sliceId;
        while (length > 0) {
            ChannelBuffer s = slices[i];
            int adjustment = indices[i];
            int localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.setBytes(index - adjustment, src, srcIndex, localLength);
            index += localLength;
            srcIndex += localLength;
            length -= localLength;
            i++;
        }
    }

    public void setBytes(int index, ByteBuffer src) {
        int sliceId = sliceId(index);
        int limit = src.limit();
        int length = src.remaining();
        if (index > capacity() - length) {
            throw new IndexOutOfBoundsException();
        }
        int i = sliceId;
        try {
            while (length > 0) {
                ChannelBuffer s = slices[i];
                int adjustment = indices[i];
                int localLength = Math.min(length, s.capacity() - (index - adjustment));
                src.limit(src.position() + localLength);
                s.setBytes(index - adjustment, src);
                index += localLength;
                length -= localLength;
                i++;
            }
        } finally {
            src.limit(limit);
        }
    }

    public void setBytes(int index, ChannelBuffer src, int srcIndex, int length) {
        int sliceId = sliceId(index);
        if (index > capacity() - length || srcIndex > src.capacity() - length) {
            throw new IndexOutOfBoundsException();
        }
        int i = sliceId;
        while (length > 0) {
            ChannelBuffer s = slices[i];
            int adjustment = indices[i];
            int localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.setBytes(index - adjustment, src, srcIndex, localLength);
            index += localLength;
            srcIndex += localLength;
            length -= localLength;
            i++;
        }
    }

    public int setBytes(int index, InputStream in, int length) throws IOException {
        int sliceId = sliceId(index);
        if (index > capacity() - length) {
            throw new IndexOutOfBoundsException();
        }
        int i = sliceId;
        int readBytes = 0;
        do {
            ChannelBuffer s = slices[i];
            int adjustment = indices[i];
            int localLength = Math.min(length, s.capacity() - (index - adjustment));
            int localReadBytes = s.setBytes(index - adjustment, in, localLength);
            if (localReadBytes < 0) {
                if (readBytes == 0) {
                    return -1;
                } else {
                    break;
                }
            }
            if (localReadBytes == localLength) {
                index += localLength;
                length -= localLength;
                readBytes += localLength;
                i++;
            } else {
                index += localReadBytes;
                length -= localReadBytes;
                readBytes += localReadBytes;
            }
        } while (length > 0);
        return readBytes;
    }

    public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
        int sliceId = sliceId(index);
        if (index > capacity() - length) {
            throw new IndexOutOfBoundsException();
        }
        int i = sliceId;
        int readBytes = 0;
        do {
            ChannelBuffer s = slices[i];
            int adjustment = indices[i];
            int localLength = Math.min(length, s.capacity() - (index - adjustment));
            int localReadBytes = s.setBytes(index - adjustment, in, localLength);
            if (localReadBytes == localLength) {
                index += localLength;
                length -= localLength;
                readBytes += localLength;
                i++;
            } else {
                index += localReadBytes;
                length -= localReadBytes;
                readBytes += localReadBytes;
            }
        } while (length > 0);
        return readBytes;
    }

    public ChannelBuffer duplicate() {
        ChannelBuffer duplicate = new AggregateChannelBuffer(this);
        duplicate.setIndex(readerIndex(), writerIndex());
        return duplicate;
    }

    public ChannelBuffer copy(int index, int length) {
        int sliceId = sliceId(index);
        if (index > capacity() - length) {
            throw new IndexOutOfBoundsException();
        }
        ChannelBuffer dst = factory().getBuffer(order(), length);
        copyTo(index, length, sliceId, dst);
        return dst;
    }

    private void copyTo(int index, int length, int sliceId, ChannelBuffer dst) {
        int dstIndex = 0;
        int i = sliceId;
        while (length > 0) {
            ChannelBuffer s = slices[i];
            int adjustment = indices[i];
            int localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.getBytes(index - adjustment, dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
            length -= localLength;
            i++;
        }
        dst.writerIndex(dst.capacity());
    }

    public ByteBuffer toByteBuffer(int index, int length) {
        if (slices.length == 1) {
            return slices[0].toByteBuffer(index, length);
        }
        ByteBuffer[] buffers = toByteBuffers(index, length);
        ByteBuffer merged = ByteBuffer.allocate(length).order(order());
        for (ByteBuffer b : buffers) {
            merged.put(b);
        }
        merged.flip();
        return merged;
    }

    @Override
    public ByteBuffer[] toByteBuffers(int index, int length) {
        int sliceId = sliceId(index);
        if (index + length > capacity()) {
            throw new IndexOutOfBoundsException();
        }
        List<ByteBuffer> buffers = new ArrayList<ByteBuffer>(slices.length);
        int i = sliceId;
        while (length > 0) {
            ChannelBuffer s = slices[i];
            int adjustment = indices[i];
            int localLength = Math.min(length, s.capacity() - (index - adjustment));
            buffers.add(s.toByteBuffer(index - adjustment, localLength));
            index += localLength;
            length -= localLength;
            i++;
        }
        return buffers.toArray(new ByteBuffer[buffers.size()]);
    }

    public String toString(int index, int length, String charsetName) {
        int sliceId = sliceId(index);
        if (index + length <= indices[sliceId + 1]) {
            return slices[sliceId].toString(index - indices[sliceId], length, charsetName);
        }
        byte[] data = new byte[length];
        int dataIndex = 0;
        int i = sliceId;
        while (length > 0) {
            ChannelBuffer s = slices[i];
            int adjustment = indices[i];
            int localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.getBytes(index - adjustment, data, dataIndex, localLength);
            index += localLength;
            dataIndex += localLength;
            length -= localLength;
            i++;
        }
        try {
            return new String(data, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedCharsetException(charsetName);
        }
    }

    private int sliceId(int index) {
        int lastSliceId = this.lastSliceId;
        if (index >= indices[lastSliceId]) {
            if (index < indices[lastSliceId + 1]) {
                return lastSliceId;
            }
            for (int i = lastSliceId + 1; i < slices.length; i++) {
                if (index < indices[i + 1]) {
                    this.lastSliceId = i;
                    return i;
                }
            }
        } else {
            for (int i = lastSliceId - 1; i >= 0; i--) {
                if (index >= indices[i]) {
                    this.lastSliceId = i;
                    return i;
                }
            }
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public String toString() {
        String result = super.toString();
        result = result.substring(0, result.length() - 1);
        return result + ", slices=" + slices.length + ")";
    }
}
