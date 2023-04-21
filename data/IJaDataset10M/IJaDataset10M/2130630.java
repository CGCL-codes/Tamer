package net.java.nioserver;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import net.java.nioserver.operations.OpAccept;
import net.java.nioserver.operations.OpRead;
import net.java.nioserver.operations.OpWrite;
import net.java.nioserver.utils.Pool;

/**
 * @author Leonid Shlyapnikov
 */
public abstract class BasicServiceBuilder {

    protected InetSocketAddress address;

    protected ExecutorService executor;

    protected Pool<ByteBuffer> byteBufferPool;

    protected OpAccept opAccept;

    protected OpRead opRead;

    protected OpWrite opWrite;

    public abstract BasicService build();

    public BasicServiceBuilder address(InetSocketAddress address) {
        this.address = address;
        return this;
    }

    public BasicServiceBuilder executor(ExecutorService executor) {
        this.executor = executor;
        return this;
    }

    public BasicServiceBuilder byteBufferPool(Pool<ByteBuffer> byteBufferPool) {
        this.byteBufferPool = byteBufferPool;
        return this;
    }

    public BasicServiceBuilder opAccept(OpAccept opAccept) {
        this.opAccept = opAccept;
        return this;
    }

    public BasicServiceBuilder opRead(OpRead opRead) {
        this.opRead = opRead;
        return this;
    }

    public BasicServiceBuilder opWrite(OpWrite opWrite) {
        this.opWrite = opWrite;
        return this;
    }

    public BasicServiceBuilder validate() {
        if (null == address) {
            throw new NullPointerException("address property is not set");
        }
        if (null == byteBufferPool) {
            throw new NullPointerException("byteBufferPool is not set");
        }
        if (null == opRead) {
            throw new NullPointerException("opRead property is not set");
        }
        return this;
    }
}
