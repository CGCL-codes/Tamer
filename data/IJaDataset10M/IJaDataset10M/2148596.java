package logic.common.player.playerListeners;

import logic.common.team.Team;

public interface PlayerTeamListener {

    public static final String TEAMCHANGED = "teamChanged";

    public static final String TEAMCHANGEFAILED = "teamChangeFailed";

    public void teamChanged(Team newTeam);

    public void teamChangeFailed();
}
