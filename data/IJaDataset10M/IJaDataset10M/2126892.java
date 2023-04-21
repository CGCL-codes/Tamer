package jswisstour.ranker;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jswisstour.Game;
import jswisstour.Player;

public final class PondElo extends AbstractRanker<Float> {

    private static final double K = 32;

    private static final double F = 2;

    private static final double INIT = 1500d;

    @Override
    public Map<Player, Comparable> compute(List<Player> players, Collection<Game<Float>> games) {
        final Map<Player, Double> elo = new HashMap<Player, Double>();
        for (Player p : players) {
            elo.put(p, INIT);
        }
        for (Game<Float> g : games) {
            final List<Player> gamePlayers = g.ranks();
            final Map<Player, Double> currentElo = new HashMap<Player, Double>();
            for (Player p : gamePlayers) {
                currentElo.put(p, elo.get(p));
            }
            final double firstPlayerScore = g.getScore(gamePlayers.get(0));
            for (int i = gamePlayers.size(); --i >= 0; ) {
                for (int j = i; --j >= 0; ) {
                    final Player winner = gamePlayers.get(j);
                    final Player loser = gamePlayers.get(i);
                    final double result = 1 / (1 + Math.exp(F * (g.getScore(loser) - g.getScore(winner)) / firstPlayerScore));
                    final double d = currentElo.get(winner) - currentElo.get(loser);
                    final double expected = 1 / (1 + Math.pow(10, -d / 400));
                    final double change = g.getFactor() * K * (result - expected) / (gamePlayers.size() - 1);
                    elo.put(winner, elo.get(winner) + change);
                    elo.put(loser, elo.get(loser) - change);
                }
            }
        }
        return new HashMap<Player, Comparable>(elo);
    }
}
