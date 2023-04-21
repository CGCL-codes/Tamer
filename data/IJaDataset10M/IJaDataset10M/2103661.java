package jaxlib.io.channel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;
import jaxlib.io.IO;
import jaxlib.io.file.Files;
import jaxlib.jaxlib_private.CheckArg;
import jaxlib.util.CheckBounds;

/**
 * A {@code Pipe} implementation which uses a file as intermediate buffer between its sink and source
 * channels.
 * <p>
 * The underlying file of a {@code FilePipe} gets closed when the {@link #source() source channel}
 * gets closed because of a call to its {@link SourceChannel#close() close} method or because of thread
 * interruption. Closing the {@link #sink() sink channel} has no effect on the source channel. The file
 * but not the source channel will be closed if all bytes have been read and the sink channel has been
 * closed. In latter case each read operation will return zero count bytes.
 * </p><p>
 * The underlying file grows with the number of bytes written. For performance purposes the pipe never
 * applies any purging to the file as long as the {@code source} channel is open. Thus you should be aware of
 * disk space for long running connections transferring big amounts of data.
 * </p><p>
 * It is recommended to use some sort of buffering when writing to the sink or reading from the source
 * channel. Byte-by-byte transfer will slow down performance like you would read/write byte-by-byte to a
 * {@link RandomAccessFile}.
 * </p><p>
 * If you need input- and/or ouput streams you should use the implementations returned by
 * {@link FilePipe.SinkChannel#asOutputStream()} and
 * {@link FilePipe.SourceChannel#asInputStream()}. Those are somewhat more efficient than external channel
 * wrappers.
 * </p><p>
 * Typically a {@code FilePipe} is used when large amounts of data may coming in to the
 * {@link #sink() sink} but may be read relatively slow from the {@link #source() source channel}.
 * E.g. one thread quickly generates data and another sends it to a remote through a slow network connection.
 * </p><p>
 * A {@code FilePipe} is only worth to be used if the amount of bytes to be send is greater than
 * a few tens of kilobytes and if the data is coming in much faster than it is going out.
 * By disabling automatic deletion of the underlying file, {@code FilePipe} is also usefull for logging,
 * debugging and caching purposes.
 * </p>
 *
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: FilePipe.java 3029 2011-12-29 00:36:48Z joerg_wassmer $
 */
public class FilePipe extends Pipe {

    private final long initialSize;

    private final FilePipe.SinkChannel sink;

    private final FilePipe.SourceChannel source;

    /**
   * Creates a new {@code FilePipe} which uses a temporar file and an initial capacity of zero.
   * <p>
   * The pipe will delete the file when it isn't used anymore by the pipe.
   * </p>
   *
   * @throws IOException
   *  if an I/O error occurs.
   *
   * @since JaXLib 1.0
   */
    public FilePipe() throws IOException {
        this(Files.createTempFile(), true, 0, false);
    }

    /**
   * Creates a new {@code FilePipe} which uses a temporar file and the specified initial capacity.
   * <p>
   * The pipe will delete the file when it isn't used anymore by the pipe.
   * </p>
   *
   * @param initialCapacity
   *  the initial minimum size of the file. Allocating space in advance could improve performance.
   *  However, this depends on the filesystem.
   *
   * @throws IOException
   *  if an I/O error occurs.
   * @throws IllegalArgumentException
   *  if {@code initialCapacity < 0}.
   *
   * @since JaXLib 1.0
   */
    public FilePipe(final long initialCapacity) throws IOException {
        this(Files.createTempFile(), true, initialCapacity, false);
    }

