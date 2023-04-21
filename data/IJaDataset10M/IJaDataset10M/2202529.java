package mw.server.model.zone;

import java.io.Serializable;
import mw.server.model.MagicWarsModel;
import mw.server.model.MagicWarsModel.GameZone;

public class PlayerZone implements Serializable {

    private GameZone gameZone;

    private int playerId;

    public PlayerZone(GameZone gameZone, int playerId) {
        this.gameZone = gameZone;
        this.playerId = playerId;
    }

    public GameZone getGameZone() {
        return gameZone;
    }

    public int getPlayerId() {
        return playerId;
    }

    private static final long serialVersionUID = 1L;
}
