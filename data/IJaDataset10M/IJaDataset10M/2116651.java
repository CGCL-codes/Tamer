package architecture.common.event.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.concurrent.Executor;
import architecture.common.event.spi.EventDispatcher;
import architecture.common.event.spi.EventExecutorFactory;
import architecture.common.event.spi.ListenerInvoker;

/**
 * This dispatcher will dispatch event asynchronously if:
 * <ul>
 * <li>the event 'is' asynchronous, as resolved by the
 * {@link AsynchronousEventResolver} and</li>
 * <li>the invoker
 * {@link architecture.common.event.api.ListenerInvoker#supportAsynchronousEvents()
 * supports asynchronous events}</li>
 * </ul>
 * 
 * @since 2.0
 */
public final class AsynchronousAbleEventDispatcher implements EventDispatcher {

    /**
     * An executor that execute commands synchronously
     */
    private static final Executor SYNCHRONOUS_EXECUTOR = new Executor() {

        public void execute(Runnable command) {
            command.run();
        }
    };

    /**
     * An asynchronous executor
     */
    private final Executor asynchronousExecutor;

    /**
	 * @uml.property  name="asynchronousEventResolver"
	 * @uml.associationEnd  
	 */
    private final AsynchronousEventResolver asynchronousEventResolver;

    /**
     * The only public constructor, uses an {@link architecture.common.event.internal.AnnotationAsynchronousEventResolver}
     * @param executorFactory the executor to use for asynchronous event listener invocations
     */
    public AsynchronousAbleEventDispatcher(EventExecutorFactory executorFactory) {
        this(executorFactory, new AnnotationAsynchronousEventResolver());
    }

    public AsynchronousAbleEventDispatcher(Executor asynchronousExecutor) {
        this.asynchronousEventResolver = checkNotNull(new AnnotationAsynchronousEventResolver());
        this.asynchronousExecutor = checkNotNull(asynchronousExecutor);
    }

    AsynchronousAbleEventDispatcher(EventExecutorFactory executorFactory, AsynchronousEventResolver asynchronousEventResolver) {
        this.asynchronousEventResolver = checkNotNull(asynchronousEventResolver);
        this.asynchronousExecutor = checkNotNull(checkNotNull(executorFactory).getExecutor());
    }

    public void dispatch(final ListenerInvoker invoker, final Object event) {
        getExecutor(checkNotNull(invoker), checkNotNull(event)).execute(new Runnable() {

            public void run() {
                invoker.invoke(event);
            }
        });
    }

    private Executor getExecutor(ListenerInvoker invoker, Object event) {
        return asynchronousEventResolver.isAsynchronousEvent(event) && invoker.supportAsynchronousEvents() ? asynchronousExecutor : SYNCHRONOUS_EXECUTOR;
    }
}
