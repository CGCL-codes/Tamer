package org.apache.cocoon.bean.helpers;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 *   A output stream writing to a ByteArrayOutputStream, until an OutputStream target is defined.
 *
 * @author    huber@apache.org
 * @author    uv@upaya.co.uk
 * @version CVS $Id: DelayedOutputStream.java,v 1.2 2005/02/15 13:06:59 simijhag Exp $
 */
public class DelayedOutputStream extends OutputStream {

    /**
     * write to baos as long as fos member is still null,
     * create a ByteArrayOutputStream only
     */
    private ByteArrayOutputStream baos;

    /**
     * If fos is defined write into fos, dump content of
     * baos to fos first
     */
    private OutputStream fos;

    /**
     * Constructor for the DelayedFileOutputStream object,
     * create a ByteArrayOutputStream only
     */
    public DelayedOutputStream() {
        baos = new ByteArrayOutputStream();
        fos = null;
    }

    /**
     * Creates a file output stream to write to the file represented by the specified File object.
     *
     * @param  outputStream               The new fileOutputStream value
     * @exception  FileNotFoundException  thrown if creating of FileOutputStream fails
     */
    public void setFileOutputStream(OutputStream outputStream) throws FileNotFoundException {
        if (fos == null) {
            fos = outputStream;
        }
    }

    /**
     *   Write into ByteArrayOutputStrem, or FileOutputStream, depending on inner
     *   state of this stream
     *
     * @param  b                Description of Parameter
     * @exception  IOException  thrown iff implicitly flush of baos to fos fails, or writing
     *   of baos, or fos fails
     */
    public void write(int b) throws IOException {
        OutputStream os = getTargetOutputStream();
        os.write(b);
    }

    /**
     *   Write into ByteArrayOutputStrem, or FileOutputStream, depending on inner
     *   state of this stream
     *
     * @param  b                Description of Parameter
     * @exception  IOException  thrown iff implicitly flush of baos to fos fails, or writing
     *   of baos, or fos fails
     */
    public void write(byte[] b) throws IOException {
        OutputStream os = getTargetOutputStream();
        os.write(b);
    }

    /**
     *   Write into ByteArrayOutputStrem, or FileOutputStream, depending on inner
     *   state of this stream
     *
     * @param  b                Description of Parameter
     * @param  off              Description of Parameter
     * @param  len              Description of Parameter
     * @exception  IOException  thrown iff implicitly flush of baos to fos fails, or writing
     *   of baos, or fos fails
     */
    public void write(byte[] b, int off, int len) throws IOException {
        OutputStream os = getTargetOutputStream();
        os.write(b, off, len);
    }

    /**
     *   Close ByteArrayOutputStrem, and FileOutputStream, depending on inner
     *   state of this stream
     *
     * @exception  IOException  thrown iff implicitly flush of baos to fos fails, or closing
     *   of baos, or fos fails
     */
    public void close() throws IOException {
        IOException ioexception = null;
        getTargetOutputStream();
        try {
            if (baos != null) {
                baos.close();
            }
        } catch (IOException ioe) {
            ioexception = ioe;
        } finally {
            baos = null;
        }
        try {
            if (fos != null) {
                fos.close();
            }
        } catch (IOException ioe) {
            if (ioexception == null) {
                ioexception = ioe;
            }
        } finally {
            fos = null;
        }
        if (ioexception != null) {
            throw ioexception;
        }
    }

    /**
     *   Flush ByteArrayOutputStrem, writing content to FileOutputStream,
     *   flush FileOutputStream
     *
     * @exception  IOException  thrown iff implicitly flush of baos to fos fails, or flushing
     *   of baos, or fos fails
     */
    public void flush() throws IOException {
        IOException ioexception = null;
        getTargetOutputStream();
        try {
            if (baos != null) {
                baos.flush();
            }
        } catch (IOException ioe) {
            ioexception = ioe;
        }
        try {
            if (fos != null) {
                fos.flush();
            }
        } catch (IOException ioe) {
            if (ioexception == null) {
                ioexception = ioe;
            }
        }
        if (ioexception != null) {
            throw ioexception;
        }
    }

    /**
     *   Gets the targetOutputStream attribute of the DelayedFileOutputStream object
     *
     * @return                  The targetOutputStream value
     * @exception  IOException  thrown iff implicitly flush of baos to fos fails
     */
    private OutputStream getTargetOutputStream() throws IOException {
        if ((baos != null) && (fos == null)) {
            return baos;
        } else if ((baos != null) && (fos != null)) {
            try {
                baos.flush();
                baos.writeTo(fos);
                baos.close();
            } finally {
                baos = null;
            }
            return fos;
        } else if ((baos == null) && (fos != null)) {
            return fos;
        } else {
            throw new IOException("No outputstream available!");
        }
    }

    /**
     * Gets the size of the content of the current output stream
     */
    public int size() {
        if (baos != null) {
            return baos.size();
        }
        return 0;
    }

    /**
     * Return the contents of the stream as a byte array
     */
    public byte[] getContent() {
        if (baos != null) {
            return baos.toByteArray();
        } else {
            return null;
        }
    }
}
