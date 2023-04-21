package maze.commons.adv_shared.io.impl;

import java.io.IOException;
import java.io.InputStream;
import maze.commons.adv_shared.io.wrapper.InputStreamSafeWrapper;

/**
 * @author Normunds Mazurs (MAZE)
 * 
 */
public class LimitedInputStream extends InputStreamSafeWrapper {

    private int readLimitCounter;

    public LimitedInputStream(final InputStream inputStreamToWrap, final int readLimit) {
        super(inputStreamToWrap);
        assert readLimit >= 0;
        this.readLimitCounter = readLimit;
    }

    protected int getReadLimitCounter() {
        return readLimitCounter;
    }

    protected void setReadLimitCounter(int readLimitCounter) {
        this.readLimitCounter = readLimitCounter;
    }

    protected long calcAvailable(final long len) {
        final long c = len > getReadLimitCounter() ? getReadLimitCounter() : len;
        assert c >= 0;
        assert c <= Integer.MAX_VALUE;
        return c;
    }

    /**
	 * Can be overrided.
	 */
    protected long reCalcLen(final long len) {
        return calcAvailable(len);
    }

    protected int countReaded(final int toCount) {
        if (toCount > 0) {
            readLimitCounter -= toCount;
        }
        return readLimitCounter;
    }

    protected boolean isReadLimitReached() {
        return getReadLimitCounter() < 1;
    }

    @Override
    public void mark(final int readlimit) {
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public synchronized int available() throws IOException {
        final int superAvailableVar = super.available();
        return (int) calcAvailable(superAvailableVar);
    }

    @Override
    public synchronized int read() throws IOException {
        if (isReadLimitReached()) {
            return -1;
        }
        final int superReaded = super.read();
        countReaded(superReaded == -1 ? 0 : 1);
        return superReaded;
    }

    @Override
    public synchronized int read(final byte[] b, final int off, final int len) throws IOException {
        if (isReadLimitReached()) {
            return -1;
        }
        final int newLen = (int) reCalcLen(len);
        final int superReaded = super.read(b, off, newLen);
        countReaded(superReaded);
        return superReaded;
    }

    @Override
    public synchronized int read(final byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public synchronized void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    @Override
    public synchronized long skip(final long n) throws IOException {
        if (n <= 0 || isReadLimitReached()) {
            return 0;
        }
        final long newN = reCalcLen(n);
        final long superSkipped = super.skip(newN);
        assert superSkipped <= Integer.MAX_VALUE;
        countReaded((int) superSkipped);
        return superSkipped;
    }
}
