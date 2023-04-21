package com.wowd.wobly.examples.old;

import com.wowd.wobly.annotations.InlineSuperclass;
import com.wowd.wobly.annotations.WoblyField;

@InlineSuperclass(value = 10)
public class KlasaB extends KlasaA {

    @WoblyField(id = 0)
    private final long time;

    @WoblyField(id = 6)
    private final String key;

    public KlasaB(long z, String id, long time, String key) {
        super(z, id);
        this.time = time;
        this.key = key;
    }

    public static final com.wowd.wobly.WoblyReader<KlasaB> objectReader = new com.wowd.wobly.WoblyReaderImpl<KlasaB>() {

        @Override
        public KlasaB readObject(java.nio.ByteBuffer buf) {
            return read(buf);
        }

        @Override
        public int targetTypeID() {
            return 255 & 10;
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
            if (this.time != 0) {
                buf.put((byte) 3);
                buf.putLong(this.time);
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
            unknownsCounter = writeUnknownsUpTo(unknownsCounter, 6, buf);
            if (this.key != null) {
                buf.put((byte) 55);
                com.wowd.wobly.WoblyUtils.Buffers.putStringUTF8(buf, this.key, true);
            }
            writeUnknownsUpTo(unknownsCounter, Integer.MAX_VALUE, buf);
            com.wowd.wobly.WoblyUtils.Buffers.appendVariableSize(buf, startPositionMark);
        } catch (com.wowd.wobly.exceptions.WoblyWriteException e) {
            throw e;
        } catch (java.lang.Throwable t) {
            throw new com.wowd.wobly.exceptions.WoblyWriteException(t);
        }
    }

    private KlasaB(final java.nio.ByteBuffer buf) {
        if (buf.get() != typeID()) throw new com.wowd.wobly.exceptions.WoblyReadException();
        int tag = com.wowd.wobly.WoblyUtils.Buffers.getVIntOrMax(buf);
        tag = readUnknownsUpTo(tag, 0, buf);
        if (com.wowd.wobly.WoblyUtils.getIDFromTag(tag) > 0) this.time = 0; else {
            this.time = buf.getLong();
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
        tag = readUnknownsUpTo(tag, 6, buf);
        if (com.wowd.wobly.WoblyUtils.getIDFromTag(tag) > 6) this.key = null; else {
            this.key = com.wowd.wobly.WoblyUtils.Buffers.getStringUTF8(buf, true);
            tag = com.wowd.wobly.WoblyUtils.Buffers.getVIntOrMax(buf);
        }
        readUnknownsUpTo(tag, Integer.MAX_VALUE, buf);
    }

    @com.wowd.wobly.annotations.ReadStatic
    public static KlasaB read(java.nio.ByteBuffer buf) {
        try {
            int size = com.wowd.wobly.WoblyUtils.Buffers.getVInt(buf);
            int originalLimit = buf.limit();
            int newLimit = buf.position() + size;
            if (newLimit > originalLimit) throw new com.wowd.wobly.exceptions.WoblyReadException(newLimit + " " + originalLimit);
            buf.limit(newLimit);
            KlasaB object = new KlasaB(buf);
            buf.limit(originalLimit);
            return object;
        } catch (com.wowd.wobly.exceptions.WoblyReadException e) {
            throw e;
        } catch (java.lang.Throwable t) {
            throw new com.wowd.wobly.exceptions.WoblyReadException(t);
        }
    }

    public static KlasaB read(byte[] buf) {
        return read(java.nio.ByteBuffer.wrap(buf));
    }

    @Override
    public int getSize() {
        int size = 0;
        size += 1;
        if (this.time != 0) {
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
        if (this.key != null) {
            size += 1;
            size += com.wowd.wobly.WoblyUtils.Buffers.sizeStringUTF8(this.key, true);
        }
        if (unknownFields != null) for (com.wowd.wobly.unknown.UnknownField uf : unknownFields) size += uf.getSize();
        size += com.wowd.wobly.WoblyUtils.Buffers.sizeVInt(size);
        return size;
    }

    @Override
    public byte typeID() {
        return 10;
    }
}
