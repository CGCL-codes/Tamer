package org.gamegineer.table.internal.net.node.client;

import net.jcip.annotations.Immutable;
import org.gamegineer.table.internal.net.node.common.messages.PlayersMessage;

/**
 * A message handler for the {@link PlayersMessage} message.
 */
@Immutable
final class PlayersMessageHandler extends AbstractMessageHandler {

    /**
     * Initializes a new instance of the {@code PlayersMessageHandler} class.
     * 
     * @param remoteNodeController
     *        The control interface for the remote node associated with the
     *        message handler; must not be {@code null}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code remoteNodeController} is {@code null}.
     */
    PlayersMessageHandler(final IRemoteServerNodeController remoteNodeController) {
        super(remoteNodeController);
    }

    /**
     * Handles a {@code PlayersMessage} message.
     * 
     * @param message
     *        The message; must not be {@code null}.
     */
    @SuppressWarnings("unused")
    private void handleMessage(final PlayersMessage message) {
        assert message != null;
        getRemoteNodeController().getLocalNode().setPlayers(message.getPlayers());
    }
}
