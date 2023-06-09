package org.jactr.core.buffer.delegate;

import java.util.Collection;
import java.util.concurrent.Future;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.core.buffer.IActivationBuffer;
import org.jactr.core.chunk.IChunk;
import org.jactr.core.chunk.ISymbolicChunk;
import org.jactr.core.chunk.IllegalChunkStateException;
import org.jactr.core.production.request.ChunkRequest;
import org.jactr.core.production.request.IRequest;
import org.jactr.core.slot.IMutableSlot;
import org.jactr.core.slot.ISlot;

/**
 * takes a chunk pattern and if the chunk is already encoded, copies it, before
 * inserting into the buffer via
 * {@link IActivationBuffer#addSourceChunk(org.jactr.core.chunk.IChunk)}. If the
 * chunk is encoded and there are slots to be modified, the chunk will
 * automatically be copied.
 * 
 * @author harrison
 */
public class AddChunkRequestDelegate extends AsynchronousRequestDelegate {

    /**
   * Logger definition
   */
    private static final transient Log LOGGER = LogFactory.getLog(AddChunkRequestDelegate.class);

    private boolean _copyEncodedChunks = true;

    /**
   * default is to copy encoded chunks
   */
    public AddChunkRequestDelegate() {
        this(true);
    }

    public AddChunkRequestDelegate(boolean copyEncodedChunks) throws IllegalArgumentException {
        _copyEncodedChunks = copyEncodedChunks;
    }

    public boolean willAccept(IRequest request) {
        return request instanceof ChunkRequest;
    }

    @Override
    protected boolean isValid(IRequest request, IActivationBuffer buffer) throws IllegalArgumentException {
        if (!(request instanceof ChunkRequest)) throw new IllegalArgumentException("Request must be ChunkRequest");
        ChunkRequest cRequest = (ChunkRequest) request;
        IChunk chunk = cRequest.getChunk();
        ISymbolicChunk sChunk = chunk.getSymbolicChunk();
        Collection<? extends ISlot> slots = cRequest.getSlots();
        for (ISlot slot : slots) {
            boolean valid = false;
            try {
                valid = null != sChunk.getSlot(slot.getName());
            } catch (IllegalChunkStateException icse) {
            }
            if (!valid) throw new IllegalArgumentException("No slot named " + slot.getName() + " available in " + chunk);
        }
        return true;
    }

    @Override
    protected Object startRequest(IRequest request, IActivationBuffer buffer, double requestTime) {
        ChunkRequest cRequest = (ChunkRequest) request;
        IChunk originalChunk = cRequest.getChunk();
        Future<IChunk> copiedChunk = null;
        Collection<? extends ISlot> slots = cRequest.getSlots();
        if (originalChunk.isEncoded() && (slots.size() != 0 || _copyEncodedChunks)) copiedChunk = buffer.getModel().getDeclarativeModule().copyChunk(originalChunk);
        if (copiedChunk != null) return copiedChunk;
        return originalChunk;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void finishRequest(IRequest request, IActivationBuffer buffer, Object startValue) {
        ChunkRequest cRequest = (ChunkRequest) request;
        Collection<? extends ISlot> slots = cRequest.getSlots();
        IChunk toAdd = null;
        if (startValue instanceof IChunk) toAdd = (IChunk) startValue; else try {
            toAdd = ((Future<IChunk>) startValue).get();
        } catch (Exception e) {
            LOGGER.error("Failed to get chunk from future reference. Request:" + request + " Buffer:" + buffer + " Start:" + startValue, e);
            return;
        }
        try {
            toAdd.getWriteLock().lock();
            ISymbolicChunk sChunk = toAdd.getSymbolicChunk();
            for (ISlot slot : slots) ((IMutableSlot) sChunk.getSlot(slot.getName())).setValue(slot.getValue());
        } finally {
            toAdd.getWriteLock().unlock();
        }
        buffer.addSourceChunk(toAdd);
    }
}
