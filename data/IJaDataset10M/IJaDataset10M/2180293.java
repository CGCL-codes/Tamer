package darts.games;

import java.util.*;
import org.apache.log4j.*;
import darts.core.*;

public class X01InFivesDartGame extends DartGameDecorator {

    private static Category cat = Category.getInstance(X01InFivesDartGame.class.getName());

    private int winningScore;

    public X01InFivesDartGame(int winningScore) {
        super();
        this.winningScore = winningScore;
    }

    public X01InFivesDartGame(int winningScore, DartGame baseDartGame) {
        super(baseDartGame);
        this.winningScore = winningScore;
    }

    public boolean addScore(DartScore dartScore) {
        if (!super.addScore(dartScore)) {
            return false;
        }
        dartScore.setCountedHits(dartScore.getHits());
        dartScore.setScoredHits(dartScore.getHits());
        boolean divisableByThree = true;
        int scoreForThisTurn = 0;
        if (getThrowsInCurrentTurn() == 3) {
            cat.debug("addScore, get throws = 3");
            Vector scoresVector = getScores();
            cat.debug("scores.size = " + scoresVector.size());
            for (int i = 0; i < scoresVector.size(); i++) {
                DartScore oldDartScore = (DartScore) scoresVector.elementAt(i);
                if (oldDartScore.getTurn() == dartScore.getTurn()) {
                    cat.debug(oldDartScore.getScoredHits() + ", " + oldDartScore.getScore());
                    scoreForThisTurn += oldDartScore.getScoredHits() * oldDartScore.getScore();
                }
            }
            cat.debug("scoreForThisTurn=" + scoreForThisTurn);
            divisableByThree = scoreForThisTurn % 5 == 0;
            if (divisableByThree) {
                scoreForThisTurn /= 5;
            }
            cat.debug("divisableByThree=" + divisableByThree);
        }
        int currentPlayer = getPlayerNum(dartScore.getPlayer());
        cat.debug("total score=" + getTotalScore(currentPlayer));
        cat.debug("scoreForThisTurn=" + scoreForThisTurn);
        if (!divisableByThree || (getTotalScore(currentPlayer) + scoreForThisTurn) > winningScore) {
            cat.debug("undo moves, " + getTotalScore(currentPlayer));
            undoHitsForTurn(dartScore.getTurn());
            nextPlayer();
        }
        return true;
    }

    private void undoHitsForTurn(int turn) {
        Vector scoresVector = getScores();
        for (int i = 0; i < scoresVector.size(); i++) {
            DartScore oldDartScore = (DartScore) scoresVector.elementAt(i);
            if (oldDartScore.getTurn() == turn) {
                oldDartScore.setCountedHits(0);
                oldDartScore.setScoredHits(0);
            }
        }
    }

    public void nextPlayer() {
        if (getThrowsInCurrentTurn() < 3) {
            undoHitsForTurn(getCurrentTurn());
        }
        super.nextPlayer();
    }

    public int getTotalScore(int playerNum) {
        int totalScore = 0;
        Vector scoresVector = getScores();
        for (int i = 0; i < scoresVector.size(); i++) {
            DartScore dartScore = (DartScore) scoresVector.elementAt(i);
            if (dartScore.getPlayer().equals(getPlayer(playerNum)) && dartScore.getTurn() != getCurrentTurn()) {
                cat.debug("getTotalScore, total score += " + dartScore.getScoredHits() + " * " + dartScore.getScore());
                totalScore += dartScore.getScoredHits() * dartScore.getScore();
            }
        }
        return (int) (totalScore / 5.00);
    }

    public boolean isGameComplete() {
        if (super.isGameComplete()) {
            return true;
        }
        for (int i = 1; i < DartGame.MAX_PLAYERS; i++) {
            if (getTotalScore(i) == winningScore) {
                setWinner(getPlayer(i));
                return true;
            }
        }
        return false;
    }
}
