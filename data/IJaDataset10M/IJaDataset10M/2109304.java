package org.gamegineer.table.internal.net.node.server;

import org.easymock.IMocksControl;
import org.gamegineer.table.internal.net.node.AbstractRemoteNodeControllerTestCase;

/**
 * A fixture for testing the basic aspects of classes that implement the
 * {@link org.gamegineer.table.internal.net.node.server.IRemoteClientNodeController}
 * interface.
 * 
 * @param <T>
 *        The type of the remote client node controller.
 */
public abstract class AbstractRemoteClientNodeControllerTestCase<T extends IRemoteClientNodeController> extends AbstractRemoteNodeControllerTestCase<T, IServerNode, IRemoteClientNode> {

    /**
     * Initializes a new instance of the
     * {@code AbstractRemoteClientNodeControllerTestCase} class.
     */
    protected AbstractRemoteClientNodeControllerTestCase() {
    }

    @Override
    protected final IServerNode createMockLocalNode(final IMocksControl mocksControl) {
        return mocksControl.createMock(IServerNode.class);
    }
}
