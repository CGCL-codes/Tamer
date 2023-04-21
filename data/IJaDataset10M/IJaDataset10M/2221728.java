package jaco.mp3.resources;

import java.io.IOException;
import java.io.InputStream;

/**
 * <i>Work In Progress.</i>
 * 
 * An instance of <code>InputStreamSource</code> implements a
 * <code>Source</code> that provides data from an <code>InputStream
 * </code>. Seeking functionality is not supported. 
 * 
 * @author MDM
 */
public class InputStreamSource implements Source {

    private final InputStream in;

    public InputStreamSource(InputStream in) {
        if (in == null) throw new NullPointerException("in");
        this.in = in;
    }

    public int read(byte[] b, int offs, int len) throws IOException {
        int read = in.read(b, offs, len);
        return read;
    }

    public boolean willReadBlock() {
        return true;
    }

    public boolean isSeekable() {
        return false;
    }

    public long tell() {
        return -1;
    }

    public long seek(long to) {
        return -1;
    }

    public long length() {
        return -1;
    }
}
