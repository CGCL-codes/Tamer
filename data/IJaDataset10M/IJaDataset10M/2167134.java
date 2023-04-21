package be.jabapage.snooker.container;

import java.io.Serializable;
import lombok.Data;
import be.jabapage.snooker.jdo.administration.Team;
import be.jabapage.snooker.jdo.competition.CompetitionGame;

@Data
public class GameContainer implements Serializable {

    private static final long serialVersionUID = -7007627275713435310L;

    private Team homeTeam;

    private Team awayTeam;

    private CompetitionGame game;
}
