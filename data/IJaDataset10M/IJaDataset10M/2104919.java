package ch.unifr.nio.framework.transform;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A forwarder that forwards an array of ByteBuffers as a sequence of
 * ByteBuffers.
 *
 * @author Ronny Standtke <Ronny.Standtke@gmx.net>
 */
public class ByteBufferArraySequenceForwarder extends AbstractForwarder<ByteBuffer[], ByteBuffer> {

    private static final Logger LOGGER = Logger.getLogger(ByteBufferArraySequenceForwarder.class.getName());

    @Override
    public synchronized void forward(ByteBuffer[] inputs) throws IOException {
        if (nextForwarder == null) {
            LOGGER.log(Level.SEVERE, "no nextForwarder => data lost!");
        } else {
            for (ByteBuffer input : inputs) {
                nextForwarder.forward(input);
            }
        }
    }
}
