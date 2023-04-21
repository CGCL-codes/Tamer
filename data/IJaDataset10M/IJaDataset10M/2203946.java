package drawer;

import com.sun.opengl.util.BufferUtil;
import java.nio.FloatBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

class DisplayList extends StaticVertexSet {

    private int id;

    DisplayList(float[] array, int mode) throws RuntimeException {
        this.mode = mode;
        this.id = 0;
        this.buffer = BufferUtil.newFloatBuffer(array.length);
        this.buffer.put(array);
        this.buffer.position(0);
        this.createDisplayList();
    }

    DisplayList(FloatBuffer floatBuffer, int mode) throws RuntimeException {
        this.mode = mode;
        this.id = 0;
        this.buffer = BufferUtil.copyFloatBuffer(floatBuffer);
        this.buffer.position(0);
        this.createDisplayList();
    }

    DisplayList(IVertexSet vertexSet, int mode) throws RuntimeException {
        this(vertexSet.getBuffer(), mode);
    }

    private void createDisplayList() throws RuntimeException {
        final GL gl = GLU.getCurrentGL();
        if ((id = gl.glGenLists(1)) == 0) throw new RuntimeException("unable to create a display list");
        gl.glNewList(id, GL.GL_COMPILE);
        gl.glBegin(mode);
        for (int i = 0; i < buffer.capacity(); i += VertexSet.primitiveCount) {
            gl.glTexCoord2f(buffer.get(), buffer.get());
            gl.glVertex3f(buffer.get(), buffer.get(), buffer.get());
        }
        gl.glEnd();
        buffer.position(0);
        gl.glEndList();
        buffer.position(0);
    }

    public void setMode(int mode) {
        if (this.mode != mode) {
            this.mode = mode;
            final GL gl = GLU.getCurrentGL();
            if (id != 0) gl.glDeleteLists(id, 1);
            createDisplayList();
        }
    }

    public void draw() {
        final GL gl = GLU.getCurrentGL();
        gl.glCallList(id);
    }

    protected void finalize() {
        final GL gl = GLU.getCurrentGL();
        if (id > 0) gl.glDeleteLists(id, 1);
    }
}
