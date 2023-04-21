package darts.core;

public interface DartStorer {

    public void insertNewGame(DartGame dartGame) throws DartStorerException;

    public void insertNewScore(DartScore dartScore) throws DartStorerException;

    public void deleteTurnScores(long gameId, int turn) throws DartStorerException;

    public void updateCurrentPlayerAndRound(DartGame dartGame) throws DartStorerException;

    public void updateGameComplete(DartGame dartGame) throws DartStorerException;

    public DartGame loadGame(long gameId) throws DartStorerException;
}
