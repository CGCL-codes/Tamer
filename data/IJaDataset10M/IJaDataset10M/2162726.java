package org.gamegineer.table.internal.net.node.server;

import net.jcip.annotations.Immutable;

/**
 * Superclass for all message handlers associated with a remote client node.
 */
@Immutable
abstract class AbstractMessageHandler extends org.gamegineer.table.internal.net.node.AbstractMessageHandler<IRemoteClientNodeController> {

    /**
     * Initializes a new instance of the {@code AbstractMessageHandler} class.
     * 
     * @param remoteNodeController
     *        The control interface for the remote node associated with the
     *        message handler; must not be {@code null}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code remoteNodeController} is {@code null}.
     */
    AbstractMessageHandler(final IRemoteClientNodeController remoteNodeController) {
        super(remoteNodeController);
    }
}
