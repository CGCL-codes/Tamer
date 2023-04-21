package de.volkerraum.pokerbot.model;

import java.io.Serializable;
import de.volkerraum.pokerbot.tableengine.PlayerInfo;

public class AllPlayerData implements Serializable {

    PlayerInfo playerInfo = null;

    long playerId;

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public long getTournamentPlayerId() {
        return playerId;
    }

    public void setTournamentPlayerId(long playerId) {
        this.playerId = playerId;
    }
}
