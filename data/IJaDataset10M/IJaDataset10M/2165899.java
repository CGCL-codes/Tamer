package fr.alesia.deepstack.stat.simple;

import java.util.Set;
import fr.alesia.deepstack.bean.Player;
import fr.alesia.deepstack.bean.Ranking;
import fr.alesia.deepstack.data.RankingDao;
import fr.alesia.deepstack.factory.DaoFactory;
import fr.alesia.deepstack.factory.ServiceFactory;
import fr.alesia.deepstack.stat.PlayerStatistics;

public class PlayerStatisticsSimple implements PlayerStatistics {

    private static RankingDao daoRankings;

    private Player player;

    public PlayerStatisticsSimple(Player player) {
        daoRankings = DaoFactory.getRankingDao();
        this.player = player;
    }

    public Float getAverageNumberOfPoints() {
        Set<Ranking> rankings = daoRankings.findByPlayer(player);
        int r = rankings.size();
        int points = 0;
        for (Ranking ranking : rankings) {
            points = points + ranking.getPoints();
        }
        rankings = null;
        return (r == 0 ? null : (float) points / (float) r);
    }

    public Integer getNumberOfHeadsUp() {
        Set<Ranking> rankings = daoRankings.findByPlayer(player);
        int n = 0;
        for (Ranking ranking : rankings) {
            if (ranking.getRank() == 1 || ranking.getRank() == 2) {
                n++;
            }
        }
        rankings = null;
        return n;
    }

    public Integer getNumberOfDefeat() {
        Set<Ranking> rankings = daoRankings.findByPlayer(player);
        int n = 0;
        for (Ranking ranking : rankings) {
            if (ranking.getRank() == ServiceFactory.getTournamentService(ranking.getTournament()).getNbPlayers()) {
                n++;
            }
        }
        rankings = null;
        return n;
    }

    public Integer getNumberOfPoints() {
        Set<Ranking> rankings = daoRankings.findByPlayer(player);
        int n = 0;
        for (Ranking ranking : rankings) {
            n = n + ranking.getPoints();
        }
        rankings = null;
        return n;
    }

    public Integer getNumberOfTournaments() {
        Set<Ranking> rankings = daoRankings.findByPlayer(player);
        int n = rankings.size();
        rankings = null;
        return n;
    }

    public Integer getNumberOfVictories() {
        Set<Ranking> rankings = daoRankings.findByPlayer(player);
        int n = 0;
        for (Ranking ranking : rankings) {
            if (ranking.getRank() == 1) {
                n++;
            }
        }
        rankings = null;
        return n;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
