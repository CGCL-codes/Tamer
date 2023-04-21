package org.gamegineer.table.internal.net.node;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentLegal;
import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import net.jcip.annotations.ThreadSafe;
import org.gamegineer.common.core.util.concurrent.TaskUtils;
import org.gamegineer.table.net.ITableNetworkConfiguration;

/**
 * Ensures a block of code is executed on a node layer thread.
 */
@ThreadSafe
public final class NodeLayerRunner {

    /** The table network node that manages the node layer. */
    private final AbstractNode<?> node_;

    /**
     * Initializes a new instance of the {@code NodeLayerRunner} class.
     * 
     * @param node
     *        The node that manages the node layer; must not be {@code null}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code node} is {@code null}.
     */
    public NodeLayerRunner(final AbstractNode<?> node) {
        assertArgumentNotNull(node, "node");
        node_ = node;
    }

    /**
     * Synchronously connects the associated table network node to the table
     * network using the specified configuration.
     * 
     * @param configuration
     *        The table network configuration; must not be {@code null}.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     * @throws java.lang.NullPointerException
     *         If {@code configuration} is {@code null}.
     */
    public void connect(final ITableNetworkConfiguration configuration) throws Exception {
        assertArgumentNotNull(configuration, "configuration");
        final Future<Void> future = run(new Callable<Future<Void>>() {

            @Override
            @SuppressWarnings("synthetic-access")
            public Future<Void> call() {
                return node_.beginConnect(configuration);
            }
        });
        node_.endConnect(future);
    }

    /**
     * Synchronously disconnects the associated table network node from the
     * table network.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    public void disconnect() throws Exception {
        final Future<Void> future = run(new Callable<Future<Void>>() {

            @Override
            @SuppressWarnings("synthetic-access")
            public Future<Void> call() {
                return node_.beginDisconnect();
            }
        });
        node_.endDisconnect(future);
    }

    public <V> V run(final Callable<V> task) throws Exception {
        assertArgumentNotNull(task, "task");
        return run(task, Collections.<Class<? extends Exception>>emptyList());
    }

    public <V> V run(final Callable<V> task, final Class<? extends Exception> exceptionType) throws Exception {
        assertArgumentNotNull(task, "task");
        assertArgumentNotNull(exceptionType, "exceptionType");
        return run(task, Collections.<Class<? extends Exception>>singletonList(exceptionType));
    }

    public <V> V run(final Callable<V> task, final Collection<Class<? extends Exception>> exceptionTypes) throws Exception {
        assertArgumentNotNull(task, "task");
        assertArgumentNotNull(exceptionTypes, "exceptionTypes");
        assertArgumentLegal(!exceptionTypes.contains(null), "exceptionTypes");
        try {
            return node_.getNodeLayer().syncExec(task);
        } catch (final ExecutionException e) {
            final Throwable cause = e.getCause();
            for (final Class<? extends Exception> exceptionType : exceptionTypes) {
                if (exceptionType == cause.getClass()) {
                    throw exceptionType.cast(cause);
                }
            }
            throw TaskUtils.launderThrowable(cause);
        }
    }

    public void run(final Runnable task) throws Exception {
        assertArgumentNotNull(task, "task");
        try {
            node_.getNodeLayer().syncExec(task);
        } catch (final ExecutionException e) {
            throw TaskUtils.launderThrowable(e.getCause());
        }
    }
}
