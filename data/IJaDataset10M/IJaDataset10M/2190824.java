package org.exist.util.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of an Input Stream Filter that extends
 * any InputStream with mark() and reset() capabilities
 * by caching the read data for later re-reading.
 *
 * NOTE - Only supports reading data up to 2GB as the cache index uses an 'int' index
 *
 * @version 1.0
 *
 * @author Adam Retter <adam.retter@googlemail.com>
 */
public class CachingFilterInputStream extends FilterInputStream {

    private static final int END_OF_STREAM = -1;

    private static final String INPUTSTREAM_CLOSED = "The underlying InputStream has been closed";

    private final InputStream src;

    private final FilterInputStreamCache cache;

    private boolean srcClosed = false;

    private int srcOffset = 0;

    private int mark = 0;

    private boolean useCache = false;

    private int cacheOffset = 0;

    /**
     * @param cache The cache implementation
     * @param src The source InputStream to cache reads for
     */
    public CachingFilterInputStream(FilterInputStreamCache cache, InputStream src) {
        super(src);
        this.src = src;
        this.cache = cache;
    }

    /**
     * Constructor which uses an existing CachingFilterInputStream as its
     * underlying InputStream
     *
     * The position in the stream and any mark is reset to zero
     */
    public CachingFilterInputStream(CachingFilterInputStream cfis) {
        this(cfis.getCache(), cfis);
        this.srcClosed = cfis.srcClosed;
        this.srcOffset = 0;
        this.mark = 0;
        this.useCache = false;
        this.cacheOffset = 0;
    }

    /**
     * Gets the cache implementation
     */
    private FilterInputStreamCache getCache() {
        return cache;
    }

    @Override
    public int available() throws IOException {
        int available = 0;
        if (!srcClosed) {
            available = src.available();
            if (useCache && cacheOffset < srcOffset) {
                available += getCache().getLength() - cacheOffset;
            }
        }
        return available;
    }

    @Override
    public synchronized void mark(int readLimit) {
        mark = srcOffset;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public synchronized void reset() throws IOException {
        useCache = true;
        cacheOffset = mark;
    }

    @Override
    public int read() throws IOException {
        if (srcClosed) {
            throw new IOException(INPUTSTREAM_CLOSED);
        }
        if (useCache && cacheOffset < srcOffset) {
            int data = getCache().get(cacheOffset++);
            if (cacheOffset >= srcOffset) {
                useCache = false;
            }
            return data;
        } else {
            int data = src.read();
            if (data == END_OF_STREAM) {
                return END_OF_STREAM;
            }
            srcOffset++;
            getCache().write(data);
            return data;
        }
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (srcClosed) {
            throw new IOException(INPUTSTREAM_CLOSED);
        }
        if (useCache && cacheOffset < srcOffset) {
            int actualLen = (len > getCache().getLength() - cacheOffset ? getCache().getLength() - cacheOffset : len);
            getCache().copyTo(cacheOffset, b, off, actualLen);
            cacheOffset += actualLen;
            if (actualLen < len) {
                useCache = false;
                int srcLen = src.read(b, off + actualLen, len - actualLen);
                if (srcLen == END_OF_STREAM) {
                    return actualLen;
                }
                srcOffset += srcLen;
                getCache().write(b, off + actualLen, srcLen);
                actualLen += srcLen;
            }
            return actualLen;
        } else {
            int actualLen = src.read(b, off, len);
            if (actualLen == END_OF_STREAM) {
                return actualLen;
            }
            srcOffset += actualLen;
            getCache().write(b, off, actualLen);
            return actualLen;
        }
    }

    /**
     * Closes the src InputStream and empties the cache
     */
    @Override
    public void close() throws IOException {
        if (!srcClosed) {
            try {
                src.close();
            } finally {
                srcClosed = true;
            }
        }
        getCache().invalidate();
    }

    /**
     * We cant actually skip as we need to read so that we can cache the data,
     * however apart from the potentially increased I/O
     * and Memory, the end result is the same
     */
    @Override
    public long skip(long n) throws IOException {
        if (srcClosed) {
            throw new IOException(INPUTSTREAM_CLOSED);
        } else if (n < 1) {
            return 0;
        }
        if (useCache && cacheOffset < srcOffset) {
            long actualLen = (n > getCache().getLength() - cacheOffset ? getCache().getLength() - cacheOffset : n);
            cacheOffset += actualLen;
            if (actualLen < n) {
                useCache = false;
                byte skipped[] = new byte[(int) (n - actualLen)];
                int srcLen = src.read(skipped);
                if (srcLen == END_OF_STREAM) {
                    return actualLen;
                }
                srcOffset += srcLen;
                getCache().write(skipped, 0, srcLen);
                actualLen += srcLen;
            }
            return actualLen;
        } else {
            byte skipped[] = new byte[(int) n];
            int actualLen = src.read(skipped);
            srcOffset += actualLen;
            getCache().write(skipped, 0, actualLen);
            return actualLen;
        }
    }
}
