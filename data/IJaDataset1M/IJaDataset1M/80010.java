package de.jdufner.sudoku.solver.strategy.naked;

import de.jdufner.sudoku.common.board.Grid;
import de.jdufner.sudoku.solver.strategy.configuration.StrategyNameEnum;

/**
 * @author <a href="mailto:jdufner@users.sf.net">Jürgen Dufner</a>
 * @since 0.1
 * @version $Revision: 120 $
 */
public final class NakedQuadParallelStrategy extends AbstractNakedParallelStrategy {

    public static final int SIZE = 4;

    public static final StrategyNameEnum STRATEGY_NAME = StrategyNameEnum.NAKED_QUAD;

    public NakedQuadParallelStrategy(final Grid sudoku) {
        super(sudoku);
    }

    @Override
    public int getSize() {
        return SIZE;
    }

    @Override
    public StrategyNameEnum getStrategyName() {
        return STRATEGY_NAME;
    }
}