    /**
   * Creates a new {@code FilePipe} which uses the specfied file and initial capacity.
   *
   * @param file
   *  the file to use as buffer.
   * @param deleteFile
   *  whether to delete file once it isn't used anymore by the pipe.
   * @param initialCapacity
   *  the initial minimum size of the file. Allocating space in advance could improve performance.
   *  However, this depends on the filesystem. If the size of the file already is equal or greater than
   *  the specified initial capacity then this parameter has no effect.
   * @param keepContent
   *  whether to include initially existing bytes of the file into the source channel.
   *
   * @throws FileNotFoundException
   *  if the specified file is not existing as a normal file and this constructor can not create the file.
   * @throws IOException
   *  if the pipe can not immediately get the exclusive {@link FileChannel#tryLock() lock} of the specified
   *  file.
   * @throws IOException
   *  if an I/O error occurs.
   * @throws IllegalArgumentException
   *  if {@code initialCapacity < 0}.
   * @throws NullPointerException
   *  if {@code file == null}.
   *
   * @since JaXLib 1.0
   */
    public FilePipe(final File file, final boolean deleteFile, final long initialCapacity, final boolean keepContent) throws IOException {
        this(SelectorProvider.provider(), file, deleteFile, initialCapacity, keepContent);
    }

    /**
   * Creates a new {@code FilePipe} which uses the specfied file and initial capacity.
   *
   * @param provider
   *  the {@code SelectorProvider} to be returned by the sink and the source channel.
   *  Neither the pipe itself nor its channels are using the provider. Thus you may specify whatever
   *  applicable.
   * @param file
   *  the file to use as buffer.
   * @param deleteFile
   *  whether to delete file once it isn't used anymore by the pipe.
   * @param initialCapacity
   *  the initial minimum size of the file. Allocating space in advance could improve performance.
   *  However, this depends on the filesystem. If the size of the file already is equal or greater than
   *  the specified initial capacity then this parameter has no effect.
   * @param keepContent
   *  whether to include initially existing bytes of the file into the source channel.
   *
   * @throws FileNotFoundException
   *  if the specified file is not existing as a normal file and this constructor can not create the file.
   * @throws IOException
   *  if the pipe can not immediately get the exclusive {@link FileChannel#tryLock() lock} of the specified
   *  file.
   * @throws IOException
   *  if an I/O error occurs.
   * @throws IllegalArgumentException
   *  if {@code initialCapacity < 0}.
   * @throws NullPointerException
   *  if {@code file == null}.
   *
   * @since JaXLib 1.0
   */
    public FilePipe(@Nullable final SelectorProvider provider, final File file, final boolean deleteFile, final long initialCapacity, final boolean keepContent) throws IOException {
        super();
        CheckArg.notNull(file, "file");
        CheckArg.notNegative(initialCapacity, "initialCapacity");
        boolean createdNewFile = false;
        if (!file.isFile()) {
            if (file.createNewFile()) createdNewFile = true; else if (!file.isFile()) throw new FileNotFoundException("Unable to create file: " + file);
        }
        RandomAccessFile rf = new RandomAccessFile(file, "rw");
        try {
            FileChannel fileChannel = rf.getChannel();
            FileLock lock = fileChannel.tryLock();
            if (lock == null) throw new IOException("Unable to lock file: " + file);
            final long size = fileChannel.size();
            if (keepContent) {
                this.initialSize = size;
                fileChannel.position(size);
            } else {
                this.initialSize = 0;
            }
            if ((initialCapacity > 0) && (size < initialCapacity)) rf.setLength(initialCapacity);
            this.sink = new SinkChannel(rf, provider, this.initialSize);
            this.source = new SourceChannel(file, deleteFile, fileChannel, this.sink);
            rf = null;
        } finally {
            if (rf != null) {
                try {
                    rf.close();
                } catch (final Throwable ex) {
                }
                rf = null;
                if (createdNewFile) {
                    try {
                        file.delete();
                    } catch (final Throwable ex) {
                    }
                }
            }
        }
    }

    /**
   * The file this pipe uses as intermediate buffer between the sink and the source channel.
   *
   * @since JaXLib 1.0
   */
    public final File getFile() {
        return this.source.file;
    }

