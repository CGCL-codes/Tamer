package jogamp.opengl.glu.mipmap;

import java.nio.*;

/**
 *
 * @author Administrator
 */
public class ExtractSInt implements ExtractPrimitive {

    /** Creates a new instance of ExtractSInt */
    public ExtractSInt() {
    }

    public double extract(boolean isSwap, ByteBuffer uint) {
        int i = 0;
        if (isSwap) {
            i = Mipmap.GLU_SWAP_4_BYTES(uint.getInt());
        } else {
            i = uint.getInt();
        }
        assert (i <= 0x7FFFFFFF);
        return (i);
    }

    public void shove(double value, int index, ByteBuffer data) {
        assert (0.0 <= value && value < Integer.MAX_VALUE);
        IntBuffer ib = data.asIntBuffer();
        ib.position(index);
        ib.put((int) value);
    }
}
