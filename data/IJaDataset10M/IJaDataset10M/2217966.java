package org.arch.buffer;

/**
 * @author qiyingwang
 *
 */
public interface CodecObject {

    boolean encode(Buffer buffer);

    boolean decode(Buffer buffer);
}
