package yatzy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private Map<String, Integer> playerScore = new HashMap<String, Integer>();

    public int score(String player) {
        if (!playerScore.containsKey(player)) {
            return 0;
        }
        return playerScore.get(player);
    }

    public void addRoll(String player, List<Integer> roll, Rule rule) {
        int score = score(player);
        score = score + rule.compute(roll);
        playerScore.put(player, score);
    }

    public Object computeScoreFor(Rule rule, int[] roll) {
        return null;
    }
}
