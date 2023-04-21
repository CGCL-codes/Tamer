package client;

/**
 *
 * @author Igor
 */
public interface HangmanResponsiveUI {

    public void updateHangmanImage(final int stage);

    public void updateWordView(final String view);

    public void updateAttemptsLeftCount(final int attemptsLeft);

    public void updateTotalScore(final int totalScore);

    public void updateTitle(final String title);

    public void reportConnectionError();

    public void reportWin();

    public void reportGameOver();

    public void connectionEstablished();

    public void connectionClosed();
}
