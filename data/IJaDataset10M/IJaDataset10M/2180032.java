package com.lmax.disruptor;

/**
 * Called by the {@link RingBuffer} to pre-populate all the events to fill the RingBuffer.
 * 
 * @param <T> event implementation storing the data for sharing during exchange or parallel coordination of an event.
 */
public interface EventFactory<T> {

    T newInstance();
}
