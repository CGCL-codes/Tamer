package net.sf.asyncobjects.io;

import net.sf.asyncobjects.AResolver;
import net.sf.asyncobjects.Promise;
import net.sf.asyncobjects.When;
import java.util.logging.Logger;

/**
 * This component just reads data from one stream and writes it to another. This
 * class is too expensive to provide it in non asynchronous envronment as it
 * requires thread in synchronous case, but for asynchronous API, it is very
 * cheap.
 * 
 * @author const
 * @deprecated {@link IOUtils#forward(AInput, AOutput, long, int)}
 */
@Deprecated
public class ByteStreamForwarder {

    /** Logger. */
    private static final Logger log = Logger.getLogger(ByteStreamForwarder.class.getName());

    /**
	 * if this flag is true, there is a flush operation after each write
	 * operation.
	 */
    private boolean autoFlush;

    /** input stream */
    private final AByteInput input;

    /** output stream */
    private final AByteOutput output;

    /** buffer */
    private final int bufferSize;

    /** resolver of promise that is indication of end or start of reading */
    private AResolver<Void> resolver;

    /**
	 * a consturtor
	 * 
	 * @param in
	 *            input stream
	 * @param out
	 *            output stream
	 * @param bufferSize
	 *            buffer size
	 * @param autoFlush
	 *            if true output will be flushed after each write
	 */
    public ByteStreamForwarder(AByteInput in, AByteOutput out, int bufferSize, boolean autoFlush) {
        this.bufferSize = bufferSize;
        this.autoFlush = autoFlush;
        input = in;
        output = out;
    }

    /**
	 * start reading from one stream and writing to another. Note that CoPipe
	 * never flushed output stream and also does not close them.
	 * 
	 * @return Promise that resolves to null if stream is read until end of
	 *         stream or problem if there are problems with readin or writing.
	 */
    public Promise<Void> start() {
        Promise<Void> p = new Promise<Void>();
        resolver = p.resolver();
        continuePiping();
        if (log.isLoggable(java.util.logging.Level.FINE)) log.log(java.util.logging.Level.FINE, "starting... " + ByteStreamForwarder.this);
        return p;
    }

    /**
	 * continue reading, this method reads from input stream then writes to
	 * output stream.
	 */
    private void continuePiping() {
        if (log.isLoggable(java.util.logging.Level.FINE)) {
            log.log(java.util.logging.Level.FINE, "reading... " + ByteStreamForwarder.this);
        }
        new When<BinaryData, Void>(input.read(bufferSize)) {

            @Override
            public Promise<Void> resolved(BinaryData v) {
                BinaryData data = v;
                if (null != data) {
                    if (log.isLoggable(java.util.logging.Level.FINE)) {
                        log.log(java.util.logging.Level.FINE, "writing... " + ByteStreamForwarder.this);
                    }
                    new When<Void, Void>(output.write(data)) {

                        @Override
                        public Promise<Void> resolved(Void v) {
                            continuePiping();
                            return null;
                        }

                        @Override
                        public Promise<Void> smashed(Throwable t) {
                            resolver.smash(t);
                            if (log.isLoggable(java.util.logging.Level.FINE)) {
                                log.log(java.util.logging.Level.FINE, "finshing with error " + ByteStreamForwarder.this, t);
                            }
                            return null;
                        }
                    };
                    if (autoFlush) output.flush();
                } else {
                    if (autoFlush) {
                        new When<Void, Void>(output.flush()) {

                            @Override
                            public Promise<Void> resolved(Void v) {
                                if (log.isLoggable(java.util.logging.Level.FINE)) {
                                    log.log(java.util.logging.Level.FINE, "finshing " + ByteStreamForwarder.this);
                                }
                                resolver.resolve(null);
                                return null;
                            }

                            @Override
                            public Promise<Void> smashed(Throwable t) {
                                if (log.isLoggable(java.util.logging.Level.FINE)) {
                                    log.log(java.util.logging.Level.SEVERE, "finshing with error " + ByteStreamForwarder.this, t);
                                }
                                resolver.smash(t);
                                return null;
                            }
                        };
                    } else {
                        if (log.isLoggable(java.util.logging.Level.FINE)) {
                            log.log(java.util.logging.Level.FINE, "finshing " + ByteStreamForwarder.this);
                        }
                        resolver.resolve(null);
                    }
                }
                return null;
            }

            @Override
            public Promise<Void> smashed(Throwable t) {
                if (log.isLoggable(java.util.logging.Level.FINE)) {
                    log.log(java.util.logging.Level.SEVERE, "finshing with error " + ByteStreamForwarder.this, t);
                }
                resolver.smash(t);
                return null;
            }
        };
    }
}
