package org.elogistics.event;

/**
 * An standard-implementation of the IEvent-Interface
 * @author d6hawp
 *
 * @param <S>
 */
public class AbstractEvent<S extends IEventSource> implements IEvent<S> {

    private S source;

    public AbstractEvent(S source) {
        this.source = source;
    }

    public S getSource() {
        return this.source;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + this.getSource() + "]";
    }
}
