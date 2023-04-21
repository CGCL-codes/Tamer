package com.healthmarketscience.rmiio;

import java.io.IOException;
import java.nio.ByteBuffer;
import com.healthmarketscience.rmiio.util.SingleByteAdapter;
import com.healthmarketscience.rmiio.util.PipeBuffer;

/**
 * InputStream which facilitates generating a stream of data on demand through
 * some programmatic action.  Subclasses must implement the {@link #encode}
 * method, which should write some reasonable amount of data to the
 * OutputStream returned from {@link #createOutputStream} (which in turn will
 * forward to data to the consumer of the InputStream).  The OutputStream
 * linked to this class must <b>only</b> be written during a call to
 * <code>encode</code>.
 * <p>
 * Additionally, this class provides a "packet" based read method, which
 * returns the underlying byte[]'s (which will be approximately the size of
 * the chunk size configured for this class), which may be more efficient in
 * some applications.
 * <p>
 * Note, this class has no synchronization except that the <code>close</code>
 * method supports asynchronous closing.
 * 
 * @author James Ahlborn
 */
public abstract class EncodingInputStream extends PacketInputStream {

    /** initial size of the overflow buffer used when moving data from the
      internal _localOStream to the _localIStream. */
    public static final int DEFAULT_CHUNK_SIZE = 1024;

    /** when we are doing packet based reading, we don't have a _curBuf, so use
      this "full" buffer instead. */
    private static final ByteBuffer DUMMY_FULL_BUFFER = ByteBuffer.wrap(EMPTY_PACKET);

    /** buffer for single byte read calls */
    private final SingleByteAdapter _singleByteAdapter = new SingleByteAdapter();

    /** wrapped byte array which was passed into a read() method. */
    private ByteBuffer _curBuf;

    /** overflow buffer containing any data which was generated during a
      writeNextObject() call which did not fit into the _curBuf. */
    private final PipeBuffer _overflowBuf;

    /** <code>true</code> iff we have no more objects left in the iteration
      (indicated by a <code>false</code> return value from a call to
      writeNextObject()). */
    private boolean _gotEOF = false;

    /** <code>true</code> iff this stream has been closed.  This is volatile so
      that we can support asynchronous closing. */
    private volatile boolean _closed = false;

    protected EncodingInputStream() {
        this(DEFAULT_CHUNK_SIZE);
    }

    protected EncodingInputStream(int chunkSize) {
        this(chunkSize, false);
    }

    protected EncodingInputStream(int chunkSize, boolean noDelay) {
        super(chunkSize, noDelay);
        _overflowBuf = new PipeBuffer(getPacketSize());
    }

    /**
   * Creates an OutputStream linked to this InputStream which can be used to
   * write data as requested.  This method creates a new instance, and
   * therefore should only be called once and the result should be cached by
   * the caller.
   */
    protected PacketOutputStream createOutputStream() {
        return new OutputStreamAdapter();
    }

    @Override
    public void close() throws IOException {
        _closed = true;
    }

    /**
   * Throws an IOException if the stream is closed, otherwise, does nothing.
   */
    protected void throwIfClosed() throws IOException {
        if (_closed) {
            throw new IOException("stream closed");
        }
    }

    @Override
    public int available() throws IOException {
        throwIfClosed();
        refillPacket(true);
        return (int) _overflowBuf.remaining();
    }

