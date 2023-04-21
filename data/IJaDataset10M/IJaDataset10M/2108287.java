package com.fluendo.jst;

import java.util.*;

public class Buffer {

    private static Stack pool = new Stack();

    private static int live;

    public static final int FLAG_DISCONT = (1 << 0);

    public static final int FLAG_DELTA_UNIT = (1 << 1);

    public int flags;

    public java.lang.Object object;

    public byte[] data;

    public int offset;

    public int length;

    public Caps caps;

    public long time_offset;

    public long timestamp;

    public long timestampEnd;

    public static Buffer create() {
        Buffer result;
        try {
            result = (Buffer) pool.pop();
        } catch (EmptyStackException e) {
            result = new Buffer();
            live++;
        }
        result.time_offset = -1;
        result.timestamp = -1;
        result.timestampEnd = -1;
        result.flags = 0;
        return result;
    }

    public Buffer() {
    }

    public boolean isFlagSet(int flag) {
        return (flags & flag) == flag;
    }

    public void setFlag(int flag, boolean val) {
        if (val) flags |= flag; else flags &= ~flag;
    }

    public void free() {
        object = null;
        caps = null;
        pool.push(this);
    }

    public void ensureSize(int length) {
        if (data == null) {
            data = new byte[length];
        } else if (data.length < length) {
            data = new byte[length];
        }
    }

    /**
   * copies data into the buffer.
   * 
   * @param data: the bytearray with data to copy
   * @param offset: offset in the bytearray of first byte
   * @param length: length of data to copy
   */
    public void copyData(byte[] data, int offset, int length) {
        ensureSize(length);
        System.arraycopy(data, offset, this.data, 0, length);
        this.offset = 0;
        this.length = length;
    }
}
