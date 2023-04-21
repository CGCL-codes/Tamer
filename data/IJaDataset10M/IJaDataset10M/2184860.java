package lia.util.net.copy.transport;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lia.util.net.common.DirectByteBufferPool;
import lia.util.net.common.HeaderBufferPool;
import lia.util.net.copy.transport.internal.FDTSelectionKey;

/**
 * Abstract class implemented by both the reader and writer keys TODO - Use finer grained synchronization mechanism
 * instead of synchronized on the entire key
 * 
 * @author ramiro
 */
public abstract class FDTKeyAttachement {

    private static final AtomicInteger SEQ = new AtomicInteger(0);

    private static final HeaderBufferPool headerPool = HeaderBufferPool.getInstance();

    private static final DirectByteBufferPool payloadPool = DirectByteBufferPool.getInstance();

    private ByteBuffer header;

    private ByteBuffer payload;

    private final ByteBuffer[] _array = new ByteBuffer[2];

    protected final int seq;

    public FDTSelectionKey fdtSelectionKey;

    protected boolean useFixedSizeBlocks;

    public FDTKeyAttachement(FDTSelectionKey fdtSelectionKey, boolean useFixedSizeBlocks) {
        this.header = null;
        this.payload = null;
        this.seq = SEQ.getAndIncrement();
        this.fdtSelectionKey = fdtSelectionKey;
        this.useFixedSizeBlocks = useFixedSizeBlocks;
    }

    /**
     * This MUST stay synchronized
     * 
     * @param header
     * @param payload
     */
    public synchronized void setBuffers(ByteBuffer header, ByteBuffer payload) {
        this.header = header;
        this.payload = payload;
        setArray();
    }

    /**
     * will always be called from synchronized block
     */
    private void setArray() {
        this._array[0] = this.header;
        this._array[1] = this.payload;
    }

    /**
     * This MUST stay synchronized
     */
    public synchronized void recycleBuffers() {
        if (this.header != null) {
            headerPool.put(this.header);
            this.header = null;
        }
        if (this.payload != null) {
            payloadPool.put(this.payload);
            this.payload = null;
        }
        setArray();
    }

    public synchronized void recycleAndSetPayload(ByteBuffer bb) {
        if (this.payload != null) {
            payloadPool.put(this.payload);
        }
        this.payload = bb;
        setArray();
    }

    public synchronized void setPayload(ByteBuffer bb) {
        this.payload = bb;
        setArray();
    }

    public synchronized void recycleHeader() {
        if (this.header != null) {
            headerPool.put(this.header);
            this.header = null;
        }
        setArray();
    }

    public synchronized void recyclePaylod() {
        if (this.payload != null) {
            payloadPool.put(this.payload);
            this.payload = null;
        }
        setArray();
    }

    public synchronized boolean recycleAndSetBuffers() throws InterruptedException {
        recycleBuffers();
        ByteBuffer _header = null;
        ByteBuffer _payload = null;
        try {
            _header = headerPool.poll(2, TimeUnit.SECONDS);
            if (_header != null) {
                _payload = payloadPool.poll(2, TimeUnit.SECONDS);
            }
        } finally {
            if (_header == null) {
                return false;
            }
            if (_payload == null) {
                if (_header != null) {
                    headerPool.put(_header);
                    header = null;
                }
                return false;
            }
        }
        setBuffers(_header, _payload);
        return true;
    }

    public synchronized boolean hasBuffers() {
        return ((header != null) && (payload != null));
    }

    public synchronized boolean hasHeader() {
        return (header != null);
    }

    public synchronized boolean hasPayload() {
        return (payload != null);
    }

    public synchronized ByteBuffer[] asArray() {
        return _array;
    }

    public final boolean useFixedSizeBlocks() {
        return useFixedSizeBlocks;
    }

    public final synchronized ByteBuffer header() {
        return header;
    }

    public final synchronized ByteBuffer payload() {
        return payload;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SocketAttachement :- header: ").append(header).append(" :- payload: ").append(payload);
        return sb.toString();
    }
}
