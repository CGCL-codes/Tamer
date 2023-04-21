package com.volantis.mcs.protocols;

import java.io.IOException;
import java.io.Writer;

/**
 * OuputBufferWriter
 * A writer that writes to output buffers and knows about element
 * capabilities with regard to whether mixed output is allowed, whether
 * the element is a block element and whether the element has pre-formatted
 * whitespace.
 *
 * If the writer is attached to an OutputBuffer, the element infomation is
 * sent to the buffer along with any text that is written to the writer.
 *
 * If the writer is attached to another writer, and that writer is also an
 * OutputBufferWriter, then the element information is sent to that writer
 * along with any text that is written.
 *
 * If the writer is attached to another Writer, but that Writer is not an
 * OutputBufferWriter then any text is sent through to the decorated writer
 * but any element settings are ignored.
 *
 * @author steve
 */
public class OutputBufferWriter extends Writer {

    /**
     * The outputbuffer to write to if connected
     */
    private OutputBuffer outputBuffer;

    /**
     * The writer to erite to if we are not connected to an output buffer
     */
    private Writer out;

    private OutputBufferWriter decorated;

    /**
     * Create an OutputBufferWriter that, by default, has nothing to write to
     */
    public OutputBufferWriter() {
        out = null;
        decorated = null;
        outputBuffer = null;
    }

    /**
     * Create a writer to an output buffer
     *
     * @param buffer the buffer to write to
     */
    public OutputBufferWriter(OutputBuffer buffer) {
        setOutputBuffer(buffer);
    }

    /**
     * Create a writer to another writer
     *
     * @param writer the writer to pass output on to
     */
    public OutputBufferWriter(Writer writer) {
        setWriter(writer);
    }

    /**
     * Set the output buffer to write to
     *
     * @param buffer the buffer to write to
     */
    protected void setOutputBuffer(OutputBuffer buffer) {
        outputBuffer = buffer;
        out = null;
        decorated = null;
    }

    /**
     * Set a writer to pass output to if we have no buffer
     *
     * @param writer the writer to pass on to
     */
    private void setWriter(Writer writer) {
        out = writer;
        outputBuffer = null;
        decorated = null;
        if (writer instanceof OutputBufferWriter) {
            decorated = (OutputBufferWriter) writer;
        }
    }

    /**
     * Returns the output buffer we are writing to or null if
     * there isnt one
     *
     * @return the current output buffer
     */
    protected OutputBuffer getOutputBuffer() {
        return outputBuffer;
    }

    /**
     * Returns the writer we are passing output to or null if there isnt one
     *
     * @return the output writer
     */
    protected Writer getWriter() {
        return out;
    }

    /**
     * If we are connected to an output buffer, tell that buffer that the
     * element doing the writing can have mixed content. If we are connected to
     * another OutputBufferWriter then pass the call on to that writer
     *
     * @param b whether or not mixed content is allowed
     */
    public void setElementHasMixedContent(boolean b) {
        if (outputBuffer != null) {
            outputBuffer.setElementHasMixedContent(b);
        } else {
            if (decorated != null) {
                decorated.setElementHasMixedContent(b);
            }
        }
    }

    /**
     * If we are writing to an output buffer, tell the output buffer whether
     * or not the element doing the writing is a block element. If we are
     * connected to another OutputBufferWriter then pass the call on to that writer
     *
     * @param b true if the element is a block element, false otherwise
     */
    public void setElementIsBlock(boolean b) {
        if (outputBuffer != null) {
            outputBuffer.setElementIsBlock(b);
        } else {
            if (decorated != null) {
                decorated.setElementIsBlock(b);
            }
        }
    }

    /**
     * If we are writing to an output buffer, tell the output buffer whether
     * or not the element doing the writing is pre-formatted, in which case no
     * whitespace processing will occur within that element. If we are
     * connected to another OutputBufferWriter then pass the call on to that writer
     *
     * @param b true if the element is pre-formatted, false otherwise
     */
    public void setElementIsPreFormatted(boolean b) {
        if (outputBuffer != null) {
            outputBuffer.setElementIsPreFormatted(b);
        } else {
            if (decorated != null) {
                decorated.setElementIsPreFormatted(b);
            }
        }
    }

    /**
     * If we are connected to an output buffer, write an array of characters
     * to the buffer. If we are not connected to an output buffer, then we must
     * be connected to an underlying writer so the characters are passed on.
     *
     * @param cbuf a character array holding the characters to output
     * @param off  the index of the first character to write
     * @param len  the number of characters to write starting with off
     * @throws IOException if an error occurs while writing
     */
    public void write(char[] cbuf, int off, int len) throws IOException {
        write(cbuf, off, len, false);
    }

    protected void write(char[] cbuf, int off, int len, boolean preEncoded) throws IOException {
        if (outputBuffer != null) {
            outputBuffer.writeText(cbuf, off, len, preEncoded);
        } else if (out != null) {
            out.write(cbuf, off, len);
        } else {
            throw new IOException("Not connected to buffer or writer.");
        }
    }

    protected void write(String str, boolean preEncoded) throws IOException {
        if (outputBuffer != null) {
            outputBuffer.writeText(str, preEncoded);
        } else if (out != null) {
            out.write(str);
        } else {
            throw new IOException("Not connected to buffer or writer.");
        }
    }

    /**
     * If we are connected to an output buffer, write a String to the buffer.
     * If we are not connected to an output buffer, then we must
     * be connected to an underlying writer so the String is passed on.
     *
     * @param str the String to write
     * @throws IOException if an error occurs while writing
     */
    public void write(String str) throws IOException {
        write(str, false);
    }

    /**
     * If we are connected to a writer, this method flushes the output on that
     * writer. If we are connected to an output buffer, then this method does nothing
     */
    public void flush() throws IOException {
        if (out != null) {
            out.flush();
        }
    }

    /**
     * If we are connected to a writer, this method closes that writer.
     * If we are connected to an output buffer, then this method does nothing
     */
    public void close() throws IOException {
        if (out != null) {
            out.close();
        }
    }
}
