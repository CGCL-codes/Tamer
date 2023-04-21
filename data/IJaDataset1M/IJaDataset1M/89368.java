package hotgammon.domain;

public class AlphamonFactory implements GameFactory {

    public MoveStrategy createMoveStrategy() {
        return new AlphamonMove();
    }

    public DiceStrategy createDiceStrategy() {
        return new AlphamonDice();
    }

    public WinnerStrategy createWinnerStrategy() {
        return new AlphamonWinner();
    }
}
