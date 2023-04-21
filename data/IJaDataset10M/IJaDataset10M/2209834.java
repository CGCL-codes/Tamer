package com.myapp.games.schnellen.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import com.myapp.games.schnellen.frontend.IPlayerFrontend;

public interface IScorings extends Serializable {

    /**
     * answers the number of games that were won by a specific player
     * 
     * @param player
     * @return
     */
    int getGamesWon(String player);

    /**
     * returns the score of the player. unfinished rounds are not considered in
     * this score.
     * 
     * @see Round#getPunchCount(IPlayerFrontend)
     * @see Round#getScoreFactor()
     * @see Round#isGameFinal()
     * 
     * @param p
     *            the player to request the score for
     * @return the score
     */
    int getScore(String p);

    /**
     * determines if there is a winner in this game. the game is final after the
     * first player has reached 15 points.unfinished rounds are not considered
     * in this score. this will also refresh the current winners collection
     * available through {@link Game#getWinners()}
     * 
     * @see Round#getPunchCount(IPlayerFrontend)
     * @see Round#getScoreFactor()
     * 
     * @return if the game is final
     */
    boolean isGameFinal();

    /**
     * calculates a list of players with their current total score. highest
     * players are on the start of the list. unfinished rounds' points are not
     * considered in this ranking.
     * 
     * @return the score ranking of the current game.
     */
    List<Entry<String, Integer>> getRankings();

    /**
     * when the game is final this will return the winner whose score is the
     * highest and more than {@link Config#getScoreGoal()}. if the score of two
     * or more players is equal to the highest, they will all be winners.
     * //TODO: game-variation: revenge with double score each round between them
     * //TODO: game-variation: one round double or nothing
     * 
     * @return the winner of this game, or more
     */
    Collection<String> getWinners();

    /**
     * the score factor will be used to multiply the POINTS OF ONE ROUND
     * after it was played to add them to the player's total
     * score. the factor will be reset to 1 after each round.
     *
     * @return the current score factor for this round
     */
    int getScoreFactor();
}
