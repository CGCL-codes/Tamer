package org.jactr.core.queue.timedevents;

import java.util.concurrent.Future;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.core.buffer.IActivationBuffer;
import org.jactr.core.chunk.IChunk;

/**
 * will insert a chunk into the buffer after a specified amount of time has
 * elapsed. This uses the Future interface so that asynchronous operations can
 * be active up until the time that the chunk is actually required
 * 
 * @author developer
 */
@Deprecated
public class FutureChunkInsertionTimedEvent extends AbstractTimedEvent implements IBufferBasedTimedEvent {

    /**
   * logger definition
   */
    public static final Log LOGGER = LogFactory.getLog(FutureChunkInsertionTimedEvent.class);

    protected Future<IChunk> _chunkToInsert;

    protected IActivationBuffer _buffer;

    protected IChunk _insertedChunk;

    public FutureChunkInsertionTimedEvent(Future<IChunk> chunkToInsert, IActivationBuffer buffer, double startTime, double endTime) {
        super(startTime, endTime);
        _chunkToInsert = chunkToInsert;
        _buffer = buffer;
    }

    public IActivationBuffer getBuffer() {
        return _buffer;
    }

    public Future<IChunk> getFutureChunk() {
        return _chunkToInsert;
    }

    @Override
    public void fire(double currentTime) {
        super.fire(currentTime);
        try {
            IChunk chunk = _chunkToInsert.get();
            _insertedChunk = _buffer.addSourceChunk(chunk);
        } catch (Exception e) {
            LOGGER.error("Could not get future chunk for insertion", e);
            throw new RuntimeException("Could not get future chunk for insertion", e);
        }
    }

    public IChunk getInsertedChunk() {
        return _insertedChunk;
    }

    public IChunk getBoundChunk() {
        if (_insertedChunk != null) return _insertedChunk;
        try {
            return _chunkToInsert.get();
        } catch (Exception e) {
            LOGGER.error("Could not get future chunk for insertion", e);
            throw new RuntimeException("Could not get future chunk for insertion", e);
        }
    }
}