    /**
   * The number of bytes which already existed in the underlying file if this pipe was configured to
   * include those into the source channel.
   *
   * @since JaXLib 1.0
   */
    public final long getInitialSize() {
        return this.initialSize;
    }

    /**
   * Whether this pipe deletes the file if it doesn't require the file anymore.
   *
   * @since JaXLib 1.0
   */
    public final boolean isDeletingFile() {
        return this.source.deleteFile;
    }

    /**
   * Whether the underlying file is opened by this pipe.
   * Once it is not it will never be opened again by this pipe.
   * <p>
   * The pipe keeps the exlusive lock while it uses the file.
   * </p>
   *
   * @see FileChannel#tryLock()
   *
   * @since JaXLib 1.0
   */
    public final boolean isFileOpen() {
        final FileChannel fileChannel = this.source.fileChannel;
        return (fileChannel != null) && fileChannel.isOpen();
    }

    @Override
    public final FilePipe.SinkChannel sink() {
        return this.sink;
    }

    @Override
    public final FilePipe.SourceChannel source() {
        return this.source;
    }

    public static final class SinkChannel extends Pipe.SinkChannel implements OutputByteChannel {

        volatile boolean blocking = isBlocking();

        RandomAccessFile file;

        FileChannel fileChannel;

        final ReentrantLock lock;

        /**
     * Used to notify the SourceChannel when new bytes have been written.
     */
        final Condition notEmpty;

        /**
     * A view of this channel as a stream. Either null or instance of SinkStream.
     */
        private OutputStream stream;

        /**
     * We avoid using FileChannel#position() because this method contains a synchronized block.
     * Instead we are manually keeping track of the position. The SourceChannel does too.
     * <p>
     * This field is volatile to ensure visibility of updates without having to synchronize.
     * </p>
     */
        volatile long writePos;

        SinkChannel(final RandomAccessFile file, final SelectorProvider provider, final long size) throws IOException {
            super(provider);
            assert this.blocking;
            this.file = file;
            this.fileChannel = file.getChannel();
            this.lock = new ReentrantLock();
            this.notEmpty = this.lock.newCondition();
            this.writePos = size;
        }

        private IOException handleClosedFile(ClosedChannelException ex) throws IOException {
            try {
                close();
            } catch (final IOException ex2) {
                if (ex2.getCause() == null) throw (IOException) ex2.initCause(ex); else throw ex2;
            }
            if ((ex instanceof ClosedByInterruptException) || (ex instanceof AsynchronousCloseException)) {
                throw ex;
            } else {
                ex = null;
                throw new AsynchronousCloseException();
            }
        }

        private void lock() throws IOException {
            try {
                this.lock.lockInterruptibly();
            } catch (InterruptedException ex) {
                try {
                    ex = null;
                    try {
                        close();
                    } catch (final IOException ex2) {
                        if (ex2.getCause() == null) throw (IOException) ex2.initCause(new ClosedByInterruptException()); else throw ex2;
                    }
                    throw new ClosedByInterruptException();
                } finally {
                    Thread.currentThread().interrupt();
                }
            }
        }

        private void unlock() {
            this.lock.unlock();
        }

        final void write(final int b, final byte[] singleByte) throws IOException {
            if (!isOpen()) throw new ClosedChannelException();
            lock();
            try {
                final RandomAccessFile file = this.file;
                if (file == null) throw new AsynchronousCloseException();
                singleByte[0] = (byte) b;
                final long writePos = this.writePos;
                file.seek(writePos);
                file.write(singleByte);
                this.writePos = writePos + 1;
                this.notEmpty.signal();
            } catch (final ClosedChannelException ex) {
                throw handleClosedFile(ex);
            } finally {
                unlock();
            }
        }

