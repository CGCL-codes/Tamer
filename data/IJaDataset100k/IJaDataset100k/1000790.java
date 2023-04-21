package com.tapina.robe.runtime;

import java.awt.*;

/**
 * Created by IntelliJ IDEA. User: gareth Date: Jul 8, 2005 Time: 11:59:38 AM To change this template use File |
 * Settings | File Templates.
 */
public abstract class BinaryDataSource {

    abstract byte getByte(int offset);

    abstract int getWord(int offset);

    /**
     * Load a zero-terminated string from memory. Encoding is assumed to be ISO-8859-1.
     *
     * @param address
     * @return String loaded
     */
    public final String getString0(int address) {
        return getTerminatedString(address, StringTerminator.TERMINATE_0);
    }

    final String getTerminatedString(int address, StringTerminator terminator) {
        return getTerminatedString(address, terminator, Integer.MAX_VALUE);
    }

    abstract String getTerminatedString(int address, StringTerminator stringTerminator, int maxLength);

    public final String getString0_10_13(int address) {
        return getTerminatedString(address, StringTerminator.TERMINATE_0_10_13);
    }

    public final String getStringControlTerminated(int offset) {
        return getTerminatedString(offset, StringTerminator.TERMINATE_CONTROL);
    }

    public final String getStringControlTerminated(int offset, int length) {
        return getTerminatedString(offset, StringTerminator.TERMINATE_CONTROL, length);
    }

    abstract Rectangle getRectangle(int offset);

    abstract Point getPoint(int offset);

    public final String getStringN(int offset, int maxLength) {
        return getTerminatedString(offset, StringTerminator.TERMINATE_0, maxLength);
    }

    /**
     * Get an object which allows direct access to the underlying data block for the specified byte range.
     *
     * @param address address for which to find bytes
     * @param count   size of byte block to allow access to
     * @param create
     * @return ByteArray which reads/writes through to underlying data block.
     */
    public abstract ByteArray getByteArray(int address, int count, boolean create);

    /**
     * Take a copy of a set of bytes from the memory map.
     * Use {@link #getByteArray(int,int,boolean)} instead for efficiency.
     *
     * @param address address at which to load bytes
     * @param count   number of bytes to load
     * @return array containing bytes loaded from specified address
     */
    public abstract byte[] getBytes(int address, int count);

    public abstract long get5ByteValue(int address);

    public abstract int[] getWords(int address, int count);
}
