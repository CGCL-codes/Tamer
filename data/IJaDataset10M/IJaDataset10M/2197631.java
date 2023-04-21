package org.aquastarz.score.manager;

import java.util.List;
import javax.persistence.Query;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.domain.Team;

public class TeamManager {

    public static Team findById(String teamId) {
        return ScoreApp.getEntityManager().find(Team.class, teamId);
    }

    public static List<Team> findAllTeams() {
        Query teamQuery = ScoreApp.getEntityManager().createNamedQuery("Team.findAllOrderByTeamId");
        List<Team> teams = teamQuery.getResultList();
        return teams;
    }
}
