package org.apache.commons.net.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/***
 * This class wraps an output stream, replacing all occurrences
 * of &lt;CR&gt;&lt;LF&gt; (carriage return followed by a linefeed),
 * which is the NETASCII standard for representing a newline, with the
 * local line separator representation.  You would use this class to
 * implement ASCII file transfers requiring conversion from NETASCII.
 * <p>
 * Because of the translation process, a call to <code>flush()</code> will
 * not flush the last byte written if that byte was a carriage
 * return.  A call to {@link #close  close() }, however, will
 * flush the carriage return.
 * <p>
 * <p>
 * @author Daniel F. Savarese
 ***/
public final class FromNetASCIIOutputStream extends FilterOutputStream {

    private boolean __lastWasCR;

    /***
     * Creates a FromNetASCIIOutputStream instance that wraps an existing
     * OutputStream.
     * <p>
     * @param output  The OutputStream to wrap.
     ***/
    public FromNetASCIIOutputStream(OutputStream output) {
        super(output);
        __lastWasCR = false;
    }

    private void __write(int ch) throws IOException {
        switch(ch) {
            case '\r':
                __lastWasCR = true;
                break;
            case '\n':
                if (__lastWasCR) {
                    out.write(FromNetASCIIInputStream._lineSeparatorBytes);
                    __lastWasCR = false;
                    break;
                }
                __lastWasCR = false;
                out.write('\n');
                break;
            default:
                if (__lastWasCR) {
                    out.write('\r');
                    __lastWasCR = false;
                }
                out.write(ch);
                break;
        }
    }

    /***
     * Writes a byte to the stream.    Note that a call to this method
     * might not actually write a byte to the underlying stream until a
     * subsequent character is written, from which it can be determined if
     * a NETASCII line separator was encountered.
     * This is transparent to the programmer and is only mentioned for
     * completeness.
     * <p>
     * @param ch The byte to write.
     * @exception IOException If an error occurs while writing to the underlying
     *            stream.
     ***/
    @Override
    public synchronized void write(int ch) throws IOException {
        if (FromNetASCIIInputStream._noConversionRequired) {
            out.write(ch);
            return;
        }
        __write(ch);
    }

    /***
     * Writes a byte array to the stream.
     * <p>
     * @param buffer  The byte array to write.
     * @exception IOException If an error occurs while writing to the underlying
     *            stream.
     ***/
    @Override
    public synchronized void write(byte buffer[]) throws IOException {
        write(buffer, 0, buffer.length);
    }

    /***
     * Writes a number of bytes from a byte array to the stream starting from
     * a given offset.
     * <p>
     * @param buffer  The byte array to write.
     * @param offset  The offset into the array at which to start copying data.
     * @param length  The number of bytes to write.
     * @exception IOException If an error occurs while writing to the underlying
     *            stream.
     ***/
    @Override
    public synchronized void write(byte buffer[], int offset, int length) throws IOException {
        if (FromNetASCIIInputStream._noConversionRequired) {
            out.write(buffer, offset, length);
            return;
        }
        while (length-- > 0) __write(buffer[offset++]);
    }

    /***
     * Closes the stream, writing all pending data.
     * <p>
     * @exception IOException  If an error occurs while closing the stream.
     ***/
    @Override
    public synchronized void close() throws IOException {
        if (FromNetASCIIInputStream._noConversionRequired) {
            super.close();
            return;
        }
        if (__lastWasCR) out.write('\r');
        super.close();
    }
}
