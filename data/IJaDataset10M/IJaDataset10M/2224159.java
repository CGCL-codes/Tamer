package common;

import java.io.Serializable;

/**
 * A class that maintains the of a game.
 */
public class GameResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String winnerName;

    private String loserName;

    private boolean isTie;

    public GameResult(String winner, String loser) {
        winnerName = winner;
        loserName = loser;
        isTie = false;
    }

    public GameResult(boolean tie) {
        isTie = true;
    }

    public void setWinnerName(String name) {
        winnerName = name;
    }

    public void setLoserName(String name) {
        loserName = name;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public String getLoserName() {
        return loserName;
    }

    public boolean getIsTie() {
        return isTie;
    }
}
