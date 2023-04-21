package net.sf.asyncobjects.io;

import net.sf.asyncobjects.ACloseable;
import net.sf.asyncobjects.AsyncUnicastServer;
import net.sf.asyncobjects.Promise;
import net.sf.asyncobjects.util.RequestQueue;
import net.sf.asyncobjects.util.Serialized;

/**
 * Buffered output. The output waits util data will surpass specified limit, and
 * only after it it writes bulk of the data. The write to underlying stream
 * could be forced with {@link AOutput#flush()}
 * @param <D>
 *            a batched data type
 * @param <O>
 *            an output type
 * @author const           
 */
public class BufferedOutput<D extends BatchedData<D>, O extends AOutput<D>> extends AsyncUnicastServer<O> implements AOutput<D> {

    /** request queue actually list of runnables */
    private final RequestQueue requests = new RequestQueue();

    /** a buffered data */
    private D bufferedData = null;

    /** a limit, after limit is surpassed, data is sent to underlying stream */
    private final int limit;

    /** output stream */
    private final O output;

    /** flag that indicates that stream is closed */
    private boolean isClosed = false;

    /**
	 * A constructor from stream and buffer size
	 * 
	 * @param output
	 *            an underlying output stream
	 * @param limit
	 *            a limit, after limit is surpassed, data is sent to underlying
	 *            stream
	 */
    public BufferedOutput(O output, int limit) {
        if (limit < 1) throw new IllegalArgumentException("invalid buffer size (it should be positive): " + limit);
        this.limit = limit;
        this.output = output;
    }

    /**
	 * write data
	 * 
	 * @param data
	 *            data to write out
	 * @return promise that resolves when buffer finished to be written out
	 * @see AOutput#write(BatchedData)
	 */
    public Promise<Void> write(final D data) {
        return new Serialized<Void>(requests) {

            @Override
            protected Promise<Void> run() {
                ensureOpen();
                if (bufferedData == null) {
                    bufferedData = data;
                } else {
                    bufferedData = bufferedData.concat(data);
                }
                if (bufferedData.length() > limit) {
                    D temp = bufferedData;
                    bufferedData = null;
                    return output.write(temp);
                } else {
                    return null;
                }
            }
        }.promise();
    }

    /**
	 * Ensure that the stream is still open
	 */
    private void ensureOpen() {
        if (isClosed) {
            throw new IllegalStateException("stream is closed");
        }
    }

    /**
	 * 
	 * @return a promise that resolves when flush finishes
	 * @see AByteOutput#flush()
	 */
    public Promise<Void> flush() {
        return new Serialized<Void>(requests) {

            @Override
            protected Promise<Void> run() {
                ensureOpen();
                if (bufferedData != null) {
                    output.write(bufferedData);
                    bufferedData = null;
                }
                return output.flush();
            }
        }.promise();
    }

    /**
	 * register close request and possibly execute it.
	 * 
	 * @return a promise that resolves when stream is closed.
	 * @see ACloseable#close()
	 */
    public Promise<Void> close() {
        return new Serialized<Void>(requests) {

            @Override
            protected Promise<Void> run() {
                ensureOpen();
                if (bufferedData != null) {
                    output.write(bufferedData);
                    bufferedData = null;
                }
                isClosed = true;
                return output.close();
            }
        }.promise();
    }
}
