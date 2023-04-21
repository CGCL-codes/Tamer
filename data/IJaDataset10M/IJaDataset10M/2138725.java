package org.akinoshideout.Utils.Shaders;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;

public class IntVertexBufferObject implements VertexBufferObject {

    private int bufferID = 0;

    private IntBuffer data = null;

    public IntVertexBufferObject() {
        generateID();
    }

    public IntVertexBufferObject(IntBuffer data) {
        this.data = data;
        generateID();
        bindBuffer();
        loadData();
    }

    public void bindBuffer() {
        ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, bufferID);
    }

    public void disposeBuffer() {
        ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
        ARBVertexBufferObject.glDeleteBuffersARB(BufferUtils.createIntBuffer(1).put(bufferID));
    }

    /**
	 * @param data
	 *            the data to set
	 */
    public void setData(IntBuffer data) {
        this.data = data;
        bindBuffer();
        loadData();
    }

    /**
	 * @return the data
	 */
    public IntBuffer getData() {
        return data;
    }

    private void generateID() {
        IntBuffer id = BufferUtils.createIntBuffer(1);
        ARBVertexBufferObject.glGenBuffersARB(id);
        bufferID = id.get(0);
    }

    private void loadData() {
        ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, data, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
    }
}
