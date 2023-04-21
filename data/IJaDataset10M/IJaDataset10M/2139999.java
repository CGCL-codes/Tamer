package tictactoe.util;

import tictactoe.Desk;
import tictactoe.Game;

public class Result {

    private int winner;

    private int turns;

    private Desk screenShot;

    public Result(Game game) {
        winner = game.getWinner();
        turns = game.getTurnCount();
        screenShot = game.getDeskCopy();
    }

    public int getWinner() {
        return winner;
    }

    public int getTurnsCount() {
        return turns;
    }

    public Desk getDesk() {
        return (Desk) screenShot.clone();
    }
}
