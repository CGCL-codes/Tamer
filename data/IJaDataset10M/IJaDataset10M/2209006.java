package logic.common.missions;

import logic.common.player.Player;

public class PlayerMissionEvent extends MissionEvent {

    protected Player player;

    public PlayerMissionEvent(Mission mission, Player player) {
        super(mission);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
