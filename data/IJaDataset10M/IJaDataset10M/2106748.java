package de.carne.nio;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * Utility class providing <code>Buffer</code> related functions.
 */
public final class Buffers {

    /**
	 * Limit a buffer.
	 * 
	 * @param buffer The buffer to limit.
	 * @param remaining The maximum remaining capacity the buffer shall provide.
	 * @return The previous buffer limit.
	 */
    public static final int limit(Buffer buffer, int remaining) {
        final int oldLimit = buffer.limit();
        final int newLimit = Math.min(buffer.position() + remaining, oldLimit);
        buffer.limit(newLimit);
        return oldLimit;
    }

    /**
	 * Select a portion of a buffer.
	 * 
	 * @param buffer The buffer to select from.
	 * @param position The position of the selected buffer.
	 * @param remaining The maximum remaining capacity the selected buffer shall provide.
	 * @return The selected buffer.
	 */
    public static final ByteBuffer select(ByteBuffer buffer, int position, int remaining) {
        final ByteBuffer selectBuffer = buffer.duplicate();
        selectBuffer.order(buffer.order());
        int selectLimit = selectBuffer.limit();
        final int selectPosition = Math.min(position, selectBuffer.limit());
        selectBuffer.position(selectPosition);
        selectLimit = Math.min(selectPosition + remaining, selectLimit);
        selectBuffer.limit(selectLimit);
        return selectBuffer;
    }

    /**
	 * Re-allocates a <code>ByteBuffer</code> to ensure a minimum capacity.
	 * 
	 * @param buffer The buffer to re-allocate.
	 * @param capacity The minimum capacity of the returned buffer.
	 * @return The re-allocated and cleared buffer.
	 */
    public static final ByteBuffer reallocate(ByteBuffer buffer, int capacity) {
        ByteBuffer newBuffer;
        if (buffer.capacity() < capacity) {
            newBuffer = ByteBuffer.allocate(capacity);
            newBuffer.order(buffer.order());
        } else {
            newBuffer = buffer;
            newBuffer.clear();
        }
        return newBuffer;
    }
}
