package fr.x9c.cadmium.primitives.unix;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import fr.x9c.cadmium.kernel.Block;
import fr.x9c.cadmium.kernel.Channel;
import fr.x9c.cadmium.kernel.CodeRunner;
import fr.x9c.cadmium.kernel.Context;
import fr.x9c.cadmium.kernel.Fail;
import fr.x9c.cadmium.kernel.FalseExit;
import fr.x9c.cadmium.kernel.Primitive;
import fr.x9c.cadmium.kernel.PrimitiveProvider;
import fr.x9c.cadmium.kernel.Value;

/**
 * This class provides implementation for 'unix_pipe' primitive.
 *
 * @author <a href="mailto:cadmium@x9c.fr">Xavier Clerc</a>
 * @version 1.0
 * @since 1.0
 */
@PrimitiveProvider
public final class Pipe {

    /**
     * No instance of this class.
     */
    private Pipe() {
    }

    /**
     * Creates a pipe.
     * @param ctxt context
     * @param unit ignored
     * @return <i>(in_descriptor, out_descriptor)</i>
     * @throws Fail.Exception if pipe creation fails
     */
    @Primitive
    public static Value unix_pipe(final CodeRunner ctxt, final Value unit) throws Fail.Exception, FalseExit {
        try {
            final PipedInputStream readStream = new PipedInputStream();
            final PipedOutputStream writeStream = new PipedOutputStream(readStream);
            final Channel readChannel = new Channel(readStream);
            final Channel writeChannel = new Channel(writeStream);
            final Context c = ctxt.getContext();
            final Block res = Block.createBlock(0, Value.createFromLong(c.addChannel(readChannel)), Value.createFromLong(c.addChannel(writeChannel)));
            return Value.createFromBlock(res);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Unix.fail(ctxt, "pipe", ioe);
            return Value.UNIT;
        }
    }
}
