package player.action;

import java.io.Serializable;
import player.Player;
import team.Team;

public interface Action extends Serializable {

    void start(Player myPlayer, Team myTeam, Team opponentTeam);
}
