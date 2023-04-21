package org.gamegineer.engine.internal.core.extensions.commandqueue;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import org.gamegineer.engine.core.ICommand;
import org.gamegineer.engine.core.IEngineContext;
import org.gamegineer.engine.core.extensions.commandqueue.AbstractCommandQueueTestCase;
import org.gamegineer.engine.core.extensions.commandqueue.ICommandQueue;

/**
 * A fixture for testing the
 * {@link org.gamegineer.engine.internal.core.extensions.commandqueue.CommandQueueExtension}
 * class to ensure it does not violate the contract of the
 * {@link org.gamegineer.engine.core.extensions.commandqueue.ICommandQueue}
 * interface.
 */
public final class CommandQueueExtensionAsCommandQueueTest extends AbstractCommandQueueTestCase {

    /**
     * Initializes a new instance of the
     * {@code CommandQueueExtensionAsCommandQueueTest} class.
     */
    public CommandQueueExtensionAsCommandQueueTest() {
        super();
    }

    @Override
    protected ICommandQueue createCommandQueue(final IEngineContext context) throws Exception {
        assertArgumentNotNull(context, "context");
        final CommandQueueExtension extension = new CommandQueueExtension(new FakeCommandQueue());
        extension.start(context);
        return extension;
    }

    @Override
    protected void shutdownCommandQueue(final ICommandQueue commandQueue) throws Exception {
        final CommandQueueExtension extension = (CommandQueueExtension) commandQueue;
        final FakeCommandQueue delegate = (FakeCommandQueue) extension.getDelegate();
        delegate.shutdown();
    }

    /**
     * A fake implementation of {@code ICommandQueue} to test the implementation
     * of {@code CommandQueueExtension}.
     */
    private static final class FakeCommandQueue implements ICommandQueue {

        /**
         * The executor service used to execute commands submitted to this
         * queue.
         */
        private final ExecutorService m_executorService;

        /**
         * Initializes a new instance of the {@code FakeCommandQueue} class.
         */
        FakeCommandQueue() {
            m_executorService = Executors.newSingleThreadExecutor();
        }

        /**
         * Shuts down this command queue.
         */
        void shutdown() {
            m_executorService.shutdown();
        }

        public <T> Future<T> submitCommand(final IEngineContext context, final ICommand<T> command) {
            assertArgumentNotNull(context, "context");
            assertArgumentNotNull(command, "command");
            final Callable<T> callable = new Callable<T>() {

                public T call() throws Exception {
                    return command.execute(context);
                }
            };
            try {
                return m_executorService.submit(callable);
            } catch (final RejectedExecutionException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
