package org.xith3d.loaders.models.impl.cal3d.buffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/** ScalarfBuffer provides a wrapper to FloatBuffer for floats.
 *  It is also the intended extension point for high-performance implementations
 *  of vector linear algebra.
 * @author Dave Lloyd, (c) Short Fuze Ltd., 2003.
 */
public class ScalarfBuffer {

    protected int length;

    FloatBuffer floatBuffer;

    public ScalarfBuffer(int length) {
        this.length = length;
        floatBuffer = ByteBuffer.allocateDirect(length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public void set(ScalarfBuffer v) {
        for (int n = 0; n < length; n++) {
            floatBuffer.put(n, v.floatBuffer.get(n));
        }
    }

    public void put(int n, float x) {
        floatBuffer.put(n, x);
    }

    public float get(int n) {
        return floatBuffer.get(n);
    }

    public Buffer getBuffer() {
        return floatBuffer;
    }
}
