package org.gamegineer.table.internal.net.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import net.jcip.annotations.Immutable;
import org.easymock.EasyMock;
import org.gamegineer.table.internal.net.ITableNetworkController;
import org.gamegineer.table.internal.net.TableNetworkConfigurations;
import org.gamegineer.table.internal.net.transport.ITransportLayer;
import org.gamegineer.table.internal.net.transport.ITransportLayerContext;
import org.gamegineer.table.internal.net.transport.fake.FakeTransportLayerFactory;
import org.gamegineer.table.net.IPlayer;
import org.gamegineer.table.net.ITableNetworkConfiguration;
import org.gamegineer.table.net.TableNetworkException;
import org.junit.Before;
import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.table.internal.net.node.AbstractNode} class.
 */
public final class AbstractNodeTest {

    /** The table network node under test in the fixture. */
    private volatile AbstractNode<?> node_;

    /** The node layer runner for use in the fixture. */
    private NodeLayerRunner nodeLayerRunner_;

    /**
     * Initializes a new instance of the {@code AbstractNodeTest} class.
     */
    public AbstractNodeTest() {
    }

    /**
     * Sets up the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Before
    public void setUp() throws Exception {
        node_ = new MockNode.Factory().createNode(EasyMock.createMock(ITableNetworkController.class));
        nodeLayerRunner_ = new NodeLayerRunner(node_);
    }

    /**
     * Ensures the connect operation adds a table proxy for the local player
     * before either the {@code connecting} or {@code connected} methods are
     * invoked.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test
    public void testConnect_AddsLocalTableProxy() throws Exception {
        final ITableNetworkConfiguration configuration = TableNetworkConfigurations.createDefaultTableNetworkConfiguration();
        final MockNode.Factory nodeFactory = new MockNode.Factory() {

            @Override
            protected MockNode createNode(final INodeLayer nodeLayer, final ITableNetworkController tableNetworkController) {
                return new MockNode(nodeLayer, tableNetworkController) {

                    @Override
                    protected void connected() throws TableNetworkException {
                        super.connected();
                        assertTrue(isTableBound(configuration.getLocalPlayerName()));
                    }

                    @Override
                    protected void connecting(@SuppressWarnings("hiding") final ITableNetworkConfiguration configuration) throws TableNetworkException {
                        super.connecting(configuration);
                        assertTrue(isTableBound(configuration.getLocalPlayerName()));
                    }
                };
            }
        };
        final MockNode node = nodeFactory.createNode(EasyMock.createMock(ITableNetworkController.class));
        final NodeLayerRunner nodeLayerRunner = new NodeLayerRunner(node);
        nodeLayerRunner.connect(configuration);
    }

    /**
     * Ensures the constructor throws an exception when passed a {@code null}
     * node layer.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructor_NodeLayer_Null() {
        new MockNode(null, EasyMock.createMock(ITableNetworkController.class));
    }

    /**
     * Ensures the constructor throws an exception when passed a {@code null}
     * table network controller.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructor_TableNetworkController_Null() {
        new MockNode(EasyMock.createMock(INodeLayer.class), null);
    }

    /**
     * Ensures the disconnect operation removes the table proxy for the local
     * player after the {@code disconnecting} method is invoked but before the
     * {@code disconnected} method is invoked.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test
    public void testDisconnect_RemovesLocalTableProxy() throws Exception {
        final ITableNetworkConfiguration configuration = TableNetworkConfigurations.createDefaultTableNetworkConfiguration();
        final MockNode.Factory nodeFactory = new MockNode.Factory() {

            @Override
            protected MockNode createNode(final INodeLayer nodeLayer, final ITableNetworkController tableNetworkController) {
                return new MockNode(nodeLayer, tableNetworkController) {

                    @Override
                    protected void disconnected() {
                        super.disconnected();
                        assertFalse(isTableBound(configuration.getLocalPlayerName()));
                    }

                    @Override
                    protected void disconnecting() {
                        super.disconnecting();
                        assertTrue(isTableBound(configuration.getLocalPlayerName()));
                    }
                };
            }
        };
        final MockNode node = nodeFactory.createNode(EasyMock.createMock(ITableNetworkController.class));
        final NodeLayerRunner nodeLayerRunner = new NodeLayerRunner(node);
        nodeLayerRunner.connect(configuration);
        nodeLayerRunner.disconnect();
    }

    /**
     * Ensures the {@code getRemoteNode} method throws an exception when passed
     * a {@code null} player name.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test(expected = NullPointerException.class)
    public void testGetRemoteNode_PlayerName_Null() throws Exception {
        nodeLayerRunner_.run(new Runnable() {

            @Override
            @SuppressWarnings("synthetic-access")
            public void run() {
                node_.getRemoteNode(null);
            }
        });
    }

    /**
     * Ensures the {@code getRemoteNodes} method returns a copy of the bound
     * remote nodes collection.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test
    public void testGetRemoteNodes_ReturnValue_Copy() throws Exception {
        nodeLayerRunner_.run(new Runnable() {

            @Override
            @SuppressWarnings("synthetic-access")
            public void run() {
                final Collection<?> remoteNodes = node_.getRemoteNodes();
                final int expectedRemoteNodesSize = remoteNodes.size();
                remoteNodes.add(null);
                final int actualRemoteNodesSize = node_.getRemoteNodes().size();
                assertEquals(expectedRemoteNodesSize, actualRemoteNodesSize);
            }
        });
    }

    /**
     * Mock implementation of {@link AbstractNode}.
     */
    @Immutable
    private static class MockNode extends AbstractNode<IRemoteNode> {

