package com.lmax.disruptor;

/**
 * An aggregate collection of {@link EventHandler}s that get called in sequence for each event.
 *
 * @param <T> event implementation storing the data for sharing during exchange or parallel coordination of an event.
 */
public final class AggregateEventHandler<T> implements EventHandler<T>, LifecycleAware {

    private final EventHandler<T>[] eventHandlers;

    /**
     * Construct an aggregate collection of {@link EventHandler}s to be called in sequence.
     *
     * @param eventHandlers to be called in sequence.
     */
    public AggregateEventHandler(final EventHandler<T>... eventHandlers) {
        this.eventHandlers = eventHandlers;
    }

    @Override
    public void onEvent(final T event, final long sequence, final boolean endOfBatch) throws Exception {
        for (final EventHandler<T> eventHandler : eventHandlers) {
            eventHandler.onEvent(event, sequence, endOfBatch);
        }
    }

    @Override
    public void onStart() {
        for (final EventHandler<T> eventHandler : eventHandlers) {
            if (eventHandler instanceof LifecycleAware) {
                ((LifecycleAware) eventHandler).onStart();
            }
        }
    }

    @Override
    public void onShutdown() {
        for (final EventHandler<T> eventHandler : eventHandlers) {
            if (eventHandler instanceof LifecycleAware) {
                ((LifecycleAware) eventHandler).onShutdown();
            }
        }
    }
}
