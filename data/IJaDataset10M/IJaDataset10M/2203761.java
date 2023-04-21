package sto.orz.webapp.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * Wraps Response Stream for GZipFilter
 *
 * @author  Matt Raible
 * @version $Revision: 1.3 $ $Date: 2004-05-15 20:18:10 -0600 (Sat, 15 May 2004) $
 */
public class GZIPResponseStream extends ServletOutputStream {

    protected OutputStream bufferedOutput = null;

    protected boolean closed = false;

    protected HttpServletResponse response = null;

    protected ServletOutputStream output = null;

    private int bufferSize = 50000;

    public GZIPResponseStream(HttpServletResponse response) throws IOException {
        super();
        closed = false;
        this.response = response;
        this.output = response.getOutputStream();
        bufferedOutput = new ByteArrayOutputStream();
    }

    public void close() throws IOException {
        if (closed) {
            throw new IOException("This output stream has already been closed");
        }
        if (bufferedOutput instanceof ByteArrayOutputStream) {
            ByteArrayOutputStream baos = (ByteArrayOutputStream) bufferedOutput;
            ByteArrayOutputStream compressedContent = new ByteArrayOutputStream();
            GZIPOutputStream gzipstream = new GZIPOutputStream(compressedContent);
            byte[] bytes = baos.toByteArray();
            gzipstream.write(bytes);
            gzipstream.finish();
            byte[] compressedBytes = compressedContent.toByteArray();
            response.setContentLength(compressedBytes.length);
            response.addHeader("Content-Encoding", "gzip");
            output.write(compressedBytes);
            output.flush();
            output.close();
            closed = true;
        } else if (bufferedOutput instanceof GZIPOutputStream) {
            GZIPOutputStream gzipstream = (GZIPOutputStream) bufferedOutput;
            gzipstream.finish();
            output.flush();
            output.close();
            closed = true;
        }
    }

    public void flush() throws IOException {
        if (closed) {
            throw new IOException("Cannot flush a closed output stream");
        }
        bufferedOutput.flush();
    }

    public void write(int b) throws IOException {
        if (closed) {
            throw new IOException("Cannot write to a closed output stream");
        }
        checkBufferSize(1);
        bufferedOutput.write((byte) b);
    }

    private void checkBufferSize(int length) throws IOException {
        if (bufferedOutput instanceof ByteArrayOutputStream) {
            ByteArrayOutputStream baos = (ByteArrayOutputStream) bufferedOutput;
            if ((baos.size() + length) > bufferSize) {
                response.addHeader("Content-Encoding", "gzip");
                byte[] bytes = baos.toByteArray();
                GZIPOutputStream gzipstream = new GZIPOutputStream(output);
                gzipstream.write(bytes);
                bufferedOutput = gzipstream;
            }
        }
    }

    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (closed) {
            throw new IOException("Cannot write to a closed output stream");
        }
        checkBufferSize(len);
        bufferedOutput.write(b, off, len);
    }

    public boolean closed() {
        return (this.closed);
    }

    public void reset() {
    }
}