        /**
         * Initializes a new instance of the {@code MockNode} class.
         * 
         * @param nodeLayer
         *        The node layer; must not be {@code null}.
         * @param tableNetworkController
         *        The table network controller; must not be {@code null}.
         * 
         * @throws java.lang.NullPointerException
         *         If {@code nodeLayer} or {@code tableNetworkController} is
         *         {@code null}.
         */
        MockNode(final INodeLayer nodeLayer, final ITableNetworkController tableNetworkController) {
            super(nodeLayer, tableNetworkController);
        }

        @Override
        public void cancelControlRequest() {
        }

        @Override
        protected ITransportLayer createTransportLayer() {
            return new FakeTransportLayerFactory().createActiveTransportLayer(EasyMock.createNiceMock(ITransportLayerContext.class));
        }

        @Override
        public IPlayer getPlayer() {
            return null;
        }

        @Override
        public Collection<IPlayer> getPlayers() {
            return Collections.emptyList();
        }

        @Override
        public ITableManager getTableManager() {
            return EasyMock.createMock(ITableManager.class);
        }

        @Override
        public void giveControl(@SuppressWarnings("unused") final String playerName) {
        }

        /**
         * Indicates a table is bound to this node for the specified player
         * name.
         * 
         * @param playerName
         *        The player name; must not be {@code null}.
         * 
         * @return {@code true} if a table is bound to this node for the
         *         specified player name; otherwise {@code false}.
         */
        final boolean isTableBound(final String playerName) {
            assert playerName != null;
            try {
                final Field field = AbstractNode.class.getDeclaredField("tables_");
                field.setAccessible(true);
                final Map<?, ?> tables = (Map<?, ?>) field.get(this);
                return tables.containsKey(playerName);
            } catch (final Exception e) {
                throw new AssertionError(e);
            }
        }

        @Override
        public void requestControl() {
        }

        /**
         * A factory for creating instances of {@link MockNode}.
         */
        @Immutable
        static class Factory extends AbstractFactory<MockNode> {

            /**
             * Initializes a new instance of the {@code Factory} class.
             */
            Factory() {
            }

            @Override
            protected MockNode createNode(final INodeLayer nodeLayer, final ITableNetworkController tableNetworkController) {
                return new MockNode(nodeLayer, tableNetworkController);
            }
        }
    }
}
