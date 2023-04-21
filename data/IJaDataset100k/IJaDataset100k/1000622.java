package hottargui.config;

import hottargui.framework.*;

public class BetaGameFactory implements GameFactory {

    private Game game;

    public BetaGameFactory(Game g) {
        game = g;
    }

    public Board createBoard() {
        AlphaBoardFactory abf = new AlphaBoardFactory();
        return new AlphaBoard(abf);
    }

    public PlayerTurnStrategy createTurnStrategy() {
        return new SimpleTurnStrategy();
    }

    public MoveValidationStrategy createMoveValidationStrategy() {
        return new StandardMoveValidationStrategy(game);
    }

    public WinnerStrategy createWinnerStrategy() {
        return new BetaWinnerStrategy(game);
    }

    public PutUnitsStrategy createPutUnitsStrategy() {
        return new BetaPutUnitsStrategy();
    }

    public AttackStrategy createAttackStrategy() {
        return new BetaAttackStrategy(game);
    }
}