        final void write(final byte[] a, final int offs, final int len) throws IOException {
            CheckBounds.offset(a, offs, len);
            if (!isOpen()) throw new ClosedChannelException();
            if (len > 0) {
                lock();
                try {
                    final RandomAccessFile file = this.file;
                    if (file == null) throw new AsynchronousCloseException();
                    final long writePos = this.writePos;
                    file.seek(writePos);
                    file.write(a, offs, len);
                    this.writePos = writePos + len;
                    this.notEmpty.signal();
                } catch (final ClosedChannelException ex) {
                    throw handleClosedFile(ex);
                } finally {
                    unlock();
                }
            }
        }

        @Override
        protected void implCloseSelectableChannel() throws IOException {
            this.file = null;
            this.fileChannel = null;
            this.lock.lock();
            try {
                this.notEmpty.signalAll();
            } finally {
                this.lock.unlock();
            }
        }

        @Override
        protected void implConfigureBlocking(boolean block) throws IOException {
            this.blocking = block;
        }

        /**
     * Returns a view of this channel as a stream.
     * The stream implementation is potentially more efficient than the stream returned by
     * {@link java.nio.Channels#newOutputStream(WritableByteChannel) Channels.newOutputStream(this)}.
     * <p>
     * Like this sink channel the stream also is thread-safe. The stream always blocks.
     * </p>
     *
     * @since JaXLib 1.0
     */
        public OutputStream asOutputStream() {
            synchronized (this.notEmpty) {
                OutputStream stream = this.stream;
                if (stream == null) this.stream = stream = new SinkStream(this);
                return stream;
            }
        }

        /**
     * Returns the number of bytes written through this channel.
     *
     * @since JaXLib 1.0
     */
        public final long writeCount() {
            return this.writePos;
        }

        /**
     * This implementation transfers bytes directly from the underlying {@code FileChannel}.
     *
     * @see FileChannel#transferFrom(ReadableByteChannel,long,long)
     */
        @Override
        public long transferFromByteChannel(final ReadableByteChannel in, final long maxCount) throws IOException {
            return transferFromByteChannel(in, maxCount, false);
        }

        final long transferFromByteChannel(final ReadableByteChannel in, final long maxCount, final boolean forceBlock) throws IOException {
            CheckArg.notNull(in, "in");
            CheckArg.maxCount(maxCount);
            if (!isOpen()) throw new ClosedChannelException();
            if (maxCount == 0) return 0;
            long count = 0;
            lock();
            try {
                while (isOpen()) {
                    FileChannel fileChannel = this.fileChannel;
                    if (fileChannel == null) throw new AsynchronousCloseException();
                    final long maxStep = (maxCount < 0) ? Long.MAX_VALUE : Math.min(Long.MAX_VALUE, maxCount);
                    final long writePos = this.writePos;
                    final long step = fileChannel.transferFrom(in, writePos, maxStep);
                    if (step > 0) {
                        this.writePos = writePos + step;
                        count += step;
                        this.notEmpty.signal();
                    }
                    fileChannel = null;
                    if (((maxCount < 0) || (count < maxCount)) && (forceBlock || this.blocking)) {
                        Thread.yield();
                        continue;
                    } else {
                        break;
                    }
                }
            } catch (final ClosedChannelException ex) {
                throw handleClosedFile(ex);
            } finally {
                unlock();
            }
            return count;
        }

        @Override
        public int write(final ByteBuffer src) throws IOException {
            return write(src, false);
        }

        final int write(final ByteBuffer src, final boolean forceBlock) throws IOException {
            if (!isOpen()) throw new ClosedChannelException();
            if (!src.hasRemaining()) return 0;
            lock();
            try {
                return write0(src, forceBlock);
            } finally {
                unlock();
            }
        }

