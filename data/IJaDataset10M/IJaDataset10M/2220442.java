package starcraft.comm.messagesToServer.lobby;

import common.communication.messages.IMessageToServer;

public final class JoinGameMessage implements IMessageToServer {

    private static final long serialVersionUID = 1L;

    public String name;

    public JoinGameMessage(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "join game: " + name;
    }
}
