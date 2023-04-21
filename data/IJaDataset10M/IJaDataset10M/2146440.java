package org.acid3lib.spec;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Jascha Ulrich
 */
public class CountedInputStream extends FilterInputStream {

    private long count;

    private long limit;

    public CountedInputStream(InputStream in) {
        this(in, -1);
    }

    public CountedInputStream(InputStream in, int limit) {
        super(in);
        this.count = 0;
        this.limit = limit;
    }

    public long getCount() {
        return count;
    }

    public long getLimit() {
        return limit;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (count == limit) return -1;
        if (limit != -1 && count + len > limit) {
            len = (int) (limit - count);
        }
        int bytesRead = in.read(b, off, len);
        if (bytesRead != -1) {
            count += bytesRead;
        }
        return bytesRead;
    }

    public int read() throws IOException {
        if (count == limit) return -1;
        int i = in.read();
        if (i != -1) ++count;
        return i;
    }
}
