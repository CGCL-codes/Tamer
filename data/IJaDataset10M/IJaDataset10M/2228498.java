package net.sf.asyncobjects.io;

import net.sf.asyncobjects.AsyncUnicastServer;
import net.sf.asyncobjects.Promise;
import net.sf.asyncobjects.When;
import net.sf.asyncobjects.util.Condition;
import net.sf.asyncobjects.util.RequestQueue;
import net.sf.asyncobjects.util.Serialized;

/**
 * <p>
 * A buffered input stream. Note that it reads at most {@link #toReadLimit}
 * bytes from underlying stream even if client asked for more.
 * <p>
 * 
 * <p>
 * The stream also supports prefetch mode (enabled by default). In this mode,
 * when the buffer dries out, the stream initate a read request to the
 * underlying stream. Therefore it is possible that buffer will replenish while
 * client processes the data.
 * </p>
 * 
 * @param <D>
 *            a batched data type
 * @param <I>
 *            an input type
 * @author const
 */
public class BufferedInput<D extends BatchedData<D>, I extends AInput<D>> extends AsyncUnicastServer<I> implements AInput<D> {

    /** buffered data */
    private D data;

    /** maximum amount of bites to be attempte to read from undelying stream */
    private final int toReadLimit;

    /** poxied stream */
    private final I proxiedStream;

    /** pending requests */
    private final RequestQueue requests = new RequestQueue();

    /**
	 * a problem, once set, it is returned for all read requests until close
	 * request
	 */
    private Throwable problem;

    /** true if the stream is closed */
    private boolean isClosed;

    /**
	 * true if the streams prefetches the next porition of the data when buffer
	 * is emptied
	 */
    private boolean prefetch;

    /**
	 * This variable stores a replenish promise while replenish is in the
	 * progress
	 */
    private Promise<D> prefetchPromise;

    /** if true eof has been seen */
    private boolean eofSeen;

    /**
	 * A constructor from the stream
	 * 
	 * @param proxiedStream
	 *            a stream that is being proxied
	 * @param limit
	 *            a maximum amount of bites to be attempte to read from
	 *            undelying stream
	 * @param prefetch
	 *            if true the data is fetched when it is all read out before the
	 *            next read operation false if data is fetched only during read
	 *            requests.
	 */
    public BufferedInput(I proxiedStream, int limit, boolean prefetch) {
        this.toReadLimit = limit;
        this.proxiedStream = proxiedStream;
        this.prefetch = prefetch;
    }

    /**
	 * A constructor from the stream
	 * 
	 * @param proxiedStream
	 *            a stream that is being proxied
	 * @param limit
	 *            a maximum amount of bites to be attempte to read from
	 *            undelying stream
	 */
    public BufferedInput(I proxiedStream, int limit) {
        this(proxiedStream, limit, true);
    }

    /**
	 * read some bytes in buffer
	 * 
	 * @param limit
	 *            maximum amount of bytes that will be read by this operation
	 * @return read data or promise for it
	 * @see AByteInput#read(int)
	 */
    public Promise<D> read(final int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("read limit is too small " + limit);
        }
        return new Serialized<D>(requests) {

            @Override
            protected Promise<D> run() throws Throwable {
                if (problem != null) {
                    throw problem;
                }
                ensureOpen();
                if (data == null) {
                    if (eofSeen) {
                        return null;
                    } else {
                        return bufferedRead(limit);
                    }
                } else {
                    return returnData(limit);
                }
            }
        }.promise();
    }

    /**
	 * This method is invoked when data is available to return result of read
	 * operation
	 * 
	 * @param limit
	 *            a requested limit
	 * @return a data
	 */
    private Promise<D> returnData(final int limit) {
        if (data.length() <= limit) {
            D tmp = data;
            data = null;
            fetchData(true);
            return Promise.<D>with(tmp);
        } else {
            D rc = data.head(limit);
            data = data.drop(limit);
            return new Promise<D>(rc);
        }
    }

    /**
	 * A buffered read
	 * 
	 * @param limit
	 *            a limit
	 * @return a read data
	 */
    Promise<D> bufferedRead(final int limit) {
        final Promise<D> fetchData = fetchData(false);
        if (fetchData.isResolved() && fetchData.value() != null) {
            prefetchPromise = null;
            data = fetchData.value();
            return returnData(limit);
        }
        return new When<D, D>(fetchData) {

            @Override
            public Promise<D> resolved(D d) throws Throwable {
                data = d;
                if (d == null) {
                    eofSeen = true;
                    return null;
                }
                return returnData(limit);
            }

            @Override
            public Promise<D> smashed(Throwable t) throws Throwable {
                problem = t;
                throw t;
            }

            @Override
            protected Promise<?> finallyDo() throws Throwable {
                prefetchPromise = null;
                return null;
            }
        }.promise();
    }

    /**
	 * Fetch data
	 * 
	 * @param prefetchMode
	 *            if prefetch has been requested.
	 * @return a promise for fetch data process
	 */
    Promise<D> fetchData(boolean prefetchMode) {
        if (prefetchPromise != null) {
            return prefetchPromise;
        } else if (!prefetchMode || prefetch) {
            prefetchPromise = proxiedStream.read(toReadLimit);
            return prefetchPromise;
        } else {
            return null;
        }
    }

    /**
	 * Push data back to the stream
	 * 
	 * @param pushbackData
	 *            a data that is being pushed back
	 * 
	 * @return a promise that resolves to null or breaks with exception.
	 * @see AInput#pushback(BatchedData)
	 */
    public Promise<Void> pushback(final D pushbackData) {
        return new Serialized<Void>(requests) {

            @Override
            protected Promise<Void> run() throws Throwable {
                ensureOpen();
                if (data == null) {
                    data = pushbackData;
                } else {
                    data = pushbackData.concat(data);
                }
                return null;
            }
        }.promise();
    }

    /**
	 * close stream
	 * 
	 * @return a promise that resolves to null or breaks with exception.
	 */
    public Promise<Void> close() {
        return new Serialized<Void>(requests) {

            @Override
            protected Promise<Void> run() throws Throwable {
                ensureOpen();
                isClosed = true;
                return proxiedStream.close();
            }
        }.promise();
    }

    /**
	 * Ensure that stream is open
	 */
    private void ensureOpen() {
        if (isClosed) {
            throw new IllegalStateException("the stream is already closed");
        }
    }

    /**
	 * @see net.sf.asyncobjects.io.AInput#isPushbackSupported()
	 */
    public Promise<Boolean> isPushbackSupported() {
        return Condition.truePromise();
    }
}
