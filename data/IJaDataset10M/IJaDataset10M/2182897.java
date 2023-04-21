package com.sjakkforum;

/**
 * A chess game pairing or result.
 * There is a separate result for each player, as an arbiter
 * is allowed to score the match 1-0, 0-1, 0-0, 0-1/2, and a tournament
 * may set its own rules for points awarded for win, draw and lose.
 * @author Kjartan Mikkelsen (kjartanmi@gmail.com)
 */
public class Game {

    /** White player*/
    private TournamentEntry white = null;

    /** Black player */
    private TournamentEntry black = null;

    private int roundNumber = 0;

    public enum Color {

        WHITE, BLACK
    }

    public enum Result {

        WIN, DRAW, LOSE, WALK_OVER, NOT_STARTED, IN_PROGRESS
    }

    private Result whiteResult = Result.NOT_STARTED;

    private Result blackResult = Result.NOT_STARTED;

    public Game() {
    }

    /**
     * @return Returns the white.
     */
    public TournamentEntry getWhite() {
        return white;
    }

    /**
     * @param white The white to set.
     */
    public void setWhite(TournamentEntry white) {
        this.white = white;
    }

    /**
     * @return Returns the black.
     */
    public TournamentEntry getBlack() {
        return black;
    }

    /**
     * @param black The black to set.
     */
    public void setBlack(TournamentEntry black) {
        this.black = black;
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

    /**
     * @return Returns the blackResult.
     */
    public Result getBlackResult() {
        return blackResult;
    }

    /**
     * @param blackResult The blackResult to set.
     */
    public void setBlackResult(Result blackResult) {
        this.blackResult = blackResult;
    }

    /**
     * @return Returns the whiteResult.
     */
    public Result getWhiteResult() {
        return whiteResult;
    }

    /**
     * @param whiteResult The whiteResult to set.
     */
    public void setWhiteResult(Result whiteResult) {
        this.whiteResult = whiteResult;
    }

    public void setNotStarted() {
        setWhiteResult(Result.NOT_STARTED);
        setBlackResult(Result.NOT_STARTED);
    }

    public void setInProgress() {
        setWhiteResult(Result.IN_PROGRESS);
        setBlackResult(Result.IN_PROGRESS);
    }

    public void setResult(Result white, Result black) {
        setWhiteResult(white);
        setBlackResult(black);
    }

    public Result getEntryResult(TournamentEntry entry) {
        if (entry.equals(getWhite())) {
            return getWhiteResult();
        } else if (entry.equals(getBlack())) {
            return getBlackResult();
        }
        return null;
    }

    public Color getEntryColor(TournamentEntry entry) {
        if (entry.equals(getWhite())) {
            return Color.WHITE;
        } else if (entry.equals(getBlack())) {
            return Color.BLACK;
        }
        return null;
    }
}
