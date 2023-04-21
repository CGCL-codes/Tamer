package org.encog.plugins.opencl;

import java.nio.FloatBuffer;
import org.encog.util.EngineArray;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLFloatBuffer;
import com.nativelibs4java.opencl.CLMem.Usage;
import com.nativelibs4java.util.NIOUtils;

public class ExpandingBuffer {

    private FloatBuffer buffer;

    private CLFloatBuffer clBuffer;

    private CLContext context;

    private int lastSize;

    private boolean isOutput;

    public ExpandingBuffer(CLContext context, boolean isOutput) {
        this.context = context;
        this.isOutput = isOutput;
        this.lastSize = -1;
    }

    public void setSize(int size) {
        if (this.lastSize >= size) {
            return;
        }
        this.buffer = NIOUtils.directFloats(size, context.getByteOrder());
        if (isOutput) {
            this.clBuffer = context.createFloatBuffer(Usage.Output, buffer, false);
        } else {
            this.clBuffer = context.createFloatBuffer(Usage.Input, buffer, false);
        }
        this.lastSize = size;
    }

    /**
	 * @return the buffer
	 */
    public FloatBuffer getBuffer() {
        return buffer;
    }

    /**
	 * @param buffer the buffer to set
	 */
    public void setBuffer(FloatBuffer buffer) {
        this.buffer = buffer;
    }

    /**
	 * @return the clBuffer
	 */
    public CLFloatBuffer getCLBuffer() {
        return clBuffer;
    }

    /**
	 * @param clBuffer the clBuffer to set
	 */
    public void setCLBuffer(CLFloatBuffer clBuffer) {
        this.clBuffer = clBuffer;
    }

    public void set(double[] d) {
        float[] d2 = new float[d.length];
        EngineArray.arrayCopy(d, d2);
        this.buffer.rewind();
        this.buffer.put(d2);
    }
}