        private int write0(final ByteBuffer src, final boolean forceBlock) throws IOException {
            int count = 0;
            try {
                while (isOpen()) {
                    FileChannel fileChannel = this.fileChannel;
                    if (fileChannel == null) throw new AsynchronousCloseException();
                    final long writePos = this.writePos;
                    final int step = fileChannel.write(src, writePos);
                    fileChannel = null;
                    if (step > 0) {
                        this.writePos = writePos + step;
                        count += step;
                        this.notEmpty.signal();
                    }
                    if (src.hasRemaining() && (forceBlock || this.blocking)) {
                        Thread.yield();
                        continue;
                    } else {
                        break;
                    }
                }
            } catch (final ClosedChannelException ex) {
                throw handleClosedFile(ex);
            }
            return count;
        }

        @Override
        public long write(final ByteBuffer[] src) throws IOException {
            return write(src, 0, src.length);
        }

        @Override
        public long write(final ByteBuffer[] src, int offset, int length) throws IOException {
            CheckBounds.offset(src.length, offset, length);
            if (!isOpen()) throw new ClosedChannelException();
            while (offset < offset + length) {
                final ByteBuffer buf = src[offset];
                if ((buf != null) && buf.hasRemaining()) break;
                offset++;
                length--;
            }
            if (length == 0) return 0;
            long count = 0;
            lock();
            try {
                while (isOpen()) {
                    while (offset < offset + length) {
                        final ByteBuffer buf = src[offset];
                        if ((buf != null) && buf.hasRemaining()) break;
                        offset++;
                        length--;
                    }
                    if (length == 0) break;
                    final ByteBuffer buffer = src[offset];
                    final int step = write0(buffer, false);
                    if (step <= 0) break;
                    count += step;
                    if (!buffer.hasRemaining()) {
                        offset++;
                        length--;
                        if (offset >= offset + length) break;
                    }
                    continue;
                }
            } finally {
                unlock();
            }
            return count;
        }

        @Override
        public final int writeFully(final ByteBuffer src) throws IOException {
            return write(src, true);
        }
    }

    private static final class SinkStream extends OutputStream implements OutputByteChannel {

        private SinkChannel sink;

        private byte[] singleByte;

        SinkStream(final SinkChannel sink) {
            super();
            this.sink = sink;
        }

        @Override
        public final void close() throws IOException {
            final SinkChannel sink = this.sink;
            if (sink != null) {
                this.sink = null;
                sink.close();
                this.singleByte = null;
            }
        }

        @Override
        public final boolean isOpen() {
            final SinkChannel sink = this.sink;
            return (sink != null) && sink.isOpen();
        }

        @Override
        public final long transferFromByteChannel(final ReadableByteChannel in, final long maxCount) throws IOException {
            final SinkChannel sink = this.sink;
            if (sink == null) throw new ClosedChannelException();
            return sink.transferFromByteChannel(in, maxCount, true);
        }

        @Override
        public final void write(final int b) throws IOException {
            final SinkChannel sink = this.sink;
            if (sink == null) throw new ClosedChannelException();
            byte[] singleByte = this.singleByte;
            if (singleByte == null) this.singleByte = singleByte = new byte[1];
            sink.write(b, singleByte);
        }

        @Override
        public final void write(final byte[] a, final int offs, final int len) throws IOException {
            final SinkChannel sink = this.sink;
            if (sink == null) throw new ClosedChannelException();
            sink.write(a, offs, len);
        }

        @Override
        public final int write(final ByteBuffer buffer) throws IOException {
            final SinkChannel sink = this.sink;
            if (sink == null) throw new ClosedChannelException();
            return sink.write(buffer, true);
        }

        @Override
        public final int writeFully(final ByteBuffer buffer) throws IOException {
            final SinkChannel sink = this.sink;
            if (sink == null) throw new ClosedChannelException();
            return sink.write(buffer, true);
        }
    }

    public static final class SourceChannel extends Pipe.SourceChannel implements InputByteChannel, InterruptibleChannel {

        volatile boolean blocking = isBlocking();

        final boolean deleteFile;

        File file;

        FileChannel fileChannel;

        final ReentrantLock lock;

        volatile long readPosition;

        final FilePipe.SinkChannel sink;

