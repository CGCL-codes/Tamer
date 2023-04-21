package net.sourceforge.myvd.protocol.ldap.mina.ldap.util;

import java.io.PipedInputStream;
import java.io.IOException;

/**
 * A piped input stream that fixes the "Read end Dead" issue when a single
 * thread is used.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev: 437007 $
 */
public class ParserPipedInputStream extends PipedInputStream {

    protected synchronized void receive(int b) throws IOException {
        while (in == out) {
            notifyAll();
            try {
                wait(1000);
            } catch (InterruptedException ex) {
                throw new java.io.InterruptedIOException();
            }
        }
        if (in < 0) {
            in = 0;
            out = 0;
        }
        buffer[in++] = (byte) (b & 0xFF);
        if (in >= buffer.length) {
            in = 0;
        }
    }
}
