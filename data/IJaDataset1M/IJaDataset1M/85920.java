package jogamp.opengl.glu.mipmap;

import java.nio.ByteBuffer;

/**
 *
 * @author Administrator
 */
public interface Extract {

    public void extract(boolean isSwap, ByteBuffer packedPixel, float[] extractComponents);

    public void shove(float[] shoveComponents, int index, ByteBuffer packedPixel);
}