        /**
     * A view of this channel as a stream. Either null or instance of SourceStream.
     */
        private InputStream stream;

        SourceChannel(final File file, final boolean deleteFile, final FileChannel fileChannel, final FilePipe.SinkChannel sinkChannel) {
            super(sinkChannel.provider());
            assert this.blocking;
            this.deleteFile = deleteFile;
            this.file = file;
            this.fileChannel = fileChannel;
            this.lock = new ReentrantLock();
            this.sink = sinkChannel;
        }

        private void closeFile() throws IOException {
            FileChannel fileChannel = this.fileChannel;
            if (fileChannel != null) {
                this.fileChannel = null;
                File file = this.file;
                this.file = null;
                Throwable ex = null;
                try {
                    fileChannel.close();
                } catch (final Throwable t) {
                    ex = t;
                }
                fileChannel = null;
                if (this.deleteFile && (file != null)) {
                    try {
                        file.delete();
                    } catch (final Throwable t) {
                        if (ex == null) ex = t;
                    }
                }
                if (ex != null) IO.throwException(ex);
            }
        }

        private IOException handleClosedFile(ClosedChannelException ex) throws IOException {
            try {
                close();
            } catch (final IOException ex2) {
                if (ex2.getCause() == null) throw (IOException) ex2.initCause(ex); else throw ex2;
            }
            if ((ex instanceof ClosedByInterruptException) || (ex instanceof AsynchronousCloseException)) {
                throw ex;
            } else {
                ex = null;
                throw new AsynchronousCloseException();
            }
        }

        private void lock() throws IOException {
            try {
                this.lock.lockInterruptibly();
            } catch (InterruptedException ex) {
                try {
                    ex = null;
                    try {
                        close();
                    } catch (final IOException ex2) {
                        if (ex2.getCause() == null) throw (IOException) ex2.initCause(new ClosedByInterruptException()); else throw ex2;
                    }
                    throw new ClosedByInterruptException();
                } finally {
                    Thread.currentThread().interrupt();
                }
            }
        }

        private void unlock() {
            this.lock.unlock();
        }

        private boolean waitForMore(final boolean forceBlock) throws IOException {
            if (!forceBlock && !this.blocking) return false;
            this.sink.lock();
            try {
                if (!forceBlock && !this.blocking) return false; else if (this.sink.isOpen()) {
                    if (this.fileChannel == null) return false;
                    if (this.readPosition != this.sink.writeCount()) return true;
                    try {
                        this.sink.notEmpty.await();
                        return true;
                    } catch (InterruptedException ex) {
                        ex = null;
                        IO.tryClose(this);
                        Thread.currentThread().interrupt();
                        throw new ClosedByInterruptException();
                    }
                } else if (this.fileChannel == null) return false; else if (this.readPosition != this.sink.writeCount()) return true; else {
                    closeFile();
                    return false;
                }
            } finally {
                this.sink.unlock();
            }
        }

        final int readSingleByte(final ByteBuffer singleByte) throws IOException {
            if (!isOpen()) throw new ClosedChannelException();
            lock();
            try {
                singleByte.position(0);
                if (read0(singleByte, true) > 0) return singleByte.get() & 0xff; else return -1;
            } finally {
                unlock();
            }
        }

        @Override
        protected void finalize() throws Throwable {
            closeFile();
        }

        @Override
        protected final void implCloseSelectableChannel() throws IOException {
            Throwable ex = null;
            try {
                closeFile();
            } catch (final Throwable t) {
                ex = t;
            }
            try {
                this.sink.close();
            } catch (final Throwable t) {
                if (ex == null) ex = t;
            }
            this.sink.lock.lock();
            try {
                this.sink.notEmpty.signalAll();
            } finally {
                this.sink.lock.unlock();
            }
            if (ex != null) IO.throwException(ex);
        }

