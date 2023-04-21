package com.ibm.tuningfork.infra.feed;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import com.ibm.tuningfork.infra.chunk.EventChunk;

/**
 * A collection of chunks that can be borrowed and returned. Operations on an individual Chunk should be synchronized.
 */
public class EventChunkPool {

    public static final boolean ALLOCATE_INSTEAD_OF_POOLING = false;

    private final LinkedList<EventChunk> allChunks = new LinkedList<EventChunk>();

    public EventChunkPool(int initialNumberOfChunks) {
        if (!ALLOCATE_INSTEAD_OF_POOLING) grow(initialNumberOfChunks);
    }

    public synchronized EventChunk pop() {
        try {
            return ALLOCATE_INSTEAD_OF_POOLING ? new EventChunk(FeedConstants.MAX_CHUNK_BODY_SIZE) : allChunks.removeFirst();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public synchronized void push(EventChunk chunk) {
        if (!ALLOCATE_INSTEAD_OF_POOLING) allChunks.addFirst(chunk);
    }

    public synchronized void grow(int additionalChunks) {
        for (int i = 0; i < additionalChunks; i++) {
            if (!ALLOCATE_INSTEAD_OF_POOLING) allChunks.add(new EventChunk(FeedConstants.MAX_CHUNK_BODY_SIZE));
        }
    }
}
