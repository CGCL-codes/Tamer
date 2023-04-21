package org.akinoshideout.Utils.Shaders;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;

public class FloatVertexBufferObject implements VertexBufferObject {

    private int bufferID = 0;

    private FloatBuffer data = null;

    public FloatVertexBufferObject() {
        generateID();
    }

    public FloatVertexBufferObject(FloatBuffer data) {
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
    public void setData(FloatBuffer data) {
        this.data = data;
        bindBuffer();
        loadData();
    }

    /**
	 * @return the data
	 */
    public FloatBuffer getData() {
        return data;
    }

    private void loadData() {
        ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, data, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
    }

    private void generateID() {
        IntBuffer id = BufferUtils.createIntBuffer(1);
        ARBVertexBufferObject.glGenBuffersARB(id);
        bufferID = id.get(0);
    }
}