        @Override
        protected final void implConfigureBlocking(final boolean block) throws IOException {
            this.blocking = block;
            if (!block) {
                this.sink.lock.lock();
                try {
                    this.sink.notEmpty.signalAll();
                } finally {
                    this.sink.lock.unlock();
                }
            }
        }

        /**
     * Returns a view of this channel as a stream.
     * The stream implementation is potentially more efficient than the stream returned by
     * {@link java.nio.Channels#newInputStream(ReadableByteChannel) Channels.newInputStream(this)}.
     * <p>
     * Like this source channel the stream also is thread-safe. The stream always blocks.
     * </p>
     *
     * @since JaXLib 1.0
     */
        public final InputStream asInputStream() {
            synchronized (this.lock) {
                InputStream stream = this.stream;
                if (stream == null) this.stream = stream = new SourceStream(this);
                return stream;
            }
        }

        /**
     * Returns the number of bytes read through this channel.
     *
     * @since JaXLib 1.0
     */
        public final long readCount() {
            return this.readPosition;
        }

        @Override
        public final int read(final ByteBuffer dst) throws IOException {
            return read(dst, false);
        }

        final int read(final ByteBuffer dst, final boolean forceBlock) throws IOException {
            if (!isOpen()) throw new ClosedChannelException();
            if (!dst.hasRemaining()) return 0;
            lock();
            try {
                final int count = read0(dst, forceBlock);
                return (count > 0) || (this.fileChannel != null) ? count : -1;
            } finally {
                unlock();
            }
        }

        private int read0(final ByteBuffer dst, final boolean forceBlock) throws IOException {
            final int initialDstPos = dst.position();
            final int dstLim = dst.limit();
            int dstPos = dst.position();
            long readPos = this.readPosition;
            try {
                while (isOpen()) {
                    FileChannel fileChannel = this.fileChannel;
                    if (fileChannel != null) {
                        final long writePos = this.sink.writeCount();
                        final int maxStep = (int) Math.min(dstLim - dstPos, writePos - readPos);
                        final int step;
                        if (maxStep > 0) {
                            dst.limit(dstPos + maxStep);
                            step = fileChannel.read(dst, readPos);
                            fileChannel = null;
                            if (step > 0) {
                                this.readPosition = readPos += step;
                                dstPos += step;
                            } else if (step < 0) {
                                throw new IOException("File truncated by another thread or process: " + this.file);
                            }
                        } else {
                            fileChannel = null;
                            step = 0;
                        }
                        if ((readPos >= writePos) && !this.sink.isOpen()) {
                            closeFile();
                            break;
                        } else if ((step < dstLim - dstPos) && waitForMore(forceBlock)) {
                            continue;
                        } else {
                            break;
                        }
                    } else if (isOpen()) break; else throw new AsynchronousCloseException();
                }
            } catch (final ClosedChannelException ex) {
                throw handleClosedFile(ex);
            } finally {
                dst.limit(dstLim);
            }
            return dstPos - initialDstPos;
        }

        @Override
        public final long read(final ByteBuffer[] dst) throws IOException {
            return read(dst, 0, dst.length);
        }

        @Override
        public final long read(final ByteBuffer[] dst, int offset, int length) throws IOException {
            CheckBounds.offset(dst.length, offset, length);
            if (!isOpen()) throw new ClosedChannelException();
            while (offset < offset + length) {
                final ByteBuffer buf = dst[offset];
                if ((buf != null) && buf.hasRemaining()) break;
                offset++;
                length--;
            }
            if (length == 0) return 0;
            long count = 0;
            lock();
            try {
                while (isOpen()) {
                    while (offset < offset + length) {
                        final ByteBuffer buf = dst[offset];
                        if ((buf != null) && buf.hasRemaining()) break;
                        offset++;
                        length--;
                    }
                    if (length == 0) break;
                    final ByteBuffer buffer = dst[offset];
                    final int step = read0(buffer, false);
                    if (step <= 0) break;
                    count += step;
                    if (buffer.hasRemaining()) break;
                    offset++;
                    length--;
                    if (offset >= offset + length) break;
                }
                return (count > 0) || (this.fileChannel != null) ? count : -1;
            } finally {
                unlock();
            }
        }

