package bs.teamgame;

import bs.*;

/**
 *
 * @author  Dante
 */
public class TeamGameInfo extends GameInfo {

    /** Creates a new instance of TeamGameInfo */
    public TeamGameInfo() {
        gameClass = TeamGame.class;
        playerInfoClass = TeamPlayerInfo.class;
        aiPlayerClass = TeamAIPlayer.class;
        viewPortClass = TeamGameViewPort.class;
        gameSetup = new TeamGameSetup();
    }

    public String toString() {
        return "Team Deathmatch";
    }
}
