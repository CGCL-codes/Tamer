package com.wowd.wobly.examples.old;

import com.wowd.wobly.annotations.InlineSuperclass;
import com.wowd.wobly.annotations.WoblyField;

@InlineSuperclass(value = 11)
public class KlasaC extends KlasaA {

    @WoblyField(id = 0)
    private final double value;

    public KlasaC(long z, String id, double value) {
        super(z, id);
        this.value = value;
    }

    public static final com.wowd.wobly.WoblyReader<KlasaC> objectReader = new com.wowd.wobly.WoblyReaderImpl<KlasaC>() {

        @Override
        public KlasaC readObject(java.nio.ByteBuffer buf) {
            return read(buf);
        }

        @Override
        public int targetTypeID() {
            return 255 & 11;
        }
    };

    @Override
    public void write(final java.nio.ByteBuffer buf) {
        try {
            int startPositionMark = buf.position();
            buf.position(buf.position() + 1);
            buf.put(typeID());
            int unknownsCounter = 0;
            if (unknownFields == null) unknownsCounter = Integer.MAX_VALUE;
            unknownsCounter = writeUnknownsUpTo(unknownsCounter, 0, buf);
            if (this.value != 0) {
                buf.put((byte) 3);
                buf.putDouble(this.value);
            }
            unknownsCounter = writeUnknownsUpTo(unknownsCounter, 2, buf);
            if (this.z != 0) {
                buf.put((byte) 19);
                buf.putLong(this.z);
            }
            unknownsCounter = writeUnknownsUpTo(unknownsCounter, 3, buf);
            if (this.id != null) {
                buf.put((byte) 31);
                com.wowd.wobly.WoblyUtils.Buffers.putStringUTF8(buf, this.id, true);
            }
            writeUnknownsUpTo(unknownsCounter, Integer.MAX_VALUE, buf);
            com.wowd.wobly.WoblyUtils.Buffers.appendVariableSize(buf, startPositionMark);
        } catch (com.wowd.wobly.exceptions.WoblyWriteException e) {
            throw e;
        } catch (java.lang.Throwable t) {
            throw new com.wowd.wobly.exceptions.WoblyWriteException(t);
        }
    }

    private KlasaC(final java.nio.ByteBuffer buf) {
        if (buf.get() != typeID()) throw new com.wowd.wobly.exceptions.WoblyReadException();
        int tag = com.wowd.wobly.WoblyUtils.Buffers.getVIntOrMax(buf);
        tag = readUnknownsUpTo(tag, 0, buf);
        if (com.wowd.wobly.WoblyUtils.getIDFromTag(tag) > 0) this.value = 0; else {
            this.value = buf.getDouble();
            tag = com.wowd.wobly.WoblyUtils.Buffers.getVIntOrMax(buf);
        }
        tag = readUnknownsUpTo(tag, 2, buf);
        if (com.wowd.wobly.WoblyUtils.getIDFromTag(tag) > 2) this.z = 0; else {
            this.z = buf.getLong();
            tag = com.wowd.wobly.WoblyUtils.Buffers.getVIntOrMax(buf);
        }
        tag = readUnknownsUpTo(tag, 3, buf);
        if (com.wowd.wobly.WoblyUtils.getIDFromTag(tag) > 3) this.id = null; else {
            this.id = com.wowd.wobly.WoblyUtils.Buffers.getStringUTF8(buf, true);
            tag = com.wowd.wobly.WoblyUtils.Buffers.getVIntOrMax(buf);
        }
        readUnknownsUpTo(tag, Integer.MAX_VALUE, buf);
    }

    @com.wowd.wobly.annotations.ReadStatic
    public static KlasaC read(java.nio.ByteBuffer buf) {
        try {
            int size = com.wowd.wobly.WoblyUtils.Buffers.getVInt(buf);
            int originalLimit = buf.limit();
            int newLimit = buf.position() + size;
            if (newLimit > originalLimit) throw new com.wowd.wobly.exceptions.WoblyReadException(newLimit + " " + originalLimit);
            buf.limit(newLimit);
            KlasaC object = new KlasaC(buf);
            buf.limit(originalLimit);
            return object;
        } catch (com.wowd.wobly.exceptions.WoblyReadException e) {
            throw e;
        } catch (java.lang.Throwable t) {
            throw new com.wowd.wobly.exceptions.WoblyReadException(t);
        }
    }

    public static KlasaC read(byte[] buf) {
        return read(java.nio.ByteBuffer.wrap(buf));
    }

    @Override
    public int getSize() {
        int size = 0;
        size += 1;
        if (this.value != 0) {
            size += 1;
            size += 8;
        }
        if (this.z != 0) {
            size += 1;
            size += 8;
        }
        if (this.id != null) {
            size += 1;
            size += com.wowd.wobly.WoblyUtils.Buffers.sizeStringUTF8(this.id, true);
        }
        if (unknownFields != null) for (com.wowd.wobly.unknown.UnknownField uf : unknownFields) size += uf.getSize();
        size += com.wowd.wobly.WoblyUtils.Buffers.sizeVInt(size);
        return size;
    }

    @Override
    public byte typeID() {
        return 11;
    }
}
