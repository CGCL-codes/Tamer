package javax.media.ding3d.utils.scenegraph.io.retained;

import java.io.DataOutputStream;
import java.io.IOException;

class PositionOutputStream extends java.io.OutputStream {

    private long pos = 0;

    private java.io.OutputStream stream;

    public PositionOutputStream(java.io.OutputStream stream) {
        this.stream = stream;
    }

    public void write(int p1) throws IOException {
        pos++;
        stream.write(p1);
    }

    public void write(byte[] b) throws IOException {
        pos += b.length;
        stream.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        pos += len;
        stream.write(b, off, len);
    }

    /**
     * Move the file pointer to the specified position.
     * The position MUST be greater or equal to the current position
     */
    public void seekForward(long position) throws IOException {
        if (pos > position) throw new SGIORuntimeException("Seeking Backward " + pos + "  " + position); else for (int i = 0; i < (int) (position - pos); i++) stream.write(0);
        pos = position;
    }

    public long getFilePointer() {
        return pos;
    }
}
