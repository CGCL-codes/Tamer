package net.face2face.util.net;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Patrice
 */
public class ThroughputOutputStream extends OutputStream {

    private OutputStream innerOutputStream;

    private ThroughputListener listener;

    /** Creates a new instance of TroughputOutputStream */
    public ThroughputOutputStream(OutputStream out) {
        innerOutputStream = out;
    }

    public void setThroughputListener(ThroughputListener listener) {
        this.listener = listener;
    }

    private void addByteCount(int byteCount) {
        if (listener != null) listener.notifyData(byteCount);
    }

    public void write(int b) throws IOException {
        addByteCount(1);
        innerOutputStream.write(b);
    }

    public void write(byte b[]) throws IOException {
        addByteCount(b.length);
        innerOutputStream.write(b);
    }

    public void write(byte b[], int off, int len) throws IOException {
        addByteCount(len);
        innerOutputStream.write(b, off, len);
    }

    public void flush() throws IOException {
        innerOutputStream.flush();
    }

    public void close() throws IOException {
        innerOutputStream.close();
    }
}
