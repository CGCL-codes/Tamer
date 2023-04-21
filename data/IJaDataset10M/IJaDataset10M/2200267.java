package hotgammon.domain.game;

import hotgammon.domain.game.dice.AlphaDiceStrategy;
import hotgammon.domain.game.dice.DiceStrategy;
import hotgammon.domain.game.move.AlphaMoveStrategy;
import hotgammon.domain.game.move.MoveStrategy;
import hotgammon.domain.game.winner.RedWinnerStrategy;
import hotgammon.domain.game.winner.WinnerStrategy;

/**
 *
 * @author steff
 */
public class AlphamonFactory implements GameFactory {

    public MoveStrategy createMoveStrategy() {
        return new AlphaMoveStrategy();
    }

    public DiceStrategy createDiceStrategy() {
        return new AlphaDiceStrategy();
    }

    public WinnerStrategy createWinnerStrategy() {
        return new RedWinnerStrategy();
    }
}
