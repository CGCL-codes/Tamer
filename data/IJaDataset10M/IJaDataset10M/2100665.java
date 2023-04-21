package org.orion.engine;

import java.nio.Buffer;
import javax.media.opengl.GL;
import com.sun.opengl.util.BufferUtil;

public class VertexBuffer extends Mesh {

    private int id;

    public VertexBuffer(int type, Buffer data) {
        super(type, data);
        int[] ids = new int[1];
        GLStatic.gl.glGenBuffers(1, ids, 0);
        this.id = ids[0];
        glBind();
        GLStatic.gl.glBufferData(GL.GL_ARRAY_BUFFER, data.capacity() * BufferUtil.SIZEOF_FLOAT, data, GL.GL_STATIC_DRAW);
        int[] size = new int[1];
        GLStatic.gl.glGetBufferParameteriv(GL.GL_ELEMENT_ARRAY_BUFFER, GL.GL_BUFFER_SIZE, size, 0);
        if (size[0] > 0) {
            System.out.println("Create vertex buffer size=" + size[0]);
        }
        VertexBuffer.glUnBind();
    }

    public void glBind() {
        GLStatic.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id);
    }

    public static void glUnBind() {
        GLStatic.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    }

    public void draw() {
        glPushModelView();
        glModelView();
        if (data != null) {
            GLStatic.gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
            glBind();
            GLStatic.gl.glInterleavedArrays(type, 0, 0);
            GLStatic.gl.glDrawArrays(mode, 0, count);
            VertexBuffer.glUnBind();
            GLStatic.gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
        }
        glPopModelView();
    }
}
