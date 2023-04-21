package com.google.common.util.concurrent;

/**
 * A simple ListenableFuture that holds a value or an exception.
 *
 * @author Sven Mawson
 * @since 9.09.15 <b>tentative</b>
 */
public class ValueFuture<V> extends AbstractListenableFuture<V> {

    /**
   * Creates a new {@code ValueFuture} in the default state.
   */
    public static <T> ValueFuture<T> create() {
        return new ValueFuture<T>();
    }

    /**
   * Explicit private constructor, use the {@link #create} factory method to
   * create instances of {@code ValueFuture}.
   */
    private ValueFuture() {
    }

    /**
   * Sets the value of this future.  This method will return {@code true} if
   * the value was successfully set, or {@code false} if the future has already
   * been set or cancelled.
   *
   * @param newValue the value the future should hold.
   * @return true if the value was successfully set.
   */
    @Override
    public boolean set(V newValue) {
        return super.set(newValue);
    }

    /**
   * Sets the future to having failed with the given exception.  This exception
   * will be wrapped in an ExecutionException and thrown from the get methods.
   * This method will return {@code true} if the exception was successfully set,
   * or {@code false} if the future has already been set or cancelled.
   *
   * @param t the exception the future should hold.
   * @return true if the exception was successfully set.
   */
    @Override
    public boolean setException(Throwable t) {
        return super.setException(t);
    }

    /**
   * {@inheritDoc}
   *
   * <p>A ValueFuture is never considered in the running state, so the
   * {@code mayInterruptIfRunning} argument is ignored.
   */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return super.cancel();
    }
}