        /**
     * This implementation transfers bytes directly to the underlying {@code FileChannel}.
     *
     * @see FileChannel#transferTo(long,long,WritableByteChannel)
     */
        @Override
        public final long transferToByteChannel(final WritableByteChannel out, final long maxCount) throws IOException {
            return transferToByteChannel(out, maxCount, false);
        }

        final long transferToByteChannel(final WritableByteChannel out, final long maxCount, final boolean forceBlock) throws IOException {
            CheckArg.notNull(out, "out");
            CheckArg.maxCount(maxCount);
            if (!isOpen()) throw new ClosedChannelException();
            if (maxCount == 0) return 0;
            lock();
            try {
                long count = 0;
                while (isOpen()) {
                    FileChannel fileChannel = this.fileChannel;
                    if (fileChannel != null) {
                        final long writePos = this.sink.writeCount();
                        if (count != maxCount) {
                            final long readPos = this.readPosition;
                            long step = 0;
                            if (readPosition < writePos) {
                                step = fileChannel.transferTo(readPos, Math.min((maxCount < 0) ? Long.MAX_VALUE : maxCount, writePos - readPos), out);
                            }
                            fileChannel = null;
                            if (step > 0) {
                                count += step;
                                this.readPosition = readPos + step;
                            } else {
                                if (!this.sink.isOpen()) {
                                    closeFile();
                                    break;
                                }
                            }
                        }
                        if (((maxCount < 0) || (count < maxCount)) && waitForMore(forceBlock)) continue; else break;
                    } else if (isOpen()) break; else throw new AsynchronousCloseException();
                }
                return count;
            } catch (final ClosedChannelException ex) {
                final FileChannel fileChannel = this.fileChannel;
                if ((fileChannel == null) || !fileChannel.isOpen()) throw handleClosedFile(ex); else throw ex;
            } finally {
                unlock();
            }
        }
    }

    private static final class SourceStream extends InputStream implements InputByteChannel, InterruptibleChannel {

        private SourceChannel source;

        private ByteBuffer singleByte;

        SourceStream(final SourceChannel source) {
            super();
            this.source = source;
        }

        @Override
        public final void close() throws IOException {
            final SourceChannel source = this.source;
            if (source != null) {
                this.source = null;
                source.close();
                this.singleByte = null;
            }
        }

        @Override
        public final boolean isOpen() {
            final SourceChannel source = this.source;
            return (source != null) && source.isOpen();
        }

        @Override
        public final int read() throws IOException {
            final SourceChannel source = this.source;
            if (source == null) throw new ClosedChannelException();
            ByteBuffer singleByte = this.singleByte;
            if (singleByte == null) this.singleByte = singleByte = ByteBuffer.allocate(1);
            return source.readSingleByte(singleByte);
        }

        @Override
        public final int read(final byte[] a, int offs, int len) throws IOException {
            CheckBounds.offset(a, offs, len);
            if (len != 1) {
                final SourceChannel source = this.source;
                if (source == null) throw new ClosedChannelException();
                if (len == 0) return 0;
                return source.read(ByteBuffer.wrap(a, offs, len), true);
            } else {
                final int b = read();
                if (b < 0) return -1;
                a[offs] = (byte) b;
                return 1;
            }
        }

        @Override
        public final int read(final ByteBuffer dst) throws IOException {
            final SourceChannel source = this.source;
            if (source == null) throw new ClosedChannelException();
            return source.read(dst, true);
        }

        @Override
        public final long transferToByteChannel(final WritableByteChannel out, final long maxCount) throws IOException {
            final SourceChannel source = this.source;
            if (source == null) throw new ClosedChannelException();
            return source.transferToByteChannel(out, maxCount, true);
        }
    }
}
