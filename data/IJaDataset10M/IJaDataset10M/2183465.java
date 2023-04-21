package org.mortbay.io;

/** BufferSource.
 * Represents a pool or other source of buffers and abstracts the creation
 * of specific types of buffers (eg NIO).   The concept of big and little buffers
 * is supported, but these terms have no absolute meaning and must be determined by context.
 * 
 * @author gregw
 *
 */
public interface Buffers {

    public Buffer getBuffer(int size);

    public void returnBuffer(Buffer buffer);
}
