package org.gamegineer.table.internal.net.node;

import static org.junit.Assert.assertNotNull;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.gamegineer.table.internal.net.transport.IMessage;
import org.junit.Before;
import org.junit.Test;

/**
 * A fixture for testing the basic aspects of classes that implement the
 * {@link org.gamegineer.table.internal.net.node.IRemoteNodeController}
 * interface.
 * 
 * @param <T>
 *        The type of the remote node controller.
 * @param <LocalNodeType>
 *        The type of the local table network node.
 * @param <RemoteNodeType>
 *        The type of the remote table network node.
 */
public abstract class AbstractRemoteNodeControllerTestCase<T extends IRemoteNodeController<LocalNodeType>, LocalNodeType extends INode<RemoteNodeType>, RemoteNodeType extends IRemoteNode> {

    /** The table network node for use in the fixture. */
    private LocalNodeType node_;

    /** The mocks control used to create the mock local node in the fixture. */
    private IMocksControl nodeMocksControl_;

    /** The remote node controller under test in the fixture. */
    private T remoteNodeController_;

    /**
     * Initializes a new instance of the
     * {@code AbstractRemoteNodeControllerTestCase} class.
     */
    protected AbstractRemoteNodeControllerTestCase() {
    }

    protected abstract LocalNodeType createMockLocalNode(IMocksControl mocksControl);

    @SuppressWarnings("boxing")
    private static INodeLayer createMockNodeLayer() {
        final IMocksControl mocksControl = EasyMock.createControl();
        final INodeLayer nodeLayer = mocksControl.createMock(INodeLayer.class);
        EasyMock.expect(nodeLayer.isNodeLayerThread()).andReturn(true).anyTimes();
        mocksControl.replay();
        return nodeLayer;
    }

    protected abstract T createRemoteNodeController(INodeLayer nodeLayer, LocalNodeType node) throws Exception;

    protected final T getRemoteNodeController() {
        assertNotNull(remoteNodeController_);
        return remoteNodeController_;
    }

    /**
     * Opens the remote node associated with the specified controller.
     * 
     * @param remoteNodeController
     *        The remote node controller; must not be {@code null}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code remoteNodeController} is {@code null}.
     */
    protected abstract void openRemoteNode(T remoteNodeController);

    /**
     * Sets up the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Before
    public void setUp() throws Exception {
        nodeMocksControl_ = EasyMock.createNiceControl();
        node_ = createMockLocalNode(nodeMocksControl_);
        nodeMocksControl_.replay();
        remoteNodeController_ = createRemoteNodeController(createMockNodeLayer(), node_);
        assertNotNull(remoteNodeController_);
    }

    /**
     * Ensures the {@code bind} method throws an exception when the remote node
     * is already bound.
     */
    @Test(expected = IllegalStateException.class)
    public void testBind_Bound() {
        openRemoteNode(remoteNodeController_);
        remoteNodeController_.bind("playerName");
        remoteNodeController_.bind("playerName");
    }

    /**
     * Ensures the {@code bind} method throws an exception when the remote node
     * is closed.
     */
    @Test(expected = IllegalStateException.class)
    public void testBind_Closed() {
        remoteNodeController_.bind("playerName");
    }

    /**
     * Ensures the {@code bind} method throws an exception when passed a player
     * name that is associated with a table that is already bound to the local
     * node.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBind_PlayerName_Bound() throws Exception {
        final IMocksControl nodeMocksControl = EasyMock.createControl();
        final LocalNodeType node = createMockLocalNode(nodeMocksControl);
        node.bindRemoteNode(EasyMock.<RemoteNodeType>notNull());
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());
        nodeMocksControl.replay();
        final T remoteNodeController = createRemoteNodeController(createMockNodeLayer(), node);
        openRemoteNode(remoteNodeController);
        remoteNodeController.bind("playerName");
    }

    /**
     * Ensures the {@code bind} method throws an exception when passed a
     * {@code null} player name.
     */
    @Test(expected = NullPointerException.class)
    public void testBind_PlayerName_Null() {
        remoteNodeController_.bind(null);
    }

    /**
     * Ensures the {@code close} method throws an exception when the remote node
     * is closed.
     */
    @Test(expected = IllegalStateException.class)
    public void testClose_Closed() {
        remoteNodeController_.close(null);
    }

    /**
     * Ensures the {@code getLocalNode} method does not return {@code null}.
     */
    @Test
    public void testGetLocalNode_ReturnValue_NonNull() {
        assertNotNull(remoteNodeController_.getLocalNode());
    }

    /**
     * Ensures the {@code sendMessage} method throws an exception when the
     * remote node is closed.
     */
    @Test(expected = IllegalStateException.class)
    public void testSendMessage_Closed() {
        remoteNodeController_.sendMessage(EasyMock.createMock(IMessage.class), EasyMock.createMock(IMessageHandler.class));
    }

    /**
     * Ensures the {@code sendMessage} method throws an exception when passed a
     * {@code null} message.
     */
    @Test(expected = NullPointerException.class)
    public void testSendMessage_Message_Null() {
        remoteNodeController_.sendMessage(null, EasyMock.createMock(IMessageHandler.class));
    }
}
