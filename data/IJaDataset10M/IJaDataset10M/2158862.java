package net.sf.opentranquera.pagespy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 */
public class PageSpyServletOutputStream extends ServletOutputStream {

    private ByteArrayOutputStream baos;

    private HttpServletResponse response;

    public PageSpyServletOutputStream(HttpServletResponse response) {
        this.response = response;
        this.baos = new ByteArrayOutputStream();
    }

    /**
     * Converts and returns the buffer's contents as a string
     *
     * @return the buffer's contents as a string
     */
    public ByteArrayOutputStream getByteArrayOutputStream() {
        return baos;
    }

    /**
     * Returns the underlying byte array as a String
     *
     * @return the underlying byte array as a String
     */
    public String toString() {
        return baos.toString();
    }

    /**
     * Writes the specified byte to the byte array output stream
     *
     * @param b the byte to be written.
     * @throws IOException
     */
    public void write(int b) throws IOException {
        this.baos.write(b);
        this.response.getOutputStream().write(b);
    }

    public void reset() {
        baos.reset();
    }

    public int getSize() {
        return baos.size();
    }

    public void setSize(int size) throws IOException {
        ByteArrayOutputStream oldOs = baos;
        baos = new ByteArrayOutputStream(size);
        baos.write(oldOs.toByteArray());
    }
}
