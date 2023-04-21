package net.svenbuschbeck.gwt.ao.client.events;

import java.util.Collection;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Represents the event of a value being removed from something.
 * 
 * @param <T>
 *            the value being removed
 */
public class ValueRemovedEvent<T> extends GwtEvent<ValueRemovedHandler<T>> {

    /**
	 * Handler type.
	 */
    private static Type<ValueRemovedHandler<?>> TYPE;

    /**
	 * Fires a value removed event on all registered handlers in the handler
	 * manager.If no such handlers exist, this method will do nothing.
	 * 
	 * @param <T>
	 *            the old value type
	 * @param source
	 *            the source of the handlers
	 * @param value
	 *            the value
	 */
    public static <T> void fire(HasValueRemovedHandlers<T> source, T value) {
        if (TYPE != null) {
            ValueRemovedEvent<T> event = new ValueRemovedEvent<T>(value);
            source.fireEvent(event);
        }
    }

    /**
	 * Fires value removed event and removes value from container if contained
	 * in it. Use this call rather than making the decision to short circuit
	 * yourself for safe handling of null.
	 * 
	 * @param <T>
	 *            the value type
	 * @param source
	 *            the source of the handlers
	 * @param container
	 *            the container, the value should be contained in, may be null
	 * @param valueToBeRemoved
	 *            the value which should be removed from the given container,
	 *            may be null
	 */
    public static <T> boolean removeAndFireIfNotNull(HasValueRemovedHandlers<T> source, Collection<T> container, T valueToBeRemoved) {
        if (shouldFire(source, container, valueToBeRemoved)) {
            container.remove(valueToBeRemoved);
            ValueRemovedEvent<T> event = new ValueRemovedEvent<T>(valueToBeRemoved);
            source.fireEvent(event);
            return true;
        }
        return false;
    }

    /**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
    public static Type<ValueRemovedHandler<?>> getType() {
        if (TYPE == null) {
            TYPE = new Type<ValueRemovedHandler<?>>();
        }
        return TYPE;
    }

    /**
	 * Convenience method to allow subtypes to know when they should fire a
	 * value remove event in a null-safe manner.
	 * 
	 * @param <I>
	 *            value type
	 * @param source
	 *            the source
	 * @param container
	 *            the container, the value should be removed from, may be null
	 * @param valueToBeRemoved
	 *            the value to be removed from the given container, may be null
	 * @return whether the event should be fired, because the value to be
	 *         removed is still in the container
	 */
    protected static <T> boolean shouldFire(HasValueRemovedHandlers<T> source, Collection<T> container, T valueToBeRemoved) {
        return TYPE != null && container != null && container.contains(valueToBeRemoved);
    }

    private final T value;

    /**
	 * Creates a value change event.
	 * 
	 * @param value
	 *            the value
	 */
    protected ValueRemovedEvent(T value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Type<ValueRemovedHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    /**
	 * Gets the value.
	 * 
	 * @return the value
	 */
    public T getValue() {
        return value;
    }

    @Override
    public String toDebugString() {
        return super.toDebugString() + getValue();
    }

    @Override
    protected void dispatch(ValueRemovedHandler<T> handler) {
        handler.onValueRemoved(this);
    }
}