    @Override
    public int read() throws IOException {
        throwIfClosed();
        return _singleByteAdapter.read(this);
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] buf, int pos, int len) throws IOException {
        throwIfClosed();
        int numBytesRead = 0;
        if (_overflowBuf.hasRemaining()) {
            int numBytes = Math.min((int) _overflowBuf.remaining(), len);
            _overflowBuf.read(buf, pos, numBytes);
            numBytesRead += numBytes;
            pos += numBytes;
            len -= numBytes;
        }
        if (len > 0) {
            _curBuf = ByteBuffer.wrap(buf, pos, len);
            while (_curBuf.hasRemaining() && !_gotEOF) {
                encode(_curBuf.remaining());
            }
            numBytesRead += (len - _curBuf.remaining());
            _curBuf = null;
            if (numBytesRead == 0) {
                numBytesRead = -1;
            }
        }
        return numBytesRead;
    }

    @Override
    public long skip(long len) throws IOException {
        throwIfClosed();
        if (len <= 0) {
            return 0;
        }
        long numBytesSkipped = 0;
        if (_overflowBuf.hasRemaining()) {
            long numBytes = Math.min(_overflowBuf.remaining(), len);
            _overflowBuf.skip(numBytes);
            numBytesSkipped += numBytes;
            len -= numBytes;
        }
        while ((len > 0) && !_gotEOF) {
            long numBytes = encodeSkip(len);
            numBytesSkipped += numBytes;
            len -= numBytes;
        }
        return numBytesSkipped;
    }

    private void refillPacket(boolean readPartial) throws IOException {
        if (!_gotEOF && (_overflowBuf.packetsAvailable() == 0)) {
            _curBuf = DUMMY_FULL_BUFFER;
            do {
                encode(getPacketSize());
            } while (!_gotEOF && ((!readPartial) ? (_overflowBuf.packetsAvailable() == 0) : (_overflowBuf.remaining() == 0)));
            _curBuf = null;
        }
    }

    @Override
    public byte[] readPacket(boolean readPartial) throws IOException {
        throwIfClosed();
        refillPacket(readPartial);
        byte[] packet = null;
        if (_overflowBuf.hasRemaining()) {
            packet = _overflowBuf.readPacket();
        } else if (!_gotEOF) {
            if (!readPartial) {
                throw new AssertionError("invalid state");
            }
            packet = EMPTY_PACKET;
        }
        return packet;
    }

    @Override
    public int packetsAvailable() throws IOException {
        throwIfClosed();
        return _overflowBuf.packetsAvailable();
    }

    /**
   * Skips some amount of bytes in the encoding output.  The default
   * implementation just reads bytes via the normal encode process and
   * discards them.  Subclasses may override this method to provide a more
   * efficient skip implementation.
   *
   * @return the actual number of bytes skipped by this call
   */
    protected long encodeSkip(long len) throws IOException {
        refillPacket(true);
        long numBytesSkipped = 0;
        if (_overflowBuf.hasRemaining()) {
            numBytesSkipped = Math.min(_overflowBuf.remaining(), len);
            _overflowBuf.skip(numBytesSkipped);
        }
        return numBytesSkipped;
    }

    /**
   * Called by the OutputStreamAdapter to forward bytes to this input
   * stream.
   */
    private void writeBuf(byte[] b, int pos, int len, boolean canKeep) {
        if (_curBuf == null) {
            throw new IllegalStateException("Encoder is writing outside of call to encode");
        }
        int numBytes = Math.min(_curBuf.remaining(), len);
        _curBuf.put(b, pos, numBytes);
        if (numBytes < len) {
            if (!canKeep) {
                _overflowBuf.write(b, pos + numBytes, len - numBytes);
            } else {
                _overflowBuf.writePacket(b, pos + numBytes, len - numBytes);
            }
        }
    }

    /**
   * Called by the OutputStreamAdapter when closed.
   */
    private void closeOut() {
        _gotEOF = true;
    }

    /**
   * Writes some amount of data to the OutputStream linked to the
   * EncodingInputStream calling this method.  The implementation may
   * <i>technically</i> write as much data as desired during the call.
   * However, since any data written over the given suggestedLength will be
   * buffered locally, the implementation risks running the local vm out of
   * memory if too much is written.  If too little is written, the
   * EncodingInputStream will simply repeat the call with adjusted parameters.
   * Implementation should call close on the OutputStream when finished
   * encoding.
   *
   * @param suggestedLength target amount of bytes to write
   */
    protected abstract void encode(int suggestedLength) throws IOException;

    /**
   * OutputStream which forwards bytes to the InputStreamAdapter.
   */
    private class OutputStreamAdapter extends PacketOutputStream {

        private final SingleByteAdapter _singleByteAdapter = new SingleByteAdapter();

        private OutputStreamAdapter() {
        }

        @Override
        public void close() {
            closeOut();
        }

        @Override
        public void flush() {
        }

        @Override
        public void write(int b) throws IOException {
            _singleByteAdapter.write(b, this);
        }

        @Override
        public void write(byte[] b) {
            writeBuf(b, 0, b.length, false);
        }

        @Override
        public void write(byte[] b, int pos, int len) {
            writeBuf(b, pos, len, false);
        }

        @Override
        public void writePacket(byte[] packet) {
            writeBuf(packet, 0, packet.length, true);
        }
    }
}
