package connection;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import main.IDynamicVertexSet;

class DynamicVertexSetConnector implements IDynamicVertexSet {

    private drawer.IDynamicVertexSet delegate;

    DynamicVertexSetConnector(drawer.IDynamicVertexSet delegate) {
        this.delegate = delegate;
    }

    public void draw() {
        delegate.draw();
    }

    public FloatBuffer getBuffer() {
        return (delegate.getBuffer());
    }

    public void draw(int start, int count) {
        delegate.draw(start, count);
    }

    public void multiDraw(FloatBuffer translation, FloatBuffer rotation, int limit, boolean relative) {
        delegate.multiDraw(translation, rotation, limit, relative);
    }

    public void multiDraw(FloatBuffer matrix, int limit, boolean relative) {
        delegate.multiDraw(matrix, limit, relative);
    }

    public void drawByPiece(IntBuffer first, IntBuffer count, int limit) {
        delegate.drawByPiece(first, count, limit);
    }

    public void multiDraw(FloatBuffer translation, FloatBuffer rotation, IntBuffer first, IntBuffer count, int limit, boolean relative) {
        delegate.multiDraw(translation, rotation, first, count, limit, relative);
    }

    public float get(int index) {
        return (delegate.get(index));
    }

    public float[] get() {
        return (delegate.get());
    }

    public void put(int index, float value) {
        delegate.put(index, value);
    }

    public void set(float[] value) {
        delegate.set(value);
    }

    public void set(FloatBuffer value) {
        delegate.set(value);
    }

    public void put(float[] value, int offset, int length) {
        delegate.put(value, offset, length);
    }

    public void put(FloatBuffer value, int offset, int length) {
        delegate.put(value, offset, length);
    }
}
