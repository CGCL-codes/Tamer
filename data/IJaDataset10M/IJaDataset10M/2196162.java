package net.sf.asyncobjects.io.adapters;

import java.io.InputStream;
import net.sf.asyncobjects.ACloseable;
import net.sf.asyncobjects.AsyncUnicastServer;
import net.sf.asyncobjects.Promise;
import net.sf.asyncobjects.io.AByteInput;
import net.sf.asyncobjects.io.BinaryData;
import net.sf.asyncobjects.util.Condition;
import java.util.logging.Logger;

/**
 * Adapter for input stream
 * 
 * @author const
 * 
 */
public class InputStreamAdapter extends AsyncUnicastServer<AByteInput> implements AByteInput {

    /** default size of the buffer */
    private static final int DEFAULT_BUFFER_SIZE = 4096;

    /** Logger. */
    private static final Logger log = Logger.getLogger(InputStreamAdapter.class.getName());

    /** an wrapped input stream */
    private final InputStream in;

    /** internal buffer */
    private final byte buffer[];

    /**
	 * A constructor
	 * 
	 * @param in
	 *            input stream to wrap
	 */
    public InputStreamAdapter(InputStream in) {
        this(in, DEFAULT_BUFFER_SIZE);
    }

    /**
	 * A constructor
	 * 
	 * @param in
	 *            input stream to wrap
	 * @param bufferSize
	 *            a buffer size
	 */
    public InputStreamAdapter(InputStream in, int bufferSize) {
        this.in = in;
        if (bufferSize < 1) {
            throw new IllegalArgumentException("Buffer size must be positive: " + bufferSize);
        }
        this.buffer = new byte[bufferSize];
    }

    /**
	 * read some bytes
	 * 
	 * @param limit
	 *            a maximum amount of data expected by client.
	 * @return Promise for read data or null (if eof)
	 * @see AByteInput#read(int)
	 */
    public Promise<BinaryData> read(int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("Amount of bytes to read must be positive: " + limit);
        }
        int toRead = Math.min(buffer.length, limit);
        if (log.isLoggable(java.util.logging.Level.FINE)) log.log(java.util.logging.Level.FINE, "starting reading " + toRead + " bytes.");
        int rc;
        try {
            rc = in.read(buffer, 0, toRead);
        } catch (Exception ex) {
            return Promise.smashed(ex);
        }
        if (log.isLoggable(java.util.logging.Level.FINE)) log.log(java.util.logging.Level.FINE, "read " + rc + " bytes.");
        if (rc > 0) {
            return new Promise<BinaryData>(BinaryData.fromBytes(buffer, 0, rc));
        } else {
            return null;
        }
    }

    /**
	 * close stream
	 * 
	 * @see ACloseable#close()
	 */
    public Promise<Void> close() {
        try {
            in.close();
            return new Promise<Void>(null);
        } catch (Exception ex) {
            return Promise.smashed(ex);
        }
    }

    /**
	 * @see net.sf.asyncobjects.io.AByteInput#isPushbackSupported()
	 */
    public Promise<Boolean> isPushbackSupported() {
        return Condition.falsePromise();
    }

    /**
	 * Pushback data
	 * 
	 * @param data
	 *            a data to push back
	 * @return when pushback finished
	 * @see net.sf.asyncobjects.io.AInput#pushback(net.sf.asyncobjects.io.BatchedData)
	 */
    public Promise<Void> pushback(BinaryData data) {
        throw new UnsupportedOperationException("pushback is unsupported");
    }
}
