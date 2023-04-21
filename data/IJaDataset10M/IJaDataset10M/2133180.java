package jacky.lanlan.song.io.stream;

import java.io.*;

/**
 * This class is equivalent to <code>java.io.PipedInputStream</code>. In the interface it only adds
 * a constructor which allows for specifying the buffer size. Its implementation, however, is much
 * simpler and a lot more efficient than its equivalent. It doesn't rely on polling. Instead it uses
 * proper synchronization with its counterpart <code>be.re.io.PipedOutputStream</code>.
 * 
 * Multiple readers can read from this stream concurrently. The block asked for by a reader is
 * delivered completely, or until the end of the stream if less is available. Other readers can't
 * come in between.
 * 
 * @author WD
 * @link http://developer.java.sun.com/developer/bugParade/bugs/4404700.html
 * @see FastPipedOutputStream
 */
public class FastPipedInputStream extends InputStream {

    byte[] buffer;

    boolean closed = false;

    int readLaps = 0;

    int readPosition = 0;

    FastPipedOutputStream source;

    int writeLaps = 0;

    int writePosition = 0;

    /**
	 * Creates an unconnected PipedInputStream with a default buffer size.
	 */
    FastPipedInputStream() {
        super();
        this.buffer = new byte[0x10000];
    }

    /**
	 * Creates a PipedInputStream with a default buffer size and connects it to <code>source</code>.
	 * 
	 * @exception IOException
	 *              It was already connected.
	 */
    public FastPipedInputStream(FastPipedOutputStream source) throws IOException {
        this(source, 0x10000);
    }

    /**
	 * Creates a PipedInputStream with buffer size <code>bufferSize</code> and connects it to
	 * <code>source</code>.
	 * 
	 * @exception IOException
	 *              It was already connected.
	 */
    public FastPipedInputStream(FastPipedOutputStream source, int bufferSize) throws IOException {
        super();
        if (source != null) {
            connect(source);
        }
        this.buffer = new byte[bufferSize];
    }

    @Override
    public int available() throws IOException {
        return writePosition > readPosition ? writePosition - readPosition : (writePosition < readPosition ? buffer.length - readPosition + 1 + writePosition : (writeLaps > readLaps ? buffer.length : 0));
    }

    /**
	 * @exception IOException
	 *              The pipe is not connected.
	 */
    @Override
    public void close() throws IOException {
        if (source == null) {
            throw new IOException("Unconnected pipe");
        }
        synchronized (buffer) {
            closed = true;
            buffer.notifyAll();
        }
    }

    /**
	 * @exception IOException
	 *              The pipe is already connected.
	 */
    public void connect(FastPipedOutputStream source) throws IOException {
        if (this.source != null) {
            throw new IOException("Pipe already connected");
        }
        this.source = source;
        source.sink = this;
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }

    @Override
    public void mark(int readLimit) {
        return;
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    public int read() throws IOException {
        byte[] b = new byte[1];
        return read(b, 0, b.length) == -1 ? -1 : (255 & b[0]);
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    /**
	 * @exception IOException
	 *              The pipe is not connected.
	 */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (source == null) {
            throw new IOException("Unconnected pipe");
        }
        synchronized (buffer) {
            if (writePosition == readPosition && writeLaps == readLaps) {
                if (closed) {
                    return -1;
                }
                try {
                    buffer.wait();
                } catch (InterruptedException e) {
                    throw new IOException(e.getMessage());
                }
                return read(b, off, len);
            }
            int amount = Math.min(len, (writePosition > readPosition ? writePosition : buffer.length) - readPosition);
            System.arraycopy(buffer, readPosition, b, off, amount);
            readPosition += amount;
            if (readPosition == buffer.length) {
                readPosition = 0;
                ++readLaps;
            }
            if (amount < len) {
                int second = read(b, off + amount, len - amount);
                return second == -1 ? amount : amount + second;
            }
            buffer.notifyAll();
            return amount;
        }
    }
}
