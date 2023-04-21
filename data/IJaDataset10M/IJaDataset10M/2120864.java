package drawer;

import com.sun.opengl.util.BufferUtil;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

class DynamicDefaultVertexSet extends DynamicVertexSet {

    DynamicDefaultVertexSet(float[] array, int mode) {
        this.mode = mode;
        this.buffer = BufferUtil.newFloatBuffer(array.length);
        this.buffer.put(array);
        this.buffer.position(0);
    }

    DynamicDefaultVertexSet(FloatBuffer floatBuffer, int mode) {
        this.mode = mode;
        this.buffer = BufferUtil.copyFloatBuffer(floatBuffer);
        this.buffer.position(0);
    }

    DynamicDefaultVertexSet(IVertexSet vertexSet, int mode) {
        this(vertexSet.getBuffer(), mode);
    }

    public void draw() {
        final GL gl = GLU.getCurrentGL();
        buffer.position(0);
        gl.glBegin(mode);
        for (int i = 0; i < buffer.capacity(); i += VertexSet.primitiveCount) {
            gl.glTexCoord2f(buffer.get(), buffer.get());
            gl.glVertex3f(buffer.get(), buffer.get(), buffer.get());
        }
        gl.glEnd();
        buffer.position(0);
    }

    public void draw(int start, int count) {
        final GL gl = GLU.getCurrentGL();
        buffer.position(VertexSet.primitiveCount * start);
        gl.glBegin(mode);
        for (int i = 0; i < VertexSet.primitiveCount * count && i < buffer.capacity(); i += VertexSet.primitiveCount) {
            gl.glTexCoord2f(buffer.get(), buffer.get());
            gl.glVertex3f(buffer.get(), buffer.get(), buffer.get());
        }
        gl.glEnd();
        buffer.position(0);
    }

    public void multiDraw(FloatBuffer translation, FloatBuffer rotation, int limit, boolean relative) {
        final GL gl = GLU.getCurrentGL();
        translation.position(0);
        rotation.position(0);
        if (relative) gl.glPushMatrix();
        for (int i = 0; i < limit; i++) {
            if (!relative) gl.glPushMatrix();
            gl.glTranslatef(translation.get(), translation.get(), translation.get());
            gl.glRotatef(rotation.get(), rotation.get(), rotation.get(), rotation.get());
            this.draw();
            if (!relative) gl.glPopMatrix();
        }
        if (relative) gl.glPopMatrix();
        rotation.position(0);
        translation.position(0);
    }

    public void multiDraw(FloatBuffer matrix, int limit, boolean relative) {
        final GL gl = GLU.getCurrentGL();
        float[] m = new float[16];
        matrix.position(0);
        if (relative) gl.glPushMatrix();
        for (int i = 0; i < limit; i++) {
            if (!relative) gl.glPushMatrix();
            matrix.get(m, 0, 16);
            gl.glMultMatrixf(m, 0);
            this.draw();
            if (!relative) gl.glPopMatrix();
        }
        if (relative) gl.glPopMatrix();
        matrix.position(0);
    }

    public void drawByPiece(IntBuffer first, IntBuffer count, int limit) {
        final GL gl = GLU.getCurrentGL();
        first.position(0);
        count.position(0);
        buffer.position(0);
        gl.glBegin(mode);
        for (int i = 0; i < limit; i++) {
            buffer.position(VertexSet.primitiveCount * first.get());
            for (i = 0; i < VertexSet.primitiveCount * count.get() && i < buffer.capacity(); i += VertexSet.primitiveCount) {
                gl.glTexCoord2f(buffer.get(), buffer.get());
                gl.glVertex3f(buffer.get(), buffer.get(), buffer.get());
            }
            buffer.position(0);
        }
        gl.glEnd();
        first.position(0);
        count.position(0);
        buffer.position(0);
    }

    public void multiDraw(FloatBuffer translation, FloatBuffer rotation, IntBuffer first, IntBuffer count, int limit, boolean relative) {
        final GL gl = GLU.getCurrentGL();
        translation.position(0);
        rotation.position(0);
        first.position(0);
        count.position(0);
        if (relative) gl.glPushMatrix();
        for (int i = 0; i < limit; i++) {
            if (!relative) gl.glPushMatrix();
            gl.glTranslatef(translation.get(), translation.get(), translation.get());
            gl.glRotatef(rotation.get(), rotation.get(), rotation.get(), rotation.get());
            this.draw(first.get(), count.get());
            if (!relative) gl.glPopMatrix();
        }
        if (relative) gl.glPopMatrix();
        first.position(0);
        count.position(0);
        rotation.position(0);
        translation.position(0);
    }
}
