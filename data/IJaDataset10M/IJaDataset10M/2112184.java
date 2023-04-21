package packets;

import elements.Consts.Priorities;
import network.Lobby;

/**
 *
 * @author Joel Garboden
 */
public class StartGameReq extends Request {

    /**
   *
   */
    public StartGameReq() {
        this.priority = Priorities.TURN_UPDATE;
    }

    @Override
    public boolean lobbyRequest(Lobby lobby) {
        lobby.startGameCmd(lobby.lobbyList);
        return true;
    }

    @Override
    public String toString() {
        return "StartGameRequest";
    }
}
