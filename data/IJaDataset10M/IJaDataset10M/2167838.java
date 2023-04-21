package org.ivoa.util.text;

import java.io.IOException;
import java.io.Writer;

/**
 * Unsynchronized StringBuilder writer for performance A character stream that collects its output
 * in a string builder, which can then be used to construct a string.
 *
 * @author Laurent Bourges (voparis) / Gerard Lemson (mpe)
 */
public final class StringBuilderWriter extends Writer {

    /**
   * Internal StringBuilder to act as a writer
   */
    private final StringBuilder sb;

    /**
   * Create a new string writer
   * 
   * @param capacity buffer size
   */
    public StringBuilderWriter(final int capacity) {
        this.sb = new StringBuilder(capacity);
        this.lock = this.sb;
    }

    /**
   * Create a new string writer
   * 
   * @param buffer wrapped buffer
   */
    public StringBuilderWriter(final StringBuilder buffer) {
        this.sb = buffer;
        this.lock = buffer;
    }

    /**
   * Write a single character.
   *
   * @param c int specifying a character to be written.
   */
    @Override
    public void write(final int c) {
        this.sb.append((char) c);
    }

    /**
   * Write a portion of an array of characters.
   *
   * @param cbuf Array of characters
   * @param off Offset from which to start writing characters
   * @param len Number of characters to write
   * @throws IndexOutOfBoundsException if offset or length exceed the buffer
   */
    @Override
    public void write(final char[] cbuf, final int off, final int len) {
        if ((off < 0) || (off > cbuf.length) || (len < 0) || ((off + len) > cbuf.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        this.sb.append(cbuf, off, len);
    }

    /**
   * Write a string.
   *
   * @param str String to be written
   */
    @Override
    public void write(final String str) {
        this.sb.append(str);
    }

    /**
   * Write a portion of a string.
   *
   * @param str String to be written
   * @param off Offset from which to start writing characters
   * @param len Number of characters to write
   */
    @Override
    public void write(final String str, final int off, final int len) {
        this.sb.append(str.substring(off, off + len));
    }

    /**
   * Appends the specified character sequence to this writer.<p>An invocation of this method of the form
   * <tt>out.append(csq)</tt> behaves in exactly the same way as the invocation
   * <pre>
    out.write(csq.toString()) </pre></p>
   *  <p>Depending on the specification of <tt>toString</tt> for the character sequence <tt>csq</tt>, the entire
   * sequence may not be appended. For instance, invoking the <tt>toString</tt> method of a character buffer will
   * return a subsequence whose content depends upon the buffer's position and limit.</p>
   *
   * @param csq The character sequence to append.  If <tt>csq</tt> is <tt>null</tt>, then the four characters
   *        <tt>"null"</tt> are appended to this writer.
   *
   * @return This writer
   *
   * @since 1.5
   */
    @Override
    public Writer append(final CharSequence csq) {
        if (csq == null) {
            this.write("null");
        } else {
            this.write(csq.toString());
        }
        return this;
    }

    /**
   * Appends a subsequence of the specified character sequence to this writer.<p>An invocation of this method of
   * the form <tt>out.append(csq, start, end)</tt> when <tt>csq</tt> is not <tt>null</tt>, behaves in exactly the same
   * way as the invocation<pre>
    out.write(csq.subSequence(start, end).toString()) </pre></p>
   *
   * @param csq The character sequence from which a subsequence will be appended.  If <tt>csq</tt> is <tt>null</tt>,
   *        then characters will be appended as if <tt>csq</tt> contained the four characters <tt>"null"</tt>.
   * @param start The index of the first character in the subsequence
   * @param end The index of the character following the last character in the subsequence
   *
   * @return This writer
   *
   * @since 1.5
   */
    @Override
    public Writer append(final CharSequence csq, final int start, final int end) {
        final CharSequence cs = ((csq == null) ? "null" : csq);
        this.write(cs.subSequence(start, end).toString());
        return this;
    }

    /**
   * Appends the specified character to this writer.<p>An invocation of this method of the form
   * <tt>out.append(c)</tt> behaves in exactly the same way as the invocation<pre>
    out.write(c) </pre></p>
   *
   * @param c The 16-bit character to append
   *
   * @return This writer
   *
   * @since 1.5
   */
    @Override
    public Writer append(final char c) {
        this.write(c);
        return this;
    }

    /**
   * Return the buffer's current value as a string.
   *
   * @return buffer's current value as a string
   */
    @Override
    public String toString() {
        return this.sb.toString();
    }

    /**
   * Return the string buffer itself.
   *
   * @return StringBuffer holding the current buffer value.
   */
    public StringBuilder getBuffer() {
        return this.sb;
    }

    /**
   * Flush the stream.
   */
    @Override
    public void flush() {
    }

    /**
   * Closing a <tt>StringWriter</tt> has no effect. The methods in this class can be called after
   * the stream has been closed without generating an <tt>IOException</tt>.
   *
   * @throws IOException impossible
   */
    @Override
    public void close() throws IOException {
    }
}
