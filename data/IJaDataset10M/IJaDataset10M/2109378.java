package tictactoe.util;

import tictactoe.Game;
import tictactoe.Player;
import java.util.ArrayList;
import java.util.List;

public class Score {

    private Player cross, circle;

    private int maximumGameCount;

    private List<Result> results;

    public Score(Player cross, Player circle, int maximumGameCount) {
        results = new ArrayList<Result>();
        this.cross = cross;
        this.circle = circle;
        this.maximumGameCount = maximumGameCount;
    }

    public void add(Game game) {
        if (gamesRemaining() > 0) {
            results.add(new Result(game));
        } else {
        }
    }

    public int getCrossPoints() {
        int p = 0;
        for (Result r : results) {
            if (r.getWinner() == Constants.CROSS) {
                p++;
            }
        }
        return p;
    }

    public int getCirclePoints() {
        int p = 0;
        for (Result r : results) {
            if (r.getWinner() == Constants.CIRCLE) {
                p++;
            }
        }
        return p;
    }

    public int getTurnsCount() {
        int t = 0;
        for (Result r : results) {
            t += (r.getTurnsCount() + 1) / 2;
        }
        return t;
    }

    public int gamesPlayed() {
        return results.size();
    }

    public int gamesRemaining() {
        return maximumGameCount - results.size();
    }

    public int getWinner() {
        int x = 0, o = 0;
        for (Result r : results) {
            if (r.getWinner() == Constants.CROSS) x++;
            if (r.getWinner() == Constants.CIRCLE) o++;
        }
        if (x == o) {
            return -1;
        }
        return x > 0 ? Constants.CROSS : Constants.CIRCLE;
    }

    public Player getWinningPlayer() {
        switch(getWinner()) {
            case Constants.CROSS:
                return cross;
            case Constants.CIRCLE:
                return circle;
            default:
                return null;
        }
    }

    public Player getCross() {
        return cross;
    }

    public Player getCircle() {
        return circle;
    }
}
