package br.com.meiao.escopa.player;

import br.com.meiao.game.core.Team;
import br.com.meiao.game.player.Player;

public abstract class EscopaAIPlayer implements Player {

    @SuppressWarnings("unused")
    private Team team;

    public void setTeam(Team t) {
        this.team = t;
    }
}
