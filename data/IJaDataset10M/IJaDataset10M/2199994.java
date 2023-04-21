package com.wowd.wobly.examples.inheritance.simple;

import com.wowd.wobly.annotations.InlineSuperclass;
import com.wowd.wobly.annotations.WoblyField;

@InlineSuperclass(value = 2)
public class Child2 extends Parent {

    @WoblyField(id = 1)
    String key;

    public static final com.wowd.wobly.WoblyReader<Child2> objectReader = new com.wowd.wobly.WoblyReaderImpl<Child2>() {

        @Override
        public Child2 readObject(java.nio.ByteBuffer buf) {
            return read(buf);
        }

        @Override
        public int targetTypeID() {
            return 255 & 2;
        }
    };

    @Override
    public void write(final java.nio.ByteBuffer buf) {
        try {
            int startPositionMark = buf.position();
            buf.position(buf.position() + 4);
            buf.put(typeID());
            int unknownsCounter = 0;
            if (unknownFields == null) unknownsCounter = Integer.MAX_VALUE;
            {
                buf.putInt(this.commonRequiredField);
            }
            unknownsCounter = writeUnknownsUpTo(unknownsCounter, 0, buf);
            if (this.commonField != 0) {
                buf.put((byte) 2);
                buf.putInt(this.commonField);
            }
            unknownsCounter = writeUnknownsUpTo(unknownsCounter, 1, buf);
            if (this.key != null) {
                buf.put((byte) 15);
                com.wowd.wobly.WoblyUtils.Buffers.putStringUTF8(buf, this.key, true);
            }
            writeUnknownsUpTo(unknownsCounter, Integer.MAX_VALUE, buf);
            buf.putInt(startPositionMark, buf.position() - startPositionMark - 4);
        } catch (com.wowd.wobly.exceptions.WoblyWriteException e) {
            throw e;
        } catch (java.lang.Throwable t) {
            throw new com.wowd.wobly.exceptions.WoblyWriteException(t);
        }
    }

    private Child2(final java.nio.ByteBuffer buf) {
        if (buf.get() != typeID()) throw new com.wowd.wobly.exceptions.WoblyReadException();
        {
            this.commonRequiredField = buf.getInt();
        }
        int tag = com.wowd.wobly.WoblyUtils.Buffers.getVIntOrMax(buf);
        tag = readUnknownsUpTo(tag, 0, buf);
        if (com.wowd.wobly.WoblyUtils.getIDFromTag(tag) > 0) this.commonField = 0; else {
            this.commonField = buf.getInt();
            tag = com.wowd.wobly.WoblyUtils.Buffers.getVIntOrMax(buf);
        }
        tag = readUnknownsUpTo(tag, 1, buf);
        if (com.wowd.wobly.WoblyUtils.getIDFromTag(tag) > 1) this.key = null; else {
            this.key = com.wowd.wobly.WoblyUtils.Buffers.getStringUTF8(buf, true);
            tag = com.wowd.wobly.WoblyUtils.Buffers.getVIntOrMax(buf);
        }
        readUnknownsUpTo(tag, Integer.MAX_VALUE, buf);
    }

    @com.wowd.wobly.annotations.ReadStatic
    public static Child2 read(java.nio.ByteBuffer buf) {
        try {
            int size = buf.getInt();
            int originalLimit = buf.limit();
            int newLimit = buf.position() + size;
            if (newLimit > originalLimit) throw new com.wowd.wobly.exceptions.WoblyReadException(newLimit + " " + originalLimit);
            buf.limit(newLimit);
            Child2 object = new Child2(buf);
            buf.limit(originalLimit);
            return object;
        } catch (com.wowd.wobly.exceptions.WoblyReadException e) {
            throw e;
        } catch (java.lang.Throwable t) {
            throw new com.wowd.wobly.exceptions.WoblyReadException(t);
        }
    }

    public static Child2 read(byte[] buf) {
        return read(java.nio.ByteBuffer.wrap(buf));
    }

    @Override
    public int getSize() {
        int size = 0;
        size += 1;
        {
            size += 4;
        }
        if (this.commonField != 0) {
            size += 1;
            size += 4;
        }
        if (this.key != null) {
            size += 1;
            size += com.wowd.wobly.WoblyUtils.Buffers.sizeStringUTF8(this.key, true);
        }
        if (unknownFields != null) for (com.wowd.wobly.unknown.UnknownField uf : unknownFields) size += uf.getSize();
        size += 4;
        return size;
    }

    @Override
    public byte typeID() {
        return 2;
    }
}
