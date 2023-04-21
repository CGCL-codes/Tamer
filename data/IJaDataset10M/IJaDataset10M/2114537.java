package de.kapsi.net.daap.chunks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A signed byte
 */
public abstract class SByteChunk extends AbstractChunk implements ByteChunk {

    private static final Log LOG = LogFactory.getLog(SByteChunk.class);

    public static final int MIN_VALUE = Byte.MIN_VALUE;

    public static final int MAX_VALUE = Byte.MAX_VALUE;

    protected int value = 0;

    public SByteChunk(int type, String name, int value) {
        super(type, name);
        setValue(value);
    }

    public SByteChunk(String type, String name, int value) {
        super(type, name);
        setValue(value);
    }

    public void setValue(int value) {
        this.value = checkSByteRange(value);
    }

    public int getValue() {
        return value;
    }

    /**
     * Checks if #MIN_VALUE <= value <= #MAX_VALUE and if 
     * not an IllegalArgumentException is thrown.
     */
    public static int checkSByteRange(int value) throws IllegalArgumentException {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Value is outside of signed byte range: " + value);
            }
        }
        return value;
    }

    /**
     * Returns {@see #BYTE_TYPE}
     */
    public int getType() {
        return Chunk.BYTE_TYPE;
    }

    public String toString(int indent) {
        return indent(indent) + name + "(" + getContentCodeString() + "; byte)=" + getValue();
    }
}
