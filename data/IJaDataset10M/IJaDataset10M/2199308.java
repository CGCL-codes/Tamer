package com.sun.facelets;

import java.io.IOException;
import java.io.Writer;
import com.sun.facelets.util.FastWriter;

/**
 * A class for handling state insertion.  Content is written
 * directly to "out" until an attempt to write state;  at that
 * point, it's redirected into a buffer that can be picked through
 * in theory, this buffer should be very small, since it only
 * needs to be enough to contain all the content after the close
 * of the first (and, hopefully, only) form.
 * <p>
 * Potential optimizations:
 * <ul>
 * <li>If we created a new FastWriter at each call to writingState(),
 * and stored a List of them, then we'd know that state tokens could
 * only possibly be near the start of each buffer (and might not be there
 * at all).  (There might be a close-element before the state token). Then,
 * we'd only need to check the start of the buffer for the state token;
 * if it's there, write out the real state, then blast the rest of the
 * buffer out.  This wouldn't even require toString(), which for
 * large buffers is expensive.  However, this optimization is only
 * going to be especially meaningful for the multi-form case.
 * </li>
 * <li>More of a FastWriter optimization than a StateWriter, but:
 *  it is far faster to create a set of small 1K buffers than constantly
 *  reallocating one big buffer.</li>
 * </ul>
 *
 * @author Adam Winer
 * @version $Id: StateWriter.java,v 1.1 2006/04/12 05:50:45 adamwiner Exp $
 */
final class StateWriter extends Writer {

    private int initialSize;

    private Writer out;

    private FastWriter fast;

    private boolean writtenState;

    public static StateWriter getCurrentInstance() {
        return (StateWriter) CURRENT_WRITER.get();
    }

    public StateWriter(Writer initialOut, int initialSize) {
        if (initialSize < 0) {
            throw new IllegalArgumentException("Initial Size cannot be less than 0");
        }
        this.initialSize = initialSize;
        this.out = initialOut;
        CURRENT_WRITER.set(this);
    }

    /**
     * Mark that state is about to be written.  Contrary to what you'd expect,
     * we cannot and should not assume that this location is really going
     * to have state;  it is perfectly legit to have a ResponseWriter that
     * filters out content, and ignores an attempt to write out state
     * at this point.  So, we have to check after the fact to see
     * if there really are state markers.
     */
    public void writingState() {
        if (!this.writtenState) {
            this.writtenState = true;
            this.out = this.fast = new FastWriter(this.initialSize);
        }
    }

    public boolean isStateWritten() {
        return this.writtenState;
    }

    public void close() throws IOException {
    }

    public void flush() throws IOException {
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        this.out.write(cbuf, off, len);
    }

    public void write(char[] cbuf) throws IOException {
        this.out.write(cbuf);
    }

    public void write(int c) throws IOException {
        this.out.write(c);
    }

    public void write(String str, int off, int len) throws IOException {
        this.out.write(str, off, len);
    }

    public void write(String str) throws IOException {
        this.out.write(str);
    }

    public String getAndResetBuffer() {
        if (!this.writtenState) {
            throw new IllegalStateException("Did not write state;  no buffer is available");
        }
        String result = this.fast.toString();
        this.fast.reset();
        return result;
    }

    public void release() {
        CURRENT_WRITER.set(null);
    }

    private static final ThreadLocal CURRENT_WRITER = new ThreadLocal();
}
