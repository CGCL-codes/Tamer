package jaxlib.buffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jaxlib.util.CheckArg;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: IOBufferPool.java 2805 2010-04-27 05:03:53Z joerg_wassmer $
 */
public class IOBufferPool extends Object {

    private final boolean dynamic;

    @Nonnull
    private ByteBufferPool inputBufferPool;

    @Nonnull
    private ByteBufferPool outputBufferPool;

    public IOBufferPool(final ByteBufferPool pool) {
        super();
        CheckArg.notNull(pool, "pool");
        this.dynamic = false;
        this.inputBufferPool = pool;
        this.outputBufferPool = pool;
    }

    public IOBufferPool(final ByteBufferPool inputBufferPool, final ByteBufferPool outputBufferPool) {
        super();
        CheckArg.notNull(inputBufferPool, "inputBufferPool");
        CheckArg.notNull(outputBufferPool, "outputBufferPool");
        this.dynamic = false;
        this.inputBufferPool = inputBufferPool;
        this.outputBufferPool = outputBufferPool;
    }

    public IOBufferPool(final ByteBufferPool inputBufferPool, final ByteBufferPool outputBufferPool, final boolean dynamic) {
        super();
        CheckArg.notNull(inputBufferPool, "inputBufferPool");
        CheckArg.notNull(outputBufferPool, "outputBufferPool");
        this.dynamic = dynamic;
        this.inputBufferPool = inputBufferPool;
        this.outputBufferPool = outputBufferPool;
    }

    @Nonnull
    protected ByteBufferPool getInputBufferPool() {
        return this.inputBufferPool;
    }

    @Nonnull
    protected ByteBufferPool getOutputBufferPool() {
        return this.outputBufferPool;
    }

    protected void setByteBufferPools(final ByteBufferPool inputBufferPool, final ByteBufferPool outputBufferPool) {
        CheckArg.notNull(inputBufferPool, "inputBufferPool");
        CheckArg.notNull(outputBufferPool, "outputBufferPool");
        this.inputBufferPool = inputBufferPool;
        this.outputBufferPool = outputBufferPool;
    }

    @Nonnull
    public ByteBuffer allocateInputBuffer() {
        return this.inputBufferPool.allocate();
    }

    @Nonnull
    public ByteBuffer allocateInputBuffer(final int minCapacity) {
        ByteBuffer bb = this.inputBufferPool.allocate();
        if (bb.capacity() < minCapacity) {
            this.inputBufferPool.offer(bb);
            if (this.dynamic) {
                this.inputBufferPool.setBufferSize(minCapacity);
                bb = this.inputBufferPool.allocate();
            }
            if (bb == null) bb = ByteBuffer.allocate(minCapacity);
        }
        return bb;
    }

    @Nonnull
    public ByteBuffer allocateOutputBuffer() {
        return this.outputBufferPool.allocate();
    }

    @Nonnull
    public ByteBuffer allocateOutputBuffer(final int minCapacity) {
        ByteBuffer bb = this.outputBufferPool.allocate();
        if (bb.capacity() < minCapacity) {
            this.outputBufferPool.offer(bb);
            if (this.dynamic) {
                this.outputBufferPool.setBufferSize(minCapacity);
                bb = this.outputBufferPool.allocate();
            }
            if (bb == null) bb = ByteBuffer.allocate(minCapacity);
        }
        return bb;
    }

    @Nonnegative
    public final int getInputBufferSize() {
        return this.inputBufferPool.getBufferSize();
    }

    @Nonnegative
    public final int getOutputBufferSize() {
        return this.outputBufferPool.getBufferSize();
    }

    public boolean isDynamic() {
        return this.dynamic;
    }

    public boolean offerInputBuffer(final ByteBuffer buffer) {
        return this.inputBufferPool.offer(buffer);
    }

    public boolean offerOutputBuffer(final ByteBuffer buffer) {
        return this.outputBufferPool.offer(buffer);
    }

    @Nonnull
    public final ByteBuffer read(final ReadableByteChannel in, @Nullable ByteBuffer buffer, final int maxCapacity) throws IOException {
        if (buffer == null) buffer = this.inputBufferPool.allocate(); else if (buffer.limit() >= buffer.capacity()) buffer = this.inputBufferPool.readGrow(buffer, maxCapacity);
        final int pos = buffer.position();
        buffer.limit(buffer.capacity());
        in.read(buffer);
        buffer.limit(buffer.position()).position(pos);
        return buffer;
    }
}
