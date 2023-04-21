package soccersim.base;

import java.util.HashMap;

/**
 * This embodies the score for a given game. It keeps up with two teams via the side they
 * are defending.
 * 
 * @author frank hadder, frank.hadder@gmail.com
 * @version 1.0
 */
public class Score {

    /**
	 * Constructs the score for a game with two teams on the two defending sides.
	 */
    public Score() {
        score = new HashMap<DefendingSide, Integer>(2);
        score.put(DefendingSide.East, 0);
        score.put(DefendingSide.West, 0);
    }

    /**
	 * Increases the score of the given side by 1. 
	 * 
	 * @param side the side of the team to increment the score.
	 * @throws RuntimeException if the side specified isn't being tracked in this object.
	 */
    public void incrementScore(DefendingSide side) {
        if (score.containsKey(side)) {
            score.put(side, score.get(side) + 1);
        } else {
            throw new RuntimeException("There is no score for the side " + side);
        }
    }

    /**
	 * This returns the score for a particular side.
	 * 
	 * @param side the DefendingSide of the team of whose score to return.
	 * @return int the score of the DefendingSide
	 * @throws RuntimeException if the side specified isn't being tracked in this object.
	 */
    public int getScore(DefendingSide side) {
        if (score.get(side) != null) {
            return score.get(side);
        }
        throw new RuntimeException("There is no score for the side " + side);
    }

    /**
	 * Gets the highest score of either side.
	 * @return the highest score of either side.
	 */
    public int getHighestScore() {
        return Math.max(score.get(DefendingSide.East), score.get(DefendingSide.West));
    }

    private HashMap<DefendingSide, Integer> score;
}
