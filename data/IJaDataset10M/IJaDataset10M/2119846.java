package jvs.vfs.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Unix /dev/null
 * 
 * @author qiangli
 *
 */
public class NullOutputStream extends OutputStream {

    /**
	 * 
	 */
    public NullOutputStream() {
    }

    public void write(int b) throws IOException {
    }

    public void close() throws IOException {
    }

    public void flush() throws IOException {
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (b == null) throw new NullPointerException();
        if (off < 0 || len < 0 || off + len > b.length) throw new IndexOutOfBoundsException();
    }

    public void write(byte[] b) throws IOException {
        if (b == null) throw new NullPointerException();
    }
}
