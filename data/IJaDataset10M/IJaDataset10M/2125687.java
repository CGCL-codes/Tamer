package Utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet output stream for compressing data using the GZIP algorithm.
 *
 * @see GzipCompressionWrapper
 *
 * @author Angel Sanadinov
 */
public class GzipHttpServletOutputStream extends ServletOutputStream {

    private GZIPOutputStream compressedOutputStream;

    private ByteArrayOutputStream compressedDataBuffer;

    private HttpServletResponse rawResponse;

    private boolean isClosed;

    /**
     * Constructs the compression stream using the specified data.
     *
     * @param response the raw servlet response
     * @throws IOException if an I/O error occurs
     */
    public GzipHttpServletOutputStream(HttpServletResponse response) throws IOException {
        this.rawResponse = response;
        this.compressedDataBuffer = new ByteArrayOutputStream();
        this.compressedOutputStream = new GZIPOutputStream(compressedDataBuffer);
        this.isClosed = false;
    }

    @Override
    public void write(int b) throws IOException {
        if (!isClosed) compressedOutputStream.write(b); else throw new IOException("Attempting to write to a closed stream");
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (!isClosed) compressedOutputStream.write(b, off, len); else throw new IOException("Attempting to write to a closed stream");
    }

    @Override
    public void flush() throws IOException {
        if (!isClosed) compressedOutputStream.flush(); else throw new IOException("Attempting to flush a closed stream");
    }

    /**
     * Finalises the compression of the data sent through the stream, sends the
     * response headers and commits the response. <br><br>
     *
     * All underlying streams are closed. 
     * After the stream is closed, it can no longer be used for sending data.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        if (!isClosed) {
            compressedOutputStream.finish();
            byte[] compressedData = compressedDataBuffer.toByteArray();
            ServletOutputStream rawOutputStream = rawResponse.getOutputStream();
            rawResponse.setHeader("Content-Length", Integer.toString(compressedData.length));
            rawResponse.setHeader("Content-Encoding", "gzip");
            rawOutputStream.write(compressedData);
            rawOutputStream.flush();
            compressedOutputStream.close();
            rawOutputStream.close();
            compressedOutputStream = null;
            compressedDataBuffer = null;
            rawResponse = null;
            isClosed = true;
        } else throw new IOException("Attempting to close a closed stream");
    }
}
