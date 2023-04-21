package org.gamegineer.table.internal.net.node.client.handlers;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.gamegineer.table.internal.net.node.IMessageHandler;
import org.gamegineer.table.internal.net.node.client.IClientNode;
import org.gamegineer.table.internal.net.node.client.IRemoteServerNodeController;
import org.gamegineer.table.internal.net.node.common.messages.GoodbyeMessage;
import org.gamegineer.table.net.TableNetworkError;
import org.junit.Before;
import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.table.internal.net.node.client.handlers.GoodbyeMessageHandler}
 * class.
 */
public final class GoodbyeMessageHandlerTest {

    /** The message handler under test in the fixture. */
    private IMessageHandler messageHandler_;

    /** The mocks control for use in the fixture. */
    private IMocksControl mocksControl_;

    /**
     * Initializes a new instance of the {@code GoodbyeMessageHandlerTest}
     * class.
     */
    public GoodbyeMessageHandlerTest() {
    }

    /**
     * Sets up the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Before
    public void setUp() throws Exception {
        mocksControl_ = EasyMock.createControl();
        messageHandler_ = GoodbyeMessageHandler.INSTANCE;
    }

    /**
     * Ensures the {@code handleMessage} method correctly handles a goodbye
     * message.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test
    public void testHandleMessage_GoodbyeMessage() throws Exception {
        final IClientNode localNode = mocksControl_.createMock(IClientNode.class);
        localNode.disconnect(TableNetworkError.SERVER_SHUTDOWN);
        final IRemoteServerNodeController remoteNodeController = mocksControl_.createMock(IRemoteServerNodeController.class);
        EasyMock.expect(remoteNodeController.getLocalNode()).andReturn(localNode).anyTimes();
        mocksControl_.replay();
        final GoodbyeMessage message = new GoodbyeMessage();
        messageHandler_.handleMessage(remoteNodeController, message);
        mocksControl_.verify();
    }
}
