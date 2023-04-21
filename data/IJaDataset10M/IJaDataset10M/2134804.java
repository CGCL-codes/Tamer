package com.sun.opengl.impl.mipmap;

import java.nio.ByteBuffer;

/**
 *
 * @author Administrator
 */
public class Extract233rev implements Extract {

    /** Creates a new instance of Extract223rev */
    public Extract233rev() {
    }

    public void extract(boolean isSwap, ByteBuffer packedPixel, float[] extractComponents) {
        byte ubyte = packedPixel.get();
        extractComponents[0] = (float) ((ubyte & 0x07)) / 7.0f;
        extractComponents[1] = (float) ((ubyte & 0x38) >> 3) / 7.0f;
        extractComponents[2] = (float) ((ubyte & 0xC0) >> 6) / 3.0f;
    }

    public void shove(float[] shoveComponents, int index, ByteBuffer packedPixel) {
        assert (0.0f <= shoveComponents[0] && shoveComponents[0] <= 1.0f);
        assert (0.0f <= shoveComponents[1] && shoveComponents[1] <= 1.0f);
        assert (0.0f <= shoveComponents[2] && shoveComponents[2] <= 1.0f);
        byte b = (byte) (((int) ((shoveComponents[0] * 7) + 0.5f)) & 0x07);
        b |= (byte) (((int) ((shoveComponents[1] * 7) + 0.5f) << 3) & 0x38);
        b |= (byte) (((int) ((shoveComponents[2] * 3) + 0.5f) << 6) & 0xC0);
        packedPixel.position(index);
        packedPixel.put(b);
    }
}
