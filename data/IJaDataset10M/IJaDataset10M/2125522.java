package org.waveprotocol.wave.model.operation;

/**
 * A special sink that has a contract not to throw checked exceptions.
 *
 * This interface is suitable for notification sinks, where a caller has no
 * reasonable context from which to recover from an exception anyway, so the
 * responsibility for handling the exception is given to the sink itself.
 *
 * @param <T> consumed operation type
 * @see OperationSink
 */
public interface SilentOperationSink<T extends Operation<?>> {

    /**
   * A silent operation sink that does nothing with consumed operations.
   */
    static final SilentOperationSink<Operation<?>> VOID = new SilentOperationSink<Operation<?>>() {

        @Override
        public void consume(Operation<?> op) {
        }
    };

    /**
   * A silent operation sink that throws an exception on any operation.
   */
    static final SilentOperationSink<Operation<?>> BLOCKED = new SilentOperationSink<Operation<?>>() {

        @Override
        public void consume(Operation<?> op) {
            throw new IllegalStateException("Operation sent to exception sink");
        }
    };

    /**
   * Provides a type-checked void sink.
   */
    static final class Void {

        @SuppressWarnings("unchecked")
        public static <T extends Operation<?>> SilentOperationSink<T> get() {
            return (SilentOperationSink<T>) VOID;
        }
    }

    /**
   * Provides a type-checked blocked sink.
   */
    static final class Blocked {

        @SuppressWarnings("unchecked")
        public static <T extends Operation<?>> SilentOperationSink<T> get() {
            return (SilentOperationSink<T>) BLOCKED;
        }
    }

    /**
   * Builds operation sinks which simply apply sunk operations to a target.
   */
    final class Executor {

        /**
     * Creates a new operation sink which applies all received ops to a target.
     *
     * Operation failure results in an {@link OperationRuntimeException}. Be
     * wary of using this in non-test code; operation failure must be handled.
     *
     * @param target target to which to apply ops.
     * @param <O> type of operations sunk
     * @param <T> type of the operation target
     */
        public static <O extends Operation<? super T>, T> SilentOperationSink<O> build(final T target) {
            return new SilentOperationSink<O>() {

                @Override
                public void consume(O op) {
                    try {
                        op.apply(target);
                    } catch (OperationException e) {
                        throw new OperationRuntimeException("Operation failed in silent operation executor", e);
                    }
                }
            };
        }
    }

    /**
   * Consumes an operation.  Usually, this will involve finding an appropriate
   * target for the operation, then calling {@link Operation#apply(Object)} on
   * that target.  However, this is not a strong guarantee.  The only contract
   * for a sink is to ensure that the intent of the given operation is
   * effected.
   *
   * @param op  operation to apply
   */
    public void consume(T op);
}
