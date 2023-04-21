package org.simpleframework.http.core;

import java.io.IOException;
import org.simpleframework.util.buffer.Allocator;
import org.simpleframework.util.buffer.Buffer;

/**
 * The <code>BoundaryConsumer</code> is used to consume a boundary
 * for a multipart message. This ensures that the boundary complies
 * with the multipart specification in that it ends with a carrige
 * return and line feed. This consumer implementation can be used
 * mutiple times as its internal buffer can be cleared and reset. 
 *
 * @author Niall Gallagher
 */
class BoundaryConsumer extends ArrayConsumer {

    /**
    * This is the terminal token for a multipart boundary entity.
    */
    private static final byte[] LAST = { '-', '-', '\r', '\n' };

    /**
    * This is the terminal token for a multipart boundary line.
    */
    private static final byte[] LINE = { '\r', '\n' };

    /**
    * This represents the start of the boundary line for the part.
    */
    private static final byte[] TOKEN = { '-', '-' };

    /**
    * This is used to allocate a buffer for for the boundary. 
    */
    private Allocator allocator;

    /**
    * This is used to consume the contents of the consumed buffer.
    */
    private Buffer buffer;

    /**
    * This is the actual boundary value that is to be consumed.
    */
    private byte[] boundary;

    /**
    * This counts the number of characters read from the start.
    */
    private int start;

    /**
    * This is the number of characters read from the terminal token.
    */
    private int last;

    /**
    * This is the number of characters read from the line token.
    */
    private int line;

    /**
    * This is the array seek offset used by this consumer.
    */
    private int seek;

    /**
    * This is the boundary seek offset used to compare values.
    */
    private int pos;

    /**
    * Constructor for the <code>BoundaryConsumer</code> object. This 
    * is used to create a boundary consumer for validating boundaries
    * and consuming them from a provided source. This is used to help
    * in reading multipart messages by removing boundaries from the
    * stream.
    *
    * @param boundary this is the boundary value to be consumed
    */
    public BoundaryConsumer(Allocator allocator, byte[] boundary) {
        this.allocator = allocator;
        this.boundary = boundary;
    }

    /**
    * This does not perform any processing after the boundary has 
    * been consumed. Because the boundary consumer is used only as a
    * means to remove the boundary from the underlying stream there
    * is no need to perform any processing of the value consumed.
    */
    @Override
    protected void process() throws Exception {
        int length = boundary.length + 6;
        if (buffer == null) {
            buffer = allocator.allocate(length);
        }
        buffer.append(TOKEN);
        buffer.append(boundary);
        if (last == 4) {
            buffer.append(TOKEN);
        }
        buffer.append(LINE);
    }

    /**
    * This method is used to scan for the terminal token. It searches
    * for the token and returns the number of bytes in the buffer 
    * after the terminal token. Returning the excess bytes allows the
    * consumer to reset the bytes within the consumer object.
    *
    * @return this returns the number of excess bytes consumed
    */
    @Override
    protected int scan() throws IOException {
        if (start != 2) {
            start();
        }
        if (start == 2) {
            boundary();
        }
        if (pos == boundary.length) {
            return terminal();
        }
        return 0;
    }

    /**
    * This method is used to consume the start of the boundary from 
    * the consumed bytes. This will ensure that the first characters
    * read from the boundary are two <code>-</code> characters. If
    * the characters do not match an exception is thrown.
    */
    private void start() throws IOException {
        if (start < 2) {
            while (seek < count) {
                if (array[seek++] == '-') {
                    if (++start == 2) {
                        break;
                    }
                } else {
                    throw new IOException("Invalid boundary start");
                }
            }
        }
    }

    /**
    * This method is used to consume the boundary token from the 
    * array of bytes. This will attempt to match each character from
    * the boundary with those read from the underlying source. If 
    * the characters do not match then an exception is thrown. When
    * all cha
    */
    private void boundary() throws IOException {
        if (pos < boundary.length) {
            while (seek < count) {
                if (array[seek++] != boundary[pos++]) {
                    throw new IOException("Invalid boundary value");
                }
                if (pos == boundary.length) {
                    break;
                }
            }
        }
    }

    /**
    * This method is used to read the terminal character from the end
    * of the multipart message. The terminal character for the whole
    * multipart message is two <code>-</code> characters followed by 
    * a carrage return and line feed.
    *
    * @return number of excess bytes in the buffer after the terminal
    */
    private int terminal() throws IOException {
        while (seek < count) {
            if (array[seek] == LAST[last]) {
                if (++last == 4) {
                    done = true;
                    return count - ++seek;
                }
            } else {
                if (array[seek] == LINE[line]) {
                    if (++line == 2) {
                        done = true;
                        return count - ++seek;
                    }
                } else {
                    throw new IOException("Invalid boundary ending");
                }
            }
            seek++;
        }
        return 0;
    }

    /**
    * This is used to determine whether the boundary has been read
    * from the underlying stream. This is true only when the very
    * last boundary has been read. This will be the boundary value
    * that ends with the two <code>-</code> characters.
    *
    * @return this returns true with the terminal boundary is read
    */
    public boolean isEnd() {
        return last == 4;
    }

    /**
    * This is used to clear the state of the of boundary consumer
    * such that it can be reused. This is required as the multipart
    * body may contain many parts, all delimited with the same 
    * boundary. Clearing allows the next boundary to be consumed.
    */
    public void clear() {
        done = false;
        count = seek = 0;
        last = start = 0;
        line = pos = 0;
    }
}
