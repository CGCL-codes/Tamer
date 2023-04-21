package com.sun.opengl.impl.glu.mipmap;

import java.nio.ByteBuffer;

/**
 *
 * @author Administrator
 */
public class ExtractSByte implements ExtractPrimitive {

    /** Creates a new instance of ExtractUByte */
    public ExtractSByte() {
    }

    public double extract(boolean isSwap, ByteBuffer sbyte) {
        byte b = sbyte.get();
        assert (b <= 127);
        return (b);
    }

    public void shove(double value, int index, ByteBuffer data) {
        data.position(index);
        data.put((byte) value);
    }
}
