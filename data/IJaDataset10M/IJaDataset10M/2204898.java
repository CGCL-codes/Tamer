package com.sjakkforum;

import java.util.ArrayList;
import java.util.List;

public class Round {

    int roundNumber = 0;

    private List<Game> games = new ArrayList<Game>();

    public Round() {
    }

    public List<Game> getGames() {
        return games;
    }

    public void addGame(Game game) {
        game.setRoundNumber(getRoundNumber());
        games.add(game);
        game.getWhite().addGame(game);
        game.getBlack().addGame(game);
    }

    public void removeGame(Game game) {
        games.remove(game);
        game.getBlack().removeGame(game);
        game.getWhite().removeGame(game);
    }

    /**
     * @return Returns the roundNumber.
     */
    public int getRoundNumber() {
        return roundNumber;
    }

    /**
     * @param roundNumber The roundNumber to set.
     */
    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }
}
