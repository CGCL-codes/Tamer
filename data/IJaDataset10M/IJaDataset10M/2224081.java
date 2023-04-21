package ideas.streams.transf;

import java.io.InputStream;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Zbynek Slajchrt
 * @since Aug 6, 2009 10:15:44 PM
 */
public class PipeInputProcessorImpl implements PipeInputProcessor {

    private final ArrayBlockingQueue<InputStream> queue = new ArrayBlockingQueue<InputStream>(1);

    private final InternalIS stream = new InternalIS();

    public void processInput(InputStream input) throws IOException {
        queue.add(input);
    }

    public InputStream getInput() {
        return stream;
    }

    private class InternalIS extends InputStream {

        private InputStream deleg;

        private InputStream getDelegate() {
            if (deleg == null) {
                try {
                    deleg = queue.take();
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
            return deleg;
        }

        public int read(byte b[]) throws IOException {
            return getDelegate().read(b);
        }

        public int read(byte b[], int off, int len) throws IOException {
            return getDelegate().read(b, off, len);
        }

        public long skip(long n) throws IOException {
            return getDelegate().skip(n);
        }

        public int available() throws IOException {
            return getDelegate().available();
        }

        public void close() throws IOException {
            getDelegate().close();
        }

        public synchronized void mark(int readlimit) {
            getDelegate().mark(readlimit);
        }

        public synchronized void reset() throws IOException {
            getDelegate().reset();
        }

        public boolean markSupported() {
            return getDelegate().markSupported();
        }

        public int read() throws IOException {
            return getDelegate().read();
        }
    }
}
