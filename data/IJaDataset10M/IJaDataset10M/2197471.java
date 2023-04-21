package rat.header;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

class FilteringSequenceFactory {

    private static final int BUFFER_CAPACITY = 5000;

    private final CharBuffer buffer;

    private final CharFilter filter;

    public FilteringSequenceFactory(final CharFilter filter) {
        this(BUFFER_CAPACITY, filter);
    }

    public FilteringSequenceFactory(final int capacity, final CharFilter filter) {
        this.buffer = CharBuffer.allocate(capacity);
        this.filter = filter;
    }

    public CharSequence filter(Reader reader) throws IOException {
        buffer.clear();
        boolean eof = false;
        while (!eof) {
            final int next = reader.read();
            if (next == -1 || !buffer.hasRemaining()) {
                eof = true;
            } else {
                final char character = (char) next;
                if (!filter.isFilteredOut(character)) {
                    buffer.put(character);
                }
            }
        }
        buffer.limit(buffer.position()).rewind();
        return buffer;
    }
}
