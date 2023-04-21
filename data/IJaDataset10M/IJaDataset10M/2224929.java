package org.ws4d.java.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.ws4d.java.util.Properties;

/**
 * Writes text to a buffered output stream. Holds chars in memory buffer until
 * the char count reaches the buffer size.
 */
public class BufferedOutputStreamWriter extends Writer {

    private static int bufferSize;

    private byte[] buffer;

    private int bufferPos;

    private OutputStream out;

    /**
	 * Initializes the BufferedOutputStreamWriter with an OutputStream, where
	 * the data is sent to, if the buffer is flushed or full.
	 * 
	 * @param os an OutputStream.
	 */
    public BufferedOutputStreamWriter(OutputStream os) {
        bufferSize = Properties.getInstance().getGlobalIntProperty(Properties.PROP_BUFFERED_WRITER_BUF_SIZE);
        buffer = new byte[bufferSize];
        this.out = os;
    }

    /**
	 * @see java.io.Writer#write(char[], int, int)
	 */
    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int i = off; i < (off + len); i++) {
            if (bufferPos == bufferSize) flush();
            buffer[bufferPos++] = (byte) cbuf[i];
        }
    }

    /**
	 * @see java.io.Writer#flush()
	 */
    public void flush() throws IOException {
        out.write(buffer, 0, bufferPos);
        bufferPos = 0;
    }

    /**
	 * @see java.io.Writer#close()
	 */
    public void close() throws IOException {
        flush();
        out.close();
    }
}
